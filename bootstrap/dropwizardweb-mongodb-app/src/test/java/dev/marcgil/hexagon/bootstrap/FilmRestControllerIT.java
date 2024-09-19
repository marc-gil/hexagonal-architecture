package dev.marcgil.hexagon.bootstrap;

import static org.assertj.core.api.Assertions.assertThat;

import dev.marcgil.hexagon.film.adapter.dropwizardweb.api.model.FilmDto;
import dev.marcgil.hexagon.film.adapter.dropwizardweb.api.model.GenreDto;
import dev.marcgil.hexagon.film.adapter.mongodb.DirectorRepository;
import dev.marcgil.hexagon.film.adapter.mongodb.model.DirectorDocument;
import dev.marcgil.hexagon.film.adapter.mongodb.model.FilmDocument;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Test;

class FilmRestControllerIT extends AbstractIntegrationTest {

  @Inject
  private DirectorRepository directorRepository;

  @Test
  void getFilms_year2010_origenAndShutterIsland() {
    Client client = EXTENDED_APPLICATION.client();

    try (Response response = client.target(
            String.format("http://localhost:%d/films?year=%s", EXTENDED_APPLICATION.getLocalPort(),
                2010))
        .request()
        .get()) {

      assertThat(response.getStatus()).isEqualTo(200);
      FilmDto[] films = response.readEntity(FilmDto[].class);

      assertThat(films).isNotEmpty()
          .hasSize(2)
          .containsExactlyInAnyOrder(
              new FilmDto().directorId("8abd01c4-2a37-417d-bde2-6591948f5786")
                  .title("Shutter Island")
                  .year(2010)
                  .duration("PT2H18M")
                  .genres(List.of(GenreDto.THRILLER))
                  .cast(List.of("Leonardo DiCaprio", "Mark Ruffalo", "Ben Kingsley")),
              new FilmDto().directorId("3d704cf2-3490-4529-8989-b4176a49250d")
                  .title("Origen")
                  .year(2010)
                  .duration("PT2H28M")
                  .genres(List.of(GenreDto.THRILLER))
                  .cast(List.of("Leonardo DiCaprio", "Joseph Gordon-Levitt", "Marion Cotillard")));
    }
  }

  @Test
  void createFilm_validFilmDto_filmIsAddedToTheDirector() throws Exception {
    Client client = EXTENDED_APPLICATION.client();
    List<FilmDocument> initiallyStoredFilms = directorRepository.findById(
            "8abd01c4-2a37-417d-bde2-6591948f5786")
        .map(DirectorDocument::getFilms)
        .stream()
        .flatMap(Collection::stream)
        .toList();

    FilmDto theDeparted = new FilmDto().directorId("8abd01c4-2a37-417d-bde2-6591948f5786")
        .title("The Departed")
        .year(2006)
        .duration("PT2H29M")
        .genres(List.of(GenreDto.THRILLER, GenreDto.DRAMA, GenreDto.ACTION))
        .cast(List.of("Leonardo DiCaprio", "Matt Damon", "Jack Nicholson"));

    try (Response response = client.target(
            String.format("http://localhost:%d/films", EXTENDED_APPLICATION.getLocalPort()))
        .request()
        .post(Entity.json(theDeparted))) {

      assertThat(response.getStatus()).isEqualTo(200);
      FilmDto createdFilm = response.readEntity(FilmDto.class);

      assertThat(createdFilm).satisfies(film -> {
        assertThat(film.getYear()).isEqualTo(2006);
        assertThat(film.getDuration()).isEqualTo("PT2H29M");
        assertThat(film.getGenres()).containsExactlyInAnyOrder(GenreDto.THRILLER, GenreDto.DRAMA,
            GenreDto.ACTION);
        assertThat(film.getCast()).containsExactlyInAnyOrder("Leonardo DiCaprio", "Matt Damon",
            "Jack Nicholson");
        assertThat(film.getDirectorId()).isEqualTo("8abd01c4-2a37-417d-bde2-6591948f5786");
      });

      List<FilmDocument> storedFilms = directorRepository.findById(
              "8abd01c4-2a37-417d-bde2-6591948f5786")
          .map(DirectorDocument::getFilms)
          .stream()
          .flatMap(Collection::stream)
          .toList();

      assertThat(storedFilms.size()).isEqualTo(initiallyStoredFilms.size() + 1);
    }
  }

}
