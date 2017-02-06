package com.sdadas.spring2ts.core.plugin.output.service.template.angular2;

import com.sdadas.spring2ts.core.plugin.output.service.template.base.TSRequestBuilder;
import com.sdadas.spring2ts.core.typescript.writer.CodeWriter;

import java.io.IOException;

/**
 * @author Sławomir Dadas
 */
public class TSAngular2RequestBuilder extends TSRequestBuilder {

    @Override
    protected void writeBuild(CodeWriter cw) throws IOException {
        cw.writeln(".build(this.http);");
    }
}
