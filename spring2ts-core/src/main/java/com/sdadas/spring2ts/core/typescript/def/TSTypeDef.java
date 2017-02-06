package com.sdadas.spring2ts.core.typescript.def;

import com.sdadas.spring2ts.core.typescript.types.CustomType;
import com.sdadas.spring2ts.core.typescript.types.TypeName;
import com.sdadas.spring2ts.core.typescript.writer.CodeWriter;
import com.sdadas.spring2ts.core.typescript.writer.TSWritable;

import java.io.IOException;
import java.util.*;

/**
 * @author Sławomir Dadas
 */
public abstract class TSTypeDef<T extends TSTypeDef> implements TSWritable {

    protected TypeName name;

    protected List<TSVarDef> fields = new ArrayList<>();

    protected List<TSFunctionDef> functions = new ArrayList<>();

    protected EnumSet<TSModifier> modifiers = EnumSet.noneOf(TSModifier.class);

    protected List<TSAnnotation> annotations = new ArrayList<>();

    protected List<TSWritable> customCode = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public T name(String value) {
        this.name = CustomType.of(value);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T name(TypeName value) {
        this.name = value;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T field(TSVarDef value) {
        this.fields.add(value);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T fields(Collection<TSVarDef> values) {
        this.fields.addAll(values);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T function(TSFunctionDef value) {
        this.functions.add(value);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T modifier(TSModifier first) {
        this.modifiers.add(first);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T modifiers(TSModifier first, TSModifier... values) {
        this.modifiers.addAll(EnumSet.of(first, values));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T annotations(TSAnnotation... values) {
        annotations.addAll(Arrays.asList(values));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T annotation(String value) {
        annotations.add(new TSAnnotation(value));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T customCode(TSWritable value) {
        customCode.add(value);
        return (T) this;
    }

    @Override
    public void write(CodeWriter writer) throws IOException {
        writeAnnotations(writer);
        writeNameDef(writer);
        writer.openBlock();
        writer.writeln();
        writeFields(writer);
        writeFunctions(writer);
        writeCustomCode(writer);
        writer.closeBlock();
    }

    public abstract void writeNameDef(CodeWriter writer) throws IOException;

    public void writeAnnotations(CodeWriter writer) throws IOException {
        for (TSAnnotation annotation : annotations) {
            annotation.write(writer);
            writer.writeln();
        }
    }

    public void writeFields(CodeWriter writer) throws IOException {
        for (TSVarDef field : fields) {
            field.write(writer);
        }
    }

    public void writeFunctions(CodeWriter writer) throws IOException {
        if(!functions.isEmpty()) writer.writeln();
        for (TSFunctionDef function : functions) {
            function.write(writer);
            writer.write("\n");
        }
    }

    public void writeCustomCode(CodeWriter writer) throws IOException {
        if(!customCode.isEmpty()) writer.writeln();
        for (TSWritable code : customCode) {
            code.write(writer);
        }
    }

    @Override
    public String getName() {
        return name.getBaseName();
    }
}
