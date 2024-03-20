package com.axonactive.dojo.auth.cache;

import com.axonactive.dojo.base.cache.BaseCache;
import com.axonactive.dojo.user.entity.User;

import javax.ejb.Singleton;
import java.util.List;

@Singleton
public class AuthCache extends BaseCache<Integer> {

    private static final int MAX_SIZE = 10;
    private static final int DURATION = 5;

    public AuthCache() {
        super(MAX_SIZE, DURATION);
    }

    public void setBadCredentialTimes(String email) {
        List<Integer> value = cache.getIfPresent(email);

        if(value == null) {
            cache.put(email, List.of(0));
            return;
        }

        value.add(0);
        cache.put(email, value);
    }
}
