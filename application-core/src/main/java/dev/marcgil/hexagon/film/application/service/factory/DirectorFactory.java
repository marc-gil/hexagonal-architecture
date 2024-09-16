package dev.marcgil.hexagon.film.application.service.factory;

import dev.marcgil.hexagon.film.application.port.api.CreateDirectorUseCase.CreateDirectorCommand;
import dev.marcgil.hexagon.film.domain.Director;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DirectorFactory {

  private final IdProvider idProvider;

  public Director create(CreateDirectorCommand command) {
    return Director.builder()
        .id(idProvider.newId())
        .name(command.directorName())
        .birthDate(command.directorBirthDate())
        .build();
  }

}
