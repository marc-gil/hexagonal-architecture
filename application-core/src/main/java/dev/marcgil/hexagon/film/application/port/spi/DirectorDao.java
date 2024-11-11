package dev.marcgil.hexagon.film.application.port.spi;

import dev.marcgil.hexagon.film.domain.Director;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface DirectorDao {

  Optional<Director> findById(String directorId);

  Director save(Director director);

  List<Director> findByName(String name);

}
