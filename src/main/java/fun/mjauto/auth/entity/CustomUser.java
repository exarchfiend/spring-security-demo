package fun.mjauto.auth.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author MJ
 * @description
 * @date 2023/10/24
 */
@Data
public class CustomUser {

    private final String username;

    @JsonIgnore
    private final String password;

    @JsonCreator
    public CustomUser(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
