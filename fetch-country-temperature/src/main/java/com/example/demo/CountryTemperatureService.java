package com.example.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class CountryTemperatureService {

  private static final String PROVINCE_URL = "http://www.weather.com.cn/data/city3jdata/china.html";

  private static final String CITY_URL =
      "http://www.weather.com.cn/data/city3jdata/provshi/%s.html";

  private static final String COUNTY_URL =
      "http://www.weather.com.cn/data/city3jdata/station/%s.html";

  private static final String WEATHER_URL = "http://www.weather.com.cn/data/sk/%s.html";

  private static final String INCORRECT_PLACE_INFO = "Please input correct place name";

  private String readAll(final Reader rd) throws IOException {
    final StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
  }

  private String getJsonText(final String url) throws IOException {
    try (final InputStream is = new URL(url).openStream()) {
      final BufferedReader rd =
          new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
      return readAll(rd);
    }
  }

  private String retryConnect(final String url) {
    try {
      final InputStream is = new URL(url).openStream();
      final BufferedReader rd =
          new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
      is.close();
      return readAll(rd);
    } catch (final IOException e) {
      throw new ConnectException("Couldn't connect to weather API.");
    }
  }

  private JSONObject readJsonFromUrl(final String url) throws JSONException {
    String jsonText;
    try {
      jsonText = getJsonText(url);
    } catch (final IOException e) {
      jsonText = retryConnect(url);
    }
    return new JSONObject(jsonText);
  }

  private String getTempCodeByUrlAndName(final String url, final String name) {
    try {
      final JSONObject provinceJson = readJsonFromUrl(url);
      final Iterator<String> it = provinceJson.keys();
      while (it.hasNext()) {
        final String key = it.next();
        final String value = provinceJson.getString(key);
        if (value.equals(name)) {
          return key;
        }
      }
    } catch (final JSONException e) {
      throw new ResourceNotFoundException(INCORRECT_PLACE_INFO, name);
    }
    throw new ResourceNotFoundException(INCORRECT_PLACE_INFO, name);
  }

  public int getTemperature(final String province, final String city, final String county) {
    final StringBuilder urlCode = new StringBuilder();
    final String provinceCode = getTempCodeByUrlAndName(PROVINCE_URL, province);
    urlCode.append(provinceCode);

    final String cityUrl = String.format(CITY_URL, urlCode);
    final String cityCode = getTempCodeByUrlAndName(cityUrl, city);
    urlCode.append(cityCode);

    final String countyUrl = String.format(COUNTY_URL, urlCode);
    final String countyCode = getTempCodeByUrlAndName(countyUrl, county);
    urlCode.append(countyCode);

    final String weatherUrl = String.format(WEATHER_URL, urlCode);
    try {
      final JSONObject weatherJson = readJsonFromUrl(weatherUrl);
      final String weatherInfo = (String) ((JSONObject) weatherJson.get("weatherinfo")).get("temp");
      return (int) Math.floor(Float.parseFloat(weatherInfo));
    } catch (final JSONException e) {
      throw new ResourceNotFoundException(INCORRECT_PLACE_INFO, county);
    }
  }
}
