# Hexagonal Architecture
This repository demonstrates a Hexagonal Architecture (also known as Ports and Adapters) implementation in a Java Maven multimodule setup. 
The project is designed to be modular and flexible, with separate modules for application core logic and various web and persistence adapter implementations.

The structure is designed to allow swapping out different technologies (such as different web frameworks or databases) without affecting the core business logic. 

The project includes examples of both Dropwizard and Spring Boot as web frameworks, along with MongoDB and PostgreSQL as database implementations.

---

## Project Structure
The repository is organized into three main directories: 

- /application-core
- /adapters
- /bootstrap

Each part follows the Hexagonal Architecture (also known as Ports and Adapters), which separates the business logic from the outside world (UI, database, external services).
This modular design promotes clean boundaries between different layers of the system and grants high flexibility, and maintainability.

### 1. ```/application-core``` (Business Logic)
The core of the system contains the essential business logic and domain models. 
**This module should remain completely independent of external concerns such as databases, web frameworks, or other I/O.** 
The core is designed around ports and services:
- **Ports**: Define the boundaries of the application. Ports are interfaces that allow the outside world to interact with the core (incoming ports), and that the core uses to interact with external systems (outgoing ports).
  
  - Incoming Ports (```/port/api```): These define how external actors (like web controllers or other systems) can interact with the core business logic. 
    Typically, they represent commands, queries, or use cases.  
    For example: Commands like "AddFilmToDirector" or "GetDirectorById" would be defined as interfaces here.
  - Outgoing Ports (```/port/spi```): These define interfaces that the core uses to communicate with external systems like databases, message brokers, or external APIs.  
    For example: A FilmDao interface might be defined here to describe how the core expects to interact with the persistence layer.

- Services (```/service```): These are the implementations of the incoming ports. The services contain the business logic and orchestrate the interactions between the domain models and the outgoing ports.  
  For example: The "AddFilmToDirector" command is implemented here, and this service might invoke the FilmDao (an outgoing port) to persist a film in the database.
  - Factories (```/factory```): These are helper classes to construct domain models in a consistent and encapsulated way, often used to simplify complex object creation.

- Domain Models (```/domain```): This layer represents the core business entities (e.g., Film) and the business rules that govern them. 
  These are the heart of the application, and should not depend on external systems or frameworks.

### Key Principle:

**The core knows nothing about the outside world.** It only depends on the interfaces (ports) that allow it to interact with external systems.  
This allows you to change the technology stack (e.g., switch from MongoDB to PostgreSQL) without changing the core logic.

### 2. ```/adapters``` (Ports and Adapters Implementation)
The adapters implement the ports defined in the core. There are two kinds of adapters in Hexagonal Architecture: 
primary (driving) adapters and secondary (driven) adapters.

- Primary (Driving) Adapters: These are external systems that trigger the business logic, such as web frameworks, message queues, or CLI commands. 
They consume the incoming ports (/port/api).
  - In this project, the primary adapters are the Dropwizard and Spring Web Controllers, which handle incoming HTTP requests and translate them into commands or queries for the core business logic to process.
  - Modules:
    - /dropwizard-web: Implements API ports with Dropwizard and Jakarta Resources.
    - /spring-web: Implements API ports with Spring Web Controllers.
    
- Secondary (Driven) Adapters: These are external systems that the core interacts with, such as databases, external APIs, or message brokers. They implement the outgoing ports (/port/spi).
  - In this project, the secondary adapters are the MongoDB and PostgreSQL persistence implementations, which provide the concrete implementations of the repository interfaces defined in the core.
  - Modules:
    - /mongodb-persistence: Implements the MongoDB persistence layer using the outgoing ports (e.g., FilmRepository).
    - /postgres-persistence: Implements the PostgreSQL persistence layer using Hibernate and the outgoing ports.

### Key Principle:

**Adapters are the glue between the external world and the core.** They are responsible for translating external requests (from web, databases, etc.) into a format the core understands, and vice versa.

### 3. ```/bootstrap``` (Application Launchers and Configurations)
The bootstrap modules handle the configuration and startup of the application. Each module brings together a combination of web framework and persistence technology by wiring up the core logic with the appropriate adapters.

Each bootstrap module includes the necessary configuration classes to set up and run an application in a specific environment, and docker-compose.yaml files to spin up the required services like databases.

- dropwizardweb-mongodb-app: This module combines the Dropwizard web framework with MongoDB for persistence.
  - It includes the configuration classes and Docker Compose file to start a MongoDB service for the application.
  
- springweb-mongodb-app: This module combines Spring Boot with Spring Web Controllers and MongoDB for persistence.
  - It includes the configuration classes and Docker Compose file to start a MongoDB service.
  
- springweb-postgres-app: This module combines Spring Boot with Spring Web Controllers and PostgreSQL for persistence.
  - It includes the configuration classes and Docker Compose file to start a PostgreSQL service.

Each module demonstrates how to wire up different combinations of frameworks (e.g., Dropwizard or Spring) and databases (e.g., MongoDB or PostgreSQL) without changing the core logic.

### Key Principle:

**Bootstrap modules are responsible for application orchestration.** They combine the core business logic with the necessary adapters and configurations to produce a running application.  
This allows the same core logic to be used with different technologies in a seamless way.

---

## Getting Started
### Prerequisites
- **Java 17** or later
- **Maven 3.8** or later
- **Docker** and **docker-compose** (for running MongoDB and PostgreSQL services)

### Running the Applications
Each bootstrap module contains the necessary configurations and Docker services to run a specific setup.  
You can choose the combination of a web framework and database as per your need.
1. Dropwizard + MongoDB:  
Navigate to the [dropwizardweb-mongodb-app](bootstrap/dropwizardweb-mongodb-app/README.md) module README and follow the instructions
2. Spring Web + MongoDB:  
Navigate to the [springweb-mongodb-app](bootstrap/springweb-mongodb-app/README.md) module README and follow the instructions
3. Spring Web + PostgreSQL:  
Navigate to the [springweb-postgres-app](bootstrap/springweb-postgres-app/README.md) module README and follow the instructions

---

## Modules Overview
| Module                               | Description                                         |
|--------------------------------------|-----------------------------------------------------|
| /application-core                    | Core business logic and domain models.              |
| /adapters/dropwizard-web             | Dropwizard implementation of API ports.             |
| /adapters/mongodb-persistence        | MongoDB implementation of SPI ports.                |                
| /adapters/postgres-persistence       | Hibernate + PostgreSQL implementation of SPI ports. |
| /adapters/spring-web                 | Spring Web Controllers for API ports.               |                 
| /bootstrap/dropwizardweb-mongodb-app | Dropwizard + MongoDB application launcher.          |
| /bootstrap/springweb-mongodb-app     | Spring Web + MongoDB application launcher.          |   
| /bootstrap/springweb-postgres-app    | Spring Web + PostgreSQL application launcher.       |

## Contributing
Feel free to contribute to this repository by submitting issues or pull requests. 
Any improvements, bug fixes, or additional examples are welcome!