## Spring/Micronaut/Quarkus

### Spring Framework
* Well established framework
* Reactive Stack with Spring WebFlux (from spring v5)
* Big Community support
* Most integrations and Multi Language support (Java, Groovy, Kotlin)
#### Drawbacks
- Heavy use of Reflection
- Startup time and memory usage not very suitable for serverless cloud functions.
- Only experimental support for GraalVM 

### Quarkus (quarkus.io)
* Modern cloud native framework
* Reactive stack
* Minimal memory footprint and startup time
* Based on standards and frameworks (JAX-RS,Netty,Eclipse Microprofile etc)
* GraalVM/ Serverless c;pid functions

#### Drawbacks
- No Multi Language support in preview (Kotlin,Scala)
- Slower compilation time (AOT)
- Smaller community as compared to spring.

### Micronaut (micronaut.io)
* Modern cloud native framework
* Reactive stack
* Minimal memory footprint and startup time
* No byte code modifications during compilation
* Removes all levels of reflection usage
* GraalVM / Serverless cloud functions
* Multi Language support (Java, Groovy, Kotlin)
* Similar to spring application (Easy for spring devs to learn)

#### Drawbacks
- Slower compilation time (AOT)
- Smaller community as compared to Spring


https://github.com/danielprinz/micronaut-udemy
