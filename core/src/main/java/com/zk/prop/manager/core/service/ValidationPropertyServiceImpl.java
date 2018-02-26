package com.zk.prop.manager.core.service;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.transaction.CuratorOp;

import java.util.Base64;

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
        String rawValue = new String(bytes);
        byte[] decodedBytes = Base64.getDecoder().decode(rawValue);
        return new String(decodedBytes);
    }
}
