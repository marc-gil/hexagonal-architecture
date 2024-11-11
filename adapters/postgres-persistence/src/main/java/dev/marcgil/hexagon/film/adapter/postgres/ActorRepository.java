package dev.marcgil.hexagon.film.adapter.postgres;

import dev.marcgil.hexagon.film.adapter.postgres.model.ActorDbo;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ActorRepository {

  Optional<ActorDbo> findByName(String name);

}
