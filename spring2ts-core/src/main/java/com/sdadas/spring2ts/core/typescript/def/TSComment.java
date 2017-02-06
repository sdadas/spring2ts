/*
 * To oprogramowanie jest własnością
 *
 * OPI - Ośrodek Przetwarzania Informacji,
 * Al. Niepodległości 188B, 00-608 Warszawa
 * Numer KRS: 0000127372
 * Sąd Rejonowy dla m. st. Warszawy w Warszawie XII Wydział
 * Gospodarczy KRS
 * REGON: 006746090
 * NIP: 525-000-91-40
 * Wszystkie prawa zastrzeżone. To oprogramowanie może być używane tylko
 * zgodnie z przeznaczeniem. OPI nie odpowiada za ewentualne wadliwe
 * działanie kodu.
 */
package com.sdadas.spring2ts.core.typescript.def;

import org.apache.commons.lang3.StringUtils;
import com.sdadas.spring2ts.core.typescript.writer.CodeWriter;
import com.sdadas.spring2ts.core.typescript.writer.TSWritable;

import java.io.IOException;

/**
 * @author Sławomir Dadas
 */
public class TSComment implements TSWritable {

    private StringBuilder content = new StringBuilder();

    boolean inline = false;

    public TSComment inline(boolean value) {
        this.inline = value;
        return this;
    }

    public TSComment write(String value) {
        content.append(value);
        return this;
    }

    public TSComment writeln(String value) {
        content.append(value).append('\n');
        return this;
    }

    public TSComment writeln() {
        return writeln("");
    }

    @Override
    public void write(CodeWriter writer) throws IOException {
        String value = content.toString();
        if(StringUtils.isEmpty(value)) return;
        writer.write(inline ? "// " : "/* ");
        if(inline) {
            value = value.replaceAll("\\r|\\n", "");
        }
        writer.write(value);
        if(!inline) writer.write(" */");
        writer.writeln();
    }

    @Override
    public String getName() {
        return null;
    }
}
