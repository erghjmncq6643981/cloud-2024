package com.chandler.elasticsearch.example.controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.chandler.elasticsearch.example.entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * restful风格的接口
 *
 * @author 钱丁君-chandler 2019/5/17下午2:00
 * @since 1.8
 */
@Slf4j
@RestController
@RequestMapping("es")
public class ElasticsearchProvider {
    @Autowired
    private ElasticsearchClient esClient;

    @PutMapping(value = "/create")
    public void putPerson(@RequestParam("index") String index) throws IOException {
        esClient.indices().create(c -> c
                .index(index)
        );
    }

    @PostMapping(value = "/add/data")
    public void postPerson(@RequestParam("index") String index,
                           @RequestBody Person person) throws IOException {
        IndexResponse response = esClient.index(i -> i
                .index(index)
                .id(person.getName())
                .document(person)
        );
        log.info("Indexed with version " + response.version());
    }

    @GetMapping(value = "/search")
    public Person getPerson(@RequestParam("index") String index,
                            @RequestParam("name") String name) throws IOException {
        GetResponse<Person> response = esClient.get(g -> g
                        .index(index)
                        .id(name),
                Person.class
        );
        return response.source();
    }

}
