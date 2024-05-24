package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GerenciarBanco {
    private Connection connection;

    public GerenciarBanco(String url, String username, String password) throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
    }

    

    public void saveWeatherData(String encodedCityName, String countryCode, double temperature, int humidity, String weatherDescription, double windSpeed) throws SQLException {
        temperature = temperature - 273.15;
        String sql = "INSERT INTO weather_data (city_name, country_code, temperature, humidity, weather_description, wind_speed) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, encodedCityName);
            statement.setString(2, countryCode);
            statement.setDouble(3, temperature);
            statement.setInt(4, humidity);
            statement.setString(5, weatherDescription);
            statement.setDouble(6, windSpeed);
            statement.executeUpdate();
        }
    }
    

    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
