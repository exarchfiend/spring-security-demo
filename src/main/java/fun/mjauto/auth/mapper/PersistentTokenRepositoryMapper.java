package fun.mjauto.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.mjauto.auth.entity.PersistentTokenEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author MJ
 * @description
 * @date 2023/10/31
 */
@Mapper
public interface PersistentTokenRepositoryMapper extends BaseMapper<PersistentTokenEntity> {
}
