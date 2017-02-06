package com.sdadas.spring2ts.core.plugin.output.service.template.angular2;

import com.sdadas.spring2ts.core.plugin.output.TypeMapper;
import com.sdadas.spring2ts.core.plugin.output.service.method.ServiceMethod;
import com.sdadas.spring2ts.core.plugin.output.service.template.base.TSBaseMethodTemplate;
import com.sdadas.spring2ts.core.plugin.output.service.template.base.TSRequestBuilder;
import com.sdadas.spring2ts.core.typescript.types.CustomType;
import com.sdadas.spring2ts.core.typescript.types.TypeName;

/**
 * @author Sławomir Dadas
 */
public class TSAngular2MethodTemplate extends TSBaseMethodTemplate {

    public TSAngular2MethodTemplate(ServiceMethod method, TypeMapper typeMapper) {
        super(method, typeMapper);
    }

    @Override
    protected TSRequestBuilder createRequestBuilder() {
        return new TSAngular2RequestBuilder();
    }

    @Override
    protected TypeName createMethodReturnType() {
        TypeName tn = super.createMethodReturnType();
        return tn.isVoid() ? new CustomType("Observable", "any") : new CustomType("Observable", tn.toDeclaration());
    }
}
