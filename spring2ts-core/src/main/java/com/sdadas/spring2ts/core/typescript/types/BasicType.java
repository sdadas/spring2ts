package com.sdadas.spring2ts.core.typescript.types;

import java.util.Collections;
import java.util.List;

/**
 * @author Sławomir Dadas
 */
public enum BasicType implements TypeName {

    BOOLEAN("boolean"), STRING("string"), NUMBER("number"), ANY("any"), VOID("void");

    private final String name;

    BasicType(String name) {
        this.name = name;
    }

    @Override
    public String getBaseName() {
        return name;
    }

    @Override
    public List<String> getGenerics() {
        return Collections.emptyList();
    }

    @Override
    public int getArrayDimensions() {
        return 0;
    }

    public static TypeName arrayOf(TypeName type) {
        return new CustomType(type.getBaseName(), type.getGenerics(), 0);
    }
}
