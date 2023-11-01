package fun.mjauto.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import fun.mjauto.auth.entity.GrantedAuthorityEntity;
import fun.mjauto.auth.entity.UserDetailsEntity;
import fun.mjauto.auth.mapper.GrantedAuthorityMapper;
import fun.mjauto.auth.mapper.UserDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author MJ
 * @description
 * @date 2023/10/31
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDetailsMapper userDetailsMapper;
    private final GrantedAuthorityMapper grantedAuthorityMapper;

    public UserDetailsServiceImpl(UserDetailsMapper userDetailsMapper, GrantedAuthorityMapper grantedAuthorityMapper) {
        this.userDetailsMapper = userDetailsMapper;
        this.grantedAuthorityMapper = grantedAuthorityMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 根据username查询用户
        QueryWrapper<UserDetailsEntity> wrapperUser = new QueryWrapper<>();
        wrapperUser.eq("username", username); // 在查询条件中设置字段名和值
        UserDetailsEntity userDetails = userDetailsMapper.selectOne(wrapperUser); // 执行查询并返回结果

        // 判断查询结果是否为空
        if (userDetails == null) {
            throw new UsernameNotFoundException("未查询到此用户");
        }

        // 更具user_id查询用户权限
        QueryWrapper<GrantedAuthorityEntity> wrapperAuth = new QueryWrapper<>();
        wrapperAuth.eq("user_id", userDetails.getId());
        List<GrantedAuthorityEntity> Authorities = grantedAuthorityMapper.selectList(wrapperAuth);

        // 权限标识
        List<String> collect = Authorities.stream().map(GrantedAuthorityEntity::getTag).toList();

        // 设置用户权限集合
        userDetails.setAuthorities(AuthorityUtils.createAuthorityList(collect));

        return userDetails;
    }
}
