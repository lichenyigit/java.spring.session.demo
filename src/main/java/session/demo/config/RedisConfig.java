package session.demo.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;

import java.lang.reflect.Method;
import java.net.UnknownHostException;


/**
 * Created by Administrator on 2017-02-10.
 */
@Configuration
@EnableCaching
public class RedisConfig {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Bean
    JedisConnectionFactory redisFactory(@Value("${redis.host}") String redisHost,
                                        @Value("${redis.port}") String redisPort,
                                        @Value("${redis.password}") String redisPassword,
                                        @Value("${redis.database}") String redisDatabase
    ) {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        if (StringUtils.isEmpty(redisDatabase)) {
            jedisConnectionFactory.setDatabase(0);
        } else {
            try {
                jedisConnectionFactory.setDatabase(Integer.valueOf(redisDatabase));
            } catch (NumberFormatException e) {
                jedisConnectionFactory.setDatabase(0);
            }
        }
        if (StringUtils.isEmpty(redisHost)) {
            jedisConnectionFactory.setHostName("127.0.0.1");
        } else {
            jedisConnectionFactory.setHostName(redisHost);
        }
        if (StringUtils.isEmpty(redisPort)) {
            jedisConnectionFactory.setPort(0);
        } else {
            try {
                jedisConnectionFactory.setPort(Integer.valueOf(redisPort));
            } catch (NumberFormatException e) {
                jedisConnectionFactory.setPort(6379);
            }
        }
        if (!StringUtils.isEmpty(redisPassword)) {
            jedisConnectionFactory.setPassword(redisPassword);
        }
        jedisConnectionFactory.setPoolConfig(redisPoolConfig());
        return jedisConnectionFactory;
    }

    JedisPoolConfig redisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setTestOnCreate(true);
        jedisPoolConfig.setMaxTotal(-1);
        jedisPoolConfig.setMaxIdle(1000);
        jedisPoolConfig.setMinIdle(5);
        jedisPoolConfig.setMinEvictableIdleTimeMillis(864000000);
        jedisPoolConfig.setNumTestsPerEvictionRun(300000);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(false);
        jedisPoolConfig.setTestWhileIdle(false);
        jedisPoolConfig.setMaxWaitMillis(3000);
        return jedisPoolConfig;
    }

    @Bean
    public KeyGenerator cacheKeyGenerator() {
        return new KeyGenerator() {
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getSimpleName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };

    }

    @Bean
    public CacheManager cacheManager(
            @SuppressWarnings("rawtypes") RedisTemplate redisTemplate) {
        return new RedisCacheManager(redisTemplate);
    }


    @Bean
    public RedisTemplate<Object, Object> redisTemplate(@Value("${redis.host}") String redisHost,
                                                       @Value("${redis.port}") String redisPort,
                                                       @Value("${redis.password}") String redisPassword,
                                                       @Value("${redis.database}") String redisDatabase) throws UnknownHostException {
        RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
        template.setConnectionFactory(redisFactory(redisHost, redisPort, redisPassword, redisDatabase));

        RedisSerializer<Object> objectJackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        template.setKeySerializer(objectJackson2JsonRedisSerializer);
        template.setValueSerializer(objectJackson2JsonRedisSerializer);

        log.info("-->redisTemplate init success.");
        return template;
    }

}
