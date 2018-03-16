package com.zk.prop.manager.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class DataPair implements Serializable {
    private static final long serialVersionUID = 6552517784442474455L;

    @JsonProperty
    @NotNull
    @Size(min = 1)
    private String key;
    @JsonProperty
    @NotNull
    @Size(min = 1)
    private String regExp;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRegExp() {
        return regExp;
    }

    public void setRegExp(String regExp) {
        this.regExp = regExp;
    }
}
