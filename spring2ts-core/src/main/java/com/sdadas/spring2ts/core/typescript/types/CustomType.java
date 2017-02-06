package com.sdadas.spring2ts.core.typescript.types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Sławomir Dadas
 */
public class CustomType implements TypeName {

    private String name;

    private List<String> generics = new ArrayList<>();

    private int arrayDimensions;

    public static TypeName of(String name) {
        return new CustomType(name);
    }

    public CustomType() {
    }

    public CustomType(String name) {
        this.name = name;
    }

    public CustomType(String name, String generic) {
        this.name = name;
        this.generics = Collections.singletonList(generic);
    }

    public CustomType(String name, List<String> generics, int arrayDimensions) {
        this.name = name;
        this.generics = generics;
        this.arrayDimensions = arrayDimensions;
    }

    @Override
    public String getBaseName() {
        return name;
    }

    public void setBaseName(String name) {
        this.name = name;
    }

    @Override
    public List<String> getGenerics() {
        return generics;
    }

    public void setGenerics(List<String> generics) {
        this.generics = generics;
    }

    public void addGeneric(String generic) {
        this.generics.add(generic);
    }

    @Override
    public int getArrayDimensions() {
        return arrayDimensions;
    }

    public void setArrayDimensions(int arrayDimensions) {
        this.arrayDimensions = arrayDimensions;
    }
}
