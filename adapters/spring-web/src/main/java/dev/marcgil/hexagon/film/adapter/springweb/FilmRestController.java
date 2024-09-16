package dev.marcgil.hexagon.film.adapter.springweb;

import dev.marcgil.hexagon.film.adapter.springweb.api.FilmApi;
import dev.marcgil.hexagon.film.adapter.springweb.api.model.FilmDto;
import dev.marcgil.hexagon.film.application.port.api.AddFilmToDirectorUseCase;
import dev.marcgil.hexagon.film.application.port.api.AddFilmToDirectorUseCase.AddFilmToDirectorCommand;
import dev.marcgil.hexagon.film.application.port.api.GetFilmsUseCase;
import dev.marcgil.hexagon.film.application.port.api.GetFilmsUseCase.GetFilmsQuery;
import dev.marcgil.hexagon.film.domain.Director;
import dev.marcgil.hexagon.film.domain.Film;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FilmRestController implements FilmApi {

  private final AddFilmToDirectorUseCase addFilmToDirectorUseCase;
  private final GetFilmsUseCase getFilmsUseCase;
  private final ApiMapper apiMapper;

  @Override
  public ResponseEntity<FilmDto> createFilm(FilmDto filmDto) {
    AddFilmToDirectorCommand command = apiMapper.toAddFilmToDirectorCommand(filmDto);

    Director director = addFilmToDirectorUseCase.addFilmToDirector(command);

    return ResponseEntity.ok(apiMapper.toFilmDto(director.getDirectedFilms().getLast()));
  }

  @Override
  public ResponseEntity<List<FilmDto>> getFilms(String directorId, String genre,
      Integer year) {
    List<Film> directorsFilms = getFilmsUseCase.getFilms(apiMapper.toGetFilmsQuery(directorId, genre, year));

    return ResponseEntity.ok(apiMapper.toFilmDtoList(directorsFilms));
  }

}
