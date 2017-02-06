package com.sdadas.spring2ts.plugin;

import com.sdadas.spring2ts.core.plugin.OutputType;
import com.sdadas.spring2ts.core.plugin.SourceGen;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Sławomir Dadas
 */
@Mojo(name = "spring2ts", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class Spring2TsMojo extends AbstractMojo {

    @Parameter(required = true)
    private File sourceDir;

    @Parameter(required = true)
    private File outputDir;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        SourceGen generator = new SourceGen(sourceDir, outputDir);
        try {
            Map<String, OutputType> result = generator.run();
            int size = result.size();
            if(size > 0) {
                String types = result.values().stream().map(OutputType::getName).collect(Collectors.joining(", "));
                String msg = String.format("Generated %d types in %s: %s", size, outputDir.getCanonicalPath(), types);
                getLog().info(msg);
            } else {
                getLog().info("No types generated");
            }
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to generate TypeScript source", e);
        }
    }
}
