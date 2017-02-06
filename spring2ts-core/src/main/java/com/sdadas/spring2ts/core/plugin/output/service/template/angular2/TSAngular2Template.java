package com.sdadas.spring2ts.core.plugin.output.service.template.angular2;

import com.sdadas.spring2ts.core.plugin.output.service.method.ServiceMethod;
import com.sdadas.spring2ts.core.plugin.output.service.template.base.TSBaseTemplate;
import com.sdadas.spring2ts.core.typescript.def.TSClassDef;
import com.sdadas.spring2ts.core.typescript.def.TSFunctionDef;
import com.sdadas.spring2ts.core.typescript.def.TSModifier;
import com.sdadas.spring2ts.core.typescript.def.TSVarDef;
import com.sdadas.spring2ts.core.typescript.types.CustomType;

import java.io.IOException;

/**
 * @author Sławomir Dadas
 */
public class TSAngular2Template extends TSBaseTemplate {

    @Override
    protected void onInit() throws IOException {
        loadOutputFile(typeMapper, "plugin/output/service/angular2/RequestBuilder.ts");
    }

    @Override
    protected void afterCreateClass(TSClassDef clazz) {
        clazz.annotation("Injectable");
        clazz.field(new TSVarDef("http", new CustomType("Http")).modifier(TSModifier.PRIVATE));
        clazz.function(createConstructor());
    }

    @Override
    protected TSFunctionDef createMethod(ServiceMethod method) {
        return new TSAngular2MethodTemplate(method, typeMapper).createFunction();
    }

    private TSFunctionDef createConstructor() {
        TSFunctionDef res = new TSFunctionDef();
        res.name("constructor");
        res.arg("http", new CustomType("Http"));
        res.body("this.http = http;");
        return res;
    }

    @Override
    protected void createImports() {
        super.createImports();
        typeMapper.imports("Injectable", "angular2/core");
        typeMapper.imports("Http", "angular2/http");
        typeMapper.imports("BaseRequestOptions", "angular2/http");
    }
}
