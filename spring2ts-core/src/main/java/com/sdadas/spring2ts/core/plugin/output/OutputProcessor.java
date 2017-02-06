package com.sdadas.spring2ts.core.plugin.output;

import com.sdadas.spring2ts.core.plugin.OutputType;
import org.jboss.forge.roaster.model.JavaType;
import com.sdadas.spring2ts.core.typescript.def.TSFile;

import java.util.Map;

/**
 * @author Sławomir Dadas
 */
public interface OutputProcessor {

    boolean filter(JavaType<?> type);

    void apply(JavaType<?> type);

    void setKnownTypes(Map<String, OutputType> types);

    String getFilePath(JavaType<?> type);

    String getFilePath(String name);

    TSFile createNewFile(String path);

    void writeOutput();
}
