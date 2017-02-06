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
package com.sdadas.spring2ts.core.plugin.output.service;

import com.google.common.collect.Lists;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.source.MethodHolderSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import com.sdadas.spring2ts.core.plugin.output.service.method.ServiceMethod;

import java.util.List;

/**
 * @author Sławomir Dadas
 */
public class ServiceClass {

    private JavaType<?> type;

    private List<ServiceMethod> methods;

    public ServiceClass(JavaType<?> type) {
        this.type = type;
        this.methods = createMethods((MethodHolderSource<?>) type);
    }

    private List<ServiceMethod> createMethods(MethodHolderSource<?> source) {
        List<ServiceMethod> results = Lists.newArrayList();
        for (MethodSource<?> method : source.getMethods()) {
            results.add(new ServiceMethod((JavaType<?>) source, method));
        }
        return results;
    }

    public JavaType<?> getType() {
        return type;
    }

    public List<ServiceMethod> getMethods() {
        return methods;
    }
}
