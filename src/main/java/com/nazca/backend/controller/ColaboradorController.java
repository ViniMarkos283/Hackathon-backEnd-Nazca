package com.nazca.backend.controller;

import com.nazca.backend.dto.request.ColaboradorRequest;
import com.nazca.backend.dto.response.*;
import com.nazca.backend.service.ColaboradorService;
import com.nazca.backend.service.ColaboradorService.TreinamentoPendenteItem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/colaboradores")
@RequiredArgsConstructor
@Tag(name = "Colaboradores")
public class ColaboradorController {

    private final ColaboradorService colaboradorService;

    @Operation(summary = "Lista colaboradores ativos com setor, cargo e POPs obrigatórios")
    @GetMapping
    public ResponseEntity<List<ColaboradorResponse>> listar() {
        return ResponseEntity.ok(colaboradorService.listarAtivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColaboradorResponse> buscar(@PathVariable Integer id) {
        return ResponseEntity.ok(colaboradorService.buscarPorId(id));
    }

    @Operation(summary = "Cadastra colaborador (nome, email, cargo, setor via cargo, data admissão)")
    @PostMapping
    public ResponseEntity<ColaboradorResponse> cadastrar(@Valid @RequestBody ColaboradorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(colaboradorService.cadastrar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ColaboradorResponse> atualizar(@PathVariable Integer id,
                                                          @Valid @RequestBody ColaboradorRequest request) {
        return ResponseEntity.ok(colaboradorService.atualizar(id, request));
    }

    @Operation(summary = "Conformidade individual: POPs feitos vs pendentes por colaborador")
    @GetMapping("/conformidade")
    public ResponseEntity<List<ColaboradorConformidadeResponse>> conformidade() {
        return ResponseEntity.ok(colaboradorService.conformidade());
    }

    @GetMapping("/{id}/conformidade")
    public ResponseEntity<ColaboradorConformidadeResponse> conformidadePorId(@PathVariable Integer id) {
        return ResponseEntity.ok(colaboradorService.conformidadePorId(id));
    }

    @Operation(summary = "Treinamentos pendentes para todos os colaboradores")
    @GetMapping("/treinamentos-pendentes")
    public ResponseEntity<List<TreinamentoPendenteItem>> treinamentosPendentes() {
        return ResponseEntity.ok(colaboradorService.treinamentosPendentes());
    }

    @Operation(summary = "Colaboradores que concluíram todos os POPs obrigatórios")
    @GetMapping("/concluiram-todos-pops")
    public ResponseEntity<List<ColaboradorResponse>> queConcluiramTodosPops() {
        return ResponseEntity.ok(colaboradorService.queConcluiramTodosPops());
    }
}