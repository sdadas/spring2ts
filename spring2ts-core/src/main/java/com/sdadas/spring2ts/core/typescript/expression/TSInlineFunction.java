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

/**
 * @author Sławomir Dadas
 */
public class TSInlineFunction extends TSExpression<TSInlineFunction> {

    @Override
    protected void writeInner(CodeWriter writer) throws IOException {

    }

    @Override
    public String getName() {
        return null;
    }
}
