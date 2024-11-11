package dev.marcgil.hexagon.film.application.port.spi;

import dev.marcgil.hexagon.film.domain.Film;
import dev.marcgil.hexagon.film.domain.Film.Genre;
import java.time.Year;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface FilmDao {

  List<Film> findBy(@Nullable String directorId, @Nullable Genre genre, @Nullable Year year);

}
