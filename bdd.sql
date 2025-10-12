create DATABASE empuje_comunitario_db;
use empuje_comunitario_db;

-- ============================================================
-- 🧍 TABLA DE USUARIOS
-- ============================================================
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombreUsuario VARCHAR(50) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    clave VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    rol ENUM('PRESIDENTE', 'COORDINADOR', 'VOCAL', 'VOLUNTARIO') NOT NULL,
    estaActivo TINYINT(1) DEFAULT 1
);


CREATE TABLE evento_voluntario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_evento VARCHAR(100) NOT NULL,         -- ID del evento
    id_organizacion_voluntario INT NOT NULL, -- Organización del voluntario
    id_voluntario INT NOT NULL,              -- ID dentro de SU organización
    nombre VARCHAR(100),
    apellido VARCHAR(100),
    telefono VARCHAR(50),
    email VARCHAR(100),
    fecha_adhesion DATETIME DEFAULT CURRENT_TIMESTAMP
);
-- ============================================================
-- 📦 TABLA DE INVENTARIO LOCAL
-- ============================================================
CREATE TABLE inventario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    categoria ENUM('ALIMENTOS', 'ROPA', 'HIGIENE', 'OTROS') NOT NULL,
    descripcion VARCHAR(200) NOT NULL,
    cantidad INT DEFAULT 0,
    eliminado TINYINT(1) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INT,
    updated_at TIMESTAMP NULL,
    updated_by INT,
    FOREIGN KEY (created_by) REFERENCES usuarios(id),
    FOREIGN KEY (updated_by) REFERENCES usuarios(id)
);

-- ============================================================
-- 🎉 EVENTOS INTERNOS DE LA ORGANIZACIÓN
-- ============================================================
CREATE TABLE evento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    descripcion TEXT,
    fecha_evento DATETIME NOT NULL,
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE evento_usuario (
    evento_id INT,
    usuario_id INT,
    PRIMARY KEY (evento_id, usuario_id),
    FOREIGN KEY (evento_id) REFERENCES evento(id) ON DELETE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE evento_inventario (
    evento_id INT,
    inventario_id INT,
    cantidad_usada INT,
    PRIMARY KEY (evento_id, inventario_id),
    FOREIGN KEY (evento_id) REFERENCES evento(id),
    FOREIGN KEY (inventario_id) REFERENCES inventario(id)
);

-- ============================================================
-- 📨 SOLICITUDES DE DONACIÓN (Kafka Topic: /solicitud-donaciones)
-- ============================================================
CREATE TABLE solicitud_donacion_externa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_solicitud VARCHAR(100) NOT NULL,
    id_organizacion_solicitante INT NOT NULL,
    fecha_solicitud TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activa BOOLEAN DEFAULT TRUE,
    es_externa BOOLEAN DEFAULT TRUE,
    UNIQUE KEY unique_solicitud (id_solicitud, id_organizacion_solicitante)
);

CREATE TABLE solicitud_donacion_externa_item (
    id INT AUTO_INCREMENT PRIMARY KEY,
    solicitud_id INT NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    descripcion VARCHAR(200) NOT NULL,
    FOREIGN KEY (solicitud_id) REFERENCES solicitud_donacion_externa(id)
);

-- ============================================================
-- 🎁 OFERTAS DE DONACIÓN (Kafka Topic: /oferta-donaciones)
-- ============================================================
CREATE TABLE oferta_donacion_externa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_oferta VARCHAR(100) NOT NULL,
    id_organizacion_donante INT NOT NULL,
    fecha_oferta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_oferta (id_oferta, id_organizacion_donante)
);

CREATE TABLE oferta_donacion_externa_item (
    id INT AUTO_INCREMENT PRIMARY KEY,
    oferta_id INT NOT NULL,
    categoria VARCHAR(100),
    descripcion VARCHAR(200),
    cantidad VARCHAR(50),
    FOREIGN KEY (oferta_id) REFERENCES oferta_donacion_externa(id)
);

-- ============================================================
-- 🔄 TRANSFERENCIAS ENTRE ORGANIZACIONES
-- (Kafka Topic: /transferencia-donaciones/{id-org})
-- ============================================================
CREATE TABLE transferencia_donacion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_solicitud VARCHAR(100) NOT NULL,
    id_organizacion_donante INT NOT NULL,
    id_organizacion_receptora INT NOT NULL,
    fecha_transferencia TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE transferencia_donacion_item (
    id INT AUTO_INCREMENT PRIMARY KEY,
    transferencia_id INT NOT NULL,
    categoria VARCHAR(100),
    descripcion VARCHAR(200),
    cantidad VARCHAR(50),
    FOREIGN KEY (transferencia_id) REFERENCES transferencia_donacion(id)
);

-- ============================================================
-- 🌍 EVENTOS EXTERNOS (Kafka Topic: /eventos-solidarios)
-- ============================================================
CREATE TABLE evento_externo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_evento VARCHAR(100) NOT NULL,
    id_organizacion INT NOT NULL,
    nombre VARCHAR(200),
    descripcion TEXT,
    fecha_evento DATETIME,
    vigente BOOLEAN DEFAULT TRUE,
    UNIQUE KEY unique_evento (id_evento, id_organizacion)
);

-- ============================================================
-- 🤝 ADHESIONES A EVENTOS EXTERNOS (Kafka Topic: /adhesion-evento/{id-organizador})
-- ============================================================
CREATE TABLE adhesion_evento_externo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_evento VARCHAR(100) NOT NULL,
    id_organizacion_voluntario INT NOT NULL,
    id_voluntario INT NOT NULL,
    nombre VARCHAR(100),
    apellido VARCHAR(100),
    telefono VARCHAR(50),
    email VARCHAR(100),
    fecha_adhesion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);