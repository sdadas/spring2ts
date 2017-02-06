package com.sdadas.spring2ts.test;

import com.google.common.io.Files;
import com.sdadas.spring2ts.core.plugin.OutputType;
import com.sdadas.spring2ts.core.plugin.SourceGen;
import com.sdadas.spring2ts.core.plugin.output.model.TSModelOutput;
import com.sdadas.spring2ts.core.typescript.def.TSFile;
import com.sdadas.spring2ts.core.typescript.expression.TSWritableString;
import com.sdadas.spring2ts.core.typescript.writer.CodeWriter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.Map;

/**
 * @author Sławomir Dadas
 */
public abstract class BaseClientBuilder implements ClientBuilder {

    private String serverUrl = "http://localhost";

    private String sourcePath = ".";

    @Override
    public ClientBuilder serverUrl(String serverUrl) {
        this.serverUrl = serverUrl;
        return this;
    }

    @Override
    public ClientBuilder sourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
        return this;
    }

    protected Map<String, OutputType> generateSources(File dir) throws IOException {
        SourceGen generator = new SourceGen(new File(sourcePath), dir);
        return generator.run();
    }

    protected void createEntryPoint(String fileName, File dir, Map<String, OutputType> imports) {
        TSFile file = new TSFile();
        for (Map.Entry<String, OutputType> entry : imports.entrySet()) {
            String from = "./" + StringUtils.removeEnd(entry.getValue().getFilePath(), ".ts");
            file.imports(entry.getKey(), from);
        }
        String appUrl = String.format("(window as any).APP_URL = \"%s\";", serverUrl);
        file.add(new TSWritableString(appUrl, false));

        for (Map.Entry<String, OutputType> entry : imports.entrySet()) {
            if(entry.getValue().getProcessorType().equals(TSModelOutput.class)) {
                continue;
            }
            String type = entry.getKey();
            String variable = String.format("(window as any).%s = %s;", type, type);
            file.add(new TSWritableString(variable, false));
        }

        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new File(dir, fileName), "UTF-8");
            CodeWriter cw = new CodeWriter(writer);
            cw.write(file);
            writer.flush();
        } catch (IOException e) {
            IOUtils.closeQuietly(writer);
        }
    }

    protected File createProjectDirectory() throws IOException {
        //File result = Files.createTempDir();
        //result.deleteOnExit();
        File result = new File("./" + System.currentTimeMillis());
        FileUtils.forceMkdir(result);
        return result;
    }

    protected void copyResources(String baseClassPath, File destDir, String... relativePaths) throws IOException {
        for (String relativePath : relativePaths) {
            String path = FilenameUtils.concat(baseClassPath, relativePath);
            path = path.replace('\\', '/');
            String fileName = path.contains("/") ? StringUtils.substringAfterLast(path, "/") : path;
            copyResource(new ClassPathResource(path), new File(destDir, fileName));
        }
    }

    protected void copyResource(Resource resource, File dest) throws IOException {
        InputStream is = resource.getInputStream();
        FileUtils.copyInputStreamToFile(is, dest);
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getSourcePath() {
        return sourcePath;
    }
}
