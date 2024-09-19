package dev.marcgil.hexagon.bootstrap;

import static org.assertj.core.api.Assertions.assertThat;

import dev.marcgil.hexagon.film.adapter.dropwizardweb.api.model.DirectorDto;
import dev.marcgil.hexagon.film.adapter.dropwizardweb.api.model.FilmDto;
import dev.marcgil.hexagon.film.adapter.dropwizardweb.api.model.GenreDto;
import dev.marcgil.hexagon.film.adapter.mongodb.DirectorRepository;
import dev.marcgil.hexagon.film.adapter.mongodb.model.DirectorDocument;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class DirectorRestControllerIT extends AbstractIntegrationTest {

  @Inject
  private DirectorRepository directorRepository;

  @Test
  void createDirector_clintEastwood_directorIsStored() {
    List<DirectorDocument> directors = directorRepository.findByName("Clint Eastwood");
    assertThat(directors).isEmpty();

    DirectorDto clintEastwood = new DirectorDto()
        .birthDate(LocalDate.of(1930, 5, 31))
        .name("Clint Eastwood");

    Client client = EXTENDED_APPLICATION.client();

    try (Response response = client.target(
            String.format("http://localhost:%d/directors", EXTENDED_APPLICATION.getLocalPort()))
        .request()
        .post(Entity.json(clintEastwood))) {

      assertThat(response.getStatus()).isEqualTo(200);
      DirectorDto createdDirector = response.readEntity(DirectorDto.class);

      assertThat(createdDirector).satisfies(director -> {
        assertThat(director.getBirthDate()).isEqualTo(LocalDate.of(1930, 5, 31));
        assertThat(director.getName()).isEqualTo("Clint Eastwood");
        assertThat(director.getId()).isNotBlank();
      });
      Optional<DirectorDocument> storedDirector = directorRepository.findById(
          createdDirector.getId());
      assertThat(storedDirector).isNotEmpty()
          .get()
          .extracting(DirectorDocument::getName)
          .isEqualTo("Clint Eastwood");
    }
  }

  @Test
  void getAllDirectorsByName_martinScorsese_returnsExpectedDirector() {
    Client client = EXTENDED_APPLICATION.client();

    try (Response response = client.target(
            String.format("http://localhost:%d/directors?name=%s", EXTENDED_APPLICATION.getLocalPort(),
                "Scorsese"))
        .request()
        .get()) {

      assertThat(response.getStatus()).isEqualTo(200);
      DirectorDto[] directors = response.readEntity(DirectorDto[].class);

      assertThat(directors).isNotEmpty()
          .hasSize(1)
          .contains(new DirectorDto().id("8abd01c4-2a37-417d-bde2-6591948f5786")
              .name("Martin Scorsese")
              .birthDate(LocalDate.of(1942, 11, 17)));
    }
  }

  @Test
  void getDirectorsFilms_christopherNolan_returnsOrigen() {
    Client client = EXTENDED_APPLICATION.client();

    try (Response response = client.target(
            String.format("http://localhost:%d/directors/%s/films", EXTENDED_APPLICATION.getLocalPort(),
                "3d704cf2-3490-4529-8989-b4176a49250d"))
        .request()
        .get()) {

      assertThat(response.getStatus()).isEqualTo(200);
      FilmDto[] christopherNolanFilms = response.readEntity(FilmDto[].class);

      assertThat(christopherNolanFilms).isNotEmpty()
          .hasSize(1)
          .contains(new FilmDto().directorId("3d704cf2-3490-4529-8989-b4176a49250d")
              .title("Origen")
              .year(2010)
              .duration("PT2H28M")
              .genres(List.of(GenreDto.THRILLER))
              .cast(List.of("Leonardo DiCaprio", "Joseph Gordon-Levitt", "Marion Cotillard")));
    }
  }

}
