package com.sdadas.spring2ts.examples;

import com.sdadas.spring2ts.examples.simple.HelloResponse;
import com.sdadas.spring2ts.test.ClientTester;
import com.sdadas.spring2ts.test.JQueryClientBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author Sławomir Dadas
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {Application.class})
public class ExamplesClientServerTests {

    @Autowired
    private Environment env;

    @Test
    public void simpleTest() throws IOException {
        int port = Integer.parseInt(env.getProperty("local.server.port"));
        JQueryClientBuilder builder = new JQueryClientBuilder();
        builder.serverUrl("http://localhost:" + port);
        builder.build();

        try(ClientTester tester = builder.test()) {
            tester.waitForReady();
            tester.expect("new HelloController().hello('world')", new HelloResponse(1, "Hello world"));
            tester.sleep(1000);
            tester.assertValid();
        }
    }
}
