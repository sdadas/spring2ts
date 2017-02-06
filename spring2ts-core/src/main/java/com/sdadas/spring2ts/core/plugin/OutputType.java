package com.sdadas.spring2ts.core.plugin;

import java.io.Serializable;

/**
 * @author Sławomir Dadas
 */
public class OutputType implements Serializable {

    private String name;

    private String filePath;

    private Class<?> processorType;

    public OutputType(String name, String filePath, Class<?> processorType) {
        this.name = name;
        this.filePath = filePath;
        this.processorType = processorType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Class<?> getProcessorType() {
        return processorType;
    }

    public void setProcessorType(Class<?> processorType) {
        this.processorType = processorType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
