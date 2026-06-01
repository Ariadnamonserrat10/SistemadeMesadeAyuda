package com.israelmerlyn.SistemadeMesadeAyuda.controller;

import com.israelmerlyn.SistemadeMesadeAyuda.dto.dashboard.DashboardResponse;
import com.israelmerlyn.SistemadeMesadeAyuda.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Indicadores globales del sistema")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    @Operation(summary = "Obtener resumen dinamico del dashboard")
    public DashboardResponse getDashboard() {
        return dashboardService.getDashboard();
    }
}