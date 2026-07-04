package com.training.management.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI trainingManagementOpenApi() {
        return new OpenAPI()
            .info(new Info()
                .title("物业管理系统接口文档")
                .version("1.0.0")
                .description("实训项目后端 API，包含登录认证、物业业务模块和 RBAC 权限管理。"));
    }
}
