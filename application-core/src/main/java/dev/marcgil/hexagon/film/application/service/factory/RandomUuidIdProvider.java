package dev.marcgil.hexagon.film.application.service.factory;

import java.util.UUID;

public class RandomUuidIdProvider implements IdProvider {

  @Override
  public String newId() {
    return UUID.randomUUID().toString();
  }

}
