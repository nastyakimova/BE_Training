package com.github.training.spring.ioc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataSource {
    private String username;
    private String password;
    private String url;
    private String driverClassName;
}
