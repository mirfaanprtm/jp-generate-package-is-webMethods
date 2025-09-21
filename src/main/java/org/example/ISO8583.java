package org.example;

public class ISO8583 {
    private String bitNumber;
    private String description;

    public ISO8583(String bitNumber, String description) {
        this.bitNumber = bitNumber;
        this.description = description;
    }

    public String getBitNumber() {
        return bitNumber;
    }

    public void setBitNumber(String bitNumber) {
        this.bitNumber = bitNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "org.example.ISO8583DataElements{" +
                "bitNumber:'" + bitNumber + '\'' +
                ", description:'" + description + '\'' +
                '}';
    }
}
