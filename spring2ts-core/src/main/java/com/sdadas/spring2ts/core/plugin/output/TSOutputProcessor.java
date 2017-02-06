package com.sdadas.spring2ts.core.plugin.output;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.sdadas.spring2ts.core.plugin.OutputType;
import com.sdadas.spring2ts.core.plugin.output.model.TypeContext;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.forge.roaster.model.Annotation;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.Type;
import org.jboss.forge.roaster.model.source.*;
import com.sdadas.spring2ts.core.plugin.meta.JavaTypescriptTypeMapping;
import com.sdadas.spring2ts.core.typescript.def.TSFile;
import com.sdadas.spring2ts.core.typescript.def.TSImport;
import com.sdadas.spring2ts.core.typescript.def.TSVarDef;
import com.sdadas.spring2ts.core.typescript.types.CustomType;
import com.sdadas.spring2ts.core.typescript.types.TypeName;
import com.sdadas.spring2ts.core.typescript.writer.CodeWriter;
import com.sdadas.spring2ts.core.typescript.writer.TSWritable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author Sławomir Dadas
 */
public abstract class TSOutputProcessor implements OutputProcessor, TypeMapper {

    protected final File outputDir;

    private final JavaTypescriptTypeMapping typeMapping;

    private Map<String, TSFile> files = new HashMap<>();

    private Map<String, String> knownTypes = new HashMap<>();

    private TSFile currentFile;

    public TSOutputProcessor(File outputDir) {
        this.outputDir = outputDir;
        this.typeMapping = new JavaTypescriptTypeMapping();
    }

    @Override
    public void apply(JavaType<?> type)  {
        String path = getFilePath(type);
        TSFile file = getFile(path);
        this.currentFile = file;
        TSWritable writable = transform(type);
        file.add(writable);
    }

    @Override
    public void writeOutput() {
        files.values().forEach(this::writeFile);
    }

    private TSFile getFile(String path) {
        TSFile file = files.get(path);
        if(file == null) {
            file = new TSFile(path);
            files.put(path, file);
        }
        return file;
    }

    private void writeFile(TSFile file) {
        File output = new File(outputDir, file.getPath());
        PrintWriter writer = null;
        try{
            FileUtils.forceMkdir(output.getParentFile());
            writer = new PrintWriter(new FileOutputStream(output, true));
            CodeWriter cw = new CodeWriter(writer);
            file.write(cw);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

    public abstract TSWritable transform(JavaType<?> type);

    protected boolean hasAnnotation(JavaType<?> type, String simpleName) {
        List<? extends Annotation<?>> annotations = type.getAnnotations();
        for (Annotation<?> annotation : annotations) {
            if(annotation.getName().equals(simpleName)) return true;
        }
        return false;
    }

    protected boolean isSupportedTSType(JavaType<?> type) {
        return type instanceof JavaClassSource
                || type instanceof JavaEnumSource
                || type instanceof JavaInterfaceSource;
    }

    public TypeName toTSType(Type<?> type) {
        return toTSType(type.getQualifiedNameWithGenerics());
    }

    public TypeName toTSType(Type<?> type, TypeContext context) {
        String name = type.getQualifiedNameWithGenerics();
        if(name.equals(context.getPackageName() + "." + type.getSimpleName())) {
            name = type.getSimpleName();
        }
        return toTSType(name, context);
    }

    public TypeName toTSType(String name) {
        return toTSType(name, null);
    }

    public TypeName toTSType(String name, TypeContext context) {
        if(StringUtils.isBlank(name)) return null;
        Set<String> contextualTypes = context != null ? context.getContextualTypes() : Collections.emptySet();
        CustomType type = typeMapping.getType(name, contextualTypes);
        imports(type.getBaseName());
        return type;
    }

    public List<TypeName> toTSTypes(Collection<String> names, TypeContext context) {
        if(names == null || names.isEmpty()) return Collections.emptyList();
        return names.stream().map(type -> toTSType(type, context)).collect(Collectors.toList());
    }

    protected <T extends JavaSource<T>> List<TSVarDef> createProperties(
            List<PropertySource<T>> properties, TypeContext context) {

        List<TSVarDef> results = new ArrayList<>();
        for (PropertySource<T> property : properties) {
            String name = property.getName();
            MethodSource<T> accessor = property.getAccessor();
            if(accessor != null) {
                Type<T> typeName = property.getType();
                TypeName type = toTSType(typeName, context);
                results.add(createField(name, type));
            }
        }
        return results;
    }

    protected TSVarDef createField(String name, TypeName type) {
        return new TSVarDef(name, type).optional();
    }

    public void imports(String name, String from) {
        currentFile.imports(name, from);
    }

    public void imports(TSImport imp) {
        currentFile.imports(imp);
    }

    public void imports(String name) {
        String from = knownTypes.get(name);
        if(StringUtils.isBlank(from)) {
            return;
        }
        String path = currentFile.getPath();
        if(StringUtils.equals(from, path)) {
            return; // ignore self import
        }
        currentFile.imports(name, relativePath(path, from));
    }

    private String relativePath(String source, String dest) {
        Path sourcePath = Paths.get(outputDir.getAbsolutePath(), source).getParent();
        Path destPath = Paths.get(outputDir.getAbsolutePath(), dest).getParent();
        String path = sourcePath.relativize(destPath).toString();
        if(StringUtils.isEmpty(path)) {
            path = "./";
        }
        if(!StringUtils.endsWith(path, "/")) {
            path += "/";
        }
        path = path.replace('\\', '/');
        return path + StringUtils.removeEnd(FilenameUtils.getName(dest), ".ts");
    }

    @Override
    public void setKnownTypes(Map<String, OutputType> types) {
        this.knownTypes = types.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getFilePath()));
        this.typeMapping.setKnownTypes(this.knownTypes);
    }

    @Override
    public TSFile createNewFile(String path) {
        TSFile file = new TSFile(path);
        files.put(path, file);
        return file;
    }
}
