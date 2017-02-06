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

import com.google.common.collect.Lists;
import com.sdadas.spring2ts.core.typescript.writer.CodeWriter;

import java.io.IOException;
import java.util.List;

/**
 * @author Sławomir Dadas
 */
public class TSInlineArray extends TSExpression<TSInlineArray> {

    private List<TSExpression<?>> elements = Lists.newArrayList();

    private boolean singleLine = true;

    public TSInlineArray element(TSExpression<?> value) {
        this.elements.add(value);
        return this;
    }

    public TSInlineArray elements(TSExpression<?>... values) {
        for (TSExpression<?> value : values) {
            this.elements.add(value);
        }
        return this;
    }

    public TSInlineArray singleLine() {
        this.singleLine = true;
        return this;
    }

    @Override
    protected void writeInner(CodeWriter writer) throws IOException {
        if(elements.isEmpty()) {
            writer.write("[]");
            return;
        }
        writer.openBlock().writeln();
        writeElements(writer);
        writer.closeBlock();
    }

    private void writeElements(CodeWriter writer) throws IOException {
        int idx = 0;
        for (TSExpression<?> element : elements) {
            idx++;
            if(element == null) {
                writer.write("null");
            } else {
                writer.write(element);
            }
            if(idx < elements.size()) {
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
