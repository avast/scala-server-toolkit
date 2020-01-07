# Scala Server Toolkit

[![Build Status](https://travis-ci.org/avast/scala-server-toolkit.svg?branch=master)](https://travis-ci.org/avast/scala-server-toolkit)
[![Maven Central](https://img.shields.io/maven-central/v/com.avast/sst-bundle-zio-http4s-blaze_2.12)](https://repo1.maven.org/maven2/com/avast/sst-bundle-zio-http4s-blaze_2.12/)
[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-brightgreen.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)
[![Mergify badge](https://img.shields.io/endpoint.svg?url=https://gh.mergify.io/badges/avast/scala-server-toolkit)](https://mergify.io)

This project is a culmination of years of Scala development at Avast and tries to represent the best practices of Scala server development 
we have gained together with tools that allow us to be effective. It is a set of small, flexible and cohesive building blocks that fit 
together well and allow you to build reliable server applications.

## Documentation

**[Read it here](./docs/index.md)** or you can [deep dive into example code](example/src/main/scala/com/avast/sst/example/Main.scala).

### Articles

* [Introducing Scala Server Toolkit](https://engineering.avast.io/introducing-scala-server-toolkit) (Avast Engineering)
* [SST - Creating HTTP Server](https://engineering.avast.io/scala-server-toolkit-creating-http-server) (Avast Engineering)

## Design

There are certain design decisions and constraints that are put in place to guide the development of the toolkit and recommended for 
the development of your server applications.

* Keep the number of dependencies as low as possible.
* Modular design: small, cohesive, orthogonal and composable components.
* Functional programming.
* Type safe configuration and resource lifecycle.
* No need for dependency injection.
* [Scalazzi Safe Scala Subset](https://slides.yowconference.com/yowwest2014/Morris-ParametricityTypesDocumentationCodeReadability.pdf)

## Issues

Please report issues to [GitHub](https://github.com/avast/scala-server-toolkit/issues). Pull requests are always welcome.

## Versioning

The project follows the [semantic versioning](https://semver.org) schema which means that just by looking at the version you will know 
whether to expect compatibility problems or not.

The project values backwards compatibility but it cannot prevent innovation. There will be breaking changes but they will always be properly
versioned and documented.

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md).

## Code of Conduct

See [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md).

## License

Scala Server Toolkit is licensed under [MIT License](LICENSE).
