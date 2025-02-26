package com.example.appbangiay.models;

public class AttributeModel {
    public static final int SIZE_TYPE = 0;
    public static final int COLOR_TYPE = 1;
    private int type;
    private String value;

    public AttributeModel(int type, String value) {
        this.type = type;
        this.value = value;
    };

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
