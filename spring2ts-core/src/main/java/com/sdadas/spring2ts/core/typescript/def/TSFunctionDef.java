package com.sdadas.spring2ts.core.typescript.def;

import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;
import com.sdadas.spring2ts.core.typescript.expression.TSWritableString;
import com.sdadas.spring2ts.core.typescript.types.BasicType;
import com.sdadas.spring2ts.core.typescript.types.TypeName;
import com.sdadas.spring2ts.core.typescript.types.VarType;
import com.sdadas.spring2ts.core.typescript.writer.CodeWriter;
import com.sdadas.spring2ts.core.typescript.writer.TSWritable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

/**
 * @author Sławomir Dadas
 */
public class TSFunctionDef implements TSWritable {

    private String name;

    private List<String> generics = new ArrayList<>();

    private List<TSVarDef> args = new ArrayList<>();

    private TypeName returnType;

    private TSWritable body;

    private EnumSet<TSModifier> modifiers = EnumSet.noneOf(TSModifier.class);

    public TSFunctionDef() {
    }

    public TSFunctionDef(String name, TypeName returnType) {
        this.name = name;
        this.returnType = returnType;
    }

    public TSFunctionDef name(String value) {
        this.name = value;
        return this;
    }

    public TSFunctionDef generics(String... values) {
        this.generics.addAll(Arrays.asList(values));
        return this;
    }

    public TSFunctionDef arg(TSVarDef value) {
        args.add(value);
        return this;
    }

    public TSFunctionDef args(TSVarDef... values) {
        args.addAll(Arrays.asList(values));
        return this;
    }

    public TSFunctionDef arg(String name, TypeName type) {
        TSVarDef value = new TSVarDef(name, type).varType(VarType.ARGUMENT);
        args.add(value);
        return this;
    }

    public TSFunctionDef returnType(TypeName value) {
        this.returnType = value;
        return this;
    }

    public TSFunctionDef modifier(TSModifier first) {
        this.modifiers.add(first);
        return this;
    }

    public TSFunctionDef modifiers(TSModifier first, TSModifier... values) {
        this.modifiers.addAll(EnumSet.of(first, values));
        return this;
    }

    public TSFunctionDef body(String value) {
        this.body = new TSWritableString(value, true);
        return this;
    }

    public TSFunctionDef body(TSWritable value) {
        this.body = value;
        return this;
    }

    public TSFunctionDef noBody() {
        this.body = null;
        return this;
    }

    public List<TSVarDef> getArgs() {
        return args;
    }

    public TypeName getReturnType() {
        return returnType;
    }

    public boolean isConstructor() {
        return StringUtils.equals(name, "constructor");
    }

    @Override
    public void write(CodeWriter writer) throws IOException {
        writer.writeln();
        writer.write(TSModifier.getString(modifiers));
        writer.write(name);
        writeGenerics(writer);
        writer.write("(");
        writeArguments(writer);
        writer.write(")");
        if(!isConstructor()) {
            TypeName ret = returnType != null ? returnType : BasicType.ANY;
            writer.write(": ").write(ret.toDeclaration());
        }
        writeBody(writer);
    }

    @Override
    public String getName() {
        return name;
    }

    private void writeGenerics(CodeWriter writer) throws IOException {
        if(generics.isEmpty()) return;
        String value = Joiner.on(", ").join(generics);
        writer.write("<");
        writer.write(value);
        writer.write(">");
    }

    private void writeArguments(CodeWriter writer) throws IOException {
        if(args.isEmpty()) return;
        for (int i = 0; i < args.size(); i++) {
            TSVarDef arg = args.get(i);
            arg.varType(VarType.ARGUMENT);
            arg.write(writer);
            if(i < args.size() - 1) writer.write(", ");
        }
    }

    private void writeBody(CodeWriter writer) throws IOException {
        if(body == null) {
            writer.write(";");
            return;
        }
        writer.openBlock();
        writer.write(body);
        writer.closeBlock();
    }
}
