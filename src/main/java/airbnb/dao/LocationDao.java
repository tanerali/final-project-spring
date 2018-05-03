package airbnb.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import airbnb.manager.DBManager;

public enum LocationDao {
	INSTANCE;
	private Connection connection;
	private Map<String, TreeSet<String>> locations;

	private LocationDao() {
		connection = DBManager.INSTANCE.getConnection();
		try {
			locations = loadLocationsFromDB();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Map<String, TreeSet<String>> getLocations() {
		return Collections.unmodifiableMap(locations);
	}

	private Map<String, TreeSet<String>> loadLocationsFromDB() throws SQLException {
		String sql = "SELECT co.country_name, ci.city_name " + "FROM CITIES ci " + "JOIN COUNTRIES co "
				+ "ON ci.country_code = co.code;";

		TreeMap<String, TreeSet<String>> locationsMap = new TreeMap<>();

		try (Statement statement = connection.createStatement()) {
			ResultSet resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {
				String country = resultSet.getString("country_name");
				String city = resultSet.getString("city_name");

				if (!locationsMap.containsKey(country)) {
					locationsMap.put(country, new TreeSet<>());
				}
				locationsMap.get(country).add(city);
			}

			return locationsMap;
		}
	}
}
