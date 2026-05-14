package com.nazca.backend.service;

import com.nazca.backend.dto.response.AlertaResponse;
import com.nazca.backend.exception.ResourceNotFoundException;
import com.nazca.backend.model.Alerta;
import com.nazca.backend.model.Pop;
import com.nazca.backend.model.TreinamentoColaborador;
import com.nazca.backend.model.enums.AlertaTipo;
import com.nazca.backend.model.enums.PopStatus;
import com.nazca.backend.model.enums.TreinamentoStatus;
import com.nazca.backend.repository.AlertaRepository;
import com.nazca.backend.repository.PopRepository;
import com.nazca.backend.repository.TreinamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertaService {

    private final AlertaRepository alertaRepository;
    private final PopRepository popRepository;
    private final TreinamentoRepository treinamentoRepository;

    public List<AlertaResponse> listarNaoLidos() {
        return alertaRepository.findByLidoFalseOrderByCriadoEmDesc()
                .stream().map(this::toResponse).toList();
    }

    public List<AlertaResponse> listarTodos() {
        return alertaRepository.findAllByOrderByCriadoEmDesc()
                .stream().map(this::toResponse).toList();
    }

    public long totalNaoLidos() { return alertaRepository.countByLidoFalse(); }

    @Transactional
    public AlertaResponse marcarComoLido(Integer id) {
        Alerta alerta = alertaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta não encontrado: " + id));
        alerta.setLido(true);
        return toResponse(alertaRepository.save(alerta));
    }

    @Transactional
    public void marcarTodosLidos() {
        alertaRepository.findByLidoFalseOrderByCriadoEmDesc()
                .forEach(a -> { a.setLido(true); alertaRepository.save(a); });
    }

    /**
     * Roda todo dia às 02:00 — gera alertas para POPs vencidos,
     * treinamentos atrasados e POPs em revisão.
     */
    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void gerarAlertasAutomaticos() {
        LocalDate hoje = LocalDate.now();

        // POPs vencidos
        popRepository.findVencidos().forEach(pop -> {
            if (!jaExisteAlerta(AlertaTipo.pop_vencido, "pop", pop.getId())) {
                criarAlerta(AlertaTipo.pop_vencido,
                        "POP " + pop.getCodigo() + " está vencido desde " + pop.getDataValidade(),
                        pop.getId(), "pop");
            }
        });

        // POPs vencendo em 30 dias
        popRepository.findVencendoEmBreve(hoje.plusDays(30)).forEach(pop -> {
            if (!jaExisteAlerta(AlertaTipo.pop_vencido, "pop", pop.getId())) {
                criarAlerta(AlertaTipo.pop_vencido,
                        "POP " + pop.getCodigo() + " vence em " + pop.getDataValidade(),
                        pop.getId(), "pop");
            }
        });

        // Treinamentos atrasados
        treinamentoRepository.findAtrasados().forEach(tc -> {
            if (!jaExisteAlerta(AlertaTipo.treinamento_atrasado, "treinamento", tc.getId())) {
                criarAlerta(AlertaTipo.treinamento_atrasado,
                        "Treinamento do colaborador " + tc.getColaborador().getNome()
                        + " no POP " + tc.getPop().getCodigo() + " está atrasado",
                        tc.getId(), "treinamento");
            }
        });

        // POPs em revisão
        popRepository.findByStatus(PopStatus.em_revisao).forEach(pop -> {
            if (!jaExisteAlerta(AlertaTipo.revisao_pendente, "pop", pop.getId())) {
                criarAlerta(AlertaTipo.revisao_pendente,
                        "POP " + pop.getCodigo() + " está aguardando revisão",
                        pop.getId(), "pop");
            }
        });
    }

    private boolean jaExisteAlerta(AlertaTipo tipo, String refTipo, Integer refId) {
        return alertaRepository.findByTipo(tipo).stream()
                .anyMatch(a -> refTipo.equals(a.getReferenciaTipo())
                        && refId.equals(a.getReferenciaId())
                        && Boolean.FALSE.equals(a.getLido()));
    }

    private void criarAlerta(AlertaTipo tipo, String mensagem, Integer refId, String refTipo) {
        alertaRepository.save(Alerta.builder()
                .tipo(tipo)
                .mensagem(mensagem)
                .lido(false)
                .referenciaId(refId)
                .referenciaTipo(refTipo)
                .build());
    }

    AlertaResponse toResponse(Alerta a) {
        return AlertaResponse.builder()
                .id(a.getId())
                .tipo(a.getTipo())
                .mensagem(a.getMensagem())
                .lido(a.getLido())
                .referenciaId(a.getReferenciaId())
                .referenciaTipo(a.getReferenciaTipo())
                .criadoEm(a.getCriadoEm())
                .build();
    }
}