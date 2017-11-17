package session.demo.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import session.demo.bean.MapSessionX;
import session.demo.util.CommonUtil;

import javax.servlet.http.HttpSession;

@Controller
@ResponseBody
public class DemoController {
    Logger logger = LogManager.getLogger(DemoController.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    SessionRepository sessionRepository;

    @GetMapping("/login")
    public String create(HttpSession session) {


        logger.info("sessionId-->"+session.getId());
        String sessionId = CommonUtil.generateSessionId();
        MapSessionX mySession = new MapSessionX(sessionId);
        mySession.setCreationTime(System.currentTimeMillis());
        mySession.setMaxInactiveIntervalInSeconds(10);
        mySession.setAttribute("user", "mySession1");
        redisTemplate.opsForValue().set("session:userId1", mySession);
        return "success";
    }

    @GetMapping("/update")
    public MapSessionX update() {
        JSONObject result = (JSONObject) JSONObject.toJSON(redisTemplate.opsForValue().get("session:userId1"));
        MapSessionX mySession = JSON.parseObject(result.toJSONString(), MapSessionX.class);
        return mySession;
    }

    @GetMapping("/query")
    public MapSessionX query() {
        JSONObject result = (JSONObject) JSONObject.toJSON(redisTemplate.opsForValue().get("session:userId1"));
        MapSessionX mySession = JSON.parseObject(result.toJSONString(), MapSessionX.class);
        redisTemplate.opsForValue().set("session:userId1", mySession);
        logger.info(mySession.isExpired());
        return mySession;
    }

}
