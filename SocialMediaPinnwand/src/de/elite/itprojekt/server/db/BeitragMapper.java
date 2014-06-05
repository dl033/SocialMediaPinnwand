package de.elite.itprojekt.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.elite.itprojekt.shared.bo.*;


public class BeitragMapper {
	
	private static BeitragMapper beitragMapper = null;

	/**
	 * Protected constructor to prevent to generate a new object of BeitragMapper
	 * (hidden from all)
	 */
	protected BeitragMapper() {
	}

	/**
	 * Static method to generate exact one object of UserMapper
	 * 
	 * @return The static instance of BeitragMapper
	 */
	public static BeitragMapper beitragMapper() {
		if (beitragMapper == null) {
			beitragMapper = new BeitragMapper();
		}
		return beitragMapper;
	}
	
	 public ArrayList<Beitrag> getBeitragByPinnwand(int id) {

		Connection con = DBConnection.connection();
		ArrayList <Beitrag> beitragListe= new ArrayList<Beitrag>();

		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Beitrag WHERE Nutzer_ID="+id);
			
			
		//hier eventl. mit Pinnwand verkn�pfen?!

			while (rs.next()) {
		        Beitrag b = new Beitrag();
		        b.setID(rs.getInt("Beitrag_ID"));
		        b.setErstellZeitpunkt(rs.getTimestamp("Datum"));
		        b.setText((rs.getString("Text")));
		       
		        beitragListe.add(b);
		      }
			return beitragListe;
		}

	    catch (SQLException e) {
	    		e.printStackTrace();
	    }
	return beitragListe;
	}
	
	//Beitrag in DB einf�gen
	 
	 public void textBeitragErstellen(Beitrag textBeitrag){
		 
		Connection con = DBConnection.connection();
	
		try{
			Statement stmt = con.createStatement();

	      ResultSet rs = stmt.executeQuery("SELECT MAX(Beitrag_ID) AS maxid " 
	      + "FROM Beitrag ");
	      if (rs.next()) {
	    	  
	    	  textBeitrag.setID(rs.getInt("maxid") + 1);
	    	  
	    	  stmt = con.createStatement();

		        stmt.executeUpdate("INSERT INTO Beitrag (Beitrag_ID, Nutzer_ID, Like_ID, Text, Datum) "
		            + "VALUES (" + textBeitrag.getID() + ",'" + textBeitrag.getNutzerId() + "','"  + "12" + "','" + textBeitrag.getText() + "','" + textBeitrag.getErstellZeitpunkt() +"')");

		        System.out.println(textBeitrag.getErstellZeitpunkt());
		        
	      }
	    }

	    catch (SQLException e) {
	      e.printStackTrace();
	    }

	}
	 //Alle Beitr�ge zu nutzer anzeigen
	 public ArrayList<Beitrag> findeAlleUserBeitraege(int id) {
		 Connection con = DBConnection.connection();
			Statement stmt = null;
			ResultSet rs = null;

			ArrayList<Beitrag> result = new ArrayList<Beitrag>();

			try {
				stmt = con.createStatement();

				rs = stmt.executeQuery("SELECT * FROM Beitrag " + "WHERE Nutzer_ID =" + id + " ORDER BY Datum DESC");

				while (rs.next()) {
					Beitrag beitrag = new Beitrag();
					beitrag.setID(rs.getInt("Beitrag_ID"));
					beitrag.setText(rs.getString("Text"));
					beitrag.setErstellZeitpunkt(rs.getTimestamp("Datum"));

					result.add(beitrag);
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
			}

			return result;
		}
	 	
	 	public void textBeitragLoeschen(Beitrag beitrag) {
	 		
	 		Connection con = DBConnection.connection();
	 			Statement stmt = null;

	 			try {
	 				stmt = con.createStatement();

	 				stmt.executeUpdate("DELETE FROM Beitrag "
	 						+ "WHERE Beitrag_ID=" + beitrag.getID());

	 			} catch (SQLException e2) {
	 				e2.printStackTrace();
	 			}
	 			
	 			return;
	 		}
	 
		public Beitrag updateBeitrag(Beitrag beitrag){
			
			Connection con = DBConnection.connection();

		    try {
		      Statement stmt = con.createStatement();
		      
		      stmt.executeUpdate("UPDATE Beitrag SET Text=\"" + beitrag.getText() + "\" WHERE beitrag_ID= " + beitrag.getID());
		      
		      System.out.println("Neuer Beitrag bei" + " " + beitrag.getText() + " " + beitrag.getID());

		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }

		    return sucheBeitragID(beitrag.getID());
		}
	
		
		//Beitrag per ID aus der DB holen
		public Beitrag sucheBeitragID(int id){

			Connection con = DBConnection.connection();

			try{
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM Beitrag WHERE Beitrag_ID=" + id );

				if (rs.next()) {
			        Beitrag b = new Beitrag();
			        b.setID(rs.getInt("Beitrag_ID"));
			        b.setErstellZeitpunkt(rs.getTimestamp("Datum"));
			        b.setText(rs.getString("Text"));
	
			        return b;
				}
			}

		    catch (SQLException e) {
	    		e.printStackTrace();
	    		return null;
		    }
		return null;
		}
		
		
	
}