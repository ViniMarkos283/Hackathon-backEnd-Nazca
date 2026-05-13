# Nazca — Spring Boot (Full Project)

## Estrutura de Pacotes

```
com.nazca/
├── config/
│   └── OpenApiConfig.java
├── controller/
│   ├── AlertaController.java
│   ├── CargoController.java
│   ├── ColaboradorController.java
│   ├── DashboardController.java
│   ├── EvidenciaController.java
│   ├── PopController.java
│   ├── SetorController.java
│   └── TreinamentoController.java
├── dto/
│   ├── request/
│   │   ├── CargoRequest.java
│   │   ├── ColaboradorRequest.java
│   │   ├── PopRequest.java
│   │   └── TreinamentoRequest.java
│   └── response/
│       ├── AlertaResponse.java
│       ├── CargoResponse.java
│       ├── ColaboradorConformidadeResponse.java
│       ├── ColaboradorResponse.java
│       ├── ConformidadeSetorResponse.java
│       ├── DashboardResponse.java
│       ├── PopResponse.java
│       ├── SetorResponse.java
│       └── TreinamentoResponse.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
├── model/
│   ├── enums/
│   │   ├── NivelCargo.java
│   │   ├── PopStatus.java
│   │   ├── TreinamentoStatus.java
│   │   └── AlertaTipo.java
│   ├── Alerta.java
│   ├── Cargo.java
│   ├── Colaborador.java
│   ├── EvidenciaTreinamento.java
│   ├── Pop.java
│   ├── PopCargo.java
│   ├── Setor.java
│   └── TreinamentoColaborador.java
├── repository/
│   ├── AlertaRepository.java
│   ├── CargoRepository.java
│   ├── ColaboradorRepository.java
│   ├── EvidenciaRepository.java
│   ├── PopCargoRepository.java
│   ├── PopRepository.java
│   ├── SetorRepository.java
│   └── TreinamentoRepository.java
├── service/
│   ├── AlertaService.java
│   ├── CargoService.java
│   ├── ColaboradorService.java
│   ├── DashboardService.java
│   ├── EvidenciaService.java
│   ├── PopService.java
│   ├── SetorService.java
│   └── TreinamentoService.java
└── NazcaApplication.java

src/main/resources/
├── application.properties
└── db/migration/
    ├── V1__initial_schema.sql
    └── V2__add_nivel_alerta.sql
```

---

## pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                             https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.3.0</version>
    <relativePath/>
  </parent>

  <groupId>com.nazca</groupId>
  <artifactId>nazca-api</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <name>nazca-api</name>

  <properties>
    <java.version>21</java.version>
  </properties>

  <dependencies>
    <!-- Web -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- JPA + MySQL -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
      <groupId>com.mysql</groupId>
      <artifactId>mysql-connector-j</artifactId>
      <scope>runtime</scope>
    </dependency>

    <!-- Validation -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>

    <!-- Flyway -->
    <dependency>
      <groupId>org.flywaydb</groupId>
      <artifactId>flyway-mysql</artifactId>
    </dependency>

    <!-- Lombok -->
    <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <optional>true</optional>
    </dependency>

    <!-- OpenAPI / Swagger -->
    <dependency>
      <groupId>org.springdoc</groupId>
      <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
      <version>2.5.0</version>
    </dependency>

    <!-- Apache POI (importar planilha Excel) -->
    <dependency>
      <groupId>org.apache.poi</groupId>
      <artifactId>poi-ooxml</artifactId>
      <version>5.2.5</version>
    </dependency>

    <!-- Test -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
        <configuration>
          <excludes>
            <exclude>
              <groupId>org.projectlombok</groupId>
              <artifactId>lombok</artifactId>
            </exclude>
          </excludes>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>
```

---

## application.properties

```properties
# Datasource
spring.datasource.url=jdbc:mysql://localhost:3306/nazca_db?useSSL=false&serverTimezone=America/Sao_Paulo
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Flyway
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration

# Multipart (importar planilha)
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Swagger UI
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.api-docs.path=/api-docs
```

---

## db/migration/V2__add_nivel_alerta.sql

```sql
-- Adiciona nível ao cargo
ALTER TABLE cargo
  ADD COLUMN nivel ENUM('operacional','tecnico','analista','supervisor','gerente')
  NOT NULL DEFAULT 'operacional'
  AFTER setor_id;

-- Tabela de alertas
CREATE TABLE alerta (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    tipo             ENUM('pop_vencido','treinamento_atrasado','revisao_pendente') NOT NULL,
    mensagem         TEXT NOT NULL,
    lido             BOOLEAN NOT NULL DEFAULT FALSE,
    referencia_id    INT NULL,
    referencia_tipo  VARCHAR(50) NULL,  -- 'pop' | 'treinamento'
    criado_em        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;
```

---

## NazcaApplication.java

```java
package com.nazca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NazcaApplication {
    public static void main(String[] args) {
        SpringApplication.run(NazcaApplication.class, args);
    }
}
```

---

## config/OpenApiConfig.java

```java
package com.nazca.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI nazcaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Nazca API")
                        .description("Sistema de POPs, treinamentos e conformidade")
                        .version("1.0.0"));
    }
}
```

---

## model/enums

### NivelCargo.java
```java
package com.nazca.model.enums;

public enum NivelCargo {
    operacional, tecnico, analista, supervisor, gerente
}
```

### PopStatus.java
```java
package com.nazca.model.enums;

public enum PopStatus {
    ativo, em_revisao, obsoleto
}
```

### TreinamentoStatus.java
```java
package com.nazca.model.enums;

public enum TreinamentoStatus {
    concluido, pendente, vencido
}
```

### AlertaTipo.java
```java
package com.nazca.model.enums;

public enum AlertaTipo {
    pop_vencido, treinamento_atrasado, revisao_pendente
}
```

---

## model/Setor.java

```java
package com.nazca.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "setor")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id")
public class Setor {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    void prePersist() { this.criadoEm = LocalDateTime.now(); }

    @JsonIgnore
    @OneToMany(mappedBy = "setor", fetch = FetchType.LAZY)
    private List<Cargo> cargos;

    @JsonIgnore
    @OneToMany(mappedBy = "setor", fetch = FetchType.LAZY)
    private List<Pop> pops;
}
```

---

## model/Cargo.java

```java
package com.nazca.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nazca.model.enums.NivelCargo;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "cargo")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id")
public class Cargo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "setor_id", nullable = false)
    private Setor setor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NivelCargo nivel = NivelCargo.operacional;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    void prePersist() { this.criadoEm = LocalDateTime.now(); }

    @JsonIgnore
    @OneToMany(mappedBy = "cargo", fetch = FetchType.LAZY)
    private List<Colaborador> colaboradores;

    @JsonIgnore
    @OneToMany(mappedBy = "cargo", fetch = FetchType.LAZY)
    private List<PopCargo> popCargos;
}
```

---

## model/Colaborador.java

```java
package com.nazca.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "colaborador")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id")
public class Colaborador {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(length = 150)
    private String email;

    @Column(length = 50)
    private String matricula;

    @Column(length = 255)
    private String senha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cargo_id", nullable = false)
    private Cargo cargo;

    @Column(name = "data_admissao")
    private LocalDate dataAdmissao;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    void prePersist() { this.criadoEm = LocalDateTime.now(); }
}
```

---

## model/Pop.java

```java
package com.nazca.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nazca.model.enums.PopStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pop")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id")
public class Pop {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 20, unique = true)
    private String codigo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "setor_id", nullable = false)
    private Setor setor;

    @Column(nullable = false, length = 10)
    private String versao = "1";

    @Column(name = "data_validade")
    private LocalDate dataValidade;

    @Enumerated(EnumType.STRING)
    private PopStatus status = PopStatus.ativo;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @PrePersist
    void prePersist() { if (this.dataCriacao == null) this.dataCriacao = LocalDateTime.now(); }

    @JsonIgnore
    @OneToMany(mappedBy = "pop", fetch = FetchType.LAZY)
    private List<PopCargo> popCargos;

    @JsonIgnore
    @OneToMany(mappedBy = "pop", fetch = FetchType.LAZY)
    private List<TreinamentoColaborador> treinamentos;
}
```

---

## model/PopCargo.java

```java
package com.nazca.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pop_cargo")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id")
public class PopCargo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pop_codigo", referencedColumnName = "codigo", nullable = false)
    private Pop pop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cargo_id", nullable = false)
    private Cargo cargo;

    @Column(nullable = false)
    private Boolean obrigatorio = true;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    void prePersist() { this.criadoEm = LocalDateTime.now(); }
}
```

---

## model/TreinamentoColaborador.java

```java
package com.nazca.model;

import com.nazca.model.enums.TreinamentoStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "treinamento_colaborador")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id")
public class TreinamentoColaborador {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colaborador_id", nullable = false)
    private Colaborador colaborador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pop_id", nullable = false)
    private Pop pop;

    @Column(name = "data_conclusao", nullable = false)
    private LocalDate dataConclusao;

    @Column(name = "validade_trein", nullable = false)
    private LocalDate validadeTrein;

    @Column(name = "evidencia_url", length = 255)
    private String evidenciaUrl;

    @Enumerated(EnumType.STRING)
    private TreinamentoStatus status = TreinamentoStatus.concluido;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    void prePersist() { this.criadoEm = LocalDateTime.now(); }
}
```

---

## model/EvidenciaTreinamento.java

```java
package com.nazca.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evidencia_treinamento")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id")
public class EvidenciaTreinamento {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treinamento_colaborador_id", nullable = false)
    private TreinamentoColaborador treinamento;

    @Column(nullable = false, length = 30)
    private String tipo;

    @Column(name = "arquivo_url", nullable = false, columnDefinition = "TEXT")
    private String arquivoUrl;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    void prePersist() { this.criadoEm = LocalDateTime.now(); }
}
```

---

## model/Alerta.java

```java
package com.nazca.model;

import com.nazca.model.enums.AlertaTipo;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerta")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id")
public class Alerta {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertaTipo tipo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensagem;

    @Column(nullable = false)
    private Boolean lido = false;

    @Column(name = "referencia_id")
    private Integer referenciaId;

    @Column(name = "referencia_tipo", length = 50)
    private String referenciaTipo;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    void prePersist() { this.criadoEm = LocalDateTime.now(); }
}
```

---

## repository/SetorRepository.java

```java
package com.nazca.repository;

import com.nazca.model.Setor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SetorRepository extends JpaRepository<Setor, Integer> {

    List<Setor> findByAtivo(Boolean ativo);

    @Query("""
        SELECT s FROM Setor s
        WHERE s.ativo = true
        ORDER BY s.nome
    """)
    List<Setor> findAllAtivos();
}
```

---

## repository/CargoRepository.java

```java
package com.nazca.repository;

import com.nazca.model.Cargo;
import com.nazca.model.Setor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Integer> {

    List<Cargo> findByAtivoTrue();
    List<Cargo> findBySetor(Setor setor);
    List<Cargo> findBySetorId(Integer setorId);
}
```

---

## repository/ColaboradorRepository.java

```java
package com.nazca.repository;

import com.nazca.model.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ColaboradorRepository extends JpaRepository<Colaborador, Integer> {

    List<Colaborador> findByAtivoTrue();

    @Query("SELECT c FROM Colaborador c JOIN FETCH c.cargo ca JOIN FETCH ca.setor WHERE c.ativo = true")
    List<Colaborador> findAllAtivosComCargoESetor();

    @Query("SELECT c FROM Colaborador c WHERE c.cargo.setor.id = :setorId AND c.ativo = true")
    List<Colaborador> findBySetorId(@Param("setorId") Integer setorId);

    long countByAtivoTrue();

    @Query("""
        SELECT DISTINCT c FROM Colaborador c
        WHERE c.ativo = true
        AND NOT EXISTS (
            SELECT pc FROM PopCargo pc
            WHERE pc.cargo = c.cargo
            AND pc.obrigatorio = true
            AND NOT EXISTS (
                SELECT tc FROM TreinamentoColaborador tc
                WHERE tc.colaborador = c
                AND tc.pop = pc.pop
                AND tc.status = 'concluido'
                AND tc.validadeTrein >= CURRENT_DATE
            )
        )
    """)
    List<Colaborador> findColaboradoresComTodosPops();
}
```

---

## repository/PopRepository.java

```java
package com.nazca.repository;

import com.nazca.model.Pop;
import com.nazca.model.enums.PopStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PopRepository extends JpaRepository<Pop, Integer> {

    Optional<Pop> findByCodigo(String codigo);
    List<Pop> findByStatus(PopStatus status);
    long countByStatus(PopStatus status);

    @Query("SELECT p FROM Pop p WHERE p.dataValidade <= :limite AND p.status = 'ativo'")
    List<Pop> findVencendoAte(@Param("limite") LocalDate limite);

    @Query("SELECT p FROM Pop p WHERE p.dataValidade < CURRENT_DATE AND p.status = 'ativo'")
    List<Pop> findVencidos();

    @Query("SELECT p FROM Pop p WHERE p.dataValidade BETWEEN CURRENT_DATE AND :limite")
    List<Pop> findVencendoEmBreve(@Param("limite") LocalDate limite);

    @Query("SELECT COUNT(p) FROM Pop p")
    long countTotal();
}
```

---

## repository/PopCargoRepository.java

```java
package com.nazca.repository;

import com.nazca.model.PopCargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PopCargoRepository extends JpaRepository<PopCargo, Integer> {

    List<PopCargo> findByCargoId(Integer cargoId);
    List<PopCargo> findByPopId(Integer popId);
    List<PopCargo> findByCargoIdAndObrigatorioTrue(Integer cargoId);
    boolean existsByPopIdAndCargoId(Integer popId, Integer cargoId);
}
```

---

## repository/TreinamentoRepository.java

```java
package com.nazca.repository;

import com.nazca.model.TreinamentoColaborador;
import com.nazca.model.enums.TreinamentoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TreinamentoRepository extends JpaRepository<TreinamentoColaborador, Integer> {

    List<TreinamentoColaborador> findByColaboradorId(Integer colaboradorId);
    List<TreinamentoColaborador> findByStatus(TreinamentoStatus status);

    @Query("""
        SELECT tc FROM TreinamentoColaborador tc
        WHERE tc.colaborador.id = :colaboradorId
        AND tc.status = 'concluido'
        AND tc.validadeTrein >= CURRENT_DATE
    """)
    List<TreinamentoColaborador> findConcluidos(@Param("colaboradorId") Integer colaboradorId);

    @Query("""
        SELECT tc FROM TreinamentoColaborador tc
        WHERE MONTH(tc.dataConclusao) = MONTH(CURRENT_DATE)
        AND YEAR(tc.dataConclusao) = YEAR(CURRENT_DATE)
        AND tc.status = 'concluido'
    """)
    List<TreinamentoColaborador> findConcluidosNoMesAtual();

    @Query("""
        SELECT tc FROM TreinamentoColaborador tc
        ORDER BY tc.dataConclusao DESC
    """)
    List<TreinamentoColaborador> findAllOrderByDataDesc();

    @Query("""
        SELECT tc FROM TreinamentoColaborador tc
        WHERE tc.validadeTrein < CURRENT_DATE
        AND tc.status <> 'vencido'
    """)
    List<TreinamentoColaborador> findAtrasados();

    // Treinamentos pendentes: POPs obrigatórios sem treinamento concluído e válido
    @Query("""
        SELECT pc FROM PopCargo pc
        WHERE pc.obrigatorio = true
        AND NOT EXISTS (
            SELECT tc FROM TreinamentoColaborador tc
            WHERE tc.colaborador.id = :colaboradorId
            AND tc.pop = pc.pop
            AND tc.status = 'concluido'
            AND tc.validadeTrein >= CURRENT_DATE
        )
        AND pc.cargo.id = (
            SELECT c.cargo.id FROM Colaborador c WHERE c.id = :colaboradorId
        )
    """)
    List<com.nazca.model.PopCargo> findTreinamentosPendentes(@Param("colaboradorId") Integer colaboradorId);
}
```

---

## repository/EvidenciaRepository.java

```java
package com.nazca.repository;

import com.nazca.model.EvidenciaTreinamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EvidenciaRepository extends JpaRepository<EvidenciaTreinamento, Integer> {

    List<EvidenciaTreinamento> findByTreinamentoId(Integer treinamentoId);

    long countAll();
}
```

---

## repository/AlertaRepository.java

```java
package com.nazca.repository;

import com.nazca.model.Alerta;
import com.nazca.model.enums.AlertaTipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Integer> {

    List<Alerta> findByLidoFalseOrderByCriadoEmDesc();
    List<Alerta> findByTipo(AlertaTipo tipo);
    List<Alerta> findAllByOrderByCriadoEmDesc();
    long countByLidoFalse();
}
```

---

## exception/ResourceNotFoundException.java

```java
package com.nazca.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

---

## exception/GlobalExceptionHandler.java

```java
package com.nazca.exception;

import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    record ErrorResponse(int status, String error, Object message, LocalDateTime timestamp) {}

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(404, "Not Found", ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getAllErrors().forEach(err -> {
            String field = ((FieldError) err).getField();
            errors.put(field, err.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(400, "Validation Error", errors, LocalDateTime.now()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(500, "Internal Server Error", ex.getMessage(), LocalDateTime.now()));
    }
}
```

---

## dto/request/SetorRequest.java

```java
package com.nazca.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SetorRequest {
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    private String descricao;
}
```

---

## dto/request/CargoRequest.java

```java
package com.nazca.dto.request;

import com.nazca.model.enums.NivelCargo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class CargoRequest {
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotNull(message = "Setor é obrigatório")
    private Integer setorId;

    @NotNull(message = "Nível é obrigatório")
    private NivelCargo nivel;

    private String descricao;

    // IDs dos POPs obrigatórios para esse cargo
    private List<Integer> popIdsObrigatorios;
}
```

---

## dto/request/ColaboradorRequest.java

```java
package com.nazca.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ColaboradorRequest {
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    private String email;
    private String matricula;

    @NotNull(message = "Cargo é obrigatório")
    private Integer cargoId;

    private LocalDate dataAdmissao;
}
```

---

## dto/request/PopRequest.java

```java
package com.nazca.dto.request;

import com.nazca.model.enums.PopStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class PopRequest {
    @NotBlank(message = "Código é obrigatório")
    private String codigo;

    private String descricao;

    @NotNull(message = "Setor é obrigatório")
    private Integer setorId;

    private String versao = "1";
    private LocalDate dataValidade;
    private PopStatus status = PopStatus.ativo;
}
```

---

## dto/request/TreinamentoRequest.java

```java
package com.nazca.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class TreinamentoRequest {
    @NotNull
    private Integer colaboradorId;

    @NotNull
    private Integer popId;

    @NotNull
    private LocalDate dataConclusao;

    @NotNull
    private LocalDate validadeTrein;

    private String evidenciaUrl;
}
```

---

## dto/response/SetorResponse.java

```java
package com.nazca.dto.response;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class SetorResponse {
    private Integer id;
    private String nome;
    private String descricao;
    private Boolean ativo;
    private long totalPops;
    private long totalColaboradores;
}
```

---

## dto/response/CargoResponse.java

```java
package com.nazca.dto.response;

import com.nazca.model.enums.NivelCargo;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data @Builder
public class CargoResponse {
    private Integer id;
    private String nome;
    private String setorNome;
    private Integer setorId;
    private NivelCargo nivel;
    private String descricao;
    private Boolean ativo;
    private long totalColaboradores;
    private List<PopResumo> popsObrigatorios;

    @Data @Builder
    public static class PopResumo {
        private Integer id;
        private String codigo;
        private String descricao;
        private String status;
    }
}
```

---

## dto/response/ColaboradorResponse.java

```java
package com.nazca.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data @Builder
public class ColaboradorResponse {
    private Integer id;
    private String nome;
    private String email;
    private String matricula;
    private String cargoNome;
    private String cargoNivel;
    private String setorNome;
    private LocalDate dataAdmissao;
    private Boolean ativo;
    private List<String> popsObrigatorios;
}
```

---

## dto/response/PopResponse.java

```java
package com.nazca.dto.response;

import com.nazca.model.enums.PopStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder
public class PopResponse {
    private Integer id;
    private String codigo;
    private String descricao;
    private String setorNome;
    private String versao;
    private LocalDate dataValidade;
    private PopStatus status;
    private String statusLabel;   // Vigente / Vencido / Em Revisão / Vence em Breve
    private Long diasParaVencer;
    private LocalDateTime dataCriacao;
}
```

---

## dto/response/TreinamentoResponse.java

```java
package com.nazca.dto.response;

import com.nazca.model.enums.TreinamentoStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data @Builder
public class TreinamentoResponse {
    private Integer id;
    private String colaboradorNome;
    private Integer colaboradorId;
    private String popCodigo;
    private String popDescricao;
    private LocalDate dataConclusao;
    private LocalDate validadeTrein;
    private TreinamentoStatus status;
    private String evidenciaUrl;
}
```

---

## dto/response/ColaboradorConformidadeResponse.java

```java
package com.nazca.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data @Builder
public class ColaboradorConformidadeResponse {
    private Integer colaboradorId;
    private String colaboradorNome;
    private String cargoNome;
    private String setorNome;
    private int totalPopsObrigatorios;
    private int concluidos;
    private int pendentes;
    private double percentualConformidade;
    private List<String> popsPendentes;
}
```

---

## dto/response/ConformidadeSetorResponse.java

```java
package com.nazca.dto.response;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class ConformidadeSetorResponse {
    private String setorNome;
    private long totalTreinamentos;
    private long concluidos;
    private double percentual;
}
```

---

## dto/response/AlertaResponse.java

```java
package com.nazca.dto.response;

import com.nazca.model.enums.AlertaTipo;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Builder
public class AlertaResponse {
    private Integer id;
    private AlertaTipo tipo;
    private String mensagem;
    private Boolean lido;
    private Integer referenciaId;
    private String referenciaTipo;
    private LocalDateTime criadoEm;
}
```

---

## dto/response/DashboardResponse.java

```java
package com.nazca.dto.response;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class DashboardResponse {
    private long totalPops;
    private long totalColaboradoresAtivos;
    private long totalTreinamentosMesAtual;
    private long totalEvidencias;
    private long totalAlertas;
    private long alertasNaoLidos;
    private long popsVencendo30Dias;
    private long treinamentosAtrasados;
    private long totalCargos;
    private long totalSetores;
}
```

---

## service/SetorService.java

```java
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
```

---

## service/CargoService.java

```java
package com.nazca.service;

import com.nazca.dto.request.CargoRequest;
import com.nazca.dto.response.CargoResponse;
import com.nazca.exception.ResourceNotFoundException;
import com.nazca.model.*;
import com.nazca.repository.*;
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
```

---

## service/ColaboradorService.java

```java
package com.nazca.service;

import com.nazca.dto.request.ColaboradorRequest;
import com.nazca.dto.response.*;
import com.nazca.exception.ResourceNotFoundException;
import com.nazca.model.*;
import com.nazca.repository.*;
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
```

---

## service/PopService.java

```java
package com.nazca.service;

import com.nazca.dto.request.PopRequest;
import com.nazca.dto.response.PopResponse;
import com.nazca.exception.ResourceNotFoundException;
import com.nazca.model.Pop;
import com.nazca.model.Setor;
import com.nazca.model.enums.PopStatus;
import com.nazca.repository.PopRepository;
import com.nazca.repository.SetorRepository;
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
    public PopResponse atualizar(Integer id, PopRequest request) {
        Pop pop = findOrThrow(id);
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
```

---

## service/TreinamentoService.java

```java
package com.nazca.service;

import com.nazca.dto.request.TreinamentoRequest;
import com.nazca.dto.response.ConformidadeSetorResponse;
import com.nazca.dto.response.TreinamentoResponse;
import com.nazca.exception.ResourceNotFoundException;
import com.nazca.model.*;
import com.nazca.model.enums.TreinamentoStatus;
import com.nazca.repository.*;
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
```

---

## service/EvidenciaService.java

```java
package com.nazca.service;

import com.nazca.dto.response.TreinamentoResponse;
import com.nazca.exception.ResourceNotFoundException;
import com.nazca.model.EvidenciaTreinamento;
import com.nazca.model.TreinamentoColaborador;
import com.nazca.repository.EvidenciaRepository;
import com.nazca.repository.TreinamentoRepository;
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
```

---

## service/AlertaService.java

```java
package com.nazca.service;

import com.nazca.dto.response.AlertaResponse;
import com.nazca.exception.ResourceNotFoundException;
import com.nazca.model.Alerta;
import com.nazca.model.Pop;
import com.nazca.model.TreinamentoColaborador;
import com.nazca.model.enums.AlertaTipo;
import com.nazca.model.enums.PopStatus;
import com.nazca.model.enums.TreinamentoStatus;
import com.nazca.repository.AlertaRepository;
import com.nazca.repository.PopRepository;
import com.nazca.repository.TreinamentoRepository;
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
```

---

## service/DashboardService.java

```java
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
```

---

## controller/SetorController.java

```java
package com.nazca.controller;

import com.nazca.dto.request.SetorRequest;
import com.nazca.dto.response.SetorResponse;
import com.nazca.service.SetorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/setores")
@RequiredArgsConstructor
@Tag(name = "Setores")
public class SetorController {

    private final SetorService setorService;

    @Operation(summary = "Lista todos os setores (com qtd de POPs e colaboradores)")
    @GetMapping
    public ResponseEntity<List<SetorResponse>> listar() {
        return ResponseEntity.ok(setorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SetorResponse> buscar(@PathVariable Integer id) {
        return ResponseEntity.ok(setorService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<SetorResponse> criar(@Valid @RequestBody SetorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(setorService.criar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SetorResponse> atualizar(@PathVariable Integer id,
                                                    @Valid @RequestBody SetorRequest request) {
        return ResponseEntity.ok(setorService.atualizar(id, request));
    }
}
```

---

## controller/CargoController.java

```java
package com.nazca.controller;

import com.nazca.dto.request.CargoRequest;
import com.nazca.dto.response.CargoResponse;
import com.nazca.service.CargoService;
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
```

---

## controller/ColaboradorController.java

```java
package com.nazca.controller;

import com.nazca.dto.request.ColaboradorRequest;
import com.nazca.dto.response.*;
import com.nazca.service.ColaboradorService;
import com.nazca.service.ColaboradorService.TreinamentoPendenteItem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/colaboradores")
@RequiredArgsConstructor
@Tag(name = "Colaboradores")
public class ColaboradorController {

    private final ColaboradorService colaboradorService;

    @Operation(summary = "Lista colaboradores ativos com setor, cargo e POPs obrigatórios")
    @GetMapping
    public ResponseEntity<List<ColaboradorResponse>> listar() {
        return ResponseEntity.ok(colaboradorService.listarAtivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColaboradorResponse> buscar(@PathVariable Integer id) {
        return ResponseEntity.ok(colaboradorService.buscarPorId(id));
    }

    @Operation(summary = "Cadastra colaborador (nome, email, cargo, setor via cargo, data admissão)")
    @PostMapping
    public ResponseEntity<ColaboradorResponse> cadastrar(@Valid @RequestBody ColaboradorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(colaboradorService.cadastrar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ColaboradorResponse> atualizar(@PathVariable Integer id,
                                                          @Valid @RequestBody ColaboradorRequest request) {
        return ResponseEntity.ok(colaboradorService.atualizar(id, request));
    }

    @Operation(summary = "Conformidade individual: POPs feitos vs pendentes por colaborador")
    @GetMapping("/conformidade")
    public ResponseEntity<List<ColaboradorConformidadeResponse>> conformidade() {
        return ResponseEntity.ok(colaboradorService.conformidade());
    }

    @GetMapping("/{id}/conformidade")
    public ResponseEntity<ColaboradorConformidadeResponse> conformidadePorId(@PathVariable Integer id) {
        return ResponseEntity.ok(colaboradorService.conformidadePorId(id));
    }

    @Operation(summary = "Treinamentos pendentes para todos os colaboradores")
    @GetMapping("/treinamentos-pendentes")
    public ResponseEntity<List<TreinamentoPendenteItem>> treinamentosPendentes() {
        return ResponseEntity.ok(colaboradorService.treinamentosPendentes());
    }

    @Operation(summary = "Colaboradores que concluíram todos os POPs obrigatórios")
    @GetMapping("/concluiram-todos-pops")
    public ResponseEntity<List<ColaboradorResponse>> queConcluiramTodosPops() {
        return ResponseEntity.ok(colaboradorService.queConcluiramTodosPops());
    }
}
```

---

## controller/PopController.java

```java
package com.nazca.controller;

import com.nazca.dto.request.PopRequest;
import com.nazca.dto.response.PopResponse;
import com.nazca.model.enums.PopStatus;
import com.nazca.service.PopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pops")
@RequiredArgsConstructor
@Tag(name = "POPs")
public class PopController {

    private final PopService popService;

    @Operation(summary = "Total de POPs cadastrados")
    @GetMapping("/total")
    public ResponseEntity<Map<String, Long>> total() {
        return ResponseEntity.ok(Map.of("total", popService.contarTotal()));
    }

    @Operation(summary = "Lista todos os POPs com status label (Vigente/Vencido/Vence em Breve/Em Revisão)")
    @GetMapping
    public ResponseEntity<List<PopResponse>> listar() {
        return ResponseEntity.ok(popService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PopResponse> buscar(@PathVariable Integer id) {
        return ResponseEntity.ok(popService.buscarPorId(id));
    }

    @Operation(summary = "Filtra POPs por status (ativo | em_revisao | obsoleto)")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PopResponse>> porStatus(@PathVariable PopStatus status) {
        return ResponseEntity.ok(popService.listarPorStatus(status));
    }

    @Operation(summary = "POPs que vencem nos próximos 30 dias")
    @GetMapping("/vencimentos-criticos")
    public ResponseEntity<List<PopResponse>> vencimentosCriticos() {
        return ResponseEntity.ok(popService.vencimentosCriticos());
    }

    @PostMapping
    public ResponseEntity<PopResponse> criar(@Valid @RequestBody PopRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(popService.criar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PopResponse> atualizar(@PathVariable Integer id,
                                                  @Valid @RequestBody PopRequest request) {
        return ResponseEntity.ok(popService.atualizar(id, request));
    }

    @Operation(summary = "Importa POPs via planilha Excel (.xlsx)")
    @PostMapping(value = "/importar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<PopResponse>> importar(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(popService.importarPlanilha(file));
    }
}
```

---

## controller/TreinamentoController.java

```java
package com.nazca.controller;

import com.nazca.dto.request.TreinamentoRequest;
import com.nazca.dto.response.*;
import com.nazca.service.TreinamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/treinamentos")
@RequiredArgsConstructor
@Tag(name = "Treinamentos")
public class TreinamentoController {

    private final TreinamentoService treinamentoService;

    @Operation(summary = "Registra um treinamento concluído")
    @PostMapping
    public ResponseEntity<TreinamentoResponse> registrar(@Valid @RequestBody TreinamentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(treinamentoService.registrar(request));
    }

    @GetMapping
    public ResponseEntity<List<TreinamentoResponse>> listar() {
        return ResponseEntity.ok(treinamentoService.listarTodos());
    }

    @Operation(summary = "Treinamentos concluídos no mês atual")
    @GetMapping("/mes-atual")
    public ResponseEntity<List<TreinamentoResponse>> mesAtual() {
        return ResponseEntity.ok(treinamentoService.listarMesAtual());
    }

    @GetMapping("/mes-atual/total")
    public ResponseEntity<Map<String, Long>> totalMesAtual() {
        return ResponseEntity.ok(Map.of("total", treinamentoService.totalMesAtual()));
    }

    @Operation(summary = "Adição dos treinamentos com data mais recente (últimos 20)")
    @GetMapping("/recentes")
    public ResponseEntity<List<TreinamentoResponse>> recentes() {
        return ResponseEntity.ok(treinamentoService.treinamentosRecentes());
    }

    @Operation(summary = "Conformidade por setor (% concluídos)")
    @GetMapping("/conformidade-setor")
    public ResponseEntity<List<ConformidadeSetorResponse>> conformidadePorSetor() {
        return ResponseEntity.ok(treinamentoService.conformidadePorSetor());
    }
}
```

---

## controller/AlertaController.java

