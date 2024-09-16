## Running Application

To run the dropwizard-mongodb-app:
1. Make sure you have docker and docker-compose installed
2. run the following commands in the parent project folder:

```bash
mvn clean install
cd bootsrap/dropwizard-mongodb-app
docker-compose up
mvn exec:java -Dexec.args="server application.yaml"
```

