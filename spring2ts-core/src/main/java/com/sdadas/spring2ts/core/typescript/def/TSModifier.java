package com.sdadas.spring2ts.core.typescript.def;

import java.util.EnumSet;
import java.util.stream.Collectors;

/**
 * @author Sławomir Dadas
 */
public enum TSModifier {
    EXPORT, PRIVATE, PUBLIC, STATIC;

    public static String getString(EnumSet<TSModifier> modifiers) {
        if(modifiers.isEmpty()) return "";
        String joined = modifiers.stream()
                .map(m -> m.name().toLowerCase())
                .collect(Collectors.joining(" "));
        return joined + " ";
    }
}
