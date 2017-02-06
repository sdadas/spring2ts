package com.sdadas.spring2ts.core.plugin;

import com.google.common.base.Charsets;
import com.google.common.collect.FluentIterable;
import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import com.sdadas.spring2ts.core.plugin.output.model.TSModelOutput;
import com.sdadas.spring2ts.core.plugin.output.service.TSServiceOutput;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author Sławomir Dadas
 */
public class SourceGen {

    private final File inputDir;

    private final File outputDir;

    public SourceGen(File inputDir, File outputDir) {
        this.inputDir = inputDir;
        this.outputDir = outputDir;
    }

    public Map<String, OutputType> run() throws IOException {
        JavaTypeContainer container = new JavaTypeContainer();
        scan(container);
        container.addProcessor(new TSModelOutput(outputDir));
        container.addProcessor(new TSServiceOutput(outputDir));
        cleanOutputDir();
        container.process();
        return container.getOutputTypes();
    }

    private void cleanOutputDir() throws IOException {
        if(outputDir.exists() && !outputDir.isDirectory()) {
            throw new IllegalStateException(outputDir.getAbsolutePath() + " is not a directory!");
        }

        if(outputDir.exists()) {
            FileUtils.forceDelete(outputDir);
        }
        FileUtils.forceMkdir(outputDir);
    }

    private JavaTypeContainer scan(JavaTypeContainer container) throws IOException {
        FluentIterable<File> iterable = Files.fileTreeTraverser()
                .breadthFirstTraversal(inputDir)
                .filter(f -> StringUtils.endsWithIgnoreCase(f.getName(), "java"));
        for (File file : iterable) {
            if(file.isDirectory()) continue;
            String src = Files.toString(file, Charsets.UTF_8);
            container.parse(src);
        }
        return container;
    }
}
