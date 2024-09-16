package dev.marcgil.hexagon.film.adapter.mongodb.model;

import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Data
public class PersonDocument {

  @BsonProperty("id")
  private String id;
  private String name;
  private String birthDate;

}