```java
package com.nazca.controller;

import com.nazca.dto.response.AlertaResponse;
import com.nazca.service.AlertaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alertas")
@RequiredArgsConstructor
@Tag(name = "Alertas")
public class AlertaController {

    private final AlertaService alertaService;

    @Operation(summary = "Alertas: POPs vencidos, treinamentos atrasados e revisões pendentes")
    @GetMapping
    public ResponseEntity<List<AlertaResponse>> listar(
            @RequestParam(defaultValue = "false") boolean apenasNaoLidos) {
        return ResponseEntity.ok(
                apenasNaoLidos ? alertaService.listarNaoLidos() : alertaService.listarTodos());
    }

    @GetMapping("/nao-lidos/total")
    public ResponseEntity<Map<String, Long>> totalNaoLidos() {
        return ResponseEntity.ok(Map.of("total", alertaService.totalNaoLidos()));
    }

    @Operation(summary = "Marca alerta como lido")
    @PutMapping("/{id}/lido")
    public ResponseEntity<AlertaResponse> marcarLido(@PathVariable Integer id) {
        return ResponseEntity.ok(alertaService.marcarComoLido(id));
    }

    @Operation(summary = "Marca todos os alertas como lidos")
    @PutMapping("/lidos/todos")
    public ResponseEntity<Void> marcarTodosLidos() {
        alertaService.marcarTodosLidos();
        return ResponseEntity.noContent().build();
    }
}
```

---

## controller/EvidenciaController.java

```java
package com.nazca.controller;

import com.nazca.model.EvidenciaTreinamento;
import com.nazca.service.EvidenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/evidencias")
@RequiredArgsConstructor
@Tag(name = "Evidências")
public class EvidenciaController {

    private final EvidenciaService evidenciaService;

    @GetMapping("/total")
    public ResponseEntity<Map<String, Long>> total() {
        return ResponseEntity.ok(Map.of("total", evidenciaService.totalEvidencias()));
    }

    @GetMapping("/treinamento/{treinamentoId}")
    public ResponseEntity<List<EvidenciaTreinamento>> porTreinamento(@PathVariable Integer treinamentoId) {
        return ResponseEntity.ok(evidenciaService.listarPorTreinamento(treinamentoId));
    }

    @Operation(summary = "Registra evidência de treinamento")
    @PostMapping
    public ResponseEntity<EvidenciaTreinamento> registrar(
            @RequestParam Integer treinamentoId,
            @RequestParam String tipo,
            @RequestParam String arquivoUrl,
            @RequestParam(required = false) String descricao) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(evidenciaService.registrar(treinamentoId, tipo, arquivoUrl, descricao));
    }
}
```

---

## controller/DashboardController.java

```java
package com.nazca.controller;

import com.nazca.dto.response.DashboardResponse;
import com.nazca.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(summary = "Resumo geral: total POPs, colaboradores ativos, treinamentos do mês, alertas, vencimentos etc.")
    @GetMapping
    public ResponseEntity<DashboardResponse> resumo() {
        return ResponseEntity.ok(dashboardService.resumo());
    }
}
```

---

## Mapeamento de Endpoints (resumo)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/dashboard` | Resumo geral |
| GET | `/api/pops` | Todos os POPs com statusLabel |
| GET | `/api/pops/total` | Contagem total |
| GET | `/api/pops/vencimentos-criticos` | POPs vencendo em 30 dias |
| GET | `/api/pops/status/{status}` | Filtrar por status |
| POST | `/api/pops` | Criar POP |
| POST | `/api/pops/importar` | Importar planilha .xlsx |
| GET | `/api/colaboradores` | Ativos com cargo, setor, POPs |
| POST | `/api/colaboradores` | Cadastrar colaborador |
| GET | `/api/colaboradores/conformidade` | Conformidade individual |
| GET | `/api/colaboradores/treinamentos-pendentes` | Pendentes por colaborador |
| GET | `/api/colaboradores/concluiram-todos-pops` | Quem completou tudo |
| GET | `/api/cargos` | Cargos com setor, nível e POPs |
| POST | `/api/cargos` | Criar cargo com POPs obrigatórios |
| GET | `/api/setores` | Setores com qtd de POPs e colaboradores |
| POST | `/api/treinamentos` | Registrar treinamento |
| GET | `/api/treinamentos/conformidade-setor` | Conformidade por setor |
| GET | `/api/treinamentos/recentes` | Treinamentos mais recentes |
| GET | `/api/treinamentos/mes-atual` | Do mês atual |
| GET | `/api/alertas` | Todos os alertas |
| PUT | `/api/alertas/{id}/lido` | Marcar alerta como lido |
| GET | `/api/evidencias/total` | Total de evidências |
| POST | `/api/evidencias` | Registrar evidência |

> **Swagger UI:** `http://localhost:8080/swagger-ui.html`
