## Running Application

To run the dropwizard-mongodb-app:
1. Make sure you have the prerequisites from the [README.md](/README.md)
2. run the following commands in the parent project folder:

```bash
mvn clean install
cd bootsrap/dropwizard-mongodb-app
docker-compose up
mvn exec:java -Dexec.args="server application.yaml"
```
3. Once it's running you can access the swagger file at [http://localhost:8080/openapi.json](http://localhost:8080/openapi.json)
