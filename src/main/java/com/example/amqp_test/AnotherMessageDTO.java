package com.example.amqp_test;

public class AnotherMessageDTO {
    private int numericField;

    public int getNumericField() {
        return numericField;
    }

    public void setNumericField(int numericField) {
        this.numericField = numericField;
    }

    @Override
    public String toString() {
        return "AnotherMessageDTO{" +
                "numericField=" + numericField +
                '}';
    }
}
