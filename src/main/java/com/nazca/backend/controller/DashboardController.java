package com.nazca.controller;

import com.nazca.dto.response.DashboardResponse;
import com.nazca.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(summary = "Resumo geral: total POPs, colaboradores ativos, treinamentos do mês, alertas, vencimentos etc.")
    @GetMapping
    public ResponseEntity<DashboardResponse> resumo() {
        return ResponseEntity.ok(dashboardService.resumo());
    }
}