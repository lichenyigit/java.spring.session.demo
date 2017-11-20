package session.demo.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.session.SessionRepository;
import org.springframework.session.web.http.HttpSessionStrategy;
import org.springframework.web.bind.annotation.*;
import session.demo.bean.MapSessionX;
import session.demo.bean.RequestBean;
import session.demo.bean.Result;
import session.demo.strategy.MyHttpSessionStrategy;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;

@RestController
@RequestMapping(value = "", name = "session统一管理")
public class DemoController {
    Logger logger = LogManager.getLogger(DemoController.class);

    private String redisPrefix = "spring:session:";

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "/1", method = RequestMethod.POST)
    public ResponseEntity<Result> verification(HttpServletRequest request,
                                               HttpServletResponse response,
                                               @RequestBody(required = false) RequestBean requestBean  //即将存入session的对象，可能是用户信息，也有可能是其他的对象信息
                                               ){
        MapSessionX sessionX = new MapSessionX();
        Result result = new Result();
        result.setResult(true);
        String sessoinId = httpSessionStrategy().getRequestedSessionId(request);
        logger.info(redisTemplate.opsForValue().get(redisPrefix+sessoinId));
        LinkedHashMap linkedHashMap = (LinkedHashMap)redisTemplate.opsForValue().get(redisPrefix+sessoinId);
        //如果redis中有缓存
        if(linkedHashMap != null){
            JSONObject redisSession = JSON.toJavaObject(new JSONObject(linkedHashMap), JSONObject.class);
            sessionX = JSONObject.toJavaObject(redisSession, MapSessionX.class);
            if(requestBean != null){
                if(requestBean.getMaxInactiveInterval() != null || requestBean.getObject() != null){
                    if(requestBean.getMaxInactiveInterval() != null && sessionX.isExpired()){
                        sessionX.setMaxInactiveIntervalInSeconds(requestBean.getMaxInactiveInterval());
                    }
                    if(requestBean.getObject() != null){
                        sessionX.setAttribute("userInfo", requestBean.getObject());
                    }
                }
            }
        }else{//redis中没有缓存
            if(requestBean == null){
                result = Result.build().setErrorCode(400).setDescrpition("body中的内容不能为null");
                return ResponseEntity.ok().body(result);
            }
            else if(requestBean.getObject() == null){
                result = Result.build().setErrorCode(400).setDescrpition("body中的内容不能为null");
                return ResponseEntity.ok().body(result);
            }
            sessionX.setId(sessoinId);
            sessionX.setMaxInactiveIntervalInSeconds(requestBean.getMaxInactiveInterval());
            sessionX.setAttribute("userInfo", requestBean.getObject());
        }
        redisTemplate.opsForValue().set(redisPrefix+sessoinId, sessionX);

        result = Result.build().setResult(sessionX);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/2")
    public String update(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        return session.getId();
    }

    @Bean
    public HttpSessionStrategy httpSessionStrategy() {
        return new MyHttpSessionStrategy();
    }

}