package com.sdadas.spring2ts.core;

import com.sdadas.spring2ts.core.plugin.SourceGen;

import java.io.File;
import java.io.IOException;

/**
 * @author Sławomir Dadas
 */
public class SourceGenTest {

    private final static String BASE_PATH = "E:/Projects/";

    public static void main(String [] args) throws IOException {
        SourceGen sg = new SourceGen(
                new File(BASE_PATH + "utils\\angular-example\\example-shared\\src\\main\\java"),
                new File(BASE_PATH + "utils\\angular-example\\target"));
        sg.run();
    }
}
