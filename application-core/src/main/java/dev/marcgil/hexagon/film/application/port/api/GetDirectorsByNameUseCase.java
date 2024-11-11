package dev.marcgil.hexagon.film.application.port.api;

import dev.marcgil.hexagon.film.domain.Director;
import java.util.List;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface GetDirectorsByNameUseCase {

  List<Director> getDirectorsByName(String name);

}
