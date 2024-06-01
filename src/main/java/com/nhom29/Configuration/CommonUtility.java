package com.nhom29.Configuration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@EnableCaching
@Configuration
public class CommonUtility {

    @Bean
    public JedisConnectionFactory jedisConnectionFactory(){
        RedisStandaloneConfiguration redisStandal = new RedisStandaloneConfiguration();
        redisStandal.setPort(6379);
        redisStandal.setHostName("localhost");
        return new JedisConnectionFactory(redisStandal);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(){
        RedisTemplate<String, Object> redis = new RedisTemplate<>();
        redis.setConnectionFactory(jedisConnectionFactory());
        redis.setKeySerializer(new StringRedisSerializer());
        redis.setValueSerializer(new JdkSerializationRedisSerializer()); // Sử dụng JdkSerializationRedisSerializer cho cả key và value
        redis.setHashKeySerializer(new StringRedisSerializer());
        redis.setHashValueSerializer(new JdkSerializationRedisSerializer()); // Cập nhật thành setHashValueSerializer
        redis.setEnableTransactionSupport(true);
        redis.afterPropertiesSet();
        return redis;
    }

}
