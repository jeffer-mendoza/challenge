package com.merdadolibre.challenge.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Jefferson Mendoza, jefferson.mendoza@fonyou.com
 */
@Configuration
public class AppConfig {

  /**
   * Load Business External Configuration.
   */
  @Configuration
  @PropertySource(value = "file:${business.properties.path}", encoding = "UTF-8")
  static class BasicBusinessProperties
  {}
}
