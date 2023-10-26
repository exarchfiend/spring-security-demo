package fun.mjauto.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.mjauto.auth.entity.Auth;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author MJ
 * @description
 * @date 2023/10/25
 */
@Mapper
public interface AuthMapper extends BaseMapper<Auth> {
}
