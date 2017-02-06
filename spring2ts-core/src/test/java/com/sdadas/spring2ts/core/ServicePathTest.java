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
package com.sdadas.spring2ts.core;

import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Test;
import com.sdadas.spring2ts.core.plugin.output.service.method.ServicePath;

/**
 * @author Sławomir Dadas
 */
public class ServicePathTest {

    @Test
    public void testPaths() {
        expect("/owners/{ownerId}/pets/{petId}",
                "/owners/${ownerId}/pets/${petId}", "ownerId", "petId");
        expect("/spring-web/{symbolicName:[a-z-]+}-{version:\\d\\.\\d\\.\\d}{extension:\\.[a-z]+}",
                "/spring-web/${symbolicName}-${version}${extension}", "symbolicName", "version", "extension");
        expect("/public/*/{a}/{b}/{c}/**", "/public/_/${a}/${b}/${c}/_", "a", "b", "c");
    }

    private void expect(String original, String result, String... variables) {
        ServicePath path = new ServicePath(original);
        Assert.assertEquals(result, path.getSimplified());
        Assert.assertEquals(path.getVariables(), Sets.newHashSet(variables));
    }
}
