package dev.marcgil.hexagon.film.adapter.postgres;

import dev.marcgil.hexagon.film.adapter.postgres.model.ActorDbo;
import dev.marcgil.hexagon.film.adapter.postgres.model.DirectorDbo;
import dev.marcgil.hexagon.film.adapter.postgres.model.FilmDbo;
import dev.marcgil.hexagon.film.domain.Director;
import dev.marcgil.hexagon.film.domain.Film;
import dev.marcgil.hexagon.film.domain.Person;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PersistenceMapper {

  @Mapping(target = "id", source = "id")
  @Mapping(target = "directedFilms", ignore = true)
  Director toDirector(DirectorDbo directorDbo);

  @AfterMapping
  default void setDirectorToFilms(DirectorDbo source, @MappingTarget Director director) {
    director.setDirectedFilms(toFilms(source.getFilms(), director));
  }

  List<Film> toFilms(List<FilmDbo> filmsDbo, @Context Director director);

  @Mapping(target = "yearOfRecording", source = "year")
  @Mapping(target = "originalTitle", source = "title")
  @Mapping(target = "director", expression = "java(director)")
  Film toFilmDbo(FilmDbo filmDbo, @Context Director director);

  Person toPerson(ActorDbo personDbo);

  @InheritInverseConfiguration
  @Mapping(target = "films", ignore = true)
  DirectorDbo toDirectorDbo(Director director);

  @AfterMapping
  default void setFilmsToDirector(Director source, @MappingTarget DirectorDbo directorDbo) {
    directorDbo.setFilms(toFilmsDbo(source.getDirectedFilms(), directorDbo));
  }

  default List<FilmDbo> toFilmsDbo(List<Film> films, @Context DirectorDbo directorDbo) {
    //In order to be able to persist same id actors on the same transaction we need them to be the same reference
    Map<String, ActorDbo> uniqueActors = new HashMap<>();
    if (films == null) {
      return null;
    }
    List<FilmDbo> list = new ArrayList<>(films.size());
    for (Film film : films) {
      list.add(toFilmDbo(film, directorDbo, uniqueActors));
    }
    return list;
  }

  @InheritInverseConfiguration
  @Mapping(target = "director", expression = "java(directorDbo)")
  FilmDbo toFilmDbo(Film film, @Context DirectorDbo directorDbo,
      @Context Map<String, ActorDbo> uniqueActors);

  default List<ActorDbo> toActorsDbo(List<Person> people,
      @Context Map<String, ActorDbo> uniqueActors) {
    if (people == null) {
      return null;
    }
    List<ActorDbo> list = new ArrayList<>(people.size());
    for (Person person : people) {
      ActorDbo actorDbo = uniqueActors.computeIfAbsent(person.getId(), id -> toActorDbo(person));
      list.add(actorDbo);
    }
    return list;
  }

  @InheritInverseConfiguration
  @Mapping(target = "films", ignore = true)
  ActorDbo toActorDbo(Person person);

  default Year toYear(Integer year) {
    if (year == null) {
      return null;
    }
    return Year.of(year);
  }

  default Integer toInteger(Year year) {
    if (year == null) {
      return null;
    }
    return year.getValue();
  }

}
