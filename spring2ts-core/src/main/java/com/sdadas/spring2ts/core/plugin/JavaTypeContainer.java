package com.sdadas.spring2ts.core.plugin;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.JavaUnit;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.source.TypeHolderSource;
import com.sdadas.spring2ts.core.plugin.output.OutputProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sławomir Dadas
 */
public class JavaTypeContainer {

    private Map<String, JavaType<?>> javaTypes = new HashMap<>();

    private Map<String, OutputType> outputTypes = new HashMap<>();

    private List<OutputProcessor> processors = new ArrayList<>();

    public List<OutputProcessor> getProcessors() {
        return processors;
    }

    public void setProcessors(List<OutputProcessor> processors) {
        this.processors = processors;
    }

    public void addProcessor(OutputProcessor processor) {
        this.processors.add(processor);
    }

    public void parse(String file) {
        JavaUnit unit = Roaster.parseUnit(file);
        List<JavaType<?>> types = unit.getTopLevelTypes();
        for (JavaType<?> type : types) {
            cache(type);
        }
    }

    public void process() {
        initOutputTypes();
        processors.forEach(p -> p.setKnownTypes(outputTypes));
        javaTypes.values().forEach(this::process);
        processors.forEach(OutputProcessor::writeOutput);
    }

    private void initOutputTypes() {
        for (Map.Entry<String, JavaType<?>> entry : javaTypes.entrySet()) {
            JavaType<?> type = entry.getValue();
            OutputType outputType = getOutputType(type);
            if(outputType != null) outputTypes.put(type.getName(), outputType);
        }
    }

    private OutputType getOutputType(JavaType<?> type) {
        for (OutputProcessor processor : processors) {
            if(processor.filter(type)) {
                return new OutputType(type.getName(), processor.getFilePath(type), processor.getClass());
            }
        }
        return null;
    }

    private void process(JavaType<?> type) {
        for (OutputProcessor processor : processors) {
            if(processor.filter(type)) {
                processor.apply(type);
                return;
            }
        }
    }

    private void cache(JavaType<?> type) {
        if(type instanceof TypeHolderSource<?>) {
            List<JavaSource<?>> nested = ((TypeHolderSource<?>) type).getNestedTypes();
            for (JavaSource<?> child : nested) {
                cache(child);
            }
        }

        String name = type.getName();
        if(javaTypes.containsKey(name)) {
            throw new IllegalStateException("Two java javaTypes defined with the same name: " + name);
        }
        javaTypes.put(name, type);
    }

    public Map<String, OutputType> getOutputTypes() {
        return outputTypes;
    }
}
