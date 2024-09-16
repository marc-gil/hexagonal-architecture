package dev.marcgil.hexagon.film.domain;

import dev.marcgil.hexagon.film.domain.Film.Genre;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
public class Director extends Person {

  @Setter
  @Builder.Default
  List<Film> directedFilms = List.of();

  public void addFilm(Film film) {
    ArrayList<Film> films = new ArrayList<>(directedFilms);
    films.add(film);
    this.directedFilms = Collections.unmodifiableList(films);
  }

  public Genre getPrefferedGenre() {
    return this.directedFilms.stream()
        .map(Film::getGenres)
        .flatMap(Collection::stream)
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
        .entrySet()
        .stream()
        .max(Map.Entry.comparingByValue())
        .map(Entry::getKey)
        .orElse(null);
  }

}
