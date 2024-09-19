package dev.marcgil.hexagon.film.application.port.api;

import dev.marcgil.hexagon.film.domain.Director;
import java.util.List;
import org.jspecify.annotations.NonNull;

public interface GetDirectorsByNameUseCase {

  @NonNull
  List<Director> getDirectorsByName(@NonNull String name);

}
