package fun.mjauto.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.mjauto.auth.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author MJ
 * @description 操作用户登录表
 * @date 2023/10/25
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
