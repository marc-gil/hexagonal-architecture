package dev.marcgil.hexagon.bootstrap;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import dev.marcgil.hexagon.bootstrap.EnableMongoDbTestContainerContextCustomerFactory.EnabledMongoDbTestContainer;
import dev.marcgil.hexagon.film.adapter.mongodb.model.DirectorDocument;
import org.bson.BsonArray;
import org.bson.BsonValue;
import org.bson.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@AutoConfigureMockMvc
@EnabledMongoDbTestContainer
@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AbstractIntegrationTest {

  @Autowired
  private MongoClient mongoClient;
  @Value("${spring.data.mongodb.db}")
  private String dbName;

  @BeforeAll
  void setUpDb() {
    MongoCollection<Document> collection = mongoClient.getDatabase(dbName)
        .getCollection(DirectorDocument.DIRECTOR_COLLECTION);

    try (MongoCursor<Document> cursor = collection.find().cursor()) {
      if (cursor.available() > 0) {
        return;
      }
    }

    collection.insertMany(BsonArray.parse("""
                [
                  { _id: '28b55971-2fc3-4cb4-856e-caf220019ea4', name: 'Quentin Tarantino', birthDate: '1963-03-27', films: [
                    { id: '4c7ee77e-7252-4ff2-a89c-f889b43de49d', year : 2009, title: 'Inglourious Basterds', duration: 'PT2H26M',
                        genres: ['COMEDY', 'ACTION'], cast: [
                        { id: '308f78e3-4bfd-4a34-bbe0-2b8fd6792616', name: 'Brad Pitt' },
                        { id: '6666a4a5-fd1f-4388-9f3d-6009a15b2ffe', name: 'Christoph Waltz' },
                        { id: '18a46dfc-143d-474d-875a-de9129ea6d4e', name: 'Mélanie Laurent' }]
                    },
                    { id: 'a4aba4d-9c7a-4920-8491-ea41e7487cf3', year : 2003, title: 'Kill Bill Vol. 1', duration: 'PT1H50M',
                        genres: ['ACTION'], cast: [
                        { id: '7d9735cb-2a65-4431-87db-7ade06d9a5d7', name: 'Uma Thurman' },
                        { id: '0f343dc7-c4e8-4c99-a629-debdab9768b3', name: 'Lucy Liu' },
                        { id: 'bf4fb0b6-68ef-4e8b-8ff4-1ea564d7a44d', name: 'David Carradine'}]
                    }
                  ]},
                  { _id: '3d704cf2-3490-4529-8989-b4176a49250d', name: 'Christopher Nolan', birthDate: '1970-07-30', films: [
                    { id: '3a4aba4d-9c7a-4920-8491-ea41e7487cf3', year : 2010, title: 'Origen', duration: 'PT2H28M',
                        genres: ['THRILLER'], cast: [
                        { id: '0d3676d8-fe9d-4cd1-a1ec-9e6cab073fb1', name: 'Leonardo DiCaprio' },
                        { id: '9ebc8904-799f-40bb-88dc-14c0bfdd1052', name: 'Joseph Gordon-Levitt' },
                        { id: '16c79ca9-fa83-4030-b81f-8a77337de79a', name: 'Marion Cotillard' }]
                    }
                  ]},
                  { _id: 'e818d70f-5cab-4fae-b553-70090c56bc3b', name: 'Steven Spielberg', birthDate: '1946-12-18', films: [
                    { id: 'd09db500-b1fb-452a-8403-669323e99694', year : 1993, title: 'La lista de Schindler', duration: 'PT3H15M',
                        genres: ['DRAMA'], cast: [
                        { id: 'f5539aec-348f-432e-b5ee-8bee89dbb92b', name: 'Liam Neeson' },
                        { id: '52bb7867-6bca-4ad6-bbda-9f2d06cd8a2b', name: 'Ben Kingsley' },
                        { id: '6d6ed246-268f-41ad-8fe1-6443567a1ab7', name: 'Caroline Goodall' }]
                    }
                  ]},
                  { _id: '8abd01c4-2a37-417d-bde2-6591948f5786', name: 'Martin Scorsese', birthDate: '1942-11-17', films: [
                    { id: 'b3ffaa01-1872-466a-b52d-7229db80c1bf', year : 2010, title: 'Shutter Island', duration: 'PT2H18M',
                        genres: ['THRILLER'], cast: [
                        { id: 'a277ba9c-d42b-4d83-9c89-f8bfe8c664f0', name: 'Leonardo DiCaprio' },
                        { id: 'afddfc6a-704b-4ca7-aabd-35359b55d76e', name: 'Mark Ruffalo' },
                        { id: '1567a91c-8da0-40e5-8637-04e7c15c553e', name: 'Ben Kingsley' }]
                    }
                  ]}
                ]
            """).stream()
        .map(BsonValue::asDocument)
        .map(Document::new)
        .toList());
  }

}
