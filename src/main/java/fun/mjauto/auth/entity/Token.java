package fun.mjauto.auth.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author MJ
 * @description token实体 用于token持久化 rememberMe
 * @date 2023/10/27
 */
@Data
@TableName("token")
public class Token {
    @TableField(value = "series")
    private String series;
    @TableField("username")
    private String username;
    @TableField("token")
    private String token;
    @TableField("last_used")
    private Date lastUsed;
}
