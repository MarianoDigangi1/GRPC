-- =========================================================
-- CREACIÓN DE BASE DE DATOS: EMPUJE COMUNITARIO
-- Versión: Integrada con Kafka (estructura final)
-- Estrategia: Tabla evento unificada con campo origen_organizacion_id
-- =========================================================

CREATE DATABASE IF NOT EXISTS empuje_comunitario_db;
USE empuje_comunitario_db;

-- =========================================================
-- Tabla: organizaciones
-- =========================================================
CREATE TABLE organizaciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    external_org_id VARCHAR(100) NOT NULL UNIQUE,  -- ID que identifica a la ONG dentro de la red
    nombre VARCHAR(200) NOT NULL
);

INSERT INTO organizaciones (external_org_id, nombre) VALUES ('ong-1', 'Ong comunitaria');
INSERT INTO organizaciones (external_org_id, nombre) VALUES ('ong-2', 'ONG Patitos');
INSERT INTO organizaciones (external_org_id, nombre) VALUES ('ong-3', 'ONG Sonrisas');
-- =========================================================
-- Tabla: usuarios
-- =========================================================
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombreUsuario VARCHAR(50) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    clave VARCHAR(255) NOT NULL, -- hash (bcrypt o similar)
    email VARCHAR(100) NOT NULL UNIQUE,
    rol ENUM('Presidente', 'Vocal', 'Coordinador', 'Voluntario') NOT NULL,
    estaActivo BOOLEAN NOT NULL DEFAULT TRUE
);

-- =========================================================
-- Tabla: inventario
-- =========================================================
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

-- =========================================================
-- Tabla: evento (única, incluye eventos locales y externos)
-- =========================================================
CREATE TABLE evento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    descripcion TEXT,
    fecha_evento DATETIME NOT NULL,
    origen_organizacion_id VARCHAR(100) NULL,  -- NULL = evento local; valor = evento externo
    vigente BOOLEAN DEFAULT TRUE,
    evento_id_organizacion_externa VARCHAR(100) NULL, -- ID del evento en la ONG externa
    publicado BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (origen_organizacion_id) REFERENCES organizaciones(external_org_id) ON DELETE SET NULL
);
-- =========================================================
-- Tabla: evento_usuario (relación muchos a muchos)
-- =========================================================
CREATE TABLE evento_usuario (
    evento_id INT,
    usuario_id INT,
    PRIMARY KEY (evento_id, usuario_id),
    FOREIGN KEY (evento_id) REFERENCES evento(id) ON DELETE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- =========================================================
-- Tabla: evento_inventario (asocia inventario con evento)
-- =========================================================
CREATE TABLE evento_inventario (
    evento_id INT,
    inventario_id INT,
    cantidad_usada INT NOT NULL CHECK (cantidad_usada >= 0),
    PRIMARY KEY (evento_id, inventario_id),
    FOREIGN KEY (evento_id) REFERENCES evento(id) ON DELETE CASCADE,
    FOREIGN KEY (inventario_id) REFERENCES inventario(id) ON DELETE CASCADE
);

-- =========================================================
-- Tabla: solicitud_externa
-- (Solicitudes de otras ONGs recibidas por Kafka)
-- =========================================================
CREATE TABLE solicitud_externa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    external_org_id VARCHAR(100) NOT NULL,
    solicitud_id VARCHAR(100) NOT NULL,
    contenido JSON NOT NULL, -- lista de pedidos: [{categoria, descripcion}]
    recibida_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    vigente BOOLEAN DEFAULT TRUE,
    UNIQUE (external_org_id, solicitud_id),
    FOREIGN KEY (external_org_id) REFERENCES organizaciones(external_org_id) 
);

-- =========================================================
-- Tabla: oferta_externa
-- (Ofertas de otras ONGs recibidas por Kafka)
-- =========================================================
CREATE TABLE oferta_externa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    external_org_id VARCHAR(100) NOT NULL,
    oferta_id VARCHAR(100) NOT NULL,
    contenido JSON NOT NULL,
    recibida_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (external_org_id, oferta_id),
    FOREIGN KEY (external_org_id) REFERENCES organizaciones(external_org_id) 
);

CREATE TABLE filtros_eventos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    parametros JSON NOT NULL,
    usuario_id INT NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE transferencia_donacion_externa (
	id INT AUTO_INCREMENT PRIMARY KEY,
	id_transferencia VARCHAR(255) NOT NULL,
    id_organizacion_origen VARCHAR(255) NOT NULL,
    id_organizacion_destino VARCHAR(255) NOT NULL,
    fecha_transferencia DATETIME DEFAULT CURRENT_TIMESTAMP,
	contenido JSON NOT NULL, -- lista de pedidos: [{categoria, descripcion}] 
    es_externa BOOLEAN DEFAULT TRUE, -- Quizas no haga falta
    vigente BOOLEAN DEFAULT TRUE,
    UNIQUE (id_transferencia, id_organizacion_origen),
    FOREIGN KEY (id_organizacion_origen) REFERENCES organizaciones(external_org_id),
    FOREIGN KEY (id_organizacion_destino) REFERENCES organizaciones(external_org_id)
);
