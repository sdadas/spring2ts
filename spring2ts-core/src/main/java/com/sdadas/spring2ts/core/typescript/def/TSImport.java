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

import com.google.common.base.Joiner;
import org.apache.commons.lang3.Validate;
import com.sdadas.spring2ts.core.typescript.writer.CodeWriter;
import com.sdadas.spring2ts.core.typescript.writer.TSWritable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Sławomir Dadas
 */
public class TSImport implements TSWritable {

    private List<String> names = new ArrayList<>();

    private String from;

    public TSImport() {
    }

    public TSImport(String from) {
        this.from = from;
    }

    public TSImport(String symbol, String from) {
        this.names.add(symbol);
        this.from = from;
    }

    public TSImport names(String... values) {
        this.names.addAll(Arrays.asList(values));
        return this;
    }

    public TSImport names(Collection<String> values) {
        this.names.addAll(values);
        return this;
    }

    public TSImport name(String value) {
        this.names.add(value);
        return this;
    }

    public TSImport from(String value) {
        this.from = value;
        return this;
    }

    public List<String> getNames() {
        return names;
    }

    public String getFrom() {
        return from;
    }

    @Override
    public void write(CodeWriter writer) throws IOException {
        Validate.notNull(from);
        writer.writeln();
        writer.write("import ");
        if(names != null && !names.isEmpty()) {
            writer.write("{");
            writer.write(Joiner.on(", ").skipNulls().join(names));
            writer.write("} from \"").write(from).write("\";");
        } else {
            writer.write("\"").write(from).write("\";");
        }
    }

    @Override
    public String getName() {
        return from;
    }
}
