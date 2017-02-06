package com.sdadas.spring2ts.core.typescript.def;

import org.apache.commons.lang3.StringUtils;
import com.sdadas.spring2ts.core.typescript.types.TypeName;
import com.sdadas.spring2ts.core.typescript.writer.CodeWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sławomir Dadas
 */
public class TSClassDef extends TSTypeDef<TSClassDef> {

    private TypeName extendsType;

    private List<TypeName> implementsTypes = new ArrayList<>();

    public TSClassDef extendsType(TypeName value) {
        this.extendsType = value;
        return this;
    }

    public TSClassDef implementsType(TypeName value) {
        this.implementsTypes.add(value);
        return this;
    }

    public TSClassDef implementsTypes(TypeName... values) {
        this.implementsTypes.addAll(Arrays.asList(values));
        return this;
    }

    public TSClassDef implementsTypes(Collection<TypeName> values) {
        this.implementsTypes.addAll(values);
        return this;
    }

    @Override
    public void writeNameDef(CodeWriter writer) throws IOException {
        writer.write(TSModifier.getString(modifiers));
        writer.write("class ");
        writer.write(name.toDeclaration());
        writeExtendType(writer);
        writeImplementsTypes(writer);
    }

    private void writeExtendType(CodeWriter writer) throws IOException {
        if(extendsType == null || extendsType.isAny()) return;
        writer.write(" extends ").write(extendsType.toDeclaration());
    }

    private void writeImplementsTypes(CodeWriter writer) throws IOException {
        if(implementsTypes.isEmpty()) return;
        String types = implementsTypes.stream()
                .filter(t -> t != null && !t.isAny())
                .map(TypeName::toDeclaration)
                .collect(Collectors.joining(", "));
        if (StringUtils.isNotBlank(types)) {
            writer.write(" implements ").write(types);
        }
    }
}
