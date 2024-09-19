package dev.marcgil.hexagon.film.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import dev.marcgil.hexagon.film.application.port.api.AddFilmToDirectorUseCase.AddFilmToDirectorCommand;
import dev.marcgil.hexagon.film.application.port.api.AddFilmToDirectorUseCase.FilmCharacteristics;
import dev.marcgil.hexagon.film.application.port.api.CreateDirectorUseCase.CreateDirectorCommand;
import dev.marcgil.hexagon.film.application.port.spi.DirectorDao;
import dev.marcgil.hexagon.film.application.service.factory.DirectorFactory;
import dev.marcgil.hexagon.film.application.service.factory.FilmFactory;
import dev.marcgil.hexagon.film.domain.Director;
import dev.marcgil.hexagon.film.domain.Film;
import dev.marcgil.hexagon.film.domain.Film.Genre;
import dev.marcgil.hexagon.film.domain.Person;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
class DirectorServiceTest {

  private final DirectorDao directorDao = mock(DirectorDao.class);
  private final DirectorFactory directorFactory = mock(DirectorFactory.class);
  private final FilmFactory filmFactory = mock(FilmFactory.class);
  private final DirectorService directorService = new DirectorService(directorDao, filmFactory,
      directorFactory);

  @Test
  void createDirector() {
    CreateDirectorCommand command = new CreateDirectorCommand("Martin Scorsese",
        LocalDate.of(1942, 11, 17));

    Director expectedDirector = Director.builder()
        .name("Martin Scorsese")
        .id("directorId")
        .birthDate(LocalDate.of(1942, 11, 17))
        .directedFilms(List.of())
        .build();

    when(directorFactory.create(command)).thenReturn(expectedDirector);
    when(directorDao.save(eq(expectedDirector))).thenAnswer(i -> i.getArguments()[0]);

    Director createdDirector = directorService.createDirector(command);

    assertThat(expectedDirector).isEqualTo(createdDirector);
  }

  @Test
  void getDirectorsByName() {
    Director director = Director.builder()
        .name("Martin Scorsese")
        .id("directorId")
        .birthDate(LocalDate.of(1942, 11, 17))
        .directedFilms(List.of())
        .build();

    when(directorDao.findByName("Martin")).thenReturn(List.of(director));

    List<Director> directors = directorService.getDirectorsByName("Martin");

    assertThat(directors).hasSize(1)
        .isEqualTo(List.of(Director.builder()
            .name("Martin Scorsese")
            .id("directorId")
            .birthDate(LocalDate.of(1942, 11, 17))
            .directedFilms(List.of())
            .build()));
  }

  @Test
  void addFilmToDirector_nonExistingDirectorId_exceptionIsThrown() {
    when(directorDao.findById("nonExistingId")).thenReturn(Optional.empty());

    FilmCharacteristics filmCharacteristics = new FilmCharacteristics(Year.of(2010), "Origen",
        Duration.of(148, ChronoUnit.MINUTES),
        List.of(Genre.THRILLER), List.of("Leonardo Di Caprio"));

    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> directorService.addFilmToDirector(
            new AddFilmToDirectorCommand("nonExistingId", filmCharacteristics)))
        .withMessageContaining("nonExistingId");
  }

  @Test
  void addFilmToDirector_existingDirectorId_filmIsAddedAndDirectorIsSaved() {
    Director martinScorsese = Director.builder().name("Martin Scorsese")
        .id("299a0135-3230-431c-bfd2-d8a500c54c6f")
        .birthDate(LocalDate.of(1942, 11, 17))
        .directedFilms(List.of())
        .build();
    when(directorDao.findById("299a0135-3230-431c-bfd2-d8a500c54c6f")).thenReturn(
        Optional.of(martinScorsese));

    FilmCharacteristics filmCharacteristics = new FilmCharacteristics(Year.of(2010), "Origen",
        Duration.of(148, ChronoUnit.MINUTES), List.of(Genre.THRILLER),
        List.of("Leonardo Di Caprio"));
    Film origenFilm = Film.builder()
        .id("filmId")
        .director(martinScorsese)
        .originalTitle("Origen")
        .genres(List.of(Genre.THRILLER))
        .yearOfRecording(Year.of(2010))
        .duration(Duration.of(148, ChronoUnit.MINUTES))
        .cast(List.of(Person.builder().id("personId").name("Leonardo Di Caprio").build()))
        .build();
    when(filmFactory.create(filmCharacteristics, martinScorsese)).thenReturn(origenFilm);

    when(directorDao.save(eq(martinScorsese))).thenAnswer(i -> i.getArguments()[0]);

    Director director = directorService.addFilmToDirector(
        new AddFilmToDirectorCommand("299a0135-3230-431c-bfd2-d8a500c54c6f", filmCharacteristics));

    assertThat(director.getDirectedFilms()).isNotEmpty()
        .isEqualTo(List.of(origenFilm));
  }

}
