package fun.mjauto.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.mjauto.auth.entity.GrantedAuthorityEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author MJ
 * @description
 * @date 2023/10/31
 */
@Mapper
public interface GrantedAuthorityMapper extends BaseMapper<GrantedAuthorityEntity> {
}
