package com.nazca.service;

import com.nazca.dto.response.DashboardResponse;
import com.nazca.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final PopRepository popRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final TreinamentoRepository treinamentoRepository;
    private final EvidenciaRepository evidenciaRepository;
    private final AlertaRepository alertaRepository;
    private final CargoRepository cargoRepository;
    private final SetorRepository setorRepository;

    public DashboardResponse resumo() {
        LocalDate limite30 = LocalDate.now().plusDays(30);

        return DashboardResponse.builder()
                .totalPops(popRepository.countTotal())
                .totalColaboradoresAtivos(colaboradorRepository.countByAtivoTrue())
                .totalTreinamentosMesAtual((long) treinamentoRepository.findConcluidosNoMesAtual().size())
                .totalEvidencias(evidenciaRepository.count())
                .totalAlertas(alertaRepository.count())
                .alertasNaoLidos(alertaRepository.countByLidoFalse())
                .popsVencendo30Dias((long) popRepository.findVencendoAte(limite30).size())
                .treinamentosAtrasados((long) treinamentoRepository.findAtrasados().size())
                .totalCargos(cargoRepository.count())
                .totalSetores(setorRepository.count())
                .build();
    }
}