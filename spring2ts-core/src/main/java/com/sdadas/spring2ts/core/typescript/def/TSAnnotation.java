package com.sdadas.spring2ts.core.typescript.def;

import com.sdadas.spring2ts.core.typescript.writer.CodeWriter;
import com.sdadas.spring2ts.core.typescript.writer.TSWritable;

import java.io.IOException;

/**
 * @author Sławomir Dadas
 */
public class TSAnnotation implements TSWritable {

    private String name;

    public TSAnnotation() {
    }

    public TSAnnotation(String name) {
        this.name = name;
    }

    public TSAnnotation name(String value) {
        this.name = value;
        return this;
    }

    @Override
    public void write(CodeWriter writer) throws IOException {
        writer.write("@").write(name).write("()");
    }

    @Override
    public String getName() {
        return name;
    }
}
