package com.nazca.controller;

import com.nazca.dto.request.SetorRequest;
import com.nazca.dto.response.SetorResponse;
import com.nazca.service.SetorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/setores")
@RequiredArgsConstructor
@Tag(name = "Setores")
public class SetorController {

    private final SetorService setorService;

    @Operation(summary = "Lista todos os setores (com qtd de POPs e colaboradores)")
    @GetMapping
    public ResponseEntity<List<SetorResponse>> listar() {
        return ResponseEntity.ok(setorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SetorResponse> buscar(@PathVariable Integer id) {
        return ResponseEntity.ok(setorService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<SetorResponse> criar(@Valid @RequestBody SetorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(setorService.criar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SetorResponse> atualizar(@PathVariable Integer id,
                                                    @Valid @RequestBody SetorRequest request) {
        return ResponseEntity.ok(setorService.atualizar(id, request));
    }
}