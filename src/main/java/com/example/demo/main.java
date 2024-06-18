package com.example.demo;

import java.sql.SQLException;
import java.util.Scanner;
import java.sql.Connection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mongodb.client.MongoDatabase;

@SpringBootApplication
public class main {

	/**
	 * @param args
	 * @throws SQLException
	 */
	public static void main(String[] args) throws SQLException {

		SpringApplication.run(main.class, args);
		// Menu
		Scanner scanner = new Scanner(System.in);
		// objeto de Almacen
		Almacen facturAlmacen1 = new Almacen("Matias", "pepino", 2100, 1);
		Almacen facturAlmacen2 = new Almacen("Uziel", "huevos", 1200, 2);
		Almacen facturAlmacen3 = new Almacen("Jose", "frijoles", 1900, 3);
		// objeto de Banco
		Banco cliente1 = new Banco("Matias", 0000, "1234", 1);
		Banco cliente2 = new Banco("Uziel", 999125, "4321", 2);
		Banco cliente3 = new Banco("Jose", 0000, "3241", 3);
		boolean whileSalir = true;
		while (whileSalir) {
			String[] opcion = { "Conexion y Mapeo con SQL", "Conexion y Mapeo con MongoDB",
					"Conexion y Mapeo con MySQL", "Salir" };
			System.out.println("Se muestran las siguientes opciones");
			for (int i = 0; i < opcion.length; i++) {
				System.out.println((i + 1) + (". ") + opcion[i]);
			}
			System.out.println("Igrese la opcion que desea utilizar ");
			int elegido = scanner.nextInt();

			switch (elegido) {
				case 1:
					System.out.println(("Usted eligio:") + opcion[0]);
					Connection conexionOracle = conexionSQL.conectar();
					mapeoSQL mapeadorOracle = new mapeoSQL(conexionOracle);
					conexionSQL conn = new conexionSQL();
					boolean siEstaConectado = conn.estadoConexion();
					System.out.println("Esta bien conectado: " + siEstaConectado);
					//mapeadorOracle.insertarTabla(facturAlmacen1); //DESCRIBE almacen; SELECT * FROM almacen;
					//mapeadorOracle.eliminarEntidad(Almacen.class, 1);//SELECT * FROM almacen;
					mapeadorOracle.modificarEntidad(facturAlmacen1);//SELECT * FROM almacen;
					//mapeadorOracle.recuperarDeTabla(Almacen.class);
					//revisado
					break;
				case 2:
					System.out.println(("Usted eligio:") + opcion[1]);
					MongoDatabase conexionDeMongo = ConexionMongo.conectarMongoDB();
					MapeoMongo mapearMongo = new MapeoMongo(conexionDeMongo);
					// pruebasMongo
					//mapearMongo.insertar(cliente1);
					// mapearMongo.eliminar(Banco.class, 2);
					 //mapearMongo.seleccionarPorId(Banco.class, 1);
					 mapearMongo.modificar(cliente1);
					mapearMongo.seleccionarPorId(null, mapearMongo);
					//mapearMongo.seleccionarTodo(null);
					//revisado falta los select
					break;

				case 3:
					System.out.println(("Usted eligio:") + opcion[2]);
					Connection conetarMySQL = ConexionMySQL.conectarMySQL();
					MapeoMySQL mapearMySQL = new MapeoMySQL(conetarMySQL);
					//mapearMySQL.insertar(cliente3);
					//mapearMySQL.eliminar(Banco.class, 1);
					//mapearMySQL.modificar(cliente3);
					//mapearMySQL.seleccionarPorId(null, mapearMySQL);
					//mapearMySQL.seleccionarTodo(null);
					break;

				case 4:
					System.out.println(("Usted eligio:") + opcion[3]);
					whileSalir = false;
					break;
				default:
					System.out.println("Opción inválida.");

					break;
			}

		}
		scanner.close();

		

	}

}
