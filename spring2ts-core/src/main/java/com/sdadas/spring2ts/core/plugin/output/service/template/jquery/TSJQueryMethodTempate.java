package com.sdadas.spring2ts.core.plugin.output.service.template.jquery;

import com.sdadas.spring2ts.core.plugin.output.TypeMapper;
import com.sdadas.spring2ts.core.plugin.output.service.method.ServiceMethod;
import com.sdadas.spring2ts.core.plugin.output.service.template.angular2.TSAngular2RequestBuilder;
import com.sdadas.spring2ts.core.plugin.output.service.template.base.TSBaseMethodTemplate;
import com.sdadas.spring2ts.core.plugin.output.service.template.base.TSRequestBuilder;
import com.sdadas.spring2ts.core.typescript.types.CustomType;
import com.sdadas.spring2ts.core.typescript.types.TypeName;

/**
 * @author Sławomir Dadas
 */
public class TSJQueryMethodTempate extends TSBaseMethodTemplate {

    public TSJQueryMethodTempate(ServiceMethod method, TypeMapper typeMapper) {
        super(method, typeMapper);
    }

    @Override
    protected TSRequestBuilder createRequestBuilder() {
        return new TSJQueryRequestBuilder();
    }

    @Override
    protected TypeName createMethodReturnType() {
        TypeName tn = super.createMethodReturnType();
        return tn.isVoid() ? new CustomType("JQueryPromise", "any") : new CustomType("JQueryPromise", tn.toDeclaration());
    }
}