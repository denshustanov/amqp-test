package com.example.amqp_test;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class MessageDTO implements Serializable {
    private String field1;
    private String field2;

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
                "field1='" + field1 + '\'' +
                ", field2='" + field2 + '\'' +
                '}';
    }
}
