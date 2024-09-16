package dev.marcgil.hexagon.film.adapter.mongodb.model;

import java.util.List;
import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;

@Data
public class DirectorDocument {

  public static final String DIRECTOR_COLLECTION = "directors";

  @BsonId
  private String id;
  private String name;
  private String birthDate;
  private List<FilmDocument> films;

}
