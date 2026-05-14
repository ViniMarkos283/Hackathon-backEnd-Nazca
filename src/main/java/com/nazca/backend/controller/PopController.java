package com.nazca.backend.controller;

import com.nazca.backend.dto.request.PopRequest;
import com.nazca.backend.dto.response.PopResponse;
import com.nazca.backend.model.enums.PopStatus;
import com.nazca.backend.service.PopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pops")
@RequiredArgsConstructor
@Tag(name = "POPs")
public class PopController {

    private final PopService popService;

    @Operation(summary = "Total de POPs cadastrados")
    @GetMapping("/total")
    public ResponseEntity<Map<String, Long>> total() {
        return ResponseEntity.ok(Map.of("total", popService.contarTotal()));
    }

    @Operation(summary = "Lista todos os POPs com status label (Vigente/Vencido/Vence em Breve/Em Revisão)")
    @GetMapping
    public ResponseEntity<List<PopResponse>> listar() {
        return ResponseEntity.ok(popService.listarTodos());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<PopResponse> buscar(@PathVariable String codigo) {
        return ResponseEntity.ok(popService.buscarPorCodigo(codigo));
    }

    @Operation(summary = "Filtra POPs por status (ativo | em_revisao | obsoleto)")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PopResponse>> porStatus(@PathVariable PopStatus status) {
        return ResponseEntity.ok(popService.listarPorStatus(status));
    }

    @Operation(summary = "POPs que vencem nos próximos 30 dias")
    @GetMapping("/vencimentos-criticos")
    public ResponseEntity<List<PopResponse>> vencimentosCriticos() {
        return ResponseEntity.ok(popService.vencimentosCriticos());
    }

    @PostMapping
    public ResponseEntity<PopResponse> criar(@Valid @RequestBody PopRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(popService.criar(request));
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<PopResponse> atualizar(@PathVariable String codigo,
                                                  @Valid @RequestBody PopRequest request) {
        return ResponseEntity.ok(popService.atualizar(codigo, request));
    }

    @Operation(summary = "Importa POPs via planilha Excel (.xlsx)")
    @PostMapping(value = "/importar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<PopResponse>> importar(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(popService.importarPlanilha(file));
    }
}