package main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.SessionFactory;

import model.Vuelo;

public class Principal {

	public static void main(String[] args) {
		
		SessionFactory session = HibernateUtil.getSessionFactory(); 
		EntityManager entityManager = session.createEntityManager();
		
		entityManager.getTransaction().begin();
		
		Vuelo newVuelo = new Vuelo();
		newVuelo.setAeropuertoOrigen("Barcelona");
		newVuelo.setAeropuertoDestino("Concepcion");
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
		
        entityManager.persist(newVuelo);	

        entityManager.getTransaction().commit();
        
        String sql = "SELECT v from Vuelo v where v.aeropuertodestino = 'Concepcion'";
        Query query = entityManager.createQuery(sql);
        Vuelo vuelo = (Vuelo) query.getSingleResult();
         
        System.out.println(vuelo.getAeropuertoOrigen());
        System.out.println(vuelo.getAeropuertoDestino());
        System.out.println(vuelo.getTipoVuelo());
        System.out.println(vuelo.getFechaVuelo());
        
        
        
        entityManager.close();
        session.close();
	}

}
