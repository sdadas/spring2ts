package com.sdadas.spring2ts.core.plugin.output.service.template.jquery;

import com.sdadas.spring2ts.core.plugin.output.service.method.ServiceMethod;
import com.sdadas.spring2ts.core.plugin.output.service.template.angular2.TSAngular2MethodTemplate;
import com.sdadas.spring2ts.core.plugin.output.service.template.base.TSBaseTemplate;
import com.sdadas.spring2ts.core.typescript.def.TSFunctionDef;
import com.sdadas.spring2ts.core.typescript.def.TSImport;

import java.io.IOException;

/**
 * @author Sławomir Dadas
 */
public class TSJQueryTemplate extends TSBaseTemplate {

    @Override
    protected void onInit() throws IOException {
        loadOutputFile(typeMapper, "plugin/output/service/jquery/RequestBuilder.ts");
    }

    @Override
    protected TSFunctionDef createMethod(ServiceMethod method) {
        return new TSJQueryMethodTempate(method, typeMapper).createFunction();
    }

    protected void createImports() {
        typeMapper.imports("RequestBuilder", "./RequestBuilder");
        typeMapper.imports("HttpMethods", "./RequestBuilder");
        typeMapper.imports("ContentTypes", "./RequestBuilder");
    }
}
