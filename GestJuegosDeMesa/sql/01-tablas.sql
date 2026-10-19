SET NAMES utf8mb4;

-- Datos de la asociación: una sola fila (id = 1)
CREATE TABLE asociacion (
    id             INT PRIMARY KEY,
    razon_social   VARCHAR(100) NOT NULL,
    cif            VARCHAR(9)   NOT NULL,
    direccion      VARCHAR(150) NOT NULL,
    telefono       VARCHAR(15),
    email          VARCHAR(100),
    fecha_apertura DATE         NOT NULL
);

CREATE TABLE socias (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    documento VARCHAR(20)  NOT NULL UNIQUE,  -- DNI, NIE o pasaporte
    nombre    VARCHAR(50)  NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    telefono  VARCHAR(15),
    edad      INT          NOT NULL,
    email     VARCHAR(100),
    eliminada BOOLEAN      NOT NULL DEFAULT FALSE,  -- baja lógica
    CONSTRAINT chk_socias_edad CHECK (edad > 0)
);

CREATE TABLE juegos (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    nombre        VARCHAR(100) NOT NULL,
    autores       VARCHAR(150) NOT NULL,
    descripcion   VARCHAR(500),
    min_jugadores INT          NOT NULL,
    max_jugadores INT          NOT NULL,
    duracion      INT          NOT NULL,  -- en minutos
    tematica      VARCHAR(50)  NOT NULL,
    tipo          ENUM ('COMPETITIVO', 'COOPERATIVO') NOT NULL,
    ubicacion     VARCHAR(100) NOT NULL,
    estado        ENUM ('DISPONIBLE', 'PRESTADO', 'EN_REPARACION') NOT NULL DEFAULT 'DISPONIBLE',
    socia_id      INT NULL,  -- propietaria; NULL = el juego es de la asociación
    fecha_alta    DATE         NOT NULL,
    eliminado     BOOLEAN      NOT NULL DEFAULT FALSE,  -- baja lógica
    CONSTRAINT fk_juegos_socia FOREIGN KEY (socia_id) REFERENCES socias (id),
    CONSTRAINT chk_juegos_jugadores CHECK (min_jugadores > 0 AND max_jugadores >= min_jugadores),
    CONSTRAINT chk_juegos_duracion CHECK (duracion > 0)
);

CREATE TABLE partidas (
    id                 INT AUTO_INCREMENT PRIMARY KEY,
    juego_id           INT  NOT NULL,
    fecha              DATE NOT NULL,
    hora               TIME NOT NULL,
    jugadoras_deseadas INT  NOT NULL,
    CONSTRAINT fk_partidas_juego FOREIGN KEY (juego_id) REFERENCES juegos (id)
);

-- Jugadoras de cada partida: una socia o una invitada (solo con su nombre)
CREATE TABLE partida_jugadoras (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    partida_id INT     NOT NULL,
    socia_id   INT NULL,
    invitada   VARCHAR(100) NULL,
    ganadora   BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_pj_partida FOREIGN KEY (partida_id) REFERENCES partidas (id) ON DELETE CASCADE,
    CONSTRAINT fk_pj_socia FOREIGN KEY (socia_id) REFERENCES socias (id),
    CONSTRAINT uq_pj_socia UNIQUE (partida_id, socia_id),
    CONSTRAINT chk_pj_socia_o_invitada CHECK ((socia_id IS NOT NULL AND invitada IS NULL)
                                           OR (socia_id IS NULL AND invitada IS NOT NULL))
);