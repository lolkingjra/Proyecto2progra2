# Desarrollo de una Herramienta Java para ORM/ODM con Soporte Multiplataforma y Parámetros de Tipo de Base de Datos

## Objetivo

El proyecto tiene como objetivo principal la creación de una herramienta en Java que actúe como 
una librería multifuncional capaz de operar como Object-Relational Mapping (ORM) o Object
Document Mapping (ODM), dependiendo del tipo de base de datos proporcionado como 
parámetro de configuración. Esta herramienta proporcionará una interfaz intuitiva y flexible para 
mapear objetos de clases Java a tablas en bases de datos relacionales o a documentos en bases de 
datos NoSQL, facilitando así el desarrollo de aplicaciones Java escalables y eficientes.

## Estado del Proyecto

Estado del proyecto terminado, sin embargo, a esperas de correciones para la mejoría de eficiencia.

## Instalación
### Prerequisitos
1. Ocupas descargar un IDE para trabajar con JAVA o usar uno en linea.
2. Además, Java Development Kit (JDK): Asegúrate de tener instalado JDK 11 o superior. Puedes descargarlo desde Oracle JDK o OpenJDK.
3. Maven: Este proyecto asume el uso de Apache Maven para gestionar las dependencias. Puedes descargar Maven desde Apache Maven.
4. MongoDB: Instala MongoDB Community Server desde MongoDB Downloads.
5. MySQL: Instala MySQL Server desde MySQL Downloads.
6. Spring Boot: El proyecto está basado en Spring Boot, pero Maven se encargará de gestionar las dependencias necesarias.

### Requisitos importantes para buen funcionamiento

- Dependencia de MongoDB
- Dependencia de MySQL
- Dependencia de SQL

## Uso
**Uso de las operaciones ODM y ORM**

```java
public static void main(String[] args) throws SQLException {
        SpringApplication.run(Main.class, args);

        Banco cliente1 = new Banco("Juan", 184942, "4321", 1);

        // Conexión y mapeo a MySQL
        Connection conectarMySQL = ConexionMySQL.conectarMySQL();
        MapeoMySQL mapearMySQL = new MapeoMySQL(conectarMySQL);
        mapearMySQL.modificar(cliente1);

        // Conexión y mapeo a MongoDB
        MongoDatabase conexionDeMongo = ConexionMongo.conectarMongoDB();
        MapeoMongo mapearMongo = new MapeoMongo(conexionDeMongo);
        mapearMongo.insertar(cliente1);

    }
```
## Recuerda
Debes de tener configurada bien las conexiones, esto quiere decir en el textFile de conexiones a las bases configurar con tu propio puerto, usuario y contraseña.
Ademas tener una clase de referencia bien definidad con sus atributos para poder mapear.

#Si deseas contribuir
1. Haz un fork del repositorio.
2. Crea una rama nueva (git checkout -b feature/nueva-funcionalidad).
3. Realiza tus cambios y haz commit (git commit -m 'Agrega nueva funcionalidad').
4. Sube tus cambios a la rama (git push origin feature/nueva-funcionalidad).
5. Abre un Pull Request.
## Gracias por usar este proyecto! Esperamos que encuentres útil la configuración y los ejemplos proporcionados. Si tienes alguna pregunta o necesitas asistencia adicional, no dudes en crear un issue en el repositorio. Buena suerte con tu desarrollo.
