package dev.marcgil.hexagon.film.application.port.spi;

import dev.marcgil.hexagon.film.domain.Person;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ActorDao {

  Optional<Person> findActorByName(String actorName);

}
