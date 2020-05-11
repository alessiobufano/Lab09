package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;

public class BordersDAO {

	public void loadAllCountries(Map<Integer, Country> cIdMap) {

		String sql = "SELECT ccode, StateAbb, StateNme FROM country ORDER BY ccode";
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				if(!cIdMap.containsKey(rs.getInt("ccode")))
				{
					Country c = new Country (rs.getInt("ccode"), rs.getString("StateAbb"), rs.getString("StateNme"));
					cIdMap.put(c.getcCode(), c);
				}
			}
			
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Border> getCountryPairs(int year, Map<Integer, Country> cIdMap) {

		String sql = "SELECT state1no, state2no, year FROM contiguity " +
	                 "WHERE conttype=1 AND state1no<state2no AND year<=?";
		
		List<Border> result = new LinkedList<>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, year);
			
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				
				Country state1 = cIdMap.get(rs.getInt("state1no"));
				Country state2 = cIdMap.get(rs.getInt("state2no"));
				
				if(state1!=null && state2!=null)
				{
					Border b = new Border (state1, state2, rs.getInt("year"));
					result.add(b);
				}
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public List<Country> loadAllVertexes(Map<Integer, Country> cIdMap, int year) {

		String sql = "SELECT DISTINCT c.CCode FROM country AS c, contiguity AS cont " + 
				"WHERE (c.CCode=cont.state1no OR c.CCode=cont.state2no) AND conttype=1 AND state1no<state2no AND YEAR<=? " + 
				"ORDER BY c.CCode";
		
		List<Country> result = new LinkedList<>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, year);
			
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				
				Country c = cIdMap.get(rs.getInt("ccode"));
				if(c!=null)
					result.add(c);
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

}
