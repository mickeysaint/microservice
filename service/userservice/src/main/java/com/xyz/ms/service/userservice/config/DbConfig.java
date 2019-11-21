package com.xyz.ms.service.userservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DbConfig {

    @Autowired
    private JdbcTemplate jt;

}
