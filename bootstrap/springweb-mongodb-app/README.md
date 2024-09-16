## Running Application

To run the springweb-mongodb-app:
1. Make sure you have docker and docker-compose installed
2. run the following commands in the parent project folder:

```bash
mvn clean install
cd bootsrap/springweb-mongodb-app
mvn spring-boot:run -Dspring-boot.run.profiles=local
```
