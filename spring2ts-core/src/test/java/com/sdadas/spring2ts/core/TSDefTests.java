package com.sdadas.spring2ts.core;

import com.sdadas.spring2ts.core.typescript.def.*;
import com.sdadas.spring2ts.core.typescript.types.BasicType;
import com.sdadas.spring2ts.core.typescript.types.CustomType;
import com.sdadas.spring2ts.core.typescript.writer.CodeWriter;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Sławomir Dadas
 */
public class TSDefTests {

    public static void main(String[] args) throws IOException {

        PrintWriter writer = new PrintWriter(System.out);
        CodeWriter cw = new CodeWriter(writer);
        enumTest(cw);
        cw.write("\n\n");
        interfaceTest(cw);
        writer.flush();
    }

    private static void enumTest(CodeWriter cw) throws IOException {
        TSEnumDef enumDef = new TSEnumDef().modifier(TSModifier.EXPORT);
        enumDef.name("Color").constants("Black", "Red", "White");
        enumDef.write(cw);
    }

    private static void interfaceTest(CodeWriter cw) throws IOException {
        TSInterfaceDef interfaceDef = new TSInterfaceDef();
        CustomType type = new CustomType("Interfejs");
        type.addGeneric("T extends String");
        interfaceDef.name(type)
                .modifiers(TSModifier.EXPORT, TSModifier.PUBLIC)
                .extendsTypes(CustomType.of("Costam"), BasicType.BOOLEAN)
                .field(new TSVarDef("first", BasicType.NUMBER, "123"))
                .field(new TSVarDef("second",CustomType.of("Object"), "null", true));

        TSVarDef fd = new TSVarDef().name("third").optional()
                .type(BasicType.arrayOf(BasicType.STRING));
        fd.modifiers(TSModifier.PUBLIC, TSModifier.STATIC);
        interfaceDef.field(fd);

        TSFunctionDef func = new TSFunctionDef("funkcja", BasicType.STRING).noBody();
        interfaceDef.function(func);

        TSFunctionDef constructor = new TSFunctionDef().name("constructor").noBody();
        interfaceDef.function(constructor);

        interfaceDef.write(cw);
    }
}
