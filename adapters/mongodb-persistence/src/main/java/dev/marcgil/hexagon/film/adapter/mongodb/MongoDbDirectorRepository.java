package dev.marcgil.hexagon.film.adapter.mongodb;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;
import static com.mongodb.client.model.mql.MqlValues.current;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.mql.MqlArray;
import com.mongodb.client.model.mql.MqlBoolean;
import com.mongodb.client.model.mql.MqlDocument;
import com.mongodb.client.model.mql.MqlInteger;
import com.mongodb.client.model.mql.MqlString;
import com.mongodb.client.model.mql.MqlValues;
import dev.marcgil.hexagon.film.adapter.mongodb.model.DirectorDocument;
import dev.marcgil.hexagon.film.adapter.mongodb.model.PersonDocument;
import dev.marcgil.hexagon.film.domain.Film.Genre;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.bson.conversions.Bson;

public class MongoDbDirectorRepository implements DirectorRepository {

  private final MongoCollection<DirectorDocument> collection;

  public MongoDbDirectorRepository(MongoClient mongoClient, String dbName) {
    this.collection = mongoClient.getDatabase(dbName)
        .getCollection(DirectorDocument.DIRECTOR_COLLECTION, DirectorDocument.class);
  }

  @Override
  public Optional<DirectorDocument> findById(String directorId) {
    return Optional.ofNullable(collection.find(eq("_id", directorId)).first());
  }

  @Override
  public DirectorDocument save(DirectorDocument director) {
    Bson findByIdFilter = Filters.eq("_id", director.getId());

    collection.replaceOne(findByIdFilter, director,
        new ReplaceOptions().upsert(true));

    return director;
  }

  @Override
  public List<DirectorDocument> findByName(String name) {
    return collection.find(regex("name", name)).into(new ArrayList<>());
  }

  @Override
  public List<DirectorDocument> findBy(String directorId, Genre genre, Year yearOfRecording) {
    List<Bson> aggregate = new ArrayList<>();
    if (directorId != null) {
      aggregate.add(Aggregates.match(eq("_id", directorId)));
    }
    MqlArray<MqlDocument> films = current().getArray("films");
    if (genre != null || yearOfRecording != null) {
      Bson projectStage = Aggregates.project(
          Projections.fields(
              Projections.include("name", "birthDate"),
              Projections.computed("films",
                  films.filter(film -> {
                    MqlInteger year = film.getInteger("year");
                    MqlArray<MqlString> genres = film.getArray("genres");
                    Optional<MqlBoolean> yearEqFilter = Optional.ofNullable(yearOfRecording)
                        .map(Year::getValue)
                        .map(MqlValues::of)
                        .map(year::eq);
                    Optional<MqlBoolean> genresInFilter = Optional.ofNullable(genre)
                        .map(Enum::name)
                        .map(MqlValues::of)
                        .map(genres::contains);

                    return Stream.concat(genresInFilter.stream(), yearEqFilter.stream())
                        .reduce(MqlBoolean::and)
                        .orElseThrow();
                  })
              )
          )
      );
      aggregate.add(projectStage);
    }
    return new ArrayList<>(collection.aggregate(aggregate).into(new ArrayList<>()));
  }

  @Override
  public Optional<PersonDocument> findActorByName(String actorName) {
    List<Bson> aggregationPipeline = List.of(
        Aggregates.unwind("$films"),
        Aggregates.unwind("$films.cast"),
        Aggregates.match(eq("films.cast.name", actorName)),
        Aggregates.project(Projections.fields(
            Projections.excludeId(),
            Projections.computed("id", "$films.cast.id"),
            Projections.computed("name", "$films.cast.name")
        )));
    return Optional.ofNullable(
        collection.withDocumentClass(PersonDocument.class).aggregate(aggregationPipeline).first());
  }

}
