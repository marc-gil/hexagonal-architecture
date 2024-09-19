package dev.marcgil.hexagon.film.application.service.factory;

import org.jspecify.annotations.NonNull;

public interface IdProvider {

  @NonNull
  String newId();

}
