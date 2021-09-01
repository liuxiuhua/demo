package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class DemoController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/echo/{echo}")
    public String echo(@PathVariable String echo) {
        if(StringUtils.isEmpty(echo)){
            echo = "Hello world, just a test!";
        }
        return "v1.0: " + echo;
    }


    @GetMapping("/user/{userId}")
    public String user(@PathVariable Long userId) {
        Map userMap = this.queryUser(userId);
        if(null == userMap){
            return "user not exists! userId: " + userId;
        }
        return userMap.toString();
    }

    @GetMapping("/log")
    public String log(@RequestParam(required = true) String level,
                        @RequestParam(required = false) Integer times,
                        @RequestParam(required = true) String content) {
        if(null == times || times < 0){
            times = 1;
        }

        if(times > 1000000){
            times = 1000000;
        }

        String result = "";
        if(level.equalsIgnoreCase("warn")){
            for(int i=0; i < times; i++) {
                log.warn(content);
            }
        }else if(level.equalsIgnoreCase("error")){
            for(int i=0; i < times; i++) {
                log.error(content);
            }
        }else if(level.equalsIgnoreCase("debug")){
            for(int i=0; i < times; i++) {
                log.debug(content);
            }
        }else if(level.equalsIgnoreCase("info")){
            for(int i=0; i < times; i++) {
                log.info(content);
            }
        }else{
            result = String.format("not support level: %s, only support error/warn/debug/info", level);
            return result;
        }

        result = String.format("log success! level: %s, times: %d,  content: %s", level, times, content);
        return result;
    }

    private Map<String, Object> queryUser(final Long userId) {
        String sql = String.format("select * from demo_user where id = %d ", userId);
        List<Map<String, Object>> users = jdbcTemplate.queryForList(sql);
        if(CollectionUtils.isEmpty(users)){
            return null;
        }
        return users.get(0);
    }
}
