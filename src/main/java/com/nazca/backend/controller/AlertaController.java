package com.nazca.controller;

import com.nazca.dto.response.AlertaResponse;
import com.nazca.service.AlertaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alertas")
@RequiredArgsConstructor
@Tag(name = "Alertas")
public class AlertaController {

    private final AlertaService alertaService;

    @Operation(summary = "Alertas: POPs vencidos, treinamentos atrasados e revisões pendentes")
    @GetMapping
    public ResponseEntity<List<AlertaResponse>> listar(
            @RequestParam(defaultValue = "false") boolean apenasNaoLidos) {
        return ResponseEntity.ok(
                apenasNaoLidos ? alertaService.listarNaoLidos() : alertaService.listarTodos());
    }

    @GetMapping("/nao-lidos/total")
    public ResponseEntity<Map<String, Long>> totalNaoLidos() {
        return ResponseEntity.ok(Map.of("total", alertaService.totalNaoLidos()));
    }

    @Operation(summary = "Marca alerta como lido")
    @PutMapping("/{id}/lido")
    public ResponseEntity<AlertaResponse> marcarLido(@PathVariable Integer id) {
        return ResponseEntity.ok(alertaService.marcarComoLido(id));
    }

    @Operation(summary = "Marca todos os alertas como lidos")
    @PutMapping("/lidos/todos")
    public ResponseEntity<Void> marcarTodosLidos() {
        alertaService.marcarTodosLidos();
        return ResponseEntity.noContent().build();
    }
}