package com.training.management.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginUserVO {

    private Long id;
    private String username;
    private String realName;
    private String roleCode;
    private String phone;
}
