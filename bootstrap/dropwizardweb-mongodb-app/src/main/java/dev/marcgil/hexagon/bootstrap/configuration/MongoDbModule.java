package dev.marcgil.hexagon.bootstrap.configuration;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

@RequiredArgsConstructor
public class MongoDbModule extends AbstractModule {

  private final ApplicationConfiguration configuration;

  @Override
  @SneakyThrows
  protected void configure() {
    bind(ApplicationConfiguration.class).toInstance(configuration);
  }

  @Provides
  @Singleton
  public MongoClient mongoClient() {
    CodecRegistry pojoCodecRegistry = fromProviders(
        PojoCodecProvider.builder().automatic(true).build());
    CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
        pojoCodecRegistry);
    return MongoClients.create(MongoClientSettings.builder()
        .applyConnectionString(new ConnectionString(configuration.getUri()))
        .codecRegistry(codecRegistry)
        .build());
  }

}
