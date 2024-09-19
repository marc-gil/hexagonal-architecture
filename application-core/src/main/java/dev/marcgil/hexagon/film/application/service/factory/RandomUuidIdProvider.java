package dev.marcgil.hexagon.film.application.service.factory;

import java.util.UUID;
import org.jspecify.annotations.NonNull;

public class RandomUuidIdProvider implements IdProvider {

  @NonNull
  @Override
  public String newId() {
    return UUID.randomUUID().toString();
  }

}
