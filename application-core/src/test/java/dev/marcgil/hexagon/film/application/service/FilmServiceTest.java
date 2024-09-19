package dev.marcgil.hexagon.film.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import dev.marcgil.hexagon.film.application.port.api.GetFilmsUseCase.GetFilmsQuery;
import dev.marcgil.hexagon.film.application.port.spi.DirectorDao;
import dev.marcgil.hexagon.film.application.port.spi.FilmDao;
import dev.marcgil.hexagon.film.domain.Director;
import dev.marcgil.hexagon.film.domain.Film;
import dev.marcgil.hexagon.film.domain.Film.Genre;
import dev.marcgil.hexagon.film.domain.Person;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
class FilmServiceTest {

  private final DirectorDao directorDao = mock(DirectorDao.class);
  private final FilmDao filmDao = mock(FilmDao.class);
  private final FilmService filmService = new FilmService(directorDao, filmDao);

  @Test
  void getFilmsByDirector_nonExistingDirectorId_exceptionIsThrown() {
    when(directorDao.findById("nonExistingId")).thenReturn(Optional.empty());

    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> filmService.getFilmsByDirector("nonExistingId"))
        .withMessageContaining("nonExistingId");
  }

  @Test
  void getFilmsByDirector_existingDirectorId_directorFilmsAreRetrieved() {
    Director director = Director.builder()
        .id("directorId")
        .name("Martin Scorsese")
        .birthDate(LocalDate.of(1942, 11, 17))
        .build();
    director.setDirectedFilms(List.of(
        Film.builder()
            .id("filmId")
            .originalTitle("Shutter Island")
            .duration(Duration.ofMinutes(138))
            .genres(List.of(Genre.THRILLER))
            .director(director)
            .yearOfRecording(Year.of(2010))
            .cast(List.of(
                Person.builder().id("personId1").name("Leonardo DiCaprio").build(),
                Person.builder().id("personId2").name("Mark Ruffalo").build(),
                Person.builder().id("personId3").name("Ben Kingsley").build()))
            .build()));
    when(directorDao.findById("directorId")).thenReturn(Optional.of(director));

    List<Film> directorsFilms = filmService.getFilmsByDirector("directorId");

    assertThat(directorsFilms).isNotNull()
        .hasSize(1)
        .isEqualTo(List.of(Film.builder()
            .id("filmId")
            .originalTitle("Shutter Island")
            .duration(Duration.ofMinutes(138))
            .genres(List.of(Genre.THRILLER))
            .director(director)
            .yearOfRecording(Year.of(2010))
            .cast(List.of(
                Person.builder().id("personId1").name("Leonardo DiCaprio").build(),
                Person.builder().id("personId2").name("Mark Ruffalo").build(),
                Person.builder().id("personId3").name("Ben Kingsley").build()))
            .build()));
  }

  @Test
  void getFilms_nullQuery_exceptionIsThrown() {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> filmService.getFilms(null))
        .withMessageContaining("At least one criteria must be specified");
  }

  @Test
  void getFilms_notNullQuery_returnsExpectedFilms() {
    List<Film> films = List.of(Film.builder()
        .id("filmId")
        .originalTitle("Shutter Island")
        .duration(Duration.ofMinutes(138))
        .genres(List.of(Genre.THRILLER))
        .director(Director.builder()
            .id("directorId")
            .name("Martin Scorsese")
            .birthDate(LocalDate.of(1942, 11, 17))
            .build())
        .yearOfRecording(Year.of(2010))
        .cast(List.of(
            Person.builder().id("personId1").name("Leonardo DiCaprio").build(),
            Person.builder().id("personId2").name("Mark Ruffalo").build(),
            Person.builder().id("personId3").name("Ben Kingsley").build()))
        .build());
    when(filmDao.findBy("directorId", Genre.THRILLER, Year.of(2010))).thenReturn(films);

    List<Film> directorsFilms = filmService.getFilms(new GetFilmsQuery("directorId", Genre.THRILLER, Year.of(2010)));

    assertThat(directorsFilms).isNotNull()
        .hasSize(1)
        .isEqualTo(List.of(Film.builder()
            .id("filmId")
            .originalTitle("Shutter Island")
            .duration(Duration.ofMinutes(138))
            .genres(List.of(Genre.THRILLER))
            .director(Director.builder()
                .id("directorId")
                .name("Martin Scorsese")
                .birthDate(LocalDate.of(1942, 11, 17))
                .build())
            .yearOfRecording(Year.of(2010))
            .cast(List.of(
                Person.builder().id("personId1").name("Leonardo DiCaprio").build(),
                Person.builder().id("personId2").name("Mark Ruffalo").build(),
                Person.builder().id("personId3").name("Ben Kingsley").build()))
            .build()));
  }

}
