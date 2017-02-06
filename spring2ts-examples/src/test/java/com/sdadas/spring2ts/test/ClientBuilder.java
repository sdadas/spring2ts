package com.sdadas.spring2ts.test;

import java.io.IOException;

/**
 * @author Sławomir Dadas
 */
public interface ClientBuilder {

    ClientBuilder build() throws IOException;

    ClientBuilder serverUrl(String serverUrl);

    ClientBuilder sourcePath(String sourcePath);

    ClientTester test();
}
