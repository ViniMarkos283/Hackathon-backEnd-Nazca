package com.nazca.backend.service;

import com.nazca.backend.dto.request.TreinamentoRequest;
import com.nazca.backend.dto.response.ConformidadeSetorResponse;
import com.nazca.backend.dto.response.TreinamentoResponse;
import com.nazca.backend.exception.ResourceNotFoundException;
import com.nazca.backend.model.*;
import com.nazca.backend.model.enums.TreinamentoStatus;
import com.nazca.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TreinamentoService {

    private final TreinamentoRepository treinamentoRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final PopRepository popRepository;
    private final SetorRepository setorRepository;

    @Transactional
    public TreinamentoResponse registrar(TreinamentoRequest request) {
        Colaborador colab = colaboradorRepository.findById(request.getColaboradorId())
                .orElseThrow(() -> new ResourceNotFoundException("Colaborador não encontrado"));
        Pop pop = popRepository.findById(request.getPopId())
                .orElseThrow(() -> new ResourceNotFoundException("POP não encontrado"));

        TreinamentoColaborador tc = TreinamentoColaborador.builder()
                .colaborador(colab)
                .pop(pop)
                .dataConclusao(request.getDataConclusao())
                .validadeTrein(request.getValidadeTrein())
                .evidenciaUrl(request.getEvidenciaUrl())
                .status(TreinamentoStatus.concluido)
                .build();
        return toResponse(treinamentoRepository.save(tc));
    }

    public List<TreinamentoResponse> listarTodos() {
        return treinamentoRepository.findAllOrderByDataDesc().stream().map(this::toResponse).toList();
    }

    public List<TreinamentoResponse> listarMesAtual() {
        return treinamentoRepository.findConcluidosNoMesAtual().stream().map(this::toResponse).toList();
    }

    public long totalMesAtual() {
        return treinamentoRepository.findConcluidosNoMesAtual().size();
    }

    public List<TreinamentoResponse> treinamentosRecentes() {
        return treinamentoRepository.findAllOrderByDataDesc().stream()
                .limit(20).map(this::toResponse).toList();
    }

    // Conformidade por setor
    public List<ConformidadeSetorResponse> conformidadePorSetor() {
        List<Setor> setores = setorRepository.findAll();
        List<ConformidadeSetorResponse> result = new ArrayList<>();

        for (Setor setor : setores) {
            List<Colaborador> colabs = colaboradorRepository.findBySetorId(setor.getId());
            long total = 0, concluidos = 0;
            for (Colaborador c : colabs) {
                List<TreinamentoColaborador> tcs = treinamentoRepository.findByColaboradorId(c.getId());
                total += tcs.size();
                concluidos += tcs.stream().filter(t -> t.getStatus() == TreinamentoStatus.concluido).count();
            }
            double pct = total == 0 ? 0 : Math.round((concluidos * 100.0 / total) * 100.0) / 100.0;
            result.add(ConformidadeSetorResponse.builder()
                    .setorNome(setor.getNome())
                    .totalTreinamentos(total)
                    .concluidos(concluidos)
                    .percentual(pct)
                    .build());
        }
        return result;
    }

    // Job para atualizar treinamentos vencidos automaticamente
    @Scheduled(cron = "0 0 1 * * *")   // todo dia às 01:00
    @Transactional
    public void atualizarStatusVencidos() {
        treinamentoRepository.findAtrasados().forEach(tc -> {
            tc.setStatus(TreinamentoStatus.vencido);
            treinamentoRepository.save(tc);
        });
    }

    TreinamentoResponse toResponse(TreinamentoColaborador tc) {
        return TreinamentoResponse.builder()
                .id(tc.getId())
                .colaboradorId(tc.getColaborador().getId())
                .colaboradorNome(tc.getColaborador().getNome())
                .popCodigo(tc.getPop().getCodigo())
                .popDescricao(tc.getPop().getDescricao())
                .dataConclusao(tc.getDataConclusao())
                .validadeTrein(tc.getValidadeTrein())
                .status(tc.getStatus())
                .evidenciaUrl(tc.getEvidenciaUrl())
                .build();
    }
}