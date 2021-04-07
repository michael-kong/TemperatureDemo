package com.example.demo;

import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestApiController
public class CountryTemperatureController {

  private final CountryTemperatureService countryTemperatureService;

  public CountryTemperatureController(final CountryTemperatureService countryTemperatureService) {
    this.countryTemperatureService = countryTemperatureService;
  }

  @GetMapping("country-temperature/{province}/{city}/{county}")
  public Optional<Integer> getTemperature(
      @PathVariable final String province,
      @PathVariable final String city,
      @PathVariable final String county) {
    int result = countryTemperatureService.getTemperature(province, city, county);
    return Optional.ofNullable(result);
  }
}
