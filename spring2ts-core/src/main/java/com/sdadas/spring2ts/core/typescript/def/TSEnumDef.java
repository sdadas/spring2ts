package com.sdadas.spring2ts.core.typescript.def;

import com.sdadas.spring2ts.core.typescript.types.CustomType;
import com.sdadas.spring2ts.core.typescript.types.TypeName;
import org.apache.commons.lang3.Validate;
import com.sdadas.spring2ts.core.typescript.writer.CodeWriter;
import com.sdadas.spring2ts.core.typescript.writer.TSWritable;

import java.io.IOException;
import java.util.*;

/**
 * @author Sławomir Dadas
 */
public class TSEnumDef implements TSWritable {

    private TypeName name;

    private List<String> constants = new ArrayList<>();

    protected EnumSet<TSModifier> modifiers = EnumSet.noneOf(TSModifier.class);

    public TSEnumDef() {
    }

    public TSEnumDef(String type) {
        name(type);
    }

    @SuppressWarnings("unchecked")
    public TSEnumDef name(String value) {
        this.name = CustomType.of(value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public TSEnumDef name(TypeName value) {
        this.name = value;
        return this;
    }

    public TSEnumDef constant(String value) {
        this.constants.add(value);
        return this;
    }

    public TSEnumDef constants(Collection<String> values) {
        this.constants.addAll(values);
        return this;
    }

    public TSEnumDef constants(String... values) {
        this.constants.addAll(Arrays.asList(values));
        return this;
    }

    public TSEnumDef modifier(TSModifier first) {
        this.modifiers.add(first);
        return this;
    }

    public TSEnumDef modifiers(TSModifier first, TSModifier... values) {
        this.modifiers.addAll(EnumSet.of(first, values));
        return this;
    }

    @Override
    public void write(CodeWriter writer) throws IOException {
        Validate.notNull(name);
        writer.write(TSModifier.getString(modifiers));
        writer.write("enum ");
        writer.write(name.toDeclaration());
        writer.openBlock();
        writer.writelns(constants, ',', true);
        writer.closeBlock();
    }

    @Override
    public String getName() {
        return name.getBaseName();
    }
}
