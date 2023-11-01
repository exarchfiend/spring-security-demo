package fun.mjauto.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import fun.mjauto.auth.entity.PersistentTokenEntity;
import fun.mjauto.auth.mapper.PersistentTokenRepositoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author MJ
 * @description
 * @date 2023/10/31
 */
@Service
public class PersistentTokenRepositoryImpl implements PersistentTokenRepository {
    private final PersistentTokenRepositoryMapper persistentTokenRepositoryMapper;

    @Autowired
    public PersistentTokenRepositoryImpl(PersistentTokenRepositoryMapper persistentTokenRepositoryMapper) {
        this.persistentTokenRepositoryMapper = persistentTokenRepositoryMapper;
    }

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        PersistentTokenEntity persistentToken = new PersistentTokenEntity();
        persistentToken.setSeries(token.getSeries());
        persistentToken.setUsername(token.getUsername());
        persistentToken.setToken(token.getTokenValue());
        persistentToken.setLastUsed(token.getDate());
        persistentTokenRepositoryMapper.insert(persistentToken);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        UpdateWrapper<PersistentTokenEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("token", tokenValue);
        updateWrapper.set("last_used", lastUsed);
        updateWrapper.eq("series", series);
        persistentTokenRepositoryMapper.update(null, updateWrapper);
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {

        PersistentTokenEntity persistentToken = persistentTokenRepositoryMapper.selectOne(
                new QueryWrapper<PersistentTokenEntity>().eq("series", seriesId)
        );

        return new PersistentRememberMeToken(
                persistentToken.getUsername(),
                persistentToken.getSeries(),
                persistentToken.getToken(),
                persistentToken.getLastUsed()
        );
    }

    @Override
    public void removeUserTokens(String username) {
        UpdateWrapper<PersistentTokenEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("username", username);
        persistentTokenRepositoryMapper.delete(updateWrapper);
    }
}
