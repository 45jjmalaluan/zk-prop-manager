package com.zk.prop.manager.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

public class ValidationModel implements Serializable {
    private static final long serialVersionUID = -223302374398156927L;

    @JsonProperty
    @NotNull
    @Size(min = 1)
    private String node;
    @JsonProperty
    @NotEmpty
    private List<DataPair> dataPair;

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public List<DataPair> getDataPair() {
        return dataPair;
    }

    public void setDataPair(List<DataPair> dataPair) {
        this.dataPair = dataPair;
    }

    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(getDataPair());
    }
}
