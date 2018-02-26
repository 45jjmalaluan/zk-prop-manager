package com.zk.prop.manager.core;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public final class ZooKeeperConnection {
    private CuratorFramework client;
    private ZooKeeper zooKeeper;

    private int sessionTimeout;

    public ZooKeeperConnection(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public ZooKeeper open(String connectString) throws IOException, InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        zooKeeper = new ZooKeeper(connectString, sessionTimeout, event -> {
            if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                latch.countDown();
            }
        });
        if (!latch.await(10, TimeUnit.SECONDS)) {
            throw new IOException("Failed to connect to ZooKeeper");
        }
        return zooKeeper;
    }

    public void close() throws InterruptedException {
        zooKeeper.close();
    }

    public CuratorFramework openClient(String connectString) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.builder()
                .connectString(connectString)
                .retryPolicy(retryPolicy)
                .sessionTimeoutMs(sessionTimeout)
                .build();
        client.start();
        return client;
    }

    public void closeClient() {
        client.close();
    }
}
