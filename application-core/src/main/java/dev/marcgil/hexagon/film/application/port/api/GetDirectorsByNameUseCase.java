package dev.marcgil.hexagon.film.application.port.api;

import dev.marcgil.hexagon.film.domain.Director;
import java.util.List;

public interface GetDirectorsByNameUseCase {

  List<Director> getDirectorsByName(String name);

}
