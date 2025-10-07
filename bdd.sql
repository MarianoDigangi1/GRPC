create DATABASE empuje_comunitario_db;
use empuje_comunitario_db;


CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombreUsuario VARCHAR(50) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    clave VARCHAR(255) NOT NULL, -- se guarda encriptada (hash)
    email VARCHAR(100) NOT NULL UNIQUE,
    rol ENUM('Presidente', 'Vocal', 'Coordinador', 'Voluntario') NOT NULL,
    estaActivo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE inventario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    categoria ENUM('ROPA','ALIMENTOS','JUGUETES','UTILES_ESCOLARES') NOT NULL,
    descripcion VARCHAR(200),
    cantidad INT NOT NULL CHECK (cantidad >= 0),
    eliminado BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INT,
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    updated_by INT,
    FOREIGN KEY (created_by) REFERENCES usuarios(id),
    FOREIGN KEY (updated_by) REFERENCES usuarios(id)
);

CREATE TABLE evento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    descripcion TEXT,
    fecha_evento DATETIME NOT NULL
);

-- Relación Evento - Usuario (muchos a muchos)
CREATE TABLE evento_usuario (
    evento_id INT,
    usuario_id INT,
    PRIMARY KEY (evento_id, usuario_id),
    FOREIGN KEY (evento_id) REFERENCES evento(id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);
-- Relación Evento - Inventario (opcional, si se reparten donaciones)
CREATE TABLE evento_inventario (
    evento_id INT,
    inventario_id INT,
    cantidad_usada INT NOT NULL CHECK (cantidad_usada >= 0),
    PRIMARY KEY (evento_id, inventario_id),
    FOREIGN KEY (evento_id) REFERENCES evento(id),
    FOREIGN KEY (inventario_id) REFERENCES inventario(id)
);

CREATE TABLE solicitud_donacion_externa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_solicitud VARCHAR(100) NOT NULL UNIQUE,
    id_organizacion_solicitante INT NOT NULL,
    fecha_solicitud TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activa BOOLEAN DEFAULT TRUE,
    es_externa boolean DEFAULT FALSE,
    UNIQUE KEY unique_solicitud (id_solicitud, id_organizacion_solicitante)
);

CREATE TABLE solicitud_donacion_externa_item (
    id INT AUTO_INCREMENT PRIMARY KEY,
    solicitud_id INT NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    descripcion VARCHAR(200) NOT NULL,
    FOREIGN KEY (solicitud_id) REFERENCES solicitud_donacion_externa(id)
);

INSERT INTO usuarios (nombreUsuario, nombre, apellido, telefono, clave, email, rol, estaActivo)
VALUES
('jpresidente', 'Juan', 'Pérez', '+541112345678', 'hash_ejemplo1', 'juan.perez@email.com', 'Presidente', TRUE),
('mvocal', 'María', 'Vocales', '+541198765432', 'hash_ejemplo2', 'maria.vocales@email.com', 'Vocal', TRUE),
('cconsejero', 'Carlos', 'Consejo', '+549112233445', 'hash_ejemplo3', 'carlos.consejo@email.com', 'Coordinador', FALSE),
('otrousuario', 'Ana', 'Gómez', '+541167788990', 'hash_ejemplo4', 'ana.gomez@email.com', 'Voluntario', TRUE),
('usuarioinactivo', 'Luis', 'Ramírez', '+549112299887', 'hash_ejemplo5', 'luis.ramirez@email.com', 'Voluntario', FALSE);

select * from empuje_comunitario_db.usuarios;

