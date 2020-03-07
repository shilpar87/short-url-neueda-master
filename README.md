# short-url
> 

## About

The short-url is an API for short URL creation.  

The main requirements that guided the design and implementation of short-url:
*	Design and implement an API for short URL creation
*	Implement forwarding of short URLs to the original ones
*	Add an API for gathering different statistics
*	There should be some form of persistent storage
*	The application should be distributed as one or more Docker images
*	It should be readable, maintainable, and extensible where appropriate
*	The implementation should preferably be in Java

#### Next steps

There are still other requirements that will guide the next steps of this API implementation:
* Implement authentication and authorization services
* Implement a cache to improve API performance

#### Technical Specification
* [Spring Boot](http://spring.io/projects/spring-boot) for creating the RESTful Web Services
* [MockMVC](https://spring.io/guides/gs/testing-web/) for testing the Web Layer
* [Mockito](https://site.mockito.org/) for testing the Services Layer
* [Postgres](https://www.postgresql.org/) as database
* [Maven](https://maven.apache.org/) for managing the project's build
* [Docker](https://www.docker.com/) for building and managing the application distribution using containers 

#### Download the repository
```sh
$ git clone https://github.com/shilpar87/short-url-neueda-master.git
```
#### With docker and docker-compose installed
```sh
$ cd short-url-neueda && docker-compose up
```

