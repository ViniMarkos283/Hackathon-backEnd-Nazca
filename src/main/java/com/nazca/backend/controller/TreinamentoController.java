package com.nazca.controller;

import com.nazca.dto.request.TreinamentoRequest;
import com.nazca.dto.response.*;
import com.nazca.service.TreinamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/treinamentos")
@RequiredArgsConstructor
@Tag(name = "Treinamentos")
public class TreinamentoController {

    private final TreinamentoService treinamentoService;

    @Operation(summary = "Registra um treinamento concluído")
    @PostMapping
    public ResponseEntity<TreinamentoResponse> registrar(@Valid @RequestBody TreinamentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(treinamentoService.registrar(request));
    }

    @GetMapping
    public ResponseEntity<List<TreinamentoResponse>> listar() {
        return ResponseEntity.ok(treinamentoService.listarTodos());
    }

    @Operation(summary = "Treinamentos concluídos no mês atual")
    @GetMapping("/mes-atual")
    public ResponseEntity<List<TreinamentoResponse>> mesAtual() {
        return ResponseEntity.ok(treinamentoService.listarMesAtual());
    }

    @GetMapping("/mes-atual/total")
    public ResponseEntity<Map<String, Long>> totalMesAtual() {
        return ResponseEntity.ok(Map.of("total", treinamentoService.totalMesAtual()));
    }

    @Operation(summary = "Adição dos treinamentos com data mais recente (últimos 20)")
    @GetMapping("/recentes")
    public ResponseEntity<List<TreinamentoResponse>> recentes() {
        return ResponseEntity.ok(treinamentoService.treinamentosRecentes());
    }

    @Operation(summary = "Conformidade por setor (% concluídos)")
    @GetMapping("/conformidade-setor")
    public ResponseEntity<List<ConformidadeSetorResponse>> conformidadePorSetor() {
        return ResponseEntity.ok(treinamentoService.conformidadePorSetor());
    }
}