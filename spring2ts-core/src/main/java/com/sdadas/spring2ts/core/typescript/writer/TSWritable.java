package com.sdadas.spring2ts.core.typescript.writer;

import java.io.IOException;

/**
 * @author Sławomir Dadas
 */
public interface TSWritable {

    void write(CodeWriter writer) throws IOException;

    String getName();
}
