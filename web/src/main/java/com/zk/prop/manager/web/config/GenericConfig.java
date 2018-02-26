package com.zk.prop.manager.web.config;

import com.zk.prop.manager.core.ZooKeeperConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

@Configuration
public class GenericConfig {
    @Resource
    private Environment env;

    @Bean
    public ZooKeeperConnection zooKeeperConnection() {
        String sessionTimeout = env.getRequiredProperty("zookeeper.session.timeout");
        return new ZooKeeperConnection(Integer.valueOf(sessionTimeout));
    }
}
