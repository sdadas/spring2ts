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
public class TSInterfaceDef extends TSTypeDef<TSInterfaceDef> {

    private List<TypeName> extendsTypes = new ArrayList<>();

    public TSInterfaceDef extendsType(TypeName type) {
        this.extendsTypes.add(type);
        return this;
    }

    public TSInterfaceDef extendsTypes(Collection<TypeName> types) {
        this.extendsTypes.addAll(types);
        return this;
    }

    public TSInterfaceDef extendsTypes(TypeName... types) {
        extendsTypes.addAll(Arrays.asList(types));
        return this;
    }

    @Override
    public void writeNameDef(CodeWriter writer) throws IOException {
        writer.write(TSModifier.getString(modifiers));
        writer.write("interface ");
        writer.write(name.toDeclaration());
        writeExtendsTypes(writer);
    }

    private void writeExtendsTypes(CodeWriter writer) throws IOException {
        if(extendsTypes.isEmpty()) return;
        String types = extendsTypes.stream()
                .filter(t -> t != null && !t.isAny())
                .map(TypeName::toDeclaration)
                .collect(Collectors.joining(", "));
        if(StringUtils.isNotBlank(types)) {
            writer.write(" extends ").write(types);
        }
    }
}
