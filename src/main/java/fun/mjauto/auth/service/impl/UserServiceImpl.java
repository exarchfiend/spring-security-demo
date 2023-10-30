package fun.mjauto.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import fun.mjauto.auth.entity.Auth;
import fun.mjauto.auth.entity.User;
import fun.mjauto.auth.mapper.AuthMapper;
import fun.mjauto.auth.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author MJ
 * @description 实现UserDetailsService 重写loadUserByUsername方法 实现数据库登录认证
 * @date 2023/10/25
 */
@Service
public class UserServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;
    private final AuthMapper authMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, AuthMapper authMapper) {
        this.userMapper = userMapper;
        this.authMapper = authMapper;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 制造异常查看调用堆栈
//        int i = 10 / 0;

        // 根据username查询用户
        QueryWrapper<User> wrapperUser = new QueryWrapper<>();
        wrapperUser.eq("username", username); // 在查询条件中设置字段名和值
        User user = userMapper.selectOne(wrapperUser); // 执行查询并返回结果

        // 判断查询结果是否为空
        if (user == null) {
            throw new UsernameNotFoundException("未查询到此用户");
        }

        // 更具user_id查询用户权限
        QueryWrapper<Auth> wrapperAuth = new QueryWrapper<>();
        wrapperAuth.eq("user_id", user.getId());
        List<Auth> auths = authMapper.selectList(wrapperAuth);

        // 权限标识
        List<String> collect = auths.stream().map(Auth::getTag).toList();

        // 设置用户权限集合
        user.setAuthorities(AuthorityUtils.createAuthorityList(collect));

        return user;
    }
}
