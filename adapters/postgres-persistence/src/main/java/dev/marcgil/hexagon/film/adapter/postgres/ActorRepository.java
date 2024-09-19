package dev.marcgil.hexagon.film.adapter.postgres;

import dev.marcgil.hexagon.film.adapter.postgres.model.ActorDbo;
import java.util.Optional;

public interface ActorRepository {

  Optional<ActorDbo> findByName(String name);

}
