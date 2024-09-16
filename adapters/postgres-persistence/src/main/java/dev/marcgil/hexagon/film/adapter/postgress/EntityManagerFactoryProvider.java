package dev.marcgil.hexagon.film.adapter.postgress;

import jakarta.persistence.EntityManagerFactory;

public interface EntityManagerFactoryProvider {

  EntityManagerFactory getEntityManagerFactory();

}
