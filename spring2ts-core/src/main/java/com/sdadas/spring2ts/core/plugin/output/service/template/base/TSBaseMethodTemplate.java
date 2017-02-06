package com.sdadas.spring2ts.core.plugin.output.service.template.base;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.jboss.forge.roaster.model.Type;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import com.sdadas.spring2ts.core.plugin.output.TypeMapper;
import com.sdadas.spring2ts.core.plugin.output.service.method.ServiceMethod;
import com.sdadas.spring2ts.core.plugin.output.service.method.ServiceRequestProps;
import com.sdadas.spring2ts.core.plugin.output.service.params.ServiceParam;
import com.sdadas.spring2ts.core.plugin.output.service.params.ServiceParamType;
import com.sdadas.spring2ts.core.typescript.def.TSFunctionDef;
import com.sdadas.spring2ts.core.typescript.def.TSModifier;
import com.sdadas.spring2ts.core.typescript.def.TSVarDef;
import com.sdadas.spring2ts.core.typescript.types.CustomType;
import com.sdadas.spring2ts.core.typescript.types.TypeName;
import com.sdadas.spring2ts.core.typescript.types.VarType;

import java.util.*;

/**
 * TODO; write comments
 * TODO: support multiple paths for single method
 * TODO: support content type parameter
 * TODO: support accepts parameter
 * TODO: support required header constants
 *
 * @author Sławomir Dadas
 */
public class TSBaseMethodTemplate {

    private ServiceMethod method;

    private TypeMapper typeMapper;

    private List<String> comments = Lists.newArrayList();

    private ServiceRequestProps props;

    private Map<String, TSVarDef> args = Maps.newLinkedHashMap();

    private AdditionalOptions options = new AdditionalOptions();

    public TSBaseMethodTemplate(ServiceMethod method, TypeMapper typeMapper) {
        this.method = method;
        this.typeMapper = typeMapper;
        this.props = prepareMethodProps(method.getProps());
    }

    private ServiceRequestProps prepareMethodProps(ServiceRequestProps in) {
        // Set default HTTP method for this request if not specified
        if(in.supportsAllHttpMethods() || in.supportsNoHttpMethods()) {
            in.setMethods(EnumSet.of(method.requiredPayload() ? RequestMethod.POST : RequestMethod.GET));
        } else if(in.supportsManyHttpMethods()) {
            options.add(AdditionalOption.HTTP_METHOD);
        }
        // Set first path if supports many
        if(in.supportsManyPaths()) {
            in.setPaths(Collections.singletonList(in.getPaths().get(0)));
        }
        return in;
    }

    public TSFunctionDef createFunction() {
        TSFunctionDef res = new TSFunctionDef();
        res.name(method.getName());
        res.returnType(createMethodReturnType());
        res.modifier(TSModifier.PUBLIC);
        createMethodParams(res);
        createMethodBody(res);
        return res;
    }

    private void createMethodParams(TSFunctionDef res) {
        method.getParams().forEach(p -> createMethodParam(res, p));
        options.get().keySet().forEach(o -> createAdditionalOptionParam(res, o));
    }

    private void createMethodParam(TSFunctionDef res, ServiceParam param) {
        if(param.isIgnored()) {
            comments.add(String.format("%s %s ", param.getInputType().getName(), param.getName()));
            return;
        }
        TSVarDef argument = getArgument(param);
        res.arg(argument);
        args.put(argument.getName(), argument);
    }

    private void createAdditionalOptionParam(TSFunctionDef res, AdditionalOption option) {
        if(options.isInitialized(option)) return;
        String varName = createVariableName(option.getVariableName());
        TSVarDef variable = new TSVarDef(varName, new CustomType(option.getTypeName())).varType(VarType.ARGUMENT);
        res.arg(variable);
        args.put(varName, variable);
        options.setName(option, varName);
    }

    private TypeName createParamType(ServiceParam param) {
        ServiceParamType paramType = param.getParamType();
        Type<?> type = param.getInputType();
        if(type.isType(Optional.class) || paramType.equals(ServiceParamType.HTTP_ENTITY)) {
            return createGenericParamType(type);
        } else if(paramType.equals(ServiceParamType.HTTP_METHOD)) {
            options.add(AdditionalOption.HTTP_METHOD, param.getName());
            return new CustomType("HttpMethod");
        } else if(paramType.equals(ServiceParamType.REQUEST_PART) && type.isType(MultipartFile.class)) {
            return new CustomType("File");
        }
        return typeMapper.toTSType(type);
    }

    private TypeName createGenericParamType(Type<?> type) {
        List<? extends Type<?>> arguments = type.getTypeArguments();
        if(arguments.isEmpty()) return new CustomType("any");
        type = arguments.get(0);
        return typeMapper.toTSType(type);
    }

    public TSVarDef getArgument(ServiceParam param) {
        return new TSVarDef(param.getName(), createParamType(param)).varType(VarType.ARGUMENT);
    }

    protected TSRequestBuilder createRequestBuilder() {
        return new TSRequestBuilder();
    }

    private void createMethodBody(TSFunctionDef res) {
        TSRequestBuilder builder = createRequestBuilder();
        builder.setPath(getPath());
        if(!options.isPresent(AdditionalOption.HTTP_METHOD)) {
            builder.setMethod(props.getMethods().iterator().next());
        }
        for (ServiceParam param : method.getParams()) {
            TSVarDef arg = args.get(param.getName());
            builder.add(arg, param);
        }
        for (Map.Entry<AdditionalOption, String> entry : options.get().entrySet()) {
            TSVarDef arg = args.get(entry.getValue());
            builder.add(arg, entry.getKey());
        }
        res.body(builder);
    }

    private String getPath() {
        return props.getPaths().get(0).getSimplified();
    }

    protected TypeName createMethodReturnType() {
        Type<?> returnType = method.getMethod().getReturnType();
        return typeMapper.toTSType(returnType);
    }

    private String createVariableName(String baseName) {
        if(!args.containsKey(baseName)) return baseName;
        for (int i = 0; i < 99; i++) {
            String newName = baseName + i;
            if(!args.containsKey(newName)) return newName;
        }
        throw new IllegalArgumentException("Problem creating variable name, too many arguments: " + baseName);
    }

    enum AdditionalOption {

        HTTP_METHOD("HttpMethod", "method"),
        CONTENT_TYPE("ContentType", "contentType"),
        PATH("string", "path");

        private final String typeName;

        private final String variableName;

        AdditionalOption(String typeName, String variableName) {
            this.typeName = typeName;
            this.variableName = variableName;
        }

        public String getTypeName() {
            return typeName;
        }

        public String getVariableName() {
            return variableName;
        }
    }

    class AdditionalOptions {

        private Map<AdditionalOption, String> options = Maps.newLinkedHashMap();

        public void add(AdditionalOption option) {
            add(option, null);
        }

        public void add(AdditionalOption option, String name) {
            options.put(option, name);
        }

        public boolean isPresent(AdditionalOption option) {
            return options.containsKey(option);
        }

        public boolean isInitialized(AdditionalOption option) {
            return StringUtils.isNotBlank(options.get(option));
        }

        public void setName(AdditionalOption option, String name) {
            Validate.isTrue(isPresent(option));
            options.put(option, name);
        }

        public String getName(AdditionalOption option) {
            Validate.isTrue(isPresent(option));
            return options.get(option);
        }

        public Map<AdditionalOption, String> get() {
            return options;
        }
    }
}
