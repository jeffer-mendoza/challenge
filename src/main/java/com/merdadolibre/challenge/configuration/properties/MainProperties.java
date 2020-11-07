package com.merdadolibre.challenge.configuration.properties;

import com.merdadolibre.challenge.model.Vector;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Main Properties.
 *
 * @author Jefferson Mendoza, mendosajefferson@gmail.com
 * @since 1.0
 */
@Getter
@Setter
@Configuration
@Valid
@ConfigurationProperties(prefix = "main")
public class MainProperties {

  @NotEmpty
  private List<Vector> satellites;

  private double toleranceError = 0.1;
}
