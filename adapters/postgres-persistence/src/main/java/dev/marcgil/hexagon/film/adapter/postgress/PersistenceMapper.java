package dev.marcgil.hexagon.film.adapter.postgress;

import dev.marcgil.hexagon.film.adapter.postgress.model.ActorDbo;
import dev.marcgil.hexagon.film.adapter.postgress.model.DirectorDbo;
import dev.marcgil.hexagon.film.adapter.postgress.model.FilmDbo;
import dev.marcgil.hexagon.film.domain.Director;
import dev.marcgil.hexagon.film.domain.Film;
import dev.marcgil.hexagon.film.domain.Person;
import java.time.Year;
import java.util.List;
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
  @Mapping(target = "directedFilms", ignore = true)//, source = "films")
  Director toDirector(DirectorDbo directorDbo);

  @Mapping(target = "yearOfRecording", source = "year")
  @Mapping(target = "originalTitle", source = "title")
  @Mapping(target = "director", expression = "java(director)")
  Film toFilm(FilmDbo filmDbo, @Context Director director);

  List<Film> toFilms(List<FilmDbo> filmDbos, @Context Director director);

  @AfterMapping
  default void setDirectorToFilms(DirectorDbo source, @MappingTarget Director director) {
    director.setDirectedFilms(toFilms(source.getFilms(), director));
  }

  Person toPerson(ActorDbo personDbo);

  @InheritInverseConfiguration
  @Mapping(target = "films", source = "directedFilms")
  DirectorDbo toDirectorDocument(Director directorDocument);

  @InheritInverseConfiguration
  FilmDbo toFilm(Film film);

  @InheritInverseConfiguration
  @Mapping(target = "films", ignore = true)
  ActorDbo toPersonDbo(Person person);

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
