package dev.marcgil.hexagon.film.application.service.factory;

import dev.marcgil.hexagon.film.application.port.api.AddFilmToDirectorUseCase.FilmCharacteristics;
import dev.marcgil.hexagon.film.domain.Director;
import dev.marcgil.hexagon.film.domain.Film;
import dev.marcgil.hexagon.film.domain.Film.FilmBuilder;
import dev.marcgil.hexagon.film.domain.Person;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FilmFactory {

  private final IdProvider idProvider;

  public Film create(FilmCharacteristics filmCharacteristics, Director director) {
    FilmBuilder filmBuilder = Film.builder()
        .id(idProvider.newId())
        .director(director)
        .yearOfRecording(filmCharacteristics.yearOfRecording())
        .originalTitle(filmCharacteristics.originalTitle())
        .duration(filmCharacteristics.duration())
        .genres(filmCharacteristics.genres());

    if (filmCharacteristics.cast() != null) {
      List<Person> cast = filmCharacteristics.cast().stream()
          .map(this::buildPerson)
          .filter(Objects::nonNull)
          .toList();
      filmBuilder.cast(cast);
    }
    return filmBuilder.build();
  }

  private Person buildPerson(String name) {
    if (name == null) {
      return null;
    }
    return Person.builder()
        .id(idProvider.newId())
        .name(name)
        .build();
  }

}
