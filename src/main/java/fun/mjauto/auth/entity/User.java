package fun.mjauto.auth.entity;

import lombok.Data;

/**
 * @author MJ
 * @description
 * @date 2023/10/24
 */
@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String wxProf;
    private Integer status;
    private Integer isDel;
}
