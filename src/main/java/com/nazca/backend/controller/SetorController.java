package com.nazca.backend.controller;

import com.nazca.backend.dto.request.SetorRequest;
import com.nazca.backend.dto.response.SetorResponse;
import com.nazca.backend.service.SetorService;
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

    private final SetorService service;

    @Operation(summary = "Lista todos os setores")
    @GetMapping
    public ResponseEntity<List<SetorResponse>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SetorResponse> buscar(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<SetorResponse> criar(@Valid @RequestBody SetorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SetorResponse> atualizar(@PathVariable Integer id,
                                                    @Valid @RequestBody SetorRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }
}
