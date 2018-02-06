package com.deity.flymessage.entity;

/**
 * Created by Mr.Jude on 2015/7/18.
 */
public class Person {
    private String face;
    private String name;

    private String sign;

    public Person(String face, String name, String sign) {
        this.face = face;
        this.name = name;
        this.sign = sign;
    }



    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
