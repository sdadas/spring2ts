package com.sdadas.spring2ts.core.plugin.meta;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.StringUtils;
import com.sdadas.spring2ts.core.typescript.types.CustomType;
import com.sdadas.spring2ts.core.typescript.types.TypeName;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Sławomir Dadas
 */
public class JavaTypescriptTypeMapping {

    private final static Pattern GENERIC_PARAM_PATTERN = Pattern.compile("\\w+(\\[\\])?([\\w\\.,<>\\[\\]]*?>)?(,|$)");

    private final static String ANY_TYPE = "any";

    private Map<String, String> mapping = createMapping();

    private Set<String> javaSimpleCollectionNames = createSimpleCollectionNames();

    private Set<String> javaOtherCollectionNames = createOtherCollectionNames();

    private Map<String, String> knownTypes;

    public CustomType getType(String name, Set<String> context) {
        if(context.contains(name)) {
            return new CustomType(name);
        } else if(mapping.containsKey(name)) {
            String res = mapping.get(name);
            return res != null ? new CustomType(res) : null;
        } else if(StringUtils.endsWith(name, "[]")) {
            CustomType res = getType(StringUtils.removeEnd(name, "[]"), context);
            if(res == null) res = new CustomType(ANY_TYPE);
            res.setArrayDimensions(res.getArrayDimensions() + 1);
            return res;
        } else if(StringUtils.contains(name, "<")) {
            return getTypeWithGenerics(name, context);
        } else if(StringUtils.endsWith(name, ".void")) {
            return new CustomType("void");
        }
        return getKnownType(name, Collections.emptyList(), 0);
    }

    private CustomType getKnownType(String name, List<String> generics, int arrayDimensions) {
        String baseName = stripPackage(name);
        if(knownTypes == null || knownTypes.containsKey(baseName)) {
            return new CustomType(baseName, generics, arrayDimensions);
        } else {
            return new CustomType(ANY_TYPE, Collections.emptyList(), arrayDimensions);
        }
    }

    private String stripPackage(String name) {
        String canonicalName = StringUtils.substringBefore(name, "<");
        if(StringUtils.contains(canonicalName, '.')) {
            return StringUtils.substringAfterLast(canonicalName, ".");
        } else {
            return canonicalName;
        }
    }

    private String stripGenerics(String name) {
        return StringUtils.substringBefore(name, "<");
    }

    private List<String> getGenericParams(String name) {
        String generics = StringUtils.substringBeforeLast(StringUtils.substringAfter(name, "<"), ">");
        generics = generics.replaceAll("\\s+", "");
        Matcher matcher = GENERIC_PARAM_PATTERN.matcher(generics);
        List<String> results = new ArrayList<>();
        String current = "";
        while(matcher.find()) {
            current += matcher.group();
            if(checkRightNumberOfParenthesis(current)) {
                results.add(StringUtils.strip(current, ","));
                current = "";
            }
        }
        if(StringUtils.isNotBlank(current)) {
            results.add(current);
        }
        return results;
    }

    private boolean checkRightNumberOfParenthesis(String group) {
        return StringUtils.countMatches(group, '<') == StringUtils.countMatches(group, '>');
    }

    private CustomType getTypeWithGenerics(String name, Set<String> context) {
        String canonical = stripGenerics(name);
        if(javaSimpleCollectionNames.contains(canonical)) {
            String param = getGenericParams(name).get(0);
            CustomType res = getType(param, context);
            if(res == null) res = new CustomType(ANY_TYPE);
            res.setArrayDimensions(res.getArrayDimensions() + 1);
            return res;
        } else if(javaOtherCollectionNames.contains(canonical)) {
            return new CustomType(ANY_TYPE);
        } else {
            List<String> names = getGenericParams(name);
            List<String> types = names.stream()
                    .map(type -> getType(type, context))
                    .map(TypeName::toDeclaration)
                    .collect(Collectors.toList());
            return getKnownType(name, types, 0);
        }
    }

    private Map<String, String> createMapping() {
        Map<String, String> map = new HashMap<>();
        mapPrimitives(map);
        mapBasicTypes(map);
        return map;
    }

    private void mapPrimitives(Map<String, String> map) {
        map.put("byte", "number");
        map.put("short", "number");
        map.put("int", "number");
        map.put("long", "number");
        map.put("float", "number");
        map.put("double", "number");
        map.put("boolean", "boolean");
        map.put("char", "string");
        map.put("java.lang.Byte", "number");
        map.put("java.lang.Short", "number");
        map.put("java.lang.Integer", "number");
        map.put("java.lang.Long", "number");
        map.put("java.lang.Float", "number");
        map.put("java.lang.Double", "number");
        map.put("java.lang.Boolean", "boolean");
        map.put("Byte", "number");
        map.put("Short", "number");
        map.put("Integer", "number");
        map.put("Long", "number");
        map.put("Float", "number");
        map.put("Double", "number");
        map.put("Boolean", "boolean");
        map.put("java.lang.String", "string");
        map.put("String", "string");
        map.put("java.lang.Character", "string");
        map.put("Character", "string");
    }

    private void mapBasicTypes(Map<String, String> map) {
        map.put("void", "void");
        map.put("java.lang.Void", "void");
        map.put("Void", "void");
        map.put("java.lang.Object", "any");
        map.put("Object", "any");
        map.put("java.lang.BigDecimal", "number");
        map.put("java.lang.BigInteger", "number");
        map.put("java.util.Date", "number");
        map.put("Date", "number");
        map.put("java.util.Calendar", "number");
        map.put("java.util.GregorianCalendar", "number");
        map.put("sun.util.BuddhistCalendar", "number");
        map.put("java.util.JapaneseImperialCalendar", "number");
    }

    private Set<String> createSimpleCollectionNames() {
        ImmutableSet.Builder<String> set = ImmutableSet.builder();
        set.add("java.util.Collection");
        set.add("java.util.Deque", "java.util.Queue");
        set.add("java.util.PriorityQueue", "java.util.ArrayDeque");
        set.add("java.util.EnumSet", "java.util.Set", "java.util.SortedSet", "java.util.NavigableSet");
        set.add("java.util.HashSet", "java.util.TreeSet", "java.util.Hashtable", "java.util.LinkedHashSet");
        set.add("java.util.List", "java.util.Vector", "java.util.Stack");
        set.add("java.util.ArrayList", "java.util.LinkedList");
        set.add("Collection", "Deque", "Queue", "PriorityQueue", "ArrayDeque");
        set.add("EnumSet", "Set", "SortedSet", "NavigableSet", "HashSet", "TreeSet", "Hashtable", "LinkedHashSet");
        set.add("List", "Vector", "Stack", "ArrayList", "LinkedList");
        return set.build();
    }

    private Set<String> createOtherCollectionNames() {
        ImmutableSet.Builder<String> set = ImmutableSet.builder();
        set.add("java.util.Map", "java.util.NavigableMap", "java.util.SortedMap");
        set.add("java.util.HashMap", "java.util.TreeMap", "java.util.LinkedHashMap");
        set.add("Map", "NavigableMap", "SortedMap", "HashMap", "TreeMap", "LinkedHashMap");
        return set.build();
    }

    public void setKnownTypes(Map<String, String> knownTypes) {
        this.knownTypes = knownTypes;
    }
}
