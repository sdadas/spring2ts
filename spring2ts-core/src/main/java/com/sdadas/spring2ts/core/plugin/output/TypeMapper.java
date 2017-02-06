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
package com.sdadas.spring2ts.core.plugin.output;

import com.sdadas.spring2ts.core.plugin.output.model.TypeContext;
import org.jboss.forge.roaster.model.Type;
import com.sdadas.spring2ts.core.typescript.def.TSImport;
import com.sdadas.spring2ts.core.typescript.types.TypeName;

import java.util.Collection;
import java.util.List;

/**
 * @author Sławomir Dadas
 */
public interface TypeMapper {

    TypeName toTSType(Type<?> type);

    TypeName toTSType(Type<?> type, TypeContext context);

    TypeName toTSType(String name);

    TypeName toTSType(String name, TypeContext context);

    List<TypeName> toTSTypes(Collection<String> names, TypeContext context);

    void imports(String name, String from);

    void imports(TSImport imp);

    void imports(String name);
}
