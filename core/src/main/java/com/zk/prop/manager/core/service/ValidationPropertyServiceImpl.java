package com.zk.prop.manager.core.service;

import com.zk.prop.manager.core.model.DataPair;
import com.zk.prop.manager.core.util.DataUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.GetChildrenBuilder;
import org.apache.curator.framework.api.transaction.CuratorOp;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.List;

public class ValidationPropertyServiceImpl extends ZnodePropertyServiceImpl {
    public ValidationPropertyServiceImpl(String zNodePath) {
        super(zNodePath);
    }

    @Override
    public void create(CuratorFramework client, String znode, String payload) throws Exception {
        if (!exists(client, znode)) {
            String path = getPath(znode);
            String encodedString = Base64.getEncoder().encodeToString(payload.getBytes());
            CuratorOp createOp = client.transactionOp().create().forPath(path, encodedString.getBytes());
            client.transaction().forOperations(createOp);
        } else {
            throw new RuntimeException("Creation Not Allowed");
        }
    }

    @Override
    public String getData(CuratorFramework client, String znode) throws Exception {
        byte[] bytes = client.getData().forPath(getPath(znode));
        return DataUtil.decodeString(bytes);
    }

    public File generateFile(CuratorFramework client, String znode, String fileName) throws Exception {
        File property = File.createTempFile(fileName, ".properties");
        FileWriter fileWriter = new FileWriter(property);
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(fileWriter);
            if (exists(client, znode)) {
                GetChildrenBuilder childrenBuilder = client.getChildren();
                List<String> childNodes = childrenBuilder.forPath(getPath(znode));
                if (childNodes != null) {
                    for (String cnode : childNodes) {
                        String jsonArray = getData(client, cnode);
                        List<DataPair> dataPairList = DataUtil.getAsList(jsonArray);
                        for (DataPair dataPair : dataPairList) {
                            String line = DataUtil.printAsKeyValue(dataPair);
                            printWriter.println(line);
                        }
                    }
                }
            }
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
        return property;
    }
}
