package com.chandler.instance.test.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chandler.instance.test.domain.dataobject.User;
import com.chandler.instance.test.domain.mapper.UserMapper;
import com.chandler.instance.test.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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

    @PostMapping(value = "/demo/postPerson")
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

    @GetMapping(value = "/demo/getUser")
    public User getUser(@RequestParam("id") Long id) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId,id);
        User old =userMapper.selectOne(queryWrapper);
        return old;
    }

    @PostMapping(value = "/demo/updateUser")
    public User addOrUpdatePerson(@RequestBody User user) {
        if(Objects.nonNull(user.getId())&& 0L !=user.getId()){
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getId,user.getId());
            User old =userMapper.selectOne(queryWrapper);
            old.setName(user.getName());
            old.setAge(user.getAge());
            old.setPassword(user.getPassword());
            userMapper.update(old,queryWrapper);
        }else{
            userMapper.insert(user);
        }

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId,user.getId());
        User old =userMapper.selectOne(queryWrapper);
        return old;
    }

    @PostMapping(value = "/query/time")
    public List<User> queryByTime() {

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.le(User::getLastUpdateTime, LocalDateTime.now());
        List<User> users =userMapper.selectList(queryWrapper);
        return users;
    }
}