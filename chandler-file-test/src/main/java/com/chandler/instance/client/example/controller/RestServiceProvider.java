package com.chandler.instance.client.example.controller;

import com.chandler.instance.client.example.domain.dataobject.User;
import com.chandler.instance.client.example.domain.mapper.UserMapper;
import com.chandler.instance.client.example.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * restful风格的接口
 *
 * @author 钱丁君-chandler 2019/5/17下午2:00
 * @since 1.8
 */
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

    @GetMapping(value = "/demo/getHost")
    public User getHost(@RequestParam("id") Long id) {
        User u = new User();
        u.setId(id);
        User old = userMapper.selectOne(u);
        return old;
    }

}