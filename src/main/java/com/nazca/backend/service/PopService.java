package com.nazca.backend.service;

import com.nazca.backend.dto.request.PopRequest;
import com.nazca.backend.dto.response.PopResponse;
import com.nazca.backend.exception.ResourceNotFoundException;
import com.nazca.backend.model.Pop;
import com.nazca.backend.model.Setor;
import com.nazca.backend.model.enums.PopStatus;
import com.nazca.backend.repository.PopRepository;
import com.nazca.backend.repository.SetorRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PopService {

    private static final int DIAS_CRITICO = 30;

    private final PopRepository popRepository;
    private final SetorRepository setorRepository;

    public long contarTotal() { return popRepository.countTotal(); }

    public List<PopResponse> listarTodos() {
        return popRepository.findAll().stream().map(this::toResponse).toList();
    }

    public PopResponse buscarPorId(Integer id) {
        return toResponse(findOrThrow(id));
    }

    public PopResponse buscarPorCodigo(String codigo) {
        return toResponse(findOrThrowByCodigo(codigo));
    }

    public List<PopResponse> listarPorStatus(PopStatus status) {
        return popRepository.findByStatus(status).stream().map(this::toResponse).toList();
    }

    public List<PopResponse> vencimentosCriticos() {
        LocalDate limite = LocalDate.now().plusDays(DIAS_CRITICO);
        return popRepository.findVencendoAte(limite).stream().map(this::toResponse).toList();
    }

    @Transactional
    public PopResponse criar(PopRequest request) {
        Setor setor = setorRepository.findById(request.getSetorId())
                .orElseThrow(() -> new ResourceNotFoundException("Setor não encontrado"));
        Pop pop = Pop.builder()
                .codigo(request.getCodigo())
                .descricao(request.getDescricao())
                .setor(setor)
                .versao(request.getVersao())
                .dataValidade(request.getDataValidade())
                .status(request.getStatus())
                .build();
        return toResponse(popRepository.save(pop));
    }

    @Transactional
    public PopResponse atualizar(String codigo, PopRequest request) {
        Pop pop = findOrThrowByCodigo(codigo);

        Setor setor = setorRepository.findById(request.getSetorId())
                .orElseThrow(() -> new ResourceNotFoundException("Setor não encontrado"));

        pop.setDescricao(request.getDescricao());
        pop.setSetor(setor);
        pop.setVersao(request.getVersao());
        pop.setDataValidade(request.getDataValidade());
        pop.setStatus(request.getStatus());

        return toResponse(popRepository.save(pop));
    }

    /**
     * Importa POPs a partir de planilha Excel (.xlsx)
     * Colunas esperadas: codigo | descricao | setor_id | versao | data_validade (dd/MM/yyyy) | status
     */
    @Transactional
    public List<PopResponse> importarPlanilha(MultipartFile file) throws IOException {
        List<PopResponse> importados = new ArrayList<>();
        try (Workbook wb = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = wb.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {   // pula header
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String codigo   = cell(row, 0);
                String descricao = cell(row, 1);
                String setorStr  = cell(row, 2);
                String versao    = cell(row, 3);
                String validade  = cell(row, 4);
                String statusStr = cell(row, 5);

                if (codigo == null || codigo.isBlank()) continue;

                // Evita duplicata por código
                if (popRepository.findByCodigo(codigo).isPresent()) continue;

                Integer setorId;
                try { setorId = Integer.parseInt(setorStr); }
                catch (NumberFormatException e) { continue; }

                Setor setor = setorRepository.findById(setorId).orElse(null);
                if (setor == null) continue;

                Pop pop = Pop.builder()
                        .codigo(codigo)
                        .descricao(descricao)
                        .setor(setor)
                        .versao(versao != null ? versao : "1")
                        .dataValidade(parseDate(validade))
                        .status(parseStatus(statusStr))
                        .build();
                importados.add(toResponse(popRepository.save(pop)));
            }
        }
        return importados;
    }

    // ---- helpers ----

    private String cell(Row row, int col) {
        Cell c = row.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (c == null) return null;
        return switch (c.getCellType()) {
            case STRING  -> c.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) c.getNumericCellValue());
            default -> null;
        };
    }

    private LocalDate parseDate(String s) {
        if (s == null || s.isBlank()) return null;
        try { return LocalDate.parse(s, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")); }
        catch (Exception e) { return null; }
    }

    private PopStatus parseStatus(String s) {
        if (s == null) return PopStatus.ativo;
        try { return PopStatus.valueOf(s.toLowerCase()); }
        catch (Exception e) { return PopStatus.ativo; }
    }

    private Pop findOrThrow(Integer id) {
        return popRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("POP não encontrado: " + id));
    }

    private Pop findOrThrowByCodigo(String codigo) {
        return popRepository.findByCodigo(codigo)
                .orElseThrow(() ->
                        new ResourceNotFoundException("POP não encontrado: " + codigo));
    }

    PopResponse toResponse(Pop pop) {
        LocalDate hoje = LocalDate.now();
        Long diasParaVencer = pop.getDataValidade() != null
                ? ChronoUnit.DAYS.between(hoje, pop.getDataValidade()) : null;

        String statusLabel;
        if (pop.getStatus() == PopStatus.em_revisao) {
            statusLabel = "Em Revisão";
        } else if (pop.getDataValidade() != null && pop.getDataValidade().isBefore(hoje)) {
            statusLabel = "Vencido";
        } else if (diasParaVencer != null && diasParaVencer <= DIAS_CRITICO && diasParaVencer >= 0) {
            statusLabel = "Vence em Breve";
        } else {
            statusLabel = "Vigente";
        }

        return PopResponse.builder()
                .id(pop.getId())
                .codigo(pop.getCodigo())
                .descricao(pop.getDescricao())
                .setorNome(pop.getSetor().getNome())
                .versao(pop.getVersao())
                .dataValidade(pop.getDataValidade())
                .status(pop.getStatus())
                .statusLabel(statusLabel)
                .diasParaVencer(diasParaVencer)
                .dataCriacao(pop.getDataCriacao())
                .build();
    }
}