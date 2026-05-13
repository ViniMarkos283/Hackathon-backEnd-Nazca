package com.nazca.cosmeticos.controller;

import com.nazca.cosmeticos.model.Setor;
import com.nazca.cosmeticos.service.SetorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/setores")
@RequiredArgsConstructor
public class SetorController {

    private final SetorService service;

    // GET /api/setores
    @GetMapping
    public ResponseEntity<List<Setor>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    // GET /api/setores/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Setor> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // POST /api/setores
    @PostMapping
    public ResponseEntity<Setor> criar(@RequestBody Setor setor) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(setor));
    }

    // PUT /api/setores/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Setor> atualizar(@PathVariable Long id, @RequestBody Setor setor) {
        return ResponseEntity.ok(service.atualizar(id, setor));
    }

    // DELETE /api/setores/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
