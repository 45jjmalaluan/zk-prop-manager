package com.zk.prop.manager.core.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;

public class ZnodePropertyServiceImpl implements ZnodePropertyService {
    protected String zNodePath;

    public ZnodePropertyServiceImpl(String zNodePath) {
        this.zNodePath = zNodePath;
    }

    public ZnodePropertyServiceImpl() {
        this.zNodePath = _getPath();
    }

    @Override
    public String getPath(String znode) {
        String path;
        if (StringUtils.isBlank(znode)) {
            path = String.format("%s", zNodePath);
        } else {
            path = String.format("%s/%s", zNodePath, znode);
        }
        return path;
    }

    @Override
    public boolean exists(CuratorFramework client, String znode) throws Exception {
        Stat stat = client.checkExists().forPath(getPath(znode));
        return stat != null;
    }

    @Override
    public void create(CuratorFramework client, String znode, String payload) throws Exception {
    }

    @Override
    public String getData(CuratorFramework client, String znode) throws Exception {
        return null;
    }

    @Override
    public void delete(CuratorFramework client, String znode) throws Exception {
        client.delete().forPath(getPath(znode));
    }
}
