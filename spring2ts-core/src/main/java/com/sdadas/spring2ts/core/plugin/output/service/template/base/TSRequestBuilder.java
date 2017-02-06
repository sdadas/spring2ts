package com.sdadas.spring2ts.core.plugin.output.service.template.base;

import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import com.sdadas.spring2ts.core.plugin.output.service.params.ServiceParam;
import com.sdadas.spring2ts.core.plugin.output.service.params.ServiceParamType;
import com.sdadas.spring2ts.core.typescript.def.TSVarDef;
import com.sdadas.spring2ts.core.typescript.types.TypeName;
import com.sdadas.spring2ts.core.typescript.writer.CodeWriter;
import com.sdadas.spring2ts.core.typescript.writer.TSWritable;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.google.common.collect.ImmutableSet.of;

/**
 * @author Sławomir Dadas
 */
public class TSRequestBuilder implements TSWritable {

    private String path;

    private RequestMethod method;

    private Map<String, Param> params = Maps.newLinkedHashMap();

    public TSRequestBuilder() {
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setMethod(RequestMethod method) {
        this.method = method;
    }

    public void add(TSVarDef var, ServiceParam param) {
        TypeName type = var.getType();
        String name = null;
        if(type.isPrimitive()) {
            name = MoreObjects.firstNonNull(param.getProps().getName(), var.getName());
        }
        Param res = new Param(name, var.getName(), ParamType.getFromParam(param.getParamType()));
        res.pathVar = param.getProps().getPathVar();
        add(res);
    }

    public void add(TSVarDef var, TSBaseMethodTemplate.AdditionalOption option) {
        add(new Param(null, var.getName(), ParamType.getFromOption(option)));
    }

    private void add(Param param) {
        String value = param.value;
        if(params.containsKey(value)) return;
        params.put(value, param);
    }

    @Override
    public void write(CodeWriter cw) throws IOException {
        cw.writeln("let _builder: RequestBuilder = new RequestBuilder();");
        handleModelAttributes(cw);
        cw.writeln(String.format("return _builder.path('%s')", path));
        cw.openIndent();
        writeMethod(cw);
        invoke(cw, "queryParam", p -> p.hasName() && p.is(ParamType.Query));
        invoke(cw, "queryParams", p -> !p.hasName() && p.is(ParamType.Query));
        invoke(cw, "pathVariable", p -> p.hasName() && p.is(ParamType.Path));
        invoke(cw, "pathVariables", p -> !p.hasName() && p.is(ParamType.Path));
        invoke(cw, "matrixVariable", p -> p.hasName() && !p.hasPathVar() && p.is(ParamType.Matrix));
        invoke(cw, "matrixVariables", p -> !p.hasName() && !p.hasPathVar() && p.is(ParamType.Matrix));
        invoke(cw, "pathMatrixVariable", p -> p.hasName() && p.hasPathVar() && p.is(ParamType.Matrix));
        invoke(cw, "pathMatrixVariables", p -> !p.hasName() && p.hasPathVar() && p.is(ParamType.Matrix));
        invoke(cw, "header", p -> p.hasName() && p.is(ParamType.Header));
        invoke(cw, "headers", p -> !p.hasName() && p.is(ParamType.Header));
        writeBody(cw);
        writeContentType(cw);
        writeBuild(cw);
        cw.closeIndent();
    }

    private Optional<Param> getHttpMethodParam() {
        return params.values().stream().filter(p -> p.is(ParamType.Method)).findAny();
    }

    private void handleModelAttributes(CodeWriter cw) throws IOException {
        Optional<Param> param = getHttpMethodParam();
        List<Param> models = params.values().stream().filter(p -> p.is(ParamType.Model)).collect(Collectors.toList());
        if(param.isPresent()) {
            cw.write("if (%s.payload)").openBlock();
            cw.write("_builder");
            invoke(cw, "bodyPart", p -> p.hasName() && p.is(ParamType.Model));
            invoke(cw, "bodyParts", p -> !p.hasName() && p.is(ParamType.Model));
            cw.closeBlock();
            cw.write("else").openBlock();
            cw.write("_builder");
            invoke(cw, "queryParam", p -> p.hasName() && p.is(ParamType.Model));
            invoke(cw, "queryParams", p -> !p.hasName() && p.is(ParamType.Model));
            cw.closeBlock();
        } else {
            ParamType newType = isPayloadMethod(this.method) ? ParamType.Body : ParamType.Query;
            models.forEach(m -> m.type = newType);
        }
    }

    private boolean isPayloadMethod(RequestMethod method) {
        return EnumSet.of(RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH).contains(method);
    }

    private void writeMethod(CodeWriter cw) throws IOException {
        Optional<Param> param = getHttpMethodParam();
        String methodName;
        if(param.isPresent()) {
            methodName = param.get().name;
        } else {
            methodName = String.format("HttpMethods.%s", this.method.name());
        }
        cw.writeln(".method(").write(methodName).write(")");
    }

    protected void writeBuild(CodeWriter cw) throws IOException {
        cw.writeln(".build()");
        cw.writeln(".observe();");
    }

    private void writeContentType(CodeWriter cw) throws IOException {
        cw.writeln(".contentType(ContentTypes.Json)");
    }

    private void writeBody(CodeWriter cw) throws IOException {
        if(countBodyParts() > 1) {
            invoke(cw, "bodyPart", p -> p.hasName() && p.is(ParamType.Body));
            invoke(cw, "bodyParts", p -> !p.hasName() && p.is(ParamType.Body));
        } else {
            invoke(cw, "body", p -> !p.hasName() && p.is(ParamType.Body));
            invoke(cw, "body", p -> p.hasName() && p.is(ParamType.Body), p -> {
                return new String[] {String.format("{%s: %s}", p.name, p.value)};
            });
        }
    }

    private long countBodyParts() {
        return params.values().stream().filter(p -> p.is(ParamType.Body)).count();
    }

    @Override
    public String getName() {
        return null;
    }

    private void invoke(CodeWriter writer, String methodName, Predicate<Param> filter,
                        Function<Param, String[]> arguments) throws IOException {

        List<String[]> results = params.values().stream()
                .filter(filter).map(arguments)
                .collect(Collectors.toList());
        for (String[] args : results) {
            writer.writeln(".").write(methodName)
                    .write('(').write(Joiner.on(", ").useForNull("null").join(args)).write(")");
        }
    }

    private void invoke(CodeWriter writer, String methodName, Predicate<Param> filter) throws IOException {
        invoke(writer, methodName, filter, this::defaultArgs);
    }

    private String[] defaultArgs(Param param) {
        List<String> res = Lists.newArrayList();
        if(param.hasPathVar()) res.add(param.pathVar);
        if(param.hasName()) res.add(String.format("'%s'", param.name));
        res.add(param.value);
        return res.toArray(new String[res.size()]);
    }

    private class Param {

        private String name;

        private String value;

        private ParamType type;

        private String pathVar;

        public Param(String name, String value, ParamType type) {
            this.name = name;
            this.value = value;
            this.type = type;
        }

        public boolean hasName() {
            return StringUtils.isNotBlank(name);
        }

        public boolean hasPathVar() {
            return StringUtils.isNotBlank(pathVar);
        }

        public boolean is(ParamType val) {
            return val.equals(this.type);
        }
    }

    private enum ParamType {
        Model(of(ServiceParamType.MODEL_ATTRIBUTE), null),
        Body(of(ServiceParamType.HTTP_ENTITY, ServiceParamType.REQUEST_BODY, ServiceParamType.REQUEST_PART), null),
        Query(of(ServiceParamType.REQUEST_PARAM), null),
        Matrix(of(ServiceParamType.MATRIX_VARIABLE), null),
        Path(of(ServiceParamType.PATH_VARIABLE), null),
        Method(of(ServiceParamType.HTTP_METHOD), of(TSBaseMethodTemplate.AdditionalOption.HTTP_METHOD)),
        Header(of(ServiceParamType.REQUEST_HEADER), null),
        ContentType(null, of(TSBaseMethodTemplate.AdditionalOption.CONTENT_TYPE));

        private final Set<ServiceParamType> fromParams;

        private final Set<TSBaseMethodTemplate.AdditionalOption> fromOptions;

        ParamType(Set<ServiceParamType> fromParams, Set<TSBaseMethodTemplate.AdditionalOption> fromOptions) {
            this.fromParams = fromParams;
            this.fromOptions = fromOptions;
        }

        public static ParamType getFromParam(ServiceParamType type) {
            for (ParamType val : values()) {
                Set<ServiceParamType> set = val.fromParams;
                if(set != null && !set.isEmpty() && set.contains(type)) {
                    return val;
                }
            }
            return null;
        }

        public static ParamType getFromOption(TSBaseMethodTemplate.AdditionalOption option) {
            for (ParamType val : values()) {
                Set<TSBaseMethodTemplate.AdditionalOption> set = val.fromOptions;
                if(set != null && !set.isEmpty() && set.contains(option)) {
                    return val;
                }
            }
            return null;
        }
    }
}
