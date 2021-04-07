package com.example.demo;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class DemoApplicationTests {
  @InjectMocks CountryTemperatureService countryTemperatureService;

  @BeforeEach
  void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void findEmployeesSettings_withException() {
    assertThatExceptionOfType(ResourceNotFoundException.class)
        .isThrownBy(() -> countryTemperatureService.getTemperature("苏州", "江苏", "吴中"));
  }

    @Test
    void findEmployeesSettings_withoutException() {
        assertThatCode(() -> countryTemperatureService.getTemperature("江苏", "苏州", "吴中"))
                .doesNotThrowAnyException();
    }
}
