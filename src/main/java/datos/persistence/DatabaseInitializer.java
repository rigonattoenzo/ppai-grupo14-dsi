package datos.persistence;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public final class DatabaseInitializer {
    private static final String JDBC_URL = "jdbc:sqlite:ppai_grupo14.db";
    private static final String DDL_CLASSPATH = "/sql/ddl.sql";

    private DatabaseInitializer() {
    }

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            // Habilitar foreign keys en SQLite
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
            }

            // Intenta ejecutar el script DDL
            if (!executeDDLScript(conn)) {
                System.out.println("⚠️ No se encontró ddl.sql. Creando esquema básico...");
                createBasicSchema(conn);
            }

            System.out.println("✓ Base de datos SQLite inicializada correctamente en: ppai_grupo14.db");
        } catch (SQLException e) {
            System.err.println("❌ Error al inicializar BD: " + e.getMessage());
            throw new RuntimeException("No se pudo conectar a SQLite.", e);
        }
    }

    /**
     * Ejecuta el script DDL línea por línea (compatible con SQLite)
     */
    private static boolean executeDDLScript(Connection conn) {
        try (var in = DatabaseInitializer.class.getResourceAsStream(DDL_CLASSPATH)) {
            if (in == null) {
                return false;
            }

            try (var reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                StringBuilder sql = new StringBuilder();
                String line;

                BufferedReader bufferedReader = new BufferedReader(reader);
                while ((line = bufferedReader.readLine()) != null) {
                    line = line.trim();

                    // Ignorar comentarios
                    if (line.startsWith("--") || line.isEmpty()) {
                        continue;
                    }

                    sql.append(line).append(" ");

                    // Ejecutar cuando encontramos ;
                    if (line.endsWith(";")) {
                        try (Statement stmt = conn.createStatement()) {
                            stmt.execute(sql.toString());
                        }
                        sql = new StringBuilder();
                    }
                }

                System.out.println("✓ DDL script ejecutado desde: " + DDL_CLASSPATH);
                return true;
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error ejecutando DDL: " + e.getMessage());
            return false;
        }
    }

    private static void createBasicSchema(Connection conn) throws SQLException {
        String[] ddlStatements = {
                "CREATE TABLE IF NOT EXISTS rol (" +
                        "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "  codigo VARCHAR(50) UNIQUE NOT NULL," +
                        "  descripcion VARCHAR(255)" +
                        ");",

                "CREATE TABLE IF NOT EXISTS empleado (" +
                        "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "  nombre VARCHAR(100) NOT NULL," +
                        "  apellido VARCHAR(100) NOT NULL," +
                        "  codigo VARCHAR(50) UNIQUE NOT NULL," +
                        "  mail VARCHAR(150) NOT NULL," +
                        "  rol_id INTEGER REFERENCES rol(id)" +
                        ");",

                "CREATE TABLE IF NOT EXISTS estacion_sismologica (" +
                        "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "  codigo VARCHAR(100) UNIQUE NOT NULL," +
                        "  nombre VARCHAR(255)," +
                        "  latitud REAL," +
                        "  longitud REAL," +
                        "  fecha_instalacion TIMESTAMP" +
                        ");",

                "CREATE TABLE IF NOT EXISTS sismografo (" +
                        "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "  id_sismografo VARCHAR(100) UNIQUE NOT NULL," +
                        "  numero_serie VARCHAR(100)," +
                        "  fecha_instalacion TIMESTAMP," +
                        "  estacion_id INTEGER REFERENCES estacion_sismologica(id)," +
                        "  estado_actual VARCHAR(50)" +
                        ");",

                "CREATE TABLE IF NOT EXISTS orden_inspeccion (" +
                        "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "  nro_orden INTEGER UNIQUE NOT NULL," +
                        "  fecha_inicio TIMESTAMP," +
                        "  fecha_finalizacion TIMESTAMP," +
                        "  fecha_cierre TIMESTAMP," +
                        "  observacion_cierre TEXT," +
                        "  empleado_id INTEGER REFERENCES empleado(id)," +
                        "  estacion_id INTEGER REFERENCES estacion_sismologica(id)," +
                        "  estado VARCHAR(50)" +
                        ");",

                "CREATE TABLE IF NOT EXISTS motivo_tipo (" +
                        "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "  descripcion VARCHAR(255) NOT NULL" +
                        ");",

                "CREATE TABLE IF NOT EXISTS motivo_fuera_de_servicio (" +
                        "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "  motivo_id INTEGER REFERENCES motivo_tipo(id)," +
                        "  orden_id INTEGER REFERENCES orden_inspeccion(id)," +
                        "  comentario TEXT" +
                        ");",

                "CREATE TABLE IF NOT EXISTS cambio_de_estado (" +
                        "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "  sismografo_id INTEGER REFERENCES sismografo(id)," +
                        "  estado VARCHAR(50)," +
                        "  fecha_hora_inicio TIMESTAMP," +
                        "  fecha_hora_fin TIMESTAMP" +
                        ");"
        };

        try (Statement stmt = conn.createStatement()) {
            for (String ddl : ddlStatements) {
                try {
                    stmt.execute(ddl);
                } catch (SQLException e) {
                    if (!e.getMessage().contains("already exists")) {
                        System.err.println("⚠️ Error ejecutando DDL: " + e.getMessage());
                    }
                }
            }
        }
    }
}
