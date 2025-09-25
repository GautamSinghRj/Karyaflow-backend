package com.example.karyaflow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CoreController {
    @GetMapping("/")
    public String apiHomePage(){
        return "index";
    }
}
