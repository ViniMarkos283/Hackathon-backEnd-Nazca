-- ============================================================
--  NAZCA COSMÉTICOS — Schema Inicial (MySQL / Aiven)
-- ============================================================

-- 1. SETOR
CREATE TABLE setor (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    nome      VARCHAR(100) NOT NULL,
    descricao TEXT,
    ativo     BOOLEAN      NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 2. CARGO (sem coluna 'nivel' — adicionada pelo V2)
CREATE TABLE cargo (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    nome      VARCHAR(100) NOT NULL,
    setor_id  INT          NOT NULL,
    descricao TEXT,
    ativo     BOOLEAN      NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cargo_setor FOREIGN KEY (setor_id) REFERENCES setor(id)
) ENGINE=InnoDB;

-- 3. COLABORADOR
CREATE TABLE colaborador (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    nome          VARCHAR(150) NOT NULL,
    email         VARCHAR(150) NULL,
    matricula     VARCHAR(50)  NULL,
    senha         VARCHAR(255) NULL,
    cargo_id      INT          NOT NULL,
    data_admissao DATE         NULL,
    ativo         BOOLEAN      NOT NULL DEFAULT TRUE,
    criado_em     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_colab_cargo FOREIGN KEY (cargo_id) REFERENCES cargo(id)
) ENGINE=InnoDB;

-- 4. POP
CREATE TABLE pop (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    codigo        VARCHAR(20)                          NOT NULL,
    descricao     TEXT                                 NULL,
    setor_id      INT                                  NOT NULL,
    versao        VARCHAR(10)                          NOT NULL DEFAULT '1',
    data_validade DATE                                 NULL,
    status        ENUM('ativo','em_revisao','obsoleto') NOT NULL DEFAULT 'ativo',
    data_criacao  TIMESTAMP                            NULL,
    UNIQUE INDEX idx_pop_codigo (codigo),
    CONSTRAINT fk_pop_setor FOREIGN KEY (setor_id) REFERENCES setor(id)
) ENGINE=InnoDB;

-- 5. ANEXO
CREATE TABLE anexo (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    descricao     TEXT,
    pop_codigo    VARCHAR(50)  NOT NULL,
    data_criacao  DATE         NOT NULL DEFAULT (CURRENT_DATE),
    data_validade DATE         NULL,
    status        VARCHAR(20)  NOT NULL DEFAULT 'ativo',
    criado_em     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_anexo_pop FOREIGN KEY (pop_codigo) REFERENCES pop(codigo)
) ENGINE=InnoDB;

-- 6. POP_CARGO
CREATE TABLE pop_cargo (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    pop_codigo  VARCHAR(20) NOT NULL,
    cargo_id    INT         NOT NULL,
    obrigatorio BOOLEAN     NOT NULL DEFAULT TRUE,
    criado_em   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_pop_cargo_pop   FOREIGN KEY (pop_codigo) REFERENCES pop(codigo),
    CONSTRAINT fk_pop_cargo_cargo FOREIGN KEY (cargo_id)   REFERENCES cargo(id)
) ENGINE=InnoDB;

-- 7. TREINAMENTO_COLABORADOR
CREATE TABLE treinamento_colaborador (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    colaborador_id INT          NOT NULL,
    pop_id         INT          NOT NULL,
    data_conclusao DATE         NOT NULL,
    validade_trein DATE         NOT NULL,
    evidencia_url  VARCHAR(255) NULL,
    status         ENUM('concluido','pendente','vencido') NOT NULL DEFAULT 'concluido',
    criado_em      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_trein_colab FOREIGN KEY (colaborador_id) REFERENCES colaborador(id),
    CONSTRAINT fk_trein_pop   FOREIGN KEY (pop_id)         REFERENCES pop(id)
) ENGINE=InnoDB;

-- 8. EVIDENCIA_TREINAMENTO
CREATE TABLE evidencia_treinamento (
    id                         INT AUTO_INCREMENT PRIMARY KEY,
    treinamento_colaborador_id INT         NOT NULL,
    tipo                       VARCHAR(30) NOT NULL,
    arquivo_url                TEXT        NOT NULL,
    descricao                  TEXT,
    criado_em                  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_evidencia_trein FOREIGN KEY (treinamento_colaborador_id) REFERENCES treinamento_colaborador(id)
) ENGINE=InnoDB;

-- ALERTA (estava no V2)
ALTER TABLE cargo
  ADD COLUMN nivel ENUM('operacional','tecnico','analista','supervisor','gerente')
  NOT NULL DEFAULT 'operacional'
  AFTER setor_id;

CREATE TABLE alerta (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    tipo             ENUM('pop_vencido','treinamento_atrasado','revisao_pendente') NOT NULL,
    mensagem         TEXT NOT NULL,
    lido             BOOLEAN NOT NULL DEFAULT FALSE,
    referencia_id    INT NULL,
    referencia_tipo  VARCHAR(50) NULL,
    criado_em        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;