package com.sdadas.spring2ts.test;

import com.sdadas.spring2ts.core.plugin.OutputType;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author Sławomir Dadas
 */
public class JQueryClientBuilder extends BaseClientBuilder {

    private File projectDir;

    private JQueryClientBuilder createProject() throws IOException {
        File dir = createProjectDirectory();
        Map<String, OutputType> imports = generateSources(dir);
        copyResources("/test", dir, "system.js", "lodash.core.min.js", "utils.js",
                "jquery/jquery.d.ts", "jquery/jquery.min.js", "jquery/index.html");
        createEntryPoint("app.ts", dir, imports);
        this.projectDir = dir;
        return this;
    }

    private void buildProject() throws IOException {
        String cmd = SystemUtils.IS_OS_WINDOWS ? "tsc.cmd" : "tsc";
        String args = " --outFile app.js --target ES5 --allowJs --module system app.ts jquery.d.ts jquery.min.js";
        Process process = Runtime.getRuntime().exec(cmd + args, null, this.projectDir);
        try {
            InputStream is = process.getInputStream();
            String output = IOUtils.toString(is, "utf-8");
            int status = process.waitFor();
            System.out.println(output);
            if(status != 0) throw new IllegalStateException("tsc exited with an error, exit status = " + status);
        } catch (InterruptedException e) {
            String msg = "Typescript compiler is required to run this test, please make sure 'tsc' command is available";
            throw new IllegalStateException(msg, e);
        }
    }

    public JQueryClientBuilder build() throws IOException {
        createProject();
        buildProject();
        return this;
    }

    public ClientTester test() {
        ClientTester result = new ClientTester();
        result.open(new File(projectDir, "index.html"));
        return result;
    }
}
