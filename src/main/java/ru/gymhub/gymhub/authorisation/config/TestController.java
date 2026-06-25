package ru.gymhub.gymhub.authorisation.config;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityRequirement(name = "bearerAuth")
public class TestController {

    @GetMapping("/hello")
        public String hello() {
            return "Hello!";
        }

}
