package dev.marcgil.hexagon.film.adapter.springweb;

import dev.marcgil.hexagon.film.adapter.springweb.api.DirectorApi;
import dev.marcgil.hexagon.film.adapter.springweb.api.model.CreateDirectorRequestDto;
import dev.marcgil.hexagon.film.adapter.springweb.api.model.DirectorDto;
import dev.marcgil.hexagon.film.adapter.springweb.api.model.FilmDto;
import dev.marcgil.hexagon.film.application.port.api.CreateDirectorUseCase;
import dev.marcgil.hexagon.film.application.port.api.GetDirectorsByNameUseCase;
import dev.marcgil.hexagon.film.application.port.api.GetFilmsByDirectorUseCase;
import dev.marcgil.hexagon.film.domain.Director;
import dev.marcgil.hexagon.film.domain.Film;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DirectorRestController implements DirectorApi {

  private final GetFilmsByDirectorUseCase getFilmsByDirectorUseCase;
  private final GetDirectorsByNameUseCase getDirectorsByNameUseCase;
  private final CreateDirectorUseCase createDirectorUseCase;
  private final ApiMapper apiMapper;

  @Override
  public ResponseEntity<DirectorDto> createDirector(
      CreateDirectorRequestDto createDirectorRequestDto) {
    Director director = createDirectorUseCase.createDirector(
        apiMapper.toCreateDirectorCommand(createDirectorRequestDto));
    return ResponseEntity.ok(apiMapper.toDirectorDto(director));
  }

  @Override
  public ResponseEntity<List<DirectorDto>> getAllDirectorsByName(String name) {
    List<DirectorDto> directors = getDirectorsByNameUseCase.getDirectorsByName(name).stream()
        .map(apiMapper::toDirectorDto)
        .toList();
    return ResponseEntity.ok(directors);
  }

  @Override
  public ResponseEntity<List<FilmDto>> getDirectorsFilms(String directorId) {
    List<Film> directorsFilms = getFilmsByDirectorUseCase.getFilmsByDirector(directorId);

    return ResponseEntity.ok(apiMapper.toFilmDtoList(directorsFilms));
  }

}
