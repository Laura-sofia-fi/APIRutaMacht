package com.RutaMacht.RutaMacht.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Estado", description = "Comprobación de que la API está en línea")
@RestController
public class StatusController {

    @Operation(summary = "Estado de la API")
    @GetMapping ("api/status")
    public String Check(){

        return "API funcionando correctamente";

    }
}
