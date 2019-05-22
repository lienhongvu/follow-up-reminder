package com.lien.main;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class SheetResult {
    @JsonIgnore
    private String range;

    @JsonIgnore
    private String majorDimension;

    private List<List<String>> values;

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getMajorDimension() {
        return majorDimension;
    }

    public void setMajorDimension(String majorDimension) {
        this.majorDimension = majorDimension;
    }

    public List<List<String>> getValues() {
        return values;
    }

    public void setValues(List<List<String>> values) {
        this.values = values;
    }
}
