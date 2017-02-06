package com.sdadas.spring2ts.examples.types.generics;

import com.sdadas.spring2ts.annotations.SharedModel;

import java.io.Serializable;
import java.util.List;

/**
 * @author Sławomir Dadas
 */
@SharedModel
public class GenericObject<T extends CharSequence, E extends Serializable> {

    private T field;

    private List<T> list;

    private List<GenericNestedObject<GenericNestedObject<T>>> nested;

    public T getField() {
        return field;
    }

    public void setField(T field) {
        this.field = field;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public List<GenericNestedObject<GenericNestedObject<T>>> getNested() {
        return nested;
    }

    public void setNested(List<GenericNestedObject<GenericNestedObject<T>>> nested) {
        this.nested = nested;
    }
}
