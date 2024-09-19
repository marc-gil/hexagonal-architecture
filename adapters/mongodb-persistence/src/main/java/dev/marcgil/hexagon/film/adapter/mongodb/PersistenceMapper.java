package dev.marcgil.hexagon.film.adapter.mongodb;

import dev.marcgil.hexagon.film.adapter.mongodb.model.DirectorDocument;
import dev.marcgil.hexagon.film.adapter.mongodb.model.FilmDocument;
import dev.marcgil.hexagon.film.adapter.mongodb.model.PersonDocument;
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
  @Mapping(target = "directedFilms", ignore = true)
  Director toDirector(DirectorDocument directorDocument);

  @Mapping(target = "yearOfRecording", source = "year")
  @Mapping(target = "originalTitle", source = "title")
  @Mapping(target = "director", expression = "java(director)")
  Film toFilm(FilmDocument filmDocument, @Context Director director);

  List<Film> toFilms(List<FilmDocument> filmDocuments, @Context Director director);

  @AfterMapping
  default void setDirectorToFilms(DirectorDocument source, @MappingTarget Director director) {
    director.setDirectedFilms(toFilms(source.getFilms(), director));
  }

  Person toPerson(PersonDocument personDocument);

  @InheritInverseConfiguration
  @Mapping(target = "films", source = "directedFilms")
  DirectorDocument toDirectorDocument(Director directorDocument);

  @InheritInverseConfiguration
  FilmDocument toFilmDocument(Film film);

  @InheritInverseConfiguration
  PersonDocument toPersonDocument(Person person);

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
