package com.chandler.instance.client.example.controller;

import com.chandler.instance.client.example.domain.dataobject.User;
import com.chandler.instance.client.example.domain.mapper.UserMapper;
import com.chandler.instance.client.example.entity.Person;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * restful风格的接口
 *
 * @author 钱丁君-chandler 2019/5/17下午2:00
 * @since 1.8
 */
@RestController
public class RestServiceProvider {
    @Autowired
    private UserMapper userMapper;

    /**
     * @param name
     * @return Person
     * @Description: post接口
     * @create date 2018年5月19日上午9:44:08
     */
    @ApiOperation(value = "post请求测试", notes = "根据name查询person")
    @RequestMapping(value = "/demo/postPerson", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public Mono<Person> postPerson(@ApiParam(value = "姓名", required = true, defaultValue = "chandler") @RequestParam("name") String name) {

        return Mono.just(Person.builder()
                .name(name)
                .age("18")
                .sex("man")
                .build());
    }

    /**
     * @param person
     * @return Person
     * @Description: post接口
     * @create date 2018年6月27日下午5:50:56
     */
    @RequestMapping(value = "/body/postPerson", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public Mono<?> postPerson(@RequestBody Person person) {
        var result = switch (person.getName()) {
            case "chandler" -> Mono.just(person);
            default -> Mono.empty();
        };
        return result;
    }

    /**
     * @param name
     * @return String
     * @Description: get接口
     * @create date 2018年5月19日上午9:46:34
     */
    @ApiOperation(value = "get请求测试", notes = "获取ipAddress")
    @RequestMapping(value = "/demo/getHost", method = RequestMethod.GET)
    public User getHost(@ApiParam(value = "自增ID", required = true, defaultValue = "1") @RequestParam("id") Long id) {
        User u = new User();
        u.setId(id);
        User old = userMapper.selectOne(u);
        return old;
    }

}
