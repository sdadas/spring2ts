package com.sdadas.spring2ts.core.typescript.def;

import org.apache.commons.lang3.StringUtils;
import com.sdadas.spring2ts.core.typescript.types.BasicType;
import com.sdadas.spring2ts.core.typescript.types.TypeName;
import com.sdadas.spring2ts.core.typescript.types.VarType;
import com.sdadas.spring2ts.core.typescript.writer.CodeWriter;
import com.sdadas.spring2ts.core.typescript.writer.TSWritable;

import java.io.IOException;
import java.util.EnumSet;

/**
 * @author Sławomir Dadas
 */
public class TSVarDef implements TSWritable {

    private String name;

    private TypeName type;

    private String initialization;

    private EnumSet<TSModifier> modifiers = EnumSet.noneOf(TSModifier.class);

    private boolean optional;

    private VarType varType = VarType.INSTANCE;

    public TSVarDef() {
    }

    public TSVarDef(String name, TypeName type) {
        this.name = name;
        this.type = type;
    }

    public TSVarDef(String name, TypeName type, String initialization) {
        this.initialization = initialization;
        this.name = name;
        this.type = type;
    }

    public TSVarDef(String name, TypeName type, String initialization, boolean optional) {
        this.initialization = initialization;
        this.name = name;
        this.type = type;
        this.optional = optional;
    }

    public TSVarDef name(String value) {
        this.name = value;
        return this;
    }

    public TSVarDef type(TypeName value) {
        this.type = value;
        return this;
    }

    public TSVarDef initialization(String expression) {
        this.initialization = expression;
        return this;
    }

    public TSVarDef modifier(TSModifier first) {
        this.modifiers.add(first);
        return this;
    }

    public TSVarDef modifiers(TSModifier first, TSModifier... values) {
        this.modifiers.addAll(EnumSet.of(first, values));
        return this;
    }

    public TSVarDef optional() {
        this.optional = true;
        return this;
    }

    public TSVarDef varType(VarType value) {
        this.varType = value;
        return this;
    }

    @Override
    public void write(CodeWriter writer) throws IOException {
        if(!varType.equals(VarType.ARGUMENT)) writer.writeln();
        if(varType.equals(VarType.LOCAL)) {
            writer.write("let ");
        } else {
            writer.write(TSModifier.getString(modifiers));
        }
        writer.write(name);
        if(optional && varType.equals(VarType.INSTANCE)) writer.write("?");
        TypeName tn = type != null ? type : BasicType.ANY;
        writer.write(": ").write(tn.toDeclaration());
        if(StringUtils.isNotBlank(initialization)) {
            writer.write(" = ").write(initialization);
        }
        if(!varType.equals(VarType.ARGUMENT)) writer.write(";");
    }

    @Override
    public String getName() {
        return name;
    }

    public TypeName getType() {
        return type;
    }
}
