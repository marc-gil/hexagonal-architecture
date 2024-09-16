package dev.marcgil.hexagon.film.adapter.mongodb.model;

import java.util.List;
import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Data
public class FilmDocument {

  @BsonProperty("id")
  private String id;
  private int year;
  private String title;
  private String duration;
  private List<String> genres;
  private List<PersonDocument> cast;

}
