package com.zk.prop.manager.core.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zk.prop.manager.core.model.DataPair;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

public final class DataUtil {
    public static String decodeString(byte[] bytes) {
        String rawValue = new String(bytes);
        byte[] decodedBytes = Base64.getDecoder().decode(rawValue);
        return new String(decodedBytes);
    }

    public static List<DataPair> getAsList(String jsonArray) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<DataPair> dataPair = mapper.readValue(jsonArray,
                new TypeReference<List<DataPair>>() {});
        return dataPair;
    }

    public static String printAsKeyValue(DataPair dataPair) {
        return String.format("%s=%s", dataPair.getKey(), dataPair.getRegExp());
    }

    private DataUtil() {
    }
}
