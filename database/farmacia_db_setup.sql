-- Script SQL para crear la base de datos del Sistema de Farmacia
-- Ejecutar este script en MySQL

-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS farmacia_db;
USE farmacia_db;

-- Tabla clientes
CREATE TABLE IF NOT EXISTS clientes (
    id INTEGER NOT NULL AUTO_INCREMENT,
    dni VARCHAR(8) NOT NULL UNIQUE,
    nombres VARCHAR(255) NOT NULL,
    apellido_paterno VARCHAR(255) NOT NULL,
    apellido_materno VARCHAR(255) NOT NULL,
    telefono VARCHAR(255),
    fuente_datos VARCHAR(255) NOT NULL COMMENT 'RENIEC o MANUAL',
    fecha_registro DATETIME NOT NULL,
    -- Campos legacy para compatibilidad
    nombre VARCHAR(255),
    apellidos VARCHAR(255),
    PRIMARY KEY (id),
    INDEX idx_dni (dni),
    INDEX idx_nombres (nombres),
    INDEX idx_apellido_paterno (apellido_paterno)
);

-- Tabla producto
CREATE TABLE IF NOT EXISTS producto (
    id INTEGER NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(255),
    precio DOUBLE NOT NULL,
    cantidad INTEGER NOT NULL,
    fecha_vencimiento DATE,
    descripcion VARCHAR(255),
    categoria VARCHAR(255),
    PRIMARY KEY (id),
    INDEX idx_nombre (nombre),
    INDEX idx_categoria (categoria)
);

-- Tabla venta
CREATE TABLE IF NOT EXISTS venta (
    id INTEGER NOT NULL AUTO_INCREMENT,
    idcliente INTEGER NOT NULL,
    fecha_registro DATE,
    precio_total DOUBLE NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_idcliente (idcliente),
    INDEX idx_fecha_registro (fecha_registro)
);

-- Tabla venta_detalle
CREATE TABLE IF NOT EXISTS venta_detalle (
    id INTEGER NOT NULL AUTO_INCREMENT,
    idventa INTEGER NOT NULL,
    idproducto INTEGER NOT NULL,
    cantidad INTEGER NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_idventa (idventa),
    INDEX idx_idproducto (idproducto)
);

-- Datos de ejemplo para productos
INSERT INTO producto (nombre, precio, cantidad, fecha_vencimiento, descripcion, categoria) VALUES
('Paracetamol 500mg', 15.50, 100, '2025-12-31', 'Analgésico y antipirético', 'Analgésicos'),
('Ibuprofeno 400mg', 18.00, 80, '2025-11-30', 'Antiinflamatorio no esteroideo', 'Antiinflamatorios'),
('Amoxicilina 500mg', 25.00, 50, '2025-10-15', 'Antibiótico de amplio espectro', 'Antibióticos'),
('Omeprazol 20mg', 22.00, 60, '2026-01-20', 'Inhibidor de la bomba de protones', 'Gastroenterología'),
('Loratadina 10mg', 12.00, 90, '2025-09-30', 'Antihistamínico', 'Antialérgicos'),
('Acetaminofén jarabe', 20.00, 40, '2025-08-15', 'Analgésico pediátrico', 'Pediátricos'),
('Vitamina C 1000mg', 35.00, 70, '2026-03-10', 'Suplemento vitamínico', 'Vitaminas'),
('Diclofenaco gel', 28.00, 30, '2025-07-25', 'Antiinflamatorio tópico', 'Uso tópico'),
('Salbutamol inhalador', 45.00, 25, '2025-12-05', 'Broncodilatador', 'Respiratorio'),
('Metformina 850mg', 30.00, 55, '2026-02-28', 'Antidiabético', 'Endocrinología');

-- Datos de ejemplo para clientes (solo algunos de prueba)
INSERT INTO clientes (dni, nombres, apellido_paterno, apellido_materno, telefono, fuente_datos, fecha_registro) VALUES
('12345678', 'Juan Carlos', 'Pérez', 'García', '987654321', 'MANUAL', NOW()),
('87654321', 'María Elena', 'Rodríguez', 'López', '965432187', 'MANUAL', NOW()),
('11223344', 'Carlos Alberto', 'Mendoza', 'Silva', '912345678', 'MANUAL', NOW());

-- Ejemplo de venta
INSERT INTO venta (idcliente, fecha_registro, precio_total) VALUES
(1, CURDATE(), 33.50);

-- Ejemplo de detalle de venta
INSERT INTO venta_detalle (idventa, idproducto, cantidad) VALUES
(1, 1, 2),  -- 2 Paracetamol
(1, 5, 1);  -- 1 Loratadina

-- Mostrar información de las tablas creadas
SHOW TABLES;

-- Verificar los datos insertados
SELECT 'Productos insertados:' as Info;
SELECT * FROM producto;

SELECT 'Clientes insertados:' as Info;
SELECT * FROM clientes;

SELECT 'Ventas insertadas:' as Info;
SELECT * FROM venta;

SELECT 'Detalles de venta insertados:' as Info;
SELECT * FROM venta_detalle;
