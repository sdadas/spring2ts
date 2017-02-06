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
package com.sdadas.spring2ts.core.plugin.output.service.method;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Set;

/**
 * @author Sławomir Dadas
 */
public class ServicePath implements Serializable {

    private final String path;

    private final Set<String> variables = Sets.newHashSet();

    private final String simplified;

    public ServicePath(String path) {
        this.path = path;
        this.simplified = parsePath(path, variables);
    }

    private String parsePath(String path, Set<String> variables) {
        char[] chars = path.toCharArray();
        StringBuilder result = new StringBuilder();
        int start = 0;
        int count = 0;
        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            if(ch == '{') {
                count++;
                if(count == 1) start = i;
            } else if(ch == '}') {
                count--;
                if(count == 0) result.append(extractVariableName(path, start, i + 1, variables));
            } else {
                if(count == 0) result.append(ch);
            }
            if(count < 0) {
                throw new IllegalArgumentException("Invalid path: " + path);
            }
        }
        return replaceWildcards(result.toString());
    }

    private String extractVariableName(String path, int start, int end, Set<String> variables) {
        String substring = StringUtils.substring(path, start, end);
        String stripped = StringUtils.strip(substring, "{}");
        String variable = StringUtils.substringBefore(stripped, ":");
        variables.add(variable);
        return String.format("${%s}", variable);
    }

    private String replaceByIndex(String value, String replacement, int start, int end) {
        return value.substring(0, start) + replacement + value.substring(end);
    }

    private String replaceWildcards(String path) {
        return path.replaceAll("\\*+", "_");
    }

    public boolean hasVariable(String var) {
        return variables.contains(var);
    }

    public Set<String> getVariables() {
        return variables;
    }

    public String getSimplified() {
        return simplified;
    }

    public String getPath() {
        return path;
    }
}
