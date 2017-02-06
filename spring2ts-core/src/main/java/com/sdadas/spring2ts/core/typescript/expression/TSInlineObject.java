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
package com.sdadas.spring2ts.core.typescript.expression;

import com.sdadas.spring2ts.core.typescript.writer.CodeWriter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Sławomir Dadas
 */
public class TSInlineObject extends TSExpression<TSInlineObject> {

    private Map<String, TSExpression<?>> properties = new LinkedHashMap<>();

    private boolean singleLine = false;

    public TSInlineObject property(String key, TSExpression value) {
        this.properties.put(key, value);
        return this;
    }

    public TSInlineObject singleLine() {
        this.singleLine = true;
        return this;
    }

    @Override
    protected void writeInner(CodeWriter writer) throws IOException {
        if(properties.isEmpty()) {
            writer.write("{}");
            return;
        }
        writer.openBlock().writeln();
        writeProperties(writer);
        writer.closeBlock();
    }

    private void writeProperties(CodeWriter writer) throws IOException {
        int idx = 0;
        for (Map.Entry<String, TSExpression<?>> entry : properties.entrySet()) {
            idx++;
            writer.write('"').write(entry.getKey()).write("\": ");
            TSExpression value = entry.getValue();
            if(value == null) {
                writer.write("null");
            } else {
                writer.write(value);
            }
            if(idx < properties.size()) {
                writer.write(", ");
            }
            if(!singleLine) writer.writeln();
        }
    }

    @Override
    public String getName() {
        return null;
    }
}
