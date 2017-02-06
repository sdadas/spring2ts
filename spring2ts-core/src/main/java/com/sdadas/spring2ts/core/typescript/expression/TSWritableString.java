package com.sdadas.spring2ts.core.typescript.expression;

import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;
import com.sdadas.spring2ts.core.typescript.writer.CodeWriter;
import com.sdadas.spring2ts.core.typescript.writer.TSWritable;

import java.io.IOException;

/**
 * @author Sławomir Dadas
 */
public class TSWritableString implements TSWritable {

    private final String value;

    private final boolean formatted;

    public TSWritableString(String value, boolean formatted) {
        this.value = value;
        this.formatted = formatted;
    }

    @Override
    public void write(CodeWriter writer) throws IOException {
        if(formatted) {
            writeFormatted(writer);
        } else {
            writer.write(value);
        }
    }

    private void writeFormatted(CodeWriter writer) throws IOException {
        Iterable<String> lines = Splitter.on("\n").split(value);
        for (String line : lines) {
            writer.writeln(StringUtils.strip(line));
        }
    }

    @Override
    public String getName() {
        return null;
    }
}
