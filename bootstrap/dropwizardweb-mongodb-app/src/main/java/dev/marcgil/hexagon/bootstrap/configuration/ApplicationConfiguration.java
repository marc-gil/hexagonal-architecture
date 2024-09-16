package dev.marcgil.hexagon.bootstrap.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.core.Configuration;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ApplicationConfiguration extends Configuration {

  @NotEmpty
  @JsonProperty("mongodb.db")
  private String db;

  @NotEmpty
  @JsonProperty("mongodb.uri")
  private String uri;

}
