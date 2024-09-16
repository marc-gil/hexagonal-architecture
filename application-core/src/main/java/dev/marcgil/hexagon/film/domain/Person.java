package dev.marcgil.hexagon.film.domain;

import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Getter
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
public class Person {

  @NonNull
  protected String id;
  @NonNull
  protected String name;
  protected LocalDate birthDate;

}
