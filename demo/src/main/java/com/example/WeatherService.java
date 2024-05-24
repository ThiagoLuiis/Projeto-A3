package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherService {
    private static final String API_KEY = "88a695278b3c0cd359ca6cee7f153b48";
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/weather_db";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "thlas10";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o nome da cidade: ");
        String cityName = scanner.nextLine().trim();
        scanner.close();



    try {
    // Substituir espaços em branco por %20
    cityName = URLEncoder.encode(cityName, "UTF-8");

    
    String url = BASE_URL + "?q=" + cityName + "&appid=" + API_KEY;

    

    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
    connection.setRequestMethod("GET");

    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    String inputLine;
    StringBuilder content = new StringBuilder();
    while ((inputLine = in.readLine()) != null) {
        content.append(inputLine);
    }

    in.close();
    connection.disconnect();


    String[] words = cityName.toLowerCase().split(" ");
    StringBuilder formattedCityName = new StringBuilder();
    for (String word : words) {
        formattedCityName.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
    }
    formattedCityName.deleteCharAt(formattedCityName.length() - 1); // Remover espaço extra no final
    
    // Substituir espaços em branco por %20
    String encodedCityName = formattedCityName.toString().replaceAll(" ", "%20");



            // Parse the JSON response
            JSONObject json = new JSONObject(content.toString());

            JSONObject sys = json.getJSONObject("sys");
            String countryCode = sys.getString("country");

            JSONObject main = json.getJSONObject("main");
            double temp = main.getDouble("temp");
            int humidity = main.getInt("humidity");

            JSONArray weatherArray = json.getJSONArray("weather");
            String weatherDescription = weatherArray.getJSONObject(0).getString("description");

            JSONObject wind = json.getJSONObject("wind");
            double wind_speed = wind.getDouble("speed");

            // Save data to database
            GerenciarBanco dbManager = new GerenciarBanco(DB_URL, DB_USERNAME, DB_PASSWORD);
            dbManager.saveWeatherData(encodedCityName, countryCode, temp, humidity, weatherDescription, wind_speed);
            dbManager.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
