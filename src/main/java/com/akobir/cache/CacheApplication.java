package com.akobir.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@Slf4j
public class CacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(CacheApplication.class, args);
    }

    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager concurrentMapCacheManager = new ConcurrentMapCacheManager();
        concurrentMapCacheManager.setCacheNames(Collections.singletonList("students"));
//        concurrentMapCacheManager.setCacheNames(List.of("users", "posts", "comments")); // if multiple caches
        return concurrentMapCacheManager;
    }

    @CacheEvict(value = "students", allEntries = true)
    @Scheduled(initialDelay = 10, fixedDelay = 4, timeUnit = TimeUnit.DAYS)
    public void deleteAllCachedPosts() {
        log.info("All Entries Of Posts Cache Flushing");
    }
}
