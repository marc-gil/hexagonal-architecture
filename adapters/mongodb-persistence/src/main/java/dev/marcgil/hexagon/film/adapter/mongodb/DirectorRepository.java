package dev.marcgil.hexagon.film.adapter.mongodb;

import dev.marcgil.hexagon.film.adapter.mongodb.model.DirectorDocument;
import dev.marcgil.hexagon.film.adapter.mongodb.model.PersonDocument;
import dev.marcgil.hexagon.film.domain.Film.Genre;
import java.time.Year;
import java.util.List;
import java.util.Optional;

public interface DirectorRepository {

  Optional<DirectorDocument> findById(String directorId);

  DirectorDocument save(DirectorDocument director);

  List<DirectorDocument> findByName(String name);

  List<DirectorDocument> findBy(String directorId, Genre genre, Year yearOfRecording);

  Optional<PersonDocument> findActorByName(String actorName);

}
