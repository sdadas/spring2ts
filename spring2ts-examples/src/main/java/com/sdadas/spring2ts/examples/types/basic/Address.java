package com.sdadas.spring2ts.examples.types.basic;

import com.sdadas.spring2ts.annotations.SharedModel;

/**
 * @author Sławomir Dadas
 */
@SharedModel
public class Address {

    private String city;

    private String street;

    private int number;

    private String postalCode;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
