package com.sdadas.spring2ts.core.typescript.writer;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * @author Sławomir Dadas
 */
public class CodeWriter {

    private final static int INDENT_SPACES = 4;

    private final Writer writer;

    private int indent = 0;

    public CodeWriter(Writer writer) {
        this.writer = writer;
    }

    public CodeWriter openIndent() throws IOException {
        indent += INDENT_SPACES;
        return this;
    }

    public CodeWriter closeIndent() throws IOException {
        indent -= INDENT_SPACES;
        return this;
    }

    public CodeWriter openBlock() throws IOException {
        write(" {");
        indent += INDENT_SPACES;
        return this;
    }

    public CodeWriter closeBlock() throws IOException {
        indent -= INDENT_SPACES;
        writeln("}");
        return this;
    }

    public CodeWriter write(String value) throws IOException {
        writer.write(value);
        return this;
    }

    public CodeWriter writer(Object value) throws IOException {
        writer.write(value.toString());
        return this;
    }

    public CodeWriter write(char value) throws IOException {
        writer.write(value);
        return this;
    }

    public CodeWriter write(TSWritable value) throws IOException {
        value.write(this);
        return this;
    }

    public CodeWriter writelns(List<String> values) throws IOException {
        for (String value : values) {
            writeln(value);
        }
        return this;
    }

    public CodeWriter writelns(List<String> values, char endOfLine) throws IOException {
        return writelns(values, endOfLine, false);
    }

    public CodeWriter writelns(List<String> values, char endOfLine, boolean skipLastLine) throws IOException {
        for (int i = 0; i < values.size(); i++) {
            String value = values.get(i);
            if(i < values.size() - 1) {
                writeln(value + endOfLine);
            } else {
                writeln(value);
            }
        }
        return this;
    }

    public CodeWriter writeln(String value) throws IOException {
        writer.write("\n");
        writer.write(StringUtils.repeat(' ', indent));
        writer.write(value);
        return this;
    }

    public CodeWriter writeln() throws IOException {
        return writeln("");
    }
}
