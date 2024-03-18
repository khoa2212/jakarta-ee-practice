package com.axonactive.dojo.base.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class BaseCache<E> {
    public static final int MAXIMUM_SIZE = 100;
    public static final int DURATION = 10;
    private final Cache<String, List<E>> cache;

    protected BaseCache() {
        cache = Caffeine.newBuilder()
                .maximumSize(MAXIMUM_SIZE)
                .expireAfterWrite(DURATION, TimeUnit.MINUTES)
                .build();
    }

    protected BaseCache(int cacheSize, int expiredDays) {
        cache = Caffeine.newBuilder()
                .maximumSize(cacheSize)
                .expireAfterWrite(expiredDays, TimeUnit.DAYS)
                .build();
    }

    public List<E> getCacheValue(String key) {
        return cache.getIfPresent(key);
    }

    public void setCacheValue(String key, List<E> value) {
        cache.put(key, value);
    }
}
