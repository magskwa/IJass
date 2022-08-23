package ch.ijass.server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Mappings {
    @GetMapping("/coucou")
    public String coucou() {
        return "coucou";
    }

    @GetMapping("/hello")
    public String hello() {
        return "salam";
    }
}
