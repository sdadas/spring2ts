package com.sdadas.spring2ts.examples.types.generics;

import com.sdadas.spring2ts.annotations.SharedModel;

/**
 * @author Sławomir Dadas
 */
@SharedModel
public class GenericNestedObject<T> {

    private T field;

    public T getField() {
        return field;
    }

    public void setField(T field) {
        this.field = field;
    }
}
