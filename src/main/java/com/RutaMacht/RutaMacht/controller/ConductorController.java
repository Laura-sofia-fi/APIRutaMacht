package com.RutaMacht.RutaMacht.controller;

import com.RutaMacht.RutaMacht.model.Conductor;
import com.RutaMacht.RutaMacht.services.ConductorServicie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/estudiantes")
@RequiredArgsConstructor

public class ConductorController {
    private final ConductorServicie conductorService;

}
