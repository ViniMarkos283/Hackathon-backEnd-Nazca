package com.nazca.backend.service;

import com.nazca.backend.dto.response.TreinamentoResponse;
import com.nazca.backend.exception.ResourceNotFoundException;
import com.nazca.backend.model.EvidenciaTreinamento;
import com.nazca.backend.model.TreinamentoColaborador;
import com.nazca.backend.repository.EvidenciaRepository;
import com.nazca.backend.repository.TreinamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EvidenciaService {

    private final EvidenciaRepository evidenciaRepository;
    private final TreinamentoRepository treinamentoRepository;

    public long totalEvidencias() { return evidenciaRepository.count(); }

    public List<EvidenciaTreinamento> listarPorTreinamento(Integer treinamentoId) {
        return evidenciaRepository.findByTreinamentoId(treinamentoId);
    }

    @Transactional
    public EvidenciaTreinamento registrar(Integer treinamentoId, String tipo,
                                          String arquivoUrl, String descricao) {
        TreinamentoColaborador tc = treinamentoRepository.findById(treinamentoId)
                .orElseThrow(() -> new ResourceNotFoundException("Treinamento não encontrado: " + treinamentoId));

        EvidenciaTreinamento ev = EvidenciaTreinamento.builder()
                .treinamento(tc)
                .tipo(tipo)
                .arquivoUrl(arquivoUrl)
                .descricao(descricao)
                .build();
        return evidenciaRepository.save(ev);
    }
}