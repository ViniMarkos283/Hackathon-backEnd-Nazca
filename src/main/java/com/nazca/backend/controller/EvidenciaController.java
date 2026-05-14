package com.nazca.backend.controller;

import com.nazca.backend.model.EvidenciaTreinamento;
import com.nazca.backend.service.EvidenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/evidencias")
@RequiredArgsConstructor
@Tag(name = "Evidências")
public class EvidenciaController {

    private final EvidenciaService evidenciaService;

    @GetMapping("/total")
    public ResponseEntity<Map<String, Long>> total() {
        return ResponseEntity.ok(Map.of("total", evidenciaService.totalEvidencias()));
    }

    @GetMapping("/treinamento/{treinamentoId}")
    public ResponseEntity<List<EvidenciaTreinamento>> porTreinamento(@PathVariable Integer treinamentoId) {
        return ResponseEntity.ok(evidenciaService.listarPorTreinamento(treinamentoId));
    }

    @Operation(summary = "Registra evidência de treinamento")
    @PostMapping
    public ResponseEntity<EvidenciaTreinamento> registrar(
            @RequestParam Integer treinamentoId,
            @RequestParam String tipo,
            @RequestParam String arquivoUrl,
            @RequestParam(required = false) String descricao) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(evidenciaService.registrar(treinamentoId, tipo, arquivoUrl, descricao));
    }
}