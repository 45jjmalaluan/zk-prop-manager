package com.zk.prop.manager.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ValidationModel implements Serializable {
    private static final long serialVersionUID = -223302374398156927L;

    @JsonProperty
    @NotNull
    @Size(min = 1)
    private String node;
    @JsonProperty
    @NotNull
    @Size(min = 1)
    private String key;
    @JsonProperty
    @NotNull
    @Size(min = 1)
    private String regExp;

    public ValidationModel(String node, String key, String regExp) {
        setNode(node);
        setKey(key);
        setRegExp(regExp);
    }

    public ValidationModel() {
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

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

    public String toJson() throws JsonProcessingException {
        Map<String, String> modelMap = new HashMap<>();
        modelMap.put(key, regExp);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(modelMap);
    }
}
