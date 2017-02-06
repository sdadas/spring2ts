package com.sdadas.spring2ts.core.plugin.output.service.template.base;

import org.apache.commons.io.IOUtils;
import org.jboss.forge.roaster.model.JavaType;
import org.springframework.core.io.ClassPathResource;
import com.sdadas.spring2ts.core.plugin.output.OutputProcessor;
import com.sdadas.spring2ts.core.plugin.output.TypeMapper;
import com.sdadas.spring2ts.core.plugin.output.service.ServiceClass;
import com.sdadas.spring2ts.core.plugin.output.service.method.ServiceMethod;
import com.sdadas.spring2ts.core.plugin.output.service.template.TSServiceTemplate;
import com.sdadas.spring2ts.core.typescript.def.*;
import com.sdadas.spring2ts.core.typescript.writer.TSWritable;

import java.io.IOException;

/**
 * @author Sławomir Dadas
 */
public abstract class TSBaseTemplate implements TSServiceTemplate {

    protected TypeMapper typeMapper;

    @Override
    public void init(TypeMapper mapper) throws IOException {
        this.typeMapper = mapper;
        onInit();
    }

    protected abstract void onInit() throws IOException;

    protected void afterCreateClass(TSClassDef clazz) {
    }

    protected void afterCreateMethod(TSFunctionDef method) {
    }

    protected void loadOutputFile(TypeMapper mapper, String classPath) throws IOException {
        if(mapper instanceof OutputProcessor) {
            ClassPathResource resource = new ClassPathResource(classPath);
            OutputProcessor processor = (OutputProcessor) mapper;
            String path = processor.getFilePath(resource.getFilename());
            TSFile file = processor.createNewFile(path);
            file.loadContent(IOUtils.toString(resource.getInputStream()));
        }
    }

    @Override
    public TSWritable serviceClass(ServiceClass clazz) {
        TSClassDef res = createClass(clazz);
        afterCreateClass(res);
        for (ServiceMethod method : clazz.getMethods()) {
            TSFunctionDef func = createMethod(method);
            afterCreateMethod(func);
            res.function(func);
        }
        createImports();
        return res;
    }

    protected TSFunctionDef createMethod(ServiceMethod method) {
        return new TSBaseMethodTemplate(method, typeMapper).createFunction();
    }

    private TSClassDef createClass(ServiceClass clazz) {
        JavaType<?> type = clazz.getType();
        TSClassDef res = new TSClassDef();
        res.name(type.getName());
        res.modifier(TSModifier.EXPORT);
        return res;
    }

    protected void createImports() {
        typeMapper.imports("Observable", "rxjs/Observable");
        typeMapper.imports(new TSImport("rxjs/Rx"));
        typeMapper.imports("RequestBuilder", "./RequestBuilder");
        typeMapper.imports("HttpMethods", "./RequestBuilder");
        typeMapper.imports("ContentTypes", "./RequestBuilder");
    }
}
