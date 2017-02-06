package com.sdadas.spring2ts.core.typescript.types;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;

/**
 * @author Sławomir Dadas
 */
public interface TypeName {

    String getBaseName();

    List<String> getGenerics();

    int getArrayDimensions();

    default boolean isVoid() {
        return isSimpleType("void");
    }

    default boolean isAny() {
        return isSimpleType("any");
    }

    default boolean isPrimitive() {
        return isSimpleType("string", "number", "boolean");
    }

    default boolean isSimpleType(String... names) {
        Set<String> set = Sets.newHashSet(names);
        return set.contains(getBaseName()) && getGenerics().isEmpty() && getArrayDimensions() == 0;
    }

    default boolean isSimpleType(String typeName) {
        return typeName.equals(getBaseName()) && getGenerics().isEmpty() && getArrayDimensions() == 0;
    }

    default boolean isGeneric() {
        List<String> generics = getGenerics();
        return generics != null && generics.size() > 0;
    }

    default String toDeclaration() {
        StringBuilder builder = new StringBuilder();
        builder.append(getBaseName());
        if(isGeneric()) {
            builder.append('<');
            List<String> generics = getGenerics();
            builder.append(Joiner.on(", ").join(generics));
            builder.append('>');
        }
        if(getArrayDimensions() > 0) {
            builder.append(StringUtils.repeat("[]", getArrayDimensions()));
        }
        return builder.toString();
    }
}
