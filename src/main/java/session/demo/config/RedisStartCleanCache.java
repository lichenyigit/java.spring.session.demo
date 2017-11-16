package session.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Order(1)
@Component
public class RedisStartCleanCache implements CommandLineRunner {

    @Autowired
    public RedisTemplate<Object, Object> redisTemplate;

    @Override
    public void run(String... strings) throws Exception {
        redisTemplate.delete("session:userId1");
    }
}
