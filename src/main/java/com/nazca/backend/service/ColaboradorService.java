package com.nazca.backend.service;

import com.nazca.backend.dto.request.ColaboradorRequest;
import com.nazca.backend.dto.response.*;
import com.nazca.backend.exception.ResourceNotFoundException;
import com.nazca.backend.model.*;
import com.nazca.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ColaboradorService {

    private final ColaboradorRepository colaboradorRepository;
    private final CargoRepository cargoRepository;
    private final PopCargoRepository popCargoRepository;
    private final TreinamentoRepository treinamentoRepository;

    public List<ColaboradorResponse> listarAtivos() {
        return colaboradorRepository.findAllAtivosComCargoESetor()
                .stream().map(this::toResponse).toList();
    }

    public ColaboradorResponse buscarPorId(Integer id) {
        return toResponse(findOrThrow(id));
    }

    @Transactional
    public ColaboradorResponse cadastrar(ColaboradorRequest request) {
        Cargo cargo = cargoRepository.findById(request.getCargoId())
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado: " + request.getCargoId()));

        Colaborador colab = Colaborador.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .matricula(request.getMatricula())
                .cargo(cargo)
                .dataAdmissao(request.getDataAdmissao())
                .ativo(true)
                .build();
        return toResponse(colaboradorRepository.save(colab));
    }

    @Transactional
    public ColaboradorResponse atualizar(Integer id, ColaboradorRequest request) {
        Colaborador colab = findOrThrow(id);
        Cargo cargo = cargoRepository.findById(request.getCargoId())
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado"));
        colab.setNome(request.getNome());
        colab.setEmail(request.getEmail());
        colab.setMatricula(request.getMatricula());
        colab.setCargo(cargo);
        colab.setDataAdmissao(request.getDataAdmissao());
        return toResponse(colaboradorRepository.save(colab));
    }

    // Conformidade individual: POPs feitos vs pendentes
    public List<ColaboradorConformidadeResponse> conformidade() {
        return colaboradorRepository.findAllAtivosComCargoESetor()
                .stream().map(this::calcularConformidade).toList();
    }

    public ColaboradorConformidadeResponse conformidadePorId(Integer id) {
        return calcularConformidade(findOrThrow(id));
    }

    // Treinamentos pendentes por colaborador
    public List<TreinamentoPendenteItem> treinamentosPendentes() {
        return colaboradorRepository.findAllAtivosComCargoESetor().stream()
                .flatMap(c -> {
                    List<PopCargo> pendentes = treinamentoRepository.findTreinamentosPendentes(c.getId());
                    return pendentes.stream().map(pc ->
                            new TreinamentoPendenteItem(
                                    c.getId(), c.getNome(),
                                    pc.getPop().getCodigo(), pc.getPop().getDescricao()
                            ));
                }).toList();
    }

    public record TreinamentoPendenteItem(
            Integer colaboradorId, String colaboradorNome,
            String popCodigo, String popDescricao) {}

    // Colaboradores que concluíram todos os POPs obrigatórios
    public List<ColaboradorResponse> queConcluiramTodosPops() {
        return colaboradorRepository.findColaboradoresComTodosPops()
                .stream().map(this::toResponse).toList();
    }

    // ---- helpers ----

    private ColaboradorConformidadeResponse calcularConformidade(Colaborador colab) {
        List<PopCargo> obrigatorios = popCargoRepository.findByCargoIdAndObrigatorioTrue(colab.getCargo().getId());
        List<TreinamentoColaborador> concluidos = treinamentoRepository.findConcluidos(colab.getId());
        List<Integer> popIdsConcluidos = concluidos.stream().map(tc -> tc.getPop().getId()).toList();

        List<String> popsPendentes = obrigatorios.stream()
                .filter(pc -> !popIdsConcluidos.contains(pc.getPop().getId()))
                .map(pc -> pc.getPop().getCodigo())
                .toList();

        int total = obrigatorios.size();
        int concluidosCount = total - popsPendentes.size();
        double pct = total == 0 ? 100.0 : Math.round((concluidosCount * 100.0 / total) * 100.0) / 100.0;

        return ColaboradorConformidadeResponse.builder()
                .colaboradorId(colab.getId())
                .colaboradorNome(colab.getNome())
                .cargoNome(colab.getCargo().getNome())
                .setorNome(colab.getCargo().getSetor().getNome())
                .totalPopsObrigatorios(total)
                .concluidos(concluidosCount)
                .pendentes(popsPendentes.size())
                .percentualConformidade(pct)
                .popsPendentes(popsPendentes)
                .build();
    }

    private Colaborador findOrThrow(Integer id) {
        return colaboradorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Colaborador não encontrado: " + id));
    }

    ColaboradorResponse toResponse(Colaborador c) {
        List<PopCargo> popCargos = popCargoRepository.findByCargoIdAndObrigatorioTrue(c.getCargo().getId());
        List<String> pops = popCargos.stream().map(pc -> pc.getPop().getCodigo()).toList();
        return ColaboradorResponse.builder()
                .id(c.getId())
                .nome(c.getNome())
                .email(c.getEmail())
                .matricula(c.getMatricula())
                .cargoNome(c.getCargo().getNome())
                .cargoNivel(c.getCargo().getNivel() != null ? c.getCargo().getNivel().name() : null)
                .setorNome(c.getCargo().getSetor().getNome())
                .dataAdmissao(c.getDataAdmissao())
                .ativo(c.getAtivo())
                .popsObrigatorios(pops)
                .build();
    }
}