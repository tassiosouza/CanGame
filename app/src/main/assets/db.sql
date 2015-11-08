CREATE TABLE IF NOT EXISTS "tb_paciente"(
  "id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  "nome" VARCHAR(100) NOT NULL,
  "dt_nasc" DATE NOT NULL,
  "sexo" VARCHAR(1) NOT NULL,
  "nome_pai" VARCHAR(100),
  "nome_mae" VARCHAR(100),
  "contato_pai" VARCHAR(50),
  "contato_mae" VARCHAR(50),
  "image_path" VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS "tb_config"(
  "id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  "tempo_exibicao_txt" INTEGER NOT NULL,
  "tempo_reproducao_audio" INTEGER NOT NULL,
  "tempo_reproducao_video" INTEGER NOT NULL,
  "paciente_corrente" INTEGER
);

CREATE TABLE IF NOT EXISTS "tb_categoria"(
  "id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  "descricao" VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS "tb_pecs"(
  "id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  "id_categoria" INTEGER NOT NULL,
  "legenda" VARCHAR(45),
  "dt_hr_criacao" TIMESTAMP NOT NULL,
  "image_path" VARCHAR(200),
  "sound_path" VARCHAR(200),
  "video_path" VARCHAR(200),
  CONSTRAINT "fk_categoria"
    FOREIGN KEY("id_categoria")
    REFERENCES "tb_categoria"("id")
);
CREATE INDEX "tb_pecs.fk_categoria_idx" ON "tb_pecs"("id_categoria");

CREATE TABLE IF NOT EXISTS "tb_rotina"(
  "id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  "id_categoria" INTEGER NOT NULL,
  "nome" VARCHAR(45) NOT NULL,
  CONSTRAINT "fk_categoria"
    FOREIGN KEY("id_categoria")
    REFERENCES "tb_categoria"("id")
);
CREATE INDEX "tb_rotina.fk_categoria_idx" ON "tb_rotina"("id_categoria");

CREATE TABLE IF NOT EXISTS "tb_ranking"(
  "id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  "id_paciente" INTEGER NOT NULL,
  "id_pec" INTEGER,
  "id_rotina" INTEGER,
  "pontuacao" INTEGER NOT NULL,
  "dt_hr_criacao" TIMESTAMP NOT NULL,
  CONSTRAINT "fk_paciente"
    FOREIGN KEY("id_paciente")
    REFERENCES "tb_paciente"("id"),
  CONSTRAINT "fk_pec"
    FOREIGN KEY("id_pec")
    REFERENCES "tb_pecs"("id"),
  CONSTRAINT "fk_rotina"
    FOREIGN KEY("id_rotina")
    REFERENCES "tb_rotina"("id")
);
CREATE INDEX "tb_ranking.fk_paciente_idx" ON "tb_ranking"("id_paciente");
CREATE INDEX "tb_ranking.fk_pec_idx" ON "tb_ranking"("id_pec");
CREATE INDEX "tb_ranking.fk_rotina_idx" ON "tb_ranking"("id_rotina");

CREATE TABLE IF NOT EXISTS "tb_rotina_pecs"(
  "id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  "id_rotina" INTEGER NOT NULL,
  "id_pecs" INTEGER NOT NULL,
  "posicao" INTEGER NOT NULL,
  CONSTRAINT "fk_pecs"
    FOREIGN KEY("id_pecs")
    REFERENCES "tb_pecs"("id"),
  CONSTRAINT "fk_rotina"
    FOREIGN KEY("id_rotina")
    REFERENCES "tb_rotina"("id")
);
CREATE INDEX "tb_rotina_pecs.fk_pecs_idx" ON "tb_rotina_pecs"("id_pecs");
CREATE INDEX "tb_rotina_pecs.fk_rotina_idx" ON "tb_rotina_pecs"("id_rotina");
