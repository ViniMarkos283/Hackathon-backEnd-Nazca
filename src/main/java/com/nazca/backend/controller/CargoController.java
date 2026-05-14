package com.nazca.backend.controller;

import com.nazca.backend.dto.request.CargoRequest;
import com.nazca.backend.dto.response.CargoResponse;
import com.nazca.backend.service.CargoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cargos")
@RequiredArgsConstructor
@Tag(name = "Cargos")
public class CargoController {

    private final CargoService cargoService;

    @GetMapping
    public ResponseEntity<List<CargoResponse>> listar() {
        return ResponseEntity.ok(cargoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CargoResponse> buscar(@PathVariable Integer id) {
        return ResponseEntity.ok(cargoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<CargoResponse> criar(@Valid @RequestBody CargoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cargoService.criar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CargoResponse> atualizar(@PathVariable Integer id,
                                                    @Valid @RequestBody CargoRequest request) {
        return ResponseEntity.ok(cargoService.atualizar(id, request));
    }
}