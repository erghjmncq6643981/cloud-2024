package com.chandler.location.example.controller;

import com.chandler.location.example.domain.dataobject.Task;
import com.chandler.location.example.domain.dataobject.User;
import com.chandler.location.example.domain.mapper.UserMapper;
import com.chandler.location.example.entity.Cluster;
import com.chandler.location.example.entity.Person;
import com.chandler.location.example.service.DoorPointService;
import com.chandler.location.example.service.TaskService;
import com.chandler.location.example.util.DateUtil;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * restful风格的接口
 *
 * @author 钱丁君-chandler 2019/5/17下午2:00
 * @since 1.8
 */
@RestController
@RequestMapping("demo")
public class RestServiceProvider {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TaskService taskService;

    @PostMapping(value = "/postPerson")
    public Mono<Person> postPerson(@RequestParam("name") String name) {

        return Mono.just(Person.builder()
                .name(name)
                .age("18")
                .sex("man")
                .build());
    }

    @PostMapping(value = "/body/postPerson")
    public Mono<?> postPerson(@RequestBody Person person) {
        var result = switch (person.getName()) {
            case "chandler" -> Mono.just(person);
            default -> Mono.empty();
        };
        return result;
    }

    @GetMapping(value = "/getHost")
    public User getHost(@RequestParam("id") Long id) {
        User u = new User();
        u.setId(id);
        User old = userMapper.selectOne(u);
        return old;
    }

    @Resource
    private DoorPointService doorPointService;

    @GetMapping(value = "/location/test")
    public List<Cluster> test(@RequestParam Integer fetchSize,
                              @RequestParam Integer eps,
                              @RequestParam Integer minPts) {
        return taskService.test(fetchSize, eps, minPts);
    }

    @GetMapping(value = "/point/test02")
    public void getLocations(@RequestParam(required = false) Long id,
                             @RequestParam(required = false) String lpn,
                             @RequestParam(required = false) String startTime,
                             @RequestParam(required = false) String endTime) {
        Task t = new Task();
        t.setId(id);
        t.setLpn(lpn);
        if(StringUtils.isNotBlank(startTime)){
            t.setStartTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(DateUtil.stringToDay(startTime).getTime()), ZoneId.systemDefault()));
        }
        if(StringUtils.isNotBlank(endTime)){
            t.setEndTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(DateUtil.stringToDay(endTime).getTime()), ZoneId.systemDefault()));
        }
        doorPointService.taskHandle(t);
    }

    @GetMapping(value = "/point/test03")
    public void getStoppingLocations(@RequestParam(required = false) Long taskId) {
        doorPointService.parseStoppingPoint(taskId);
    }
}