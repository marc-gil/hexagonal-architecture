package dev.marcgil.hexagon.bootstrap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.marcgil.hexagon.film.adapter.postgres.DirectorRepository;
import dev.marcgil.hexagon.film.adapter.postgres.model.DirectorDbo;
import dev.marcgil.hexagon.film.adapter.springweb.api.model.DirectorDto;
import dev.marcgil.hexagon.film.adapter.springweb.api.model.FilmDto;
import dev.marcgil.hexagon.film.adapter.springweb.api.model.GenreDto;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.ObjectMapper;

class DirectorRestControllerIT extends AbstractIntegrationTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private DirectorRepository directorRepository;

  @Test
  void createDirector_clintEastwood_directorIsStored() throws Exception {
    List<DirectorDbo> directors = directorRepository.findByName("Clint Eastwood");
    assertThat(directors).isEmpty();

    DirectorDto clintEastwood = new DirectorDto()
        .birthDate(LocalDate.of(1930, 5, 31))
        .name("Clint Eastwood");

    MvcResult mvcResult = mockMvc.perform(post("/directors")
            .content(objectMapper.writeValueAsString(clintEastwood))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpectAll(status().isOk(), content().contentType(MediaType.APPLICATION_JSON))
        .andReturn();

    DirectorDto createdDirector = objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(),
        DirectorDto.class);

    assertThat(createdDirector).satisfies(director -> {
      assertThat(director.getBirthDate()).isEqualTo(LocalDate.of(1930, 5, 31));
      assertThat(director.getName()).isEqualTo("Clint Eastwood");
      assertThat(director.getId()).isNotBlank();
    });

    Optional<DirectorDbo> storedDirector = directorRepository.findById(createdDirector.getId());

    assertThat(storedDirector).isNotEmpty()
        .get()
        .extracting(DirectorDbo::getName)
        .isEqualTo("Clint Eastwood");
  }

  @Test
  void getAllDirectorsByName_martinScorsese_returnsExpectedDirector() throws Exception {
    MvcResult mvcResult = mockMvc.perform(get("/directors").queryParam("name", "Scorsese"))
        .andExpectAll(status().isOk(), content().contentType(MediaType.APPLICATION_JSON))
        .andReturn();

    DirectorDto[] directors = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
        DirectorDto[].class);

    assertThat(directors).isNotEmpty()
        .hasSize(1)
        .contains(new DirectorDto().id("8abd01c4-2a37-417d-bde2-6591948f5786")
            .name("Martin Scorsese")
            .birthDate(LocalDate.of(1942, 11, 17)));
  }

  @Test
  void getDirectorsFilms_christopherNolan_returnsOrigen() throws Exception {
    MvcResult mvcResult = mockMvc.perform(
            get("/directors/{directorId}/films", "3d704cf2-3490-4529-8989-b4176a49250d"))
        .andExpectAll(status().isOk(), content().contentType(MediaType.APPLICATION_JSON))
        .andReturn();

    FilmDto[] christopherNolanFilms = objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(), FilmDto[].class);

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
