package dev.marcgil.hexagon.film.adapter.dropwizard;

import dev.marcgil.hexagon.film.adapter.dropwizardweb.api.model.CreateDirectorRequestDto;
import dev.marcgil.hexagon.film.adapter.dropwizardweb.api.model.DirectorDto;
import dev.marcgil.hexagon.film.adapter.dropwizardweb.api.model.FilmDto;
import dev.marcgil.hexagon.film.application.port.api.AddFilmToDirectorUseCase.AddFilmToDirectorCommand;
import dev.marcgil.hexagon.film.application.port.api.AddFilmToDirectorUseCase.FilmCharacteristics;
import dev.marcgil.hexagon.film.application.port.api.CreateDirectorUseCase.CreateDirectorCommand;
import dev.marcgil.hexagon.film.application.port.api.GetFilmsUseCase.GetFilmsQuery;
import dev.marcgil.hexagon.film.domain.Director;
import dev.marcgil.hexagon.film.domain.Film;
import dev.marcgil.hexagon.film.domain.Person;
import java.time.Year;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ApiMapper {

  DirectorDto toDirectorDto(Director director);

  @Mapping(target = "directorId", source = "directorId")
  @Mapping(target = "filmCharacteristics", source = ".")
  AddFilmToDirectorCommand toAddFilmToDirectorCommand(FilmDto filmDto);

  @Mapping(target = "duration", source = "duration")
  @Mapping(target = "yearOfRecording", source = "year")
  @Mapping(target = "originalTitle", source = "title")
  FilmCharacteristics toFilmCharacteristics(FilmDto filmDto);

  List<FilmDto> toFilmDtoList(List<Film> films);

  @Mapping(target = "duration", source = "duration")
  @Mapping(target = "year", source = "yearOfRecording")
  @Mapping(target = "title", source = "originalTitle")
  @Mapping(target = "directorId", source = "director.id")
  @Mapping(target = "removeGenresItem", ignore = true)
  @Mapping(target = "removeCastItem", ignore = true)
  FilmDto toFilmDto(Film film);

  default List<String> extractCastNames(List<Person> cast) {
    if (cast == null) {
      return null;
    }
    return cast.stream().map(Person::getName).toList();
  }

  default Year toYear(Integer year) {
    if (year == null) {
      return null;
    }
    return Year.of(year);
  }

  default Integer toInteger(Year year) {
    if (year == null) {
      return null;
    }
    return year.getValue();
  }

  @Mapping(target = "directorName", source = "name")
  @Mapping(target = "directorBirthDate", source = "birthDate")
  CreateDirectorCommand toCreateDirectorCommand(CreateDirectorRequestDto createDirectorRequestDto);

  @Mapping(target = "yearOfRecording", source = "year")
  GetFilmsQuery toGetFilmsQuery(String directorId, String genre, Integer year);

}
