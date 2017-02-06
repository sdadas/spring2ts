package com.sdadas.spring2ts.core.plugin.output.service.template.jquery;

import com.sdadas.spring2ts.core.plugin.output.service.template.base.TSRequestBuilder;
import com.sdadas.spring2ts.core.typescript.writer.CodeWriter;

import java.io.IOException;

/**
 * @author Sławomir Dadas
 */
public class TSJQueryRequestBuilder  extends TSRequestBuilder {

    @Override
    protected void writeBuild(CodeWriter cw) throws IOException {
        cw.writeln(".build();");
    }
}
