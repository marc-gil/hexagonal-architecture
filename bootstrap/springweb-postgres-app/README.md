## Running Application

To run the springweb-postgres-app:
1. Make sure you have the prerequisites from the [README.md](/README.md)
2. run the following commands in the parent project folder:

```bash
mvn clean install
cd bootsrap/springweb-postgres-app
mvn spring-boot:run -Dspring-boot.run.profiles=local
```
3. Once it's running you can access the swagger ui at [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
