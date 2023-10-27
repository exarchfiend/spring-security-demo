package fun.mjauto.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import fun.mjauto.auth.entity.Token;
import fun.mjauto.auth.mapper.TokenMapper;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author MJ
 * @description
 * @date 2023/10/27
 */
@Service
public class TokenServiceImpl implements PersistentTokenRepository {

    private final TokenMapper tokenMapper;

    @Autowired
    public TokenServiceImpl(TokenMapper tokenMapper) {
        this.tokenMapper = tokenMapper;
    }

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        Token myToken = new Token();
        myToken.setSeries(token.getSeries());
        myToken.setUsername(token.getUsername());
        myToken.setToken(token.getTokenValue());
        myToken.setLastUsed(token.getDate());

        tokenMapper.insert(myToken);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        UpdateWrapper<Token> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("token", tokenValue);
        updateWrapper.set("last_used", lastUsed);

        updateWrapper.eq("series", series);

        tokenMapper.update(null, updateWrapper);
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {

        Token token = tokenMapper.selectOne(new QueryWrapper<Token>().eq("series", seriesId));

        return new PersistentRememberMeToken(
                token.getUsername(),
                token.getSeries(),
                token.getToken(),
                token.getLastUsed()
        );
    }

    @Override
    public void removeUserTokens(String username) {
        UpdateWrapper<Token> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("username", username);

        tokenMapper.delete(updateWrapper);
    }
}
