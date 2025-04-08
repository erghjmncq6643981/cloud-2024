package org.example.chandlertomcatjerseydemo.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/api")
@RequestMapping("/hello")
@RestController
public class HelloResource {
    @GET
    @Path("/hello")
    @GetMapping
    public String hello() {
        return "Hello, World!";
    }
}