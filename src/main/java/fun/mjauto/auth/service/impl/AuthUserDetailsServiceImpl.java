package fun.mjauto.auth.service.impl;

import fun.mjauto.auth.entity.CustomUser;
import fun.mjauto.auth.entity.CustomUserDetails;
import fun.mjauto.auth.entity.User;
import fun.mjauto.auth.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author MJ
 * @description
 * @date 2023/10/24
 */
@Service
public class AuthUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    AuthRepository authRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.authRepository.findUserByName(username);
        System.out.println(user);
        if (user == null) {
            throw new UsernameNotFoundException("username " + username + " is not found");
        }
        return new CustomUserDetails(user);
    }
}
