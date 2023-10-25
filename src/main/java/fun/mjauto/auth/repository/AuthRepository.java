package fun.mjauto.auth.repository;

import fun.mjauto.auth.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author MJ
 * @description
 * @date 2023/10/24
 */
@Mapper
public interface AuthRepository {
    @Select("SELECT * FROM `user`")
    User findUserByName(String username);
}
