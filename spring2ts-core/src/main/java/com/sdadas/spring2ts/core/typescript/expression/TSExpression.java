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
import com.sdadas.spring2ts.core.typescript.writer.TSWritable;

import java.io.IOException;
import java.util.List;

/**
 * @author Sławomir Dadas
 */
public abstract class TSExpression<T extends TSExpression> implements TSWritable {

    private String prefix;

    private List<Suffix> suffixes = Lists.newArrayList();

    private TSExpression<?> member;

    @SuppressWarnings("unchecked")
    public T newInstanse() {
        this.prefix = "new ";
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T del() {
        this.prefix = "del ";
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T invoke(TSExpression<?>... args) {
        suffixes.add(new MethodInvocationSuffix(args));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T arrayAccessor(int index) {
        suffixes.add(new ArrayIndexSuffix(index));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public <E extends TSExpression> E member(TSExpression<E> value) {
        this.member = value;
        return (E) value;
    }

    public TSMember member(String value) {
        TSMember result = new TSMember(value);
        this.member = result;
        return result;
    }

    @Override
    public void write(CodeWriter writer) throws IOException {
        if(prefix != null) writer.write(prefix);
        writeInner(writer);
        writeSuffixes(writer);
        if(member != null) {
            writer.write('.').write(member);
        }
    }

    private void writeSuffixes(CodeWriter writer) throws IOException {
        for (Suffix suffix : suffixes) {
            writer.write(suffix);
        }
    }

    protected abstract void writeInner(CodeWriter writer) throws IOException;

    private interface Suffix extends TSWritable {
    }

    private class MethodInvocationSuffix implements Suffix {

        private List<TSExpression<?>> args = Lists.newArrayList();

        private MethodInvocationSuffix(TSExpression<?>... args) {
            this.args = Lists.newArrayList(args);
        }

        @Override
        public void write(CodeWriter writer) throws IOException {
            writer.write("(");
            for (int i = 0; i < args.size(); i++) {
                writer.write(args.get(i));
                if(i < args.size() -1) writer.write(", ");
            }
        }

        @Override
        public String getName() {
            return null;
        }
    }

    private class ArrayIndexSuffix implements Suffix {

        private final int index;

        private ArrayIndexSuffix(int index) {
            this.index = index;
        }

        @Override
        public void write(CodeWriter writer) throws IOException {
            writer.write("[").write(String.valueOf(index)).write(']');
        }

        @Override
        public String getName() {
            return null;
        }
    }
}
