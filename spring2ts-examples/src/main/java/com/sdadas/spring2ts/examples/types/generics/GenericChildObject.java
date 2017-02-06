package com.sdadas.spring2ts.examples.types.generics;

import com.sdadas.spring2ts.annotations.SharedModel;

/**
 * @author Sławomir Dadas
 */
@SharedModel
public class GenericChildObject<T extends String> extends GenericObject<T, Integer> {

}
