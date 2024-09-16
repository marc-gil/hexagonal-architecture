package dev.marcgil.hexagon.film.application.port.spi;

import dev.marcgil.hexagon.film.domain.Film;
import dev.marcgil.hexagon.film.domain.Film.Genre;
import java.time.Year;
import java.util.List;

public interface FilmDao {

  List<Film> findBy(String directorId, Genre genre, Year year);

}
