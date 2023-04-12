package com.pay.paypilot.service;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")

public class Payposh {
    @GetMapping("/docker")
    public String jide(){
        return "This is coming from a docker image";

    }
}
