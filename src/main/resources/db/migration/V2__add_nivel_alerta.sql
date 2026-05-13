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