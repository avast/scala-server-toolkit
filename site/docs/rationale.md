# Rationale

Avast backend developers have been using Scala since 2011. Over the years they have went through the usual stages of a Scala developer:

* **Using Scala as better Java**: imperative code full of mutability and side effects.
* **Idiomatic Scala**: expression-oriented programming, full potential of Scala collections, convenient mix of OOP and FP.
* **FP Scala**: taming side effects, monads and algebraic laws, all about referential transparency, composability and separation of concerns.

The evolution process from imperative Scala towards FP Scala was very natural. As we have been learning Scala more and more it lead us to
the more functional side of Scala. Now we are in a phase in which we are dedicated to go full FP.

However not everything was rose-colored. The different styles of programming we have used in the past mean that we have very diverse
codebases and teams/people are used to do things differently. Functional programming is very popular nowadays and the amount of innovation
in that space is incredible but we are still lacking some kind of library that would give us unified approach to programming server 
applications. Thus we have decided to write our own and it is called `scala-server-toolkit`. It should help us unify our programming style,
promote best practices, functional programming and we believe it could be useful to others too.

It should be pointed out that we do not intend to write some full-blown framework that would try to solve all your server-side problems.
We are actually trying to do quite the opposite. Reuse as most open-source libraries as we can and provide only the missing integration 
pieces to simplify everyday programming. Most of the code revolves around initialization and integration of existing OSS libraries in
unified way.

There are certain design decisions and constraints that we put in place to guide the development of this library.

* Modular design: small, cohesive, orthogonal and composable components.
  * The project is split into completely separate modules mostly based on dependencies. Every module provides some functionality, usually
  something small. The idea is to compose multiple modules together to get a working application but be able to replace any module
  with different implementation in case it is needed.
* Keep the number of dependencies as low as possible.
  * Adding a dependency seems like a great idea to avoid having to implement some piece of existing logic however every dependency is also
  a burden that can cause a lot of problems. Therefore every dependency should be considered well and only very important and well-maintained 
  libraries should be added to our core.
* Functional programming.
  * We believe in functional programming so this library also utilizes its concepts.
  * We do not want to force anyone into any specific effect data type so the code is written in so-called tagless final style.
* Type safe configuration and resource lifecycle.
  * Application initialization is often overlooked. By using type safe configuration and proper resource management (to never leak resources)
  we can make it correct without much hassle.
* No need for dependency injection.
  * Dependency injection is not needed in most cases - plain constructors and good application architecture usually suffice.
  * Use a dependency injection framework if your application is huge and it justifies its cost.
* Strive for [Scalazzi Safe Scala Subset](https://slides.yowconference.com/yowwest2014/Morris-ParametricityTypesDocumentationCodeReadability.pdf).
