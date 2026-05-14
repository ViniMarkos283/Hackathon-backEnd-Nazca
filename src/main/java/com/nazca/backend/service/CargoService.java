package com.nazca.backend.service;

import com.nazca.backend.dto.request.CargoRequest;
import com.nazca.backend.dto.response.CargoResponse;
import com.nazca.backend.exception.ResourceNotFoundException;
import com.nazca.backend.model.*;
import com.nazca.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CargoService {

    private final CargoRepository cargoRepository;
    private final SetorRepository setorRepository;
    private final PopRepository popRepository;
    private final PopCargoRepository popCargoRepository;

    public List<CargoResponse> listarTodos() {
        return cargoRepository.findByAtivoTrue().stream()
                .map(this::toResponse)
                .toList();
    }

    public CargoResponse buscarPorId(Integer id) {
        return toResponse(findOrThrow(id));
    }

    @Transactional
    public CargoResponse criar(CargoRequest request) {
        Setor setor = setorRepository.findById(request.getSetorId())
                .orElseThrow(() -> new ResourceNotFoundException("Setor não encontrado: " + request.getSetorId()));

        Cargo cargo = Cargo.builder()
                .nome(request.getNome())
                .setor(setor)
                .nivel(request.getNivel())
                .descricao(request.getDescricao())
                .ativo(true)
                .build();
        cargo = cargoRepository.save(cargo);

        // Associar POPs obrigatórios
        if (request.getPopIdsObrigatorios() != null) {
            for (Integer popId : request.getPopIdsObrigatorios()) {
                Pop pop = popRepository.findById(popId)
                        .orElseThrow(() -> new ResourceNotFoundException("POP não encontrado: " + popId));
                PopCargo pc = PopCargo.builder()
                        .pop(pop)
                        .cargo(cargo)
                        .obrigatorio(true)
                        .build();
                popCargoRepository.save(pc);
            }
        }
        return toResponse(cargo);
    }

    @Transactional
    public CargoResponse atualizar(Integer id, CargoRequest request) {
        Cargo cargo = findOrThrow(id);
        Setor setor = setorRepository.findById(request.getSetorId())
                .orElseThrow(() -> new ResourceNotFoundException("Setor não encontrado"));

        cargo.setNome(request.getNome());
        cargo.setSetor(setor);
        cargo.setNivel(request.getNivel());
        cargo.setDescricao(request.getDescricao());
        return toResponse(cargoRepository.save(cargo));
    }

    private Cargo findOrThrow(Integer id) {
        return cargoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado: " + id));
    }

    CargoResponse toResponse(Cargo cargo) {
        List<PopCargo> popCargos = popCargoRepository.findByCargoIdAndObrigatorioTrue(cargo.getId());
        long totalColaboradores = cargo.getColaboradores() != null
                ? cargo.getColaboradores().stream().filter(c -> Boolean.TRUE.equals(c.getAtivo())).count()
                : 0;

        List<CargoResponse.PopResumo> popsResumo = popCargos.stream()
                .map(pc -> CargoResponse.PopResumo.builder()
                        .id(pc.getPop().getId())
                        .codigo(pc.getPop().getCodigo())
                        .descricao(pc.getPop().getDescricao())
                        .status(pc.getPop().getStatus() != null ? pc.getPop().getStatus().name() : null)
                        .build())
                .toList();

        return CargoResponse.builder()
                .id(cargo.getId())
                .nome(cargo.getNome())
                .setorId(cargo.getSetor().getId())
                .setorNome(cargo.getSetor().getNome())
                .nivel(cargo.getNivel())
                .descricao(cargo.getDescricao())
                .ativo(cargo.getAtivo())
                .totalColaboradores(totalColaboradores)
                .popsObrigatorios(popsResumo)
                .build();
    }
}