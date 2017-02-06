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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.sdadas.spring2ts.core.typescript.expression.TSWritableString;
import com.sdadas.spring2ts.core.typescript.writer.CodeWriter;
import com.sdadas.spring2ts.core.typescript.writer.TSWritable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sławomir Dadas
 */
public class TSFile implements TSWritable {

    private String path;

    private List<TSImport> imports = new ArrayList<>();

    private List<TSWritable> types = new ArrayList<>();

    private boolean optimizeImports = true;

    public TSFile() {
    }

    public TSFile(String path) {
        this.path = path;
    }

    public TSFile loadContent(String content) {
        imports.clear();
        optimizeImports = false;
        types.clear();
        types.add(new TSWritableString(content, false));
        return this;
    }

    public TSFile imports(TSImport value) {
        this.imports.add(value);
        return this;
    }

    public TSFile imports(String symbol, String from) {
        this.imports.add(new TSImport(symbol, from));
        return this;
    }

    public TSFile imports(TSImport... values) {
        this.imports.addAll(Arrays.asList(values));
        return this;
    }

    public TSFile add(TSWritable value) {
        this.types.add(value);
        return this;
    }

    public TSFile optimizeImports(boolean value) {
        this.optimizeImports = value;
        return this;
    }

    private void doOptimizeImports() {
        Multimap<String, String> map = HashMultimap.create();
        for (TSImport imp : imports) {
            if(imp.getNames().isEmpty()) {
                map.put(imp.getFrom(), null);
            } else {
                map.putAll(imp.getFrom(), imp.getNames());
            }
        }

        List<TSImport> res = new ArrayList<>();
        for (String from : map.keySet()) {
            TSImport imp = new TSImport();
            imp.from(from);
            imp.names(map.get(from).stream().filter(c -> c != null).distinct().collect(Collectors.toList()));
            res.add(imp);
        }
        this.imports = res;
    }

    public String getPath() {
        return path;
    }

    @Override
    public void write(CodeWriter writer) throws IOException {
        if(optimizeImports) doOptimizeImports();
        for (TSImport imp : imports) {
            imp.write(writer);
        }
        if(!imports.isEmpty()) {
            writer.writeln().writeln();
        }
        for (TSWritable type : types) {
            type.write(writer);
            writer.writeln().writeln();
        }
    }

    @Override
    public String getName() {
        return null;
    }
}
