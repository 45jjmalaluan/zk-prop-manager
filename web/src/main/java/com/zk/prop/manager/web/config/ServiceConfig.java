package com.zk.prop.manager.web.config;

import com.zk.prop.manager.core.service.ValidationPropertyServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

@Configuration
public class ServiceConfig {
    @Value("${znode.validation.path}")
    private String zNodePath;

    @Resource
    private Environment env;

    @Bean
    public ValidationPropertyServiceImpl validationPropertyService() {
        return new ValidationPropertyServiceImpl(zNodePath);
    }
}
