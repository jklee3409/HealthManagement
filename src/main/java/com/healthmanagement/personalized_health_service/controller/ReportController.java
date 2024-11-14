package com.healthmanagement.personalized_health_service.controller;

import com.healthmanagement.personalized_health_service.service.ReportService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/weekly")
    public ResponseEntity<Map<String, Object>> getWeeklyReport(@RequestParam Long userId) {
        Map<String, Object> report = reportService.getWeeklyReport(userId);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/monthly")
    public ResponseEntity<Map<String, Object>> getMonthlyReport(@RequestParam Long userId) {
        Map<String, Object> report = reportService.getMonthlyReport(userId);
        return ResponseEntity.ok(report);
    }
}
