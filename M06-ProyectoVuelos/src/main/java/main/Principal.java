package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;

import javax.persistence.ParameterMode;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.query.Query;

import com.mysql.cj.jdbc.CallableStatement;

import model.Personal;
import model.Vuelo;

public class Principal {

	static String usuarif = "root";
	static String contrf = "";
	static String urlf = "jdbc:mysql://localhost:3306/vuelos?useSSL=false&amp;serverTimezone=UTC";

	public static void main(String[] args) {
		Scanner lector = new Scanner(System.in);

		boolean salir = false;
		while (salir  == false) {
			System.out.println("Menu");
			System.out.println("======================");
			System.out.println("1.-Insert vuelo y mostrar");
			System.out.println("2.-Consulta del personal, los pilotos");
			System.out.println("3.-Store Procedure Vuelos");
			System.out.println("4.-Fin");
			System.out.println("=====================");

			int pos = lector.nextInt();
			switch (pos) {
			case 1:
				insertarVuelo();
				break;

			case 2:
				consultaPersonal();
				break;

			case 3:
				storeProcedureVuelos();
				break;

			case 4:
				System.out.println("Fin");
				salir=true;
				break;

			default:
				System.out.println("Escribe bien el numero");
				break;
			}
		}


	}


	public static void insertarVuelo() {
		Session session = sessionFactoryUtil.getSessionFactory().openSession(); 
		Transaction tx = session.beginTransaction();

		Vuelo newVuelo = new Vuelo();
		newVuelo.setIdentificador("ESP-BARC");
		newVuelo.setAeropuertoOrigen("BARC");
		newVuelo.setAeropuertoDestino("CONC");
		newVuelo.setTipoVuelo("Largo recorrido");
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
			String dateInString = "31-8-2020";
			Date date = sdf.parse(dateInString);
			newVuelo.setFechaVuelo(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		session.save(newVuelo);
		tx.commit();

		System.out.println("Vuelo insertado");
		System.out.println(newVuelo.getIdentificador());
		System.out.println(newVuelo.getAeropuertoOrigen());
		System.out.println(newVuelo.getAeropuertoDestino());
		System.out.println(newVuelo.getTipoVuelo());
		System.out.println(newVuelo.getFechaVuelo());


		session.close();
	}

	public static void consultaPersonal() {
		Session session = sessionFactoryUtil.getSessionFactory().openSession(); 

		List<Personal> personal = session.createQuery("from Personal where categoria='piloto'").list();

		int cont = 0;
		for (Personal p : personal) {
			cont++;
			System.out.print("Pasajero " + cont +"= ");
			System.out.print("Codigo: " + p.getCodigo() + ", ");
			System.out.print("Nombre: " + p.getNombre() + ", ");
			System.out.println("Categoria: " + p.getCategoria());
		}
		System.out.println();
	}

	public static void storeProcedureVuelos() {
		Session session = sessionFactoryUtil.getSessionFactory().openSession(); 
		ProcedureCall query = session.createStoredProcedureCall("ConsultaVuelosFecha");

		query.registerStoredProcedureParameter(1, String.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(3, String.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(4, String.class, ParameterMode.OUT);
		query.registerStoredProcedureParameter(5, Date.class, ParameterMode.OUT);		
		
		ArrayList<Vuelo> vuelos = new ArrayList<Vuelo>();
		for (int i = 1; i < 6; i++) {
			vuelos.add((Vuelo) query.getParameter(i));
			query.execute();
		}
		
		List<Vuelo> result = vuelos;
		
		for (Vuelo v : result) {
			System.out.println(v.getIdentificador());
			System.out.println(v.getTipoVuelo());
			System.out.println(v.getAeropuertoOrigen());
			System.out.println(v.getAeropuertoDestino());
			System.out.println(v.getFechaVuelo());
		}
		
		session.close();
	}
}
