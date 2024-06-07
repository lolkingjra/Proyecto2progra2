package com.example.demo;

import java.sql.SQLException;
import java.sql.Connection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mongodb.client.MongoDatabase;

import javafx.scene.layout.VBox;

@SpringBootApplication
public class main {

	/**
	 * @param args
	 * @throws SQLException
	 */
	public static void main(String[] args) throws SQLException {

		SpringApplication.run(main.class, args);

		// Connection conexionOracle = conexionSQL.conectar();
		// mapeo mapeadorOracle = new mapeo(conexionOracle);

		// conexionSQL conn = new conexionSQL();
		// boolean siEstaConectado = conn.estadoConexion();
		// System.out.println("Esta bien conectado: " + siEstaConectado);

		// objeto de almacen
		// Almacen facturAlmacen1 = new Almacen("Matias", "arroz", 2100, 1);
		// pruebasMongo
		Banco cliente1 = new Banco("Jose", 9999999, "1234", 2);
		MongoDatabase conexionDeMongo = ConexionMongo.conectarMongoDB();
		MapeoMongo mapearMongo = new MapeoMongo(conexionDeMongo);
		//mapearMongo.insertar(cliente1);
		 mapearMongo.eliminar(Banco.class, 2);

		// mapeadorOracle.insertarTabla(facturAlmacen1);
		// facturAlmacen1.setProducto("Frijoles");
		// facturAlmacen1.setPrecio(1500);
		// mapeadorOracle.eliminarEntidad(Almacen.class,
		// facturAlmacen1.getAlmacen_id());
		// mapeadorOracle.modificarEntidad(facturAlmacen1);
		System.out.println("jose");
	}

}
