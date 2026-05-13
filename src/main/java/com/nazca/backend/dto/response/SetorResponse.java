package com.nazca.service;

import com.nazca.dto.request.SetorRequest;
import com.nazca.dto.response.SetorResponse;
import com.nazca.exception.ResourceNotFoundException;
import com.nazca.model.Setor;
import com.nazca.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SetorService {

    private final SetorRepository setorRepository;
    private final PopRepository popRepository;
    private final ColaboradorRepository colaboradorRepository;

    public List<SetorResponse> listarTodos() {
        return setorRepository.findAllAtivos().stream()
                .map(this::toResponse)
                .toList();
    }

    public SetorResponse buscarPorId(Integer id) {
        Setor setor = setorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Setor não encontrado: " + id));
        return toResponse(setor);
    }

    @Transactional
    public SetorResponse criar(SetorRequest request) {
        Setor setor = Setor.builder()
                .nome(request.getNome())
                .descricao(request.getDescricao())
                .ativo(true)
                .build();
        return toResponse(setorRepository.save(setor));
    }

    @Transactional
    public SetorResponse atualizar(Integer id, SetorRequest request) {
        Setor setor = setorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Setor não encontrado: " + id));
        setor.setNome(request.getNome());
        setor.setDescricao(request.getDescricao());
        return toResponse(setorRepository.save(setor));
    }

    private SetorResponse toResponse(Setor setor) {
        long totalPops = setor.getPops() != null ? setor.getPops().size() : 0;
        long totalColabs = setor.getCargos() != null
                ? setor.getCargos().stream()
                    .flatMap(c -> c.getColaboradores() != null ? c.getColaboradores().stream() : java.util.stream.Stream.empty())
                    .filter(c -> Boolean.TRUE.equals(c.getAtivo()))
                    .count()
                : 0;
        return SetorResponse.builder()
                .id(setor.getId())
                .nome(setor.getNome())
                .descricao(setor.getDescricao())
                .ativo(setor.getAtivo())
                .totalPops(totalPops)
                .totalColaboradores(totalColabs)
                .build();
    }
}