package dev.marcgil.hexagon.film.adapter.dropwizard;

import dev.marcgil.hexagon.film.adapter.dropwizardweb.api.DirectorApi;
import dev.marcgil.hexagon.film.adapter.dropwizardweb.api.model.CreateDirectorRequestDto;
import dev.marcgil.hexagon.film.adapter.dropwizardweb.api.model.DirectorDto;
import dev.marcgil.hexagon.film.adapter.dropwizardweb.api.model.FilmDto;
import dev.marcgil.hexagon.film.application.port.api.CreateDirectorUseCase;
import dev.marcgil.hexagon.film.application.port.api.GetDirectorsByNameUseCase;
import dev.marcgil.hexagon.film.application.port.api.GetFilmsByDirectorUseCase;
import dev.marcgil.hexagon.film.domain.Director;
import dev.marcgil.hexagon.film.domain.Film;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DirectorResource implements DirectorApi {

  private final GetFilmsByDirectorUseCase getFilmsByDirectorUseCase;
  private final GetDirectorsByNameUseCase getDirectorsByNameUseCase;
  private final CreateDirectorUseCase createDirectorUseCase;
  private final ApiMapper apiMapper;

  @Override
  public DirectorDto createDirector(CreateDirectorRequestDto createDirectorRequestDto) {
    Director director = createDirectorUseCase.createDirector(
        apiMapper.toCreateDirectorCommand(createDirectorRequestDto));
    return apiMapper.toDirectorDto(director);
  }

  @Override
  public List<DirectorDto> getAllDirectorsByName(String name) {
    return getDirectorsByNameUseCase.getDirectorsByName(name).stream()
        .map(apiMapper::toDirectorDto)
        .toList();
  }

  @Override
  public List<FilmDto> getDirectorsFilms(String directorId) {
    List<Film> directorsFilms = getFilmsByDirectorUseCase.getFilmsByDirector(directorId);

    return apiMapper.toFilmDtoList(directorsFilms);
  }

}
