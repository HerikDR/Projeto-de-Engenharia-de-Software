DROP TABLE IF EXISTS tarefas CASCADE;
DROP TABLE IF EXISTS usuarios CASCADE;


CREATE TABLE usuarios (
    id       SERIAL       PRIMARY KEY,
    nome     VARCHAR(100) NOT NULL,
    email    VARCHAR(255) NOT NULL UNIQUE,
    senha    VARCHAR(255) NOT NULL
);

CREATE TABLE tarefas (
    id           SERIAL       PRIMARY KEY,
    usuario_id   INT          NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    titulo       VARCHAR(200) NOT NULL,
    descricao    TEXT,
    concluida    BOOLEAN      NOT NULL DEFAULT FALSE,
    criado_em    TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    atualizado_em TIMESTAMPTZ NOT NULL DEFAULT NOW()
);