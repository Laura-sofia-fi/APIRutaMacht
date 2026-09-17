package com.RutaMacht.RutaMacht.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusCintroller {

    @GetMapping ("api/status")
    public String Check(){

        return "API funcionando correctamente";

    }
}
