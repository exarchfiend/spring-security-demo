package fun.mjauto.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.mjauto.auth.entity.Token;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author MJ
 * @description 操作Token表 用于登录持久化
 * @date 2023/10/27
 */
@Mapper
public interface TokenMapper extends BaseMapper<Token> {
}
