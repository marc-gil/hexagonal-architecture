package dev.marcgil.hexagon.film.application.port.spi;

import dev.marcgil.hexagon.film.domain.Person;
import java.util.Optional;

public interface ActorDao {

  Optional<Person> findActorByName(String actorName);

}
