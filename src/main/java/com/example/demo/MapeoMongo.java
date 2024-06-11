package com.example.demo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import org.bson.Document;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class MapeoMongo {

    private MongoDatabase baseDeDatos;

    public MapeoMongo(MongoDatabase baseDeDatos) {
        this.baseDeDatos = baseDeDatos;
    }

    public void insertar(Object objeto) {
        try {
            Class<?> claseObjeto = objeto.getClass();
            String nombreColeccion = claseObjeto.getSimpleName().toLowerCase();
            MongoCollection<Document> coleccion = baseDeDatos.getCollection(nombreColeccion);

            Document documento = convertirAJson(objeto, claseObjeto);

            coleccion.insertOne(documento);
            System.out.println("Documento insertado en la colección " + nombreColeccion + " correctamente.");
        } catch (Exception e) {
            System.err.println("Error al insertar el objeto en MongoDB: " + e.getMessage());
        }
    }

    private Document convertirAJson(Object objeto, Class<?> claseObjeto) throws IllegalAccessException {
        Document documento = new Document();
        Field[] campos = claseObjeto.getDeclaredFields();
        String nombreCampoId = claseObjeto.getSimpleName().toLowerCase() + "_id";

        for (Field campo : campos) {
            campo.setAccessible(true);
            String nombreCampo = campo.getName();
            Object valorCampo = campo.get(objeto);

            if (nombreCampo.equals(nombreCampoId)) {
                documento.append("_id", valorCampo);
            } else {
                documento.append(nombreCampo, valorCampo);
            }
        }
        return documento;
    }

    public <T> List<T> seleccionarTodo(Class<T> claseObjeto) {
        List<T> resultados = new ArrayList<>();
        String nombreColeccion = claseObjeto.getSimpleName().toLowerCase();
        MongoCollection<Document> coleccion = baseDeDatos.getCollection(nombreColeccion);

        try (MongoCursor<Document> cursor = coleccion.find().iterator()) {
            while (cursor.hasNext()) {
                Document documento = cursor.next();
                T instancia = construirInstancia(claseObjeto, documento);
                resultados.add(instancia);
            }
        } catch (Exception e) {
            System.err.println("Error al recuperar objetos de la colección: " + e.getMessage());
        }
        return resultados;
    }

    public <T> T seleccionarPorId(Class<T> claseObjeto, Object id) {
        String nombreColeccion = claseObjeto.getSimpleName().toLowerCase();
        MongoCollection<Document> coleccion = baseDeDatos.getCollection(nombreColeccion);
        Document consulta = new Document("_id", id);

        try {
            Document documento = coleccion.find(consulta).first();
            if (documento != null) {
                return construirInstancia(claseObjeto, documento);
            }
        } catch (Exception e) {
            System.err.println("Error al recuperar el objeto de la colección: " + e.getMessage());
        }
        return null;
    }

    public void eliminar(Class<?> claseObjeto, Object id) {
        try {
            String nombreColeccion = claseObjeto.getSimpleName().toLowerCase();
            MongoCollection<Document> coleccion = baseDeDatos.getCollection(nombreColeccion);
            Document consulta = new Document("_id", id);

            coleccion.deleteOne(consulta);
            System.out.println("Documento eliminado de la colección " + nombreColeccion + " correctamente.");
        } catch (Exception e) {
            System.err.println("Error al eliminar el objeto de MongoDB: " + e.getMessage());
        }
    }

    private <T> T construirInstancia(Class<T> claseObjeto, Document documento)
            throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Constructor<T> constructor = claseObjeto.getDeclaredConstructor();
        constructor.setAccessible(true);
        T instancia = constructor.newInstance();
        Field[] campos = claseObjeto.getDeclaredFields();

        for (Field campo : campos) {
            campo.setAccessible(true);
            String nombreCampo = campo.getName();
            Object valorCampo = documento.get(nombreCampo);

            if (valorCampo == null && nombreCampo.equals(claseObjeto.getSimpleName().toLowerCase() + "_id")) {
                valorCampo = documento.get("_id");
            }

            campo.set(instancia, valorCampo);
        }

        return instancia;
    }
    public void modificar(Object objeto) {
        try {
            Class<?> clase = objeto.getClass();
            String nombreColeccion = clase.getSimpleName().toLowerCase();
            MongoCollection<Document> coleccion = baseDeDatos.getCollection(nombreColeccion);

            Document doc = convertirAJson(objeto, clase);
            String idFieldName = clase.getSimpleName().toLowerCase() + "_id";
            Field idField = clase.getDeclaredField(idFieldName);
            idField.setAccessible(true);
            Object id = idField.get(objeto);

            if (id == null) {
                throw new IllegalArgumentException("El campo ID no puede ser nulo");
            }

            coleccion.replaceOne(Filters.eq("_id", id), doc);
            System.out.println("Documento actualizado en la colección " + nombreColeccion + " correctamente.");
        } catch (Exception e) {
            System.err.println("Error al actualizar el objeto en MongoDB: " + e.getMessage());
        }
    }
}
