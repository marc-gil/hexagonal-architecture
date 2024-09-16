package dev.marcgil.hexagon.film.adapter.dropwizard;

import dev.marcgil.hexagon.film.adapter.dropwizardweb.api.FilmApi;
import dev.marcgil.hexagon.film.adapter.dropwizardweb.api.model.FilmDto;
import dev.marcgil.hexagon.film.application.port.api.AddFilmToDirectorUseCase;
import dev.marcgil.hexagon.film.application.port.api.AddFilmToDirectorUseCase.AddFilmToDirectorCommand;
import dev.marcgil.hexagon.film.application.port.api.GetFilmsUseCase;
import dev.marcgil.hexagon.film.domain.Director;
import dev.marcgil.hexagon.film.domain.Film;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FilmResource implements FilmApi {

  private final AddFilmToDirectorUseCase addFilmToDirectorUseCase;
  private final GetFilmsUseCase getFilmsUseCase;
  private final ApiMapper apiMapper;

  @Override
  public FilmDto createFilm(FilmDto filmDto) {
    AddFilmToDirectorCommand command = apiMapper.toAddFilmToDirectorCommand(filmDto);

    Director director = addFilmToDirectorUseCase.addFilmToDirector(command);

    return apiMapper.toFilmDto(director.getDirectedFilms().getLast());
  }

  @Override
  public List<FilmDto> getFilms(String directorId, String genre,
      Integer year) {
    List<Film> directorsFilms = getFilmsUseCase.getFilms(
        apiMapper.toGetFilmsQuery(directorId, genre, year));

    return apiMapper.toFilmDtoList(directorsFilms);
  }

}
