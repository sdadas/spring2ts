package com.sdadas.spring2ts.examples.simple;

import com.sdadas.spring2ts.annotations.SharedModel;

/**
 * @author Sławomir Dadas
 */
@SharedModel
public class HelloResponse {

    private int id;

    private String greeting;

    public HelloResponse() {
    }

    public HelloResponse(int id, String greeting) {
        this.id = id;
        this.greeting = greeting;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }
}
