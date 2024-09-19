package dev.marcgil.hexagon.bootstrap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.marcgil.hexagon.film.adapter.mongodb.DirectorRepository;
import dev.marcgil.hexagon.film.adapter.mongodb.model.DirectorDocument;
import dev.marcgil.hexagon.film.adapter.mongodb.model.FilmDocument;
import dev.marcgil.hexagon.film.adapter.springweb.api.model.FilmDto;
import dev.marcgil.hexagon.film.adapter.springweb.api.model.GenreDto;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

class FilmRestControllerIT extends AbstractIntegrationTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private DirectorRepository directorRepository;

  @Test
  void getFilms_year2010_origenAndShutterIsland() throws Exception {
    MvcResult mvcResult = mockMvc.perform(get("/films").queryParam("year", "2010"))
        .andExpectAll(status().isOk(), content().contentType(MediaType.APPLICATION_JSON))
        .andReturn();

    FilmDto[] films = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
        FilmDto[].class);

    assertThat(films).isNotEmpty()
        .hasSize(2)
        .containsExactlyInAnyOrder(new FilmDto().directorId("8abd01c4-2a37-417d-bde2-6591948f5786")
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

  @Test
  void createFilm_validFilmDto_filmIsAddedToTheDirector() throws Exception {
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

    MvcResult mvcResult = mockMvc.perform(post("/films")
            .content(objectMapper.writeValueAsString(theDeparted))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpectAll(status().isOk(), content().contentType(MediaType.APPLICATION_JSON))
        .andReturn();

    FilmDto createdFilm = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
        FilmDto.class);

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
