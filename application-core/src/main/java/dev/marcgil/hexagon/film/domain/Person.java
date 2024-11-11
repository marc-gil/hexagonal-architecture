package dev.marcgil.hexagon.film.domain;

import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@Getter
@NullMarked
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
public class Person {

  @NonNull
  protected String id;
  @NonNull
  protected String name;
  @Nullable
  protected LocalDate birthDate;

}
