package com.sdadas.spring2ts.core.plugin.output.model;

import com.sdadas.spring2ts.core.plugin.output.TSOutputProcessor;
import com.sdadas.spring2ts.core.typescript.def.TSEnumDef;
import com.sdadas.spring2ts.core.typescript.def.TSInterfaceDef;
import com.sdadas.spring2ts.core.typescript.def.TSModifier;
import com.sdadas.spring2ts.core.typescript.types.CustomType;
import com.sdadas.spring2ts.core.typescript.types.TypeName;
import com.sdadas.spring2ts.core.typescript.writer.TSWritable;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.source.*;

import java.io.File;
import java.util.List;

/**
 * @author Sławomir Dadas
 */
public class TSModelOutput extends TSOutputProcessor {

    public TSModelOutput(File outputDir) {
        super(outputDir);
    }

    @Override
    public TSWritable transform(JavaType<?> type) {
        if(type instanceof JavaClassSource) {
            return createClass((JavaClassSource) type);
        } else if(type instanceof JavaInterfaceSource) {
            return createInterface((JavaInterfaceSource) type);
        } else if(type instanceof JavaEnumSource) {
            return createEnum((JavaEnumSource) type);
        }
        throw new IllegalArgumentException("Unknown type: " + type.getClass().getSimpleName());
    }

    private TSWritable createClass(JavaClassSource type) {
        TSInterfaceDef ret = new TSInterfaceDef();
        TypeName parent = createTypeName(type);
        TypeContext context = new TypeContext(parent, type.getPackage());
        ret.name(parent);
        ret.extendsType(toTSType(type.getSuperType(), context));
        ret.extendsTypes(toTSTypes(type.getInterfaces(), context));
        ret.modifiers(TSModifier.EXPORT);
        ret.fields(createProperties(type.getProperties(), context));
        return ret;
    }

    private TSWritable createInterface(JavaInterfaceSource type) {
        TSInterfaceDef ret = new TSInterfaceDef();
        TypeName parent = createTypeName(type);
        TypeContext context = new TypeContext(parent, type.getPackage());
        ret.name(parent);
        ret.extendsTypes(toTSTypes(type.getInterfaces(), context));
        ret.modifiers(TSModifier.EXPORT);
        ret.fields(createProperties(type.getProperties(), context));
        return ret;
    }

    private TSWritable createEnum(JavaEnumSource type) {
        TSEnumDef ret = new TSEnumDef();
        TypeName parent = createTypeName(type);
        ret.name(parent);
        List<EnumConstantSource> constants = type.getEnumConstants();
        for (EnumConstantSource constant : constants) {
            ret.constant(constant.getName());
        }
        ret.modifier(TSModifier.EXPORT);
        return ret;
    }

    @SuppressWarnings("unchecked")
    private TypeName createTypeName(JavaType<?> type) {
        TypeName result = new CustomType(type.getName());
        if(type instanceof GenericCapableSource) {
            List<TypeVariableSource<?>> variables = ((GenericCapableSource) type).getTypeVariables();
            for (TypeVariableSource<?> variable : variables) {
                result.getGenerics().add(variable.getName());
            }
        }
        return result;
    }

    @Override
    public boolean filter(JavaType<?> type) {
        return hasAnnotation(type, "SharedModel") && isSupportedTSType(type);
    }

    @Override
    public String getFilePath(JavaType<?> type) {
        return "model/model.ts";
    }

    @Override
    public String getFilePath(String name) {
        return "model/" + name;
    }
}
