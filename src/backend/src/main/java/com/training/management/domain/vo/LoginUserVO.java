package com.training.management.domain.vo;

import java.util.List;

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
    private List<String> roleCodes;
    private List<String> permissions;
    private List<String> menus;
    private List<String> buttons;
}
