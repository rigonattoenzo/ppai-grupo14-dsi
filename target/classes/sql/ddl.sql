-- Roles
CREATE TABLE IF NOT EXISTS rol (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre VARCHAR(50) UNIQUE NOT NULL,
    descripcion VARCHAR(255)
);

-- Empleados
CREATE TABLE IF NOT EXISTS empleado (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    codigo VARCHAR(50) UNIQUE NOT NULL,
    telefono VARCHAR(255),
    mail VARCHAR(150) NOT NULL,
    rol_id INTEGER REFERENCES rol(id)
);

-- Estaciones Sismológicas
CREATE TABLE IF NOT EXISTS estacion_sismologica (
    codigo_estacion VARCHAR(100) PRIMARY KEY,
    nombre VARCHAR(255),
    latitud REAL,
    longitud REAL,
    documento_certificacion_adq VARCHAR(255),
    fecha_solicitud_certificacion TIMESTAMP,
    nro_certificacion_adquisicion VARCHAR(50)
);

-- Sismógrafos
CREATE TABLE IF NOT EXISTS sismografo (
    id_sismografo VARCHAR(100) PRIMARY KEY UNIQUE NOT NULL,
    numero_serie VARCHAR(100),
    fecha_adquisicion TIMESTAMP,
    estacion_codigo VARCHAR(100) UNIQUE REFERENCES estacion_sismologica(codigo_estacion),
    estado VARCHAR(255)
);

-- Órdenes de Inspección
CREATE TABLE IF NOT EXISTS orden_inspeccion (
    nro_orden INTEGER PRIMARY KEY,
    fecha_hora_inicio TIMESTAMP,
    fecha_hora_finalizacion TIMESTAMP,
    fecha_hora_cierre TIMESTAMP,
    observacion_cierre_orden TEXT,
    empleado_id INTEGER REFERENCES empleado(id),
    estacion_id VARCHAR(100) REFERENCES estacion_sismologica(codigo_estacion),
    estado VARCHAR(255)
);

-- Motivo Tipo
CREATE TABLE IF NOT EXISTS motivo_tipo (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    descripcion VARCHAR(255) NOT NULL
);

-- Motivo Fuera de Servicio
CREATE TABLE IF NOT EXISTS motivo_fuera_de_servicio (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    comentario TEXT,
    motivo_id INTEGER REFERENCES motivo_tipo(id),
    cambio_estado_id INTEGER REFERENCES cambio_de_estado(id)
);

-- Cambios de Estado
CREATE TABLE IF NOT EXISTS cambio_de_estado (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha_hora_inicio TIMESTAMP,
    fecha_hora_fin TIMESTAMP,
    sismografo_id INTEGER REFERENCES sismografo(id),
    empleado_id INTEGER REFERENCES empleado(id)
);

-- Usuarios
CREATE TABLE IF NOT EXISTS usuario (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre_usuario VARCHAR(100) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    empleado_id INTEGER UNIQUE NOT NULL REFERENCES empleado(id)
);