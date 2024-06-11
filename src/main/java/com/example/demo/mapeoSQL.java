package com.example.demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Field;
import java.lang.reflect.Constructor;

public class mapeoSQL {

    private Connection conn;

    public mapeoSQL(Connection conn) {
        this.conn = conn;
    }

    public void insertarTabla(Object objeto) {
        try {
            Class<?> clase = objeto.getClass();
            String nombreTabla = clase.getSimpleName().toLowerCase(); 
            try {
                crearTablaSiNoExiste(nombreTabla, clase);
            } catch (SQLException e) {
                System.err.println("Error al crear la tabla: " + e.getMessage());
            }

            insertarDatos(nombreTabla, clase, objeto);
        } catch (SQLException | IllegalAccessException e) {
            System.err.println("Error al crear la clase a la tabla: " + e.getMessage());
        }
    }

    private boolean tablaExiste(String nombreTabla) throws SQLException {
        String query = "SELECT count(*) FROM user_tables WHERE table_name = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, nombreTabla.toUpperCase());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }

    private void crearTablaSiNoExiste(String nombreTabla, Class<?> clase) throws SQLException {
        if (tablaExiste(nombreTabla)) {
            System.out.println("La tabla " + nombreTabla + " ya existe.");
            return;
        }

        StringBuilder query = new StringBuilder("CREATE TABLE ")
                .append(nombreTabla)
                .append(" (");

        Field[] campos = clase.getDeclaredFields();
        boolean tieneClavePrimaria = false;
        for (Field campo : campos) {
            campo.setAccessible(true); // Permitir acceso a campos privados
            String nombreCampo = campo.getName();
            String tipoDato = obtenerTipoDato(campo.getType());
            query.append(nombreCampo).append(" ").append(tipoDato);
            
            if (nombreCampo.equals(nombreTabla + "_id")) {
                query.append(" PRIMARY KEY");
                tieneClavePrimaria = true;
            }
            query.append(", ");
        }

        query.delete(query.length() - 2, query.length());
        query.append(")");

        if (!tieneClavePrimaria) {
            throw new SQLException("La tabla " + nombreTabla + " no tiene un campo clave primaria.");
        }

        try (PreparedStatement statement = conn.prepareStatement(query.toString())) {
            statement.executeUpdate();
            System.out.println("Tabla " + nombreTabla + " creada correctamente.");
        }
    }

    private String obtenerTipoDato(Class<?> tipo) {
        if (tipo == String.class) {
            return "VARCHAR(255)";
        } else if (tipo == int.class || tipo == Integer.class) {
            return "INT";
        } else if (tipo == double.class || tipo == Double.class) {
            return "DOUBLE";
        } else if (tipo == float.class || tipo == Float.class) {
            return "FLOAT";
        } else if (tipo == boolean.class || tipo == Boolean.class) {
            return "BOOLEAN";
        } else {
            return "VARCHAR(255)"; 
        }
    }

    private void insertarDatos(String nombreTabla, Class<?> clase, Object objeto)
            throws SQLException, IllegalAccessException {
        StringBuilder query = new StringBuilder("INSERT INTO ").append(nombreTabla).append(" (");
        StringBuilder values = new StringBuilder("VALUES (");

        Field[] campos = clase.getDeclaredFields();
        for (Field campo : campos) {
            campo.setAccessible(true);
            String nombreCampo = campo.getName();
            Object valorCampo = campo.get(objeto);

            query.append(nombreCampo).append(", ");
            values.append("'").append(valorCampo).append("', ");
        }

      
        query.delete(query.length() - 2, query.length());
        values.delete(values.length() - 2, values.length());

        query.append(") ");
        values.append(")");

        String insertQuery = query.toString() + values.toString();

        try (PreparedStatement statement = conn.prepareStatement(insertQuery)) {
            statement.executeUpdate();
            System.out.println("Datos insertados en la tabla " + nombreTabla + " correctamente.");
        }
    }

    public <T> List<T> recuperarDeTabla(Class<T> clase) {
        List<T> resultados = new ArrayList<>();
        String nombreTabla = clase.getSimpleName().toLowerCase(); 
        try {
            String query = "SELECT * FROM " + nombreTabla;
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        T instancia = construirInstancia(clase, resultSet);
                        resultados.add(instancia);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al recuperar objetos de la tabla: " + e.getMessage());
        }
        return resultados;
    }

    private <T> T construirInstancia(Class<T> clase, ResultSet resultSet) throws SQLException {
        try {
            Constructor<T> constructor = clase.getDeclaredConstructor();
            constructor.setAccessible(true);
            T instancia = constructor.newInstance();

            Field[] campos = clase.getDeclaredFields();
            for (Field campo : campos) {
                campo.setAccessible(true);
                String nombreCampo = campo.getName();
                Object valorCampo = resultSet.getObject(nombreCampo);

                if (valorCampo != null) {

                    if (campo.getType() == int.class || campo.getType() == Integer.class) {
                        valorCampo = ((Number) valorCampo).intValue();
                    } else if (campo.getType() == double.class || campo.getType() == Double.class) {
                        valorCampo = ((Number) valorCampo).doubleValue();
                    } else if (campo.getType() == float.class || campo.getType() == Float.class) {
                        valorCampo = ((Number) valorCampo).floatValue();
                    } else if (campo.getType() == long.class || campo.getType() == Long.class) {
                        valorCampo = ((Number) valorCampo).longValue();
                    } else if (campo.getType() == boolean.class || campo.getType() == Boolean.class) {
                        valorCampo = (valorCampo instanceof Number) ? ((Number) valorCampo).intValue() != 0
                                : Boolean.parseBoolean(valorCampo.toString());
                    }

                    campo.set(instancia, valorCampo);
                }
            }

            return instancia;
        } catch (NoSuchMethodException e) {
            throw new SQLException("No se pudo encontrar el constructor predeterminado para la clase "
                    + clase.getSimpleName() + ": " + e.getMessage(), e);
        } catch (InstantiationException | IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
            throw new SQLException("Error al instanciar la clase " + clase.getSimpleName() + ": " + e.getMessage(), e);
        }
    }

    public void eliminarEntidad(Class<?> clase, int id) {
        String nombreTabla = clase.getSimpleName().toLowerCase();
        String nombreCampoId = nombreTabla + "_id";

        String query = "DELETE FROM " + nombreTabla + " WHERE " + nombreCampoId + " = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setObject(1, id);
            int filasAfectadas = statement.executeUpdate();
            if (filasAfectadas > 0) {
                System.out
                        .println("Registro con ID " + id + " eliminado correctamente de la tabla " + nombreTabla + ".");
            } else {
                System.out.println("No se encontró ningún registro con ID " + id + " en la tabla " + nombreTabla + ".");
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar el registro con ID " + id + ": " + e.getMessage());
        }
    }

    public void modificarEntidad(Object objeto) {
        try {
            Class<?> clase = objeto.getClass();
            String nombreTabla = clase.getSimpleName().toLowerCase();

            if (!tablaExiste(nombreTabla)) {
                System.err.println("La tabla " + nombreTabla + " no existe.");
                return;
            }

            modificarDatos(nombreTabla, clase, objeto);
        } catch (SQLException | IllegalAccessException e) {
            System.err.println("Error al modificar la clase en la tabla: " + e.getMessage());
        }
    }

    private void modificarDatos(String nombreTabla, Class<?> clase, Object objeto)
            throws SQLException, IllegalAccessException {
        StringBuilder query = new StringBuilder("UPDATE ").append(nombreTabla).append(" SET ");

        Field[] campos = clase.getDeclaredFields();
        String nombreCampoId = nombreTabla.toLowerCase() + "_id";
        boolean encontrado = false;
        for (Field campo : campos) {
            campo.setAccessible(true);
            String nombreCampo = campo.getName();
            if (nombreCampo.equals(nombreCampoId)) {
                encontrado = true;
                continue;
            }

            @SuppressWarnings("unused")
            Object valorCampo = campo.get(objeto);
            query.append(nombreCampo).append(" = ?, ");
        }

        if (!encontrado) {
            throw new SQLException("No se encontró el campo ID en la clase " + clase.getSimpleName());
        }

        query.delete(query.length() - 2, query.length());

        query.append(" WHERE ").append(nombreCampoId).append(" = ?");

        try (PreparedStatement statement = conn.prepareStatement(query.toString())) {
            int index = 1;
            for (Field campo : campos) {
                String nombreCampo = campo.getName();
                if (nombreCampo.equals(nombreCampoId)) {
                    continue; // Saltar el campo ID
                }
                Object valorCampo = campo.get(objeto);
                statement.setObject(index, valorCampo);
                index++;
            }

            Field idField;
            try {
                idField = clase.getDeclaredField(nombreCampoId);
                idField.setAccessible(true);
                Object id = idField.get(objeto);
                statement.setObject(index, id);
            } catch (NoSuchFieldException e) {
                throw new SQLException("No se encontró el campo ID en la clase " + clase.getSimpleName());
            }

            int filasAfectadas = statement.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Datos modificados en la tabla " + nombreTabla + " correctamente.");
            } else {
                System.out.println("No se encontró ningún registro con el ID en la tabla " + nombreTabla + ".");
            }
        }
    }

}
