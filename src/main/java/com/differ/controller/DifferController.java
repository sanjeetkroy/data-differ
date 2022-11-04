package com.differ.controller;

import com.differ.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DifferController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/generate")
    public void getReport() {
        try {
            reportService.generate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
