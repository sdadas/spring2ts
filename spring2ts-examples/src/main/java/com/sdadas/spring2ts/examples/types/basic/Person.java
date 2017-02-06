package com.sdadas.spring2ts.examples.types.basic;

import com.sdadas.spring2ts.annotations.SharedModel;

import java.util.Date;

/**
 * @author Sławomir Dadas
 */
@SharedModel
public class Person {

    private String name;

    private int age;

    private Double height;

    private Address address;

    private Date dateOfBirth;

    private Object extraField1;

    private NotSharedObject extraField2;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Object getExtraField1() {
        return extraField1;
    }

    public void setExtraField1(Object extraField1) {
        this.extraField1 = extraField1;
    }

    public NotSharedObject getExtraField2() {
        return extraField2;
    }

    public void setExtraField2(NotSharedObject extraField2) {
        this.extraField2 = extraField2;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
