SET NAMES utf8mb4;

INSERT INTO asociacion (id, razon_social, cif, direccion, telefono, email, fecha_apertura)
VALUES (1, 'Asociación Lúdica Gasteiz', 'G01234567', 'Calle Dato 12, 01005 Vitoria-Gasteiz',
        '945123456', 'info@ludicagasteiz.eus', '2015-03-21');

INSERT INTO socias (documento, nombre, apellidos, telefono, edad, email)
VALUES ('12345678Z', 'Ane', 'Etxeberria Ruiz', '600111222', 34, 'ane@correo.com'),
       ('X1234567L', 'Lucía', 'Fernández Gil', '600333444', 27, 'lucia@correo.com'),
       ('PAB123456', 'Marta', 'Sánchez Díaz', '600555666', 41, 'marta@correo.com');

INSERT INTO juegos (nombre, autores, descripcion, min_jugadores, max_jugadores, duracion, tematica,
                    tipo, ubicacion, estado, socia_id, fecha_alta)
VALUES ('Catan', 'Klaus Teuber', 'Coloniza la isla comerciando con recursos', 3, 4, 90, 'Comercio',
        'COMPETITIVO', 'Estantería A', 'DISPONIBLE', NULL, '2015-04-01'),
       ('Pandemic', 'Matt Leacock', 'Salvad al mundo de cuatro enfermedades', 2, 4, 45, 'Medicina',
        'COOPERATIVO', 'Estantería B', 'DISPONIBLE', NULL, '2016-09-10'),
       ('Carcassonne', 'Klaus-Jürgen Wrede', 'Construye ciudades y caminos con losetas', 2, 5, 40, 'Medieval',
        'COMPETITIVO', 'Estantería A', 'PRESTADO', 1, '2018-01-15'),
       ('Azul', 'Michael Kiesling', 'Decora las paredes del palacio con azulejos', 2, 4, 40, 'Abstracto',
        'COMPETITIVO', 'Estantería C', 'EN_REPARACION', 2, '2020-06-05');

INSERT INTO partidas (juego_id, fecha, hora, jugadoras_deseadas)
VALUES (1, '2025-10-04', '18:00:00', 4),
       (2, '2025-11-15', '17:30:00', 3);

INSERT INTO partida_jugadoras (partida_id, socia_id, invitada, ganadora)
VALUES (1, 1, NULL, TRUE),
       (1, 2, NULL, FALSE),
       (1, NULL, 'Irene López', FALSE),
       (2, 3, NULL, FALSE);