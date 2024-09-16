package dev.marcgil.hexagon.film.domain;

import java.time.Duration;
import java.time.Year;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Film {

  @NonNull
  String id;
  @NonNull
  Director director;
  @NonNull
  Year yearOfRecording;
  @NonNull
  String originalTitle;
  @NonNull
  Duration duration;
  @NonNull
  @Builder.Default
  List<Genre> genres = List.of();
  @NonNull
  @Builder.Default
  List<Person> cast = List.of();

  public enum Genre {
    COMEDY, DRAMA, THRILLER, ROMANCE, ANIME, ACTION
  }
}
