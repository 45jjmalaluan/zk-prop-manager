package com.zk.prop.manager.core.service;

import org.apache.curator.framework.CuratorFramework;

public interface ZnodePropertyService {
    default String _getPath() {
        return "/properties";
    }

    String getPath(String znode);

    boolean exists(CuratorFramework client, String znode) throws Exception;

    void create(CuratorFramework client, String znode, String payload) throws Exception;

    String getData(CuratorFramework client, String znode) throws Exception;

    void delete(CuratorFramework client, String znode) throws Exception;
}
