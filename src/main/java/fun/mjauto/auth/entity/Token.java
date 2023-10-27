package fun.mjauto.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author MJ
 * @description
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
