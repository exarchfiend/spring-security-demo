package fun.mjauto.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author MJ
 * @description 用户权限
 * @date 2023/10/25
 */
@Data
@TableName("user_auth")
public class Auth {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("name")
    private String name;
    @TableField("tag")
    private String tag;
    @TableField("user_id")
    private Boolean userId;

    @TableField(exist = false)
    private User user;
}
