// When the user clicks on the search box, we want to toggle the search dropdown
function displayToggleSearch(e) {
  e.preventDefault();
  e.stopPropagation();

  closeDropdownSearch(e);
  
  if (idx === null) {
    console.log("Building search index...");
    prepareIdxAndDocMap();
    console.log("Search index built.");
  }
  const dropdown = document.querySelector("#search-dropdown-content");
  if (dropdown) {
    if (!dropdown.classList.contains("show")) {
      dropdown.classList.add("show");
    }
    document.addEventListener("click", closeDropdownSearch);
    document.addEventListener("keydown", searchOnKeyDown);
    document.addEventListener("keyup", searchOnKeyUp);
  }
}

//We want to prepare the index only after clicking the search bar
var idx = null
const docMap = new Map()

function prepareIdxAndDocMap() {
  const docs = [  
    {
      "title": "Bundles",
      "url": "/scala-server-toolkit/bundles.html",
      "content": "Bundles Having many small and independent subprojects is great but in practice everyone wants to use certain combination of dependencies and does not want to worry about many small dependencies. There are “bundles” for such use case - either the ones provided by this project or custom ones created by the user. One of the main decisions (dependency-wise) is to choose the effect data type. This project does not force you into specific data type and supports both ZIO and Monix out-of-the-box. So there are two main bundles one for each effect data type that also bring in http4s server/client (Blaze), PureConfig and Micrometer. Unless you have specific needs take one of these bundles and write your server application using them - it will be the simplest way."
    } ,    
    {
      "title": "Datastax Cassandra Driver",
      "url": "/scala-server-toolkit/subprojects/cassandra-datastax-driver.html",
      "content": "Datastax Cassandra Driver This subproject initializes Datastax Cassandra driver’s Session. libraryDependencies += \"com.avast\" %% \"sst-cassandra-datastax-driver\" % \"0.11.0\" import cats.effect.Resource import com.avast.sst.datastax.CassandraDatastaxDriverModule import com.avast.sst.datastax.config.CassandraDatastaxDriverConfig import com.avast.sst.datastax.pureconfig.implicits._ import com.avast.sst.pureconfig.PureConfigModule import zio._ import zio.interop.catz._ implicit val runtime = zio.Runtime.default // this is just needed in example for { configuration &lt;- Resource.eval(PureConfigModule.makeOrRaise[Task, CassandraDatastaxDriverConfig]) db &lt;- CassandraDatastaxDriverModule.make[Task](configuration) } yield db basic { contact-points = [\"localhost:9042\"] load-balancing-policy { local-datacenter = \"datacenter1\" } }"
    } ,    
    {
      "title": "Doobie",
      "url": "/scala-server-toolkit/subprojects/doobie.html",
      "content": "Doobie libraryDependencies += \"com.avast\" %% \"sst-doobie-hikari\" % \"0.11.0\" This subproject initializes a doobie Transactor: import cats.effect.Resource import com.avast.sst.doobie.DoobieHikariModule import com.avast.sst.example.config.Configuration import com.avast.sst.jvm.execution.ConfigurableThreadFactory.Config import com.avast.sst.jvm.execution.{ConfigurableThreadFactory, ExecutorModule} import com.avast.sst.micrometer.jmx.MicrometerJmxModule import com.avast.sst.pureconfig.PureConfigModule import com.zaxxer.hikari.metrics.micrometer.MicrometerMetricsTrackerFactory import scala.concurrent.ExecutionContext import zio._ import zio.interop.catz._ implicit val runtime = zio.Runtime.default // this is just needed in example for { configuration &lt;- Resource.eval(PureConfigModule.makeOrRaise[Task, Configuration]) executorModule &lt;- ExecutorModule.makeFromExecutionContext[Task](runtime.platform.executor.asEC) meterRegistry &lt;- MicrometerJmxModule.make[Task](configuration.jmx) boundedConnectExecutionContext &lt;- executorModule .makeThreadPoolExecutor( configuration.boundedConnectExecutor, new ConfigurableThreadFactory(Config(Some(\"hikari-connect-%02d\"))) ) .map(ExecutionContext.fromExecutorService) hikariMetricsFactory = new MicrometerMetricsTrackerFactory(meterRegistry) doobieTransactor &lt;- DoobieHikariModule .make[Task](configuration.database, boundedConnectExecutionContext, executorModule.blocker, Some(hikariMetricsFactory)) } yield doobieTransactor"
    } ,    
    {
      "title": "Flyway",
      "url": "/scala-server-toolkit/subprojects/flyway.html",
      "content": "Flyway libraryDependencies += \"com.avast\" %% \"sst-flyway\" % \"0.11.0\" This subproject initializes Flyway which can be used to do automated SQL DB migrations. See the documentation of Flyway on how to go about that. The method make requires javax.sql.DataSource which you can for example obtain from doobie-hikari subproject: import cats.effect.Resource import com.avast.sst.doobie.DoobieHikariModule import com.avast.sst.flyway.FlywayModule import zio.Task import zio.interop.catz._ for { doobieTransactor &lt;- DoobieHikariModule.make[Task](???, ???, ???, ???) flyway &lt;- Resource.eval(FlywayModule.make[Task](doobieTransactor.kernel, ???)) _ &lt;- Resource.eval(Task.effect(flyway.migrate())) } yield ()"
    } ,    
    {
      "title": "FS2 Kafka",
      "url": "/scala-server-toolkit/subprojects/fs2-kafka.html",
      "content": "FS2 Kafka libraryDependencies += \"com.avast\" %% \"sst-fs2-kafka\" % \"0.11.0\" This subproject initializes FS2 Kafka consumer or producer: import cats.effect.Resource import cats.syntax.flatMap._ import com.avast.sst.fs2kafka._ import fs2.kafka.{AutoOffsetReset, ProducerRecord, ProducerRecords} import zio._ import zio.interop.catz._ import zio.interop.catz.implicits._ implicit val runtime = zio.Runtime.default // this is just needed in example for { consumer &lt;- Fs2KafkaModule.makeConsumer[Task, String, String]( ConsumerConfig(List(\"localhost:9092\"), groupId = \"test\", autoOffsetReset = AutoOffsetReset.Earliest) ) _ &lt;- Resource.eval(consumer.subscribeTo(\"test\")) consumerStream &lt;- Resource.eval(consumer.stream) } yield consumerStream The configuration of Kafka client is very large therefore you can either use the provided configuration case class, or you can use the underlying ConsumerSettings/ProducerSettings builders directly. The configuration case classes contain an “escape hatch” into the full world of Kafka client configuration options via untyped properties. This is there to be flexible in case it is needed. Documentation of all the configuration properties is available here: consumer producer Beware that there is an optional dependency on jackson-databind for the default implementation of SASL/OAUTHBEARER in kafka-clients. You need to provide it explicitly: https://kafka.apache.org/documentation/#security_sasl_oauthbearer_clientconfig"
    } ,    
    {
      "title": "Getting Started",
      "url": "/scala-server-toolkit/getting-started.html",
      "content": "Getting Started Creating a simple HTTP server using http4s and ZIO is as easy as this: build.sbt libraryDependencies += \"com.avast\" %% \"sst-bundle-zio-http4s-blaze\" % \"0.11.0\" Main import cats.effect._ import com.avast.sst.http4s.client._ import com.avast.sst.http4s.server._ import com.avast.sst.jvm.execution.ExecutorModule import com.avast.sst.jvm.system.console.ConsoleModule import org.http4s.dsl.Http4sDsl import org.http4s.HttpRoutes import zio.interop.catz._ import zio.interop.catz.implicits._ import zio.Task implicit val runtime = zio.Runtime.default // this is just needed in example val dsl = Http4sDsl[Task] // this is just needed in example import dsl._ val routes = Http4sRouting.make { HttpRoutes.of[Task] { case GET -&gt; Root / \"hello\" =&gt; Ok(\"Hello World!\") } } val resource = for { executorModule &lt;- ExecutorModule.makeDefault[Task] console = ConsoleModule.make[Task] server &lt;- Http4sBlazeServerModule.make[Task](Http4sBlazeServerConfig(\"127.0.0.1\", 0), routes, executorModule.executionContext) client &lt;- Http4sBlazeClientModule.make[Task](Http4sBlazeClientConfig(), executorModule.executionContext) } yield (server, client, console) val program = resource .use { case (server, client, console) =&gt; client .expect[String](s\"http://127.0.0.1:${server.address.getPort}/hello\") .flatMap(console.printLine) } runtime.unsafeRun(program) // Hello World! Or you can use the official Giter8 template: sbt new avast/sst-seed.g8"
    } ,    
    {
      "title": "http4s",
      "url": "/scala-server-toolkit/subprojects/http4s.html",
      "content": "http4s libraryDependencies += \"com.avast\" %% \"sst-http4s-server-blaze\" % \"0.11.0\" There are multiple http4s-* subprojects that provide easy initialization of a server and a client. Http4s is an interface with multiple possible implementations - for now we provide only implementations based on Blaze. Both server and client are configured via configuration case class which contains default values taken from the underlying implementations. import cats.effect._ import com.avast.sst.http4s.client._ import com.avast.sst.http4s.server._ import com.avast.sst.jvm.execution.ExecutorModule import com.avast.sst.jvm.system.console.ConsoleModule import org.http4s.dsl.Http4sDsl import org.http4s.HttpRoutes import zio.interop.catz._ import zio.interop.catz.implicits._ import zio.Task implicit val runtime = zio.Runtime.default // this is just needed in example val dsl = Http4sDsl[Task] // this is just needed in example import dsl._ val routes = Http4sRouting.make { HttpRoutes.of[Task] { case GET -&gt; Root / \"hello\" =&gt; Ok(\"Hello World!\") } } val resource = for { executorModule &lt;- ExecutorModule.makeDefault[Task] console = ConsoleModule.make[Task] server &lt;- Http4sBlazeServerModule.make[Task](Http4sBlazeServerConfig(\"127.0.0.1\", 0), routes, executorModule.executionContext) client &lt;- Http4sBlazeClientModule.make[Task](Http4sBlazeClientConfig(), executorModule.executionContext) } yield (server, client, console) val program = resource .use { case (server, client, console) =&gt; client .expect[String](s\"http://127.0.0.1:${server.address.getPort}/hello\") .flatMap(console.printLine) } runtime.unsafeRun(program) // Hello World! Middleware Correlation ID Middleware import cats.effect._ import com.avast.sst.http4s.server._ import com.avast.sst.http4s.server.middleware.CorrelationIdMiddleware import com.avast.sst.jvm.execution.ExecutorModule import org.http4s.dsl.Http4sDsl import org.http4s.HttpRoutes import zio.interop.catz._ import zio.interop.catz.implicits._ import zio.Task val dsl = Http4sDsl[Task] // this is just needed in example import dsl._ implicit val runtime = zio.Runtime.default // this is just needed in example for { middleware &lt;- Resource.eval(CorrelationIdMiddleware.default[Task]) executorModule &lt;- ExecutorModule.makeDefault[Task] routes = Http4sRouting.make { middleware.wrap { HttpRoutes.of[Task] { case GET -&gt; Root =&gt; // val correlationId = middleware.retrieveCorrelationId(req) ??? } } } server &lt;- Http4sBlazeServerModule.make[Task](Http4sBlazeServerConfig.localhost8080, routes, executorModule.executionContext) } yield server Circuit Breaker It is a good practice to wrap any communication with external system with circuit breaking mechanism to prevent spreading of errors and bad latency. See monix-catnap for one of the options."
    } ,    
    {
      "title": "Home",
      "url": "/scala-server-toolkit/",
      "content": "This project is a culmination of years of Scala development at Avast and tries to represent the best practices of Scala server development we have gained together with tools that allow us to be effective. It is a set of small, flexible and cohesive building blocks that fit together well and allow you to build reliable server applications. Jump Right In You can use the official Giter8 template to get started: sbt new avast/sst-seed.g8 Or read documentation or deep dive into example code. SBT Dependency libraryDependencies += \"com.avast\" %% \"sst-bundle-zio-http4s-blaze\" % \"0.11.0\" Resources Articles Introducing Scala Server Toolkit (Avast Engineering) SST - Creating HTTP Server (Avast Engineering) Talks Intro to Scala Server Toolkit (Functional JVM Meetup, Prague, January 23, 2020) slides"
    } ,    
    {
      "title": "JVM",
      "url": "/scala-server-toolkit/subprojects/jvm.html",
      "content": "JVM libraryDependencies += \"com.avast\" %% \"sst-jvm\" % \"0.11.0\" Subproject sst-jvm provides pure implementations of different JVM-related utilities: creation of thread pools, standard in/out/err, and random number generation. import com.avast.sst.jvm.system.console.ConsoleModule import com.avast.sst.jvm.system.random.RandomModule import zio.interop.catz._ import zio.Task val program = for { random &lt;- RandomModule.makeRandom[Task](1234L) // do not ever use seed like this! randomNumber &lt;- random.nextInt console = ConsoleModule.make[Task] _ &lt;- console.printLine(s\"Random number: $randomNumber\") } yield () val runtime = zio.Runtime.default // this is just needed in example runtime.unsafeRun(program) // Random number: -1517918040"
    } ,    
    {
      "title": "Lettuce (Redis)",
      "url": "/scala-server-toolkit/subprojects/lettuce.html",
      "content": "FS2 Kafka libraryDependencies += \"com.avast\" %% \"sst-lettuce\" % \"0.11.0\" This subproject initializes Lettuce Redis driver: import cats.effect.Resource import com.avast.sst.lettuce.{LettuceConfig, LettuceModule} import io.lettuce.core.codec.{RedisCodec, StringCodec} import zio._ import zio.interop.catz._ implicit val runtime = zio.Runtime.default // this is just needed in example implicit val lettuceCodec: RedisCodec[String, String] = StringCodec.UTF8 for { connection &lt;- LettuceModule.makeConnection[Task, String, String](LettuceConfig(\"redis://localhost\")) value &lt;- Resource.eval(Task.effect(connection.sync().get(\"key\"))) } yield value"
    } ,    
    {
      "title": "Micrometer",
      "url": "/scala-server-toolkit/subprojects/micrometer.html",
      "content": "Micrometer libraryDependencies += \"com.avast\" %% \"sst-micrometer-jmx\" % \"0.11.0\" This subproject allows you to monitor your applications using Micrometer. There are many actual implementations of the Micrometer API one of which is JMX. Subproject sst-micrometer-jmx implements the initialization of Micrometer for JMX. There are also interop subprojects such as sst-http4s-server-micrometer which implement monitoring of HTTP server and individual routes using Micrometer. import cats.effect.{Clock, Resource} import com.avast.sst.http4s.server._ import com.avast.sst.http4s.server.micrometer.MicrometerHttp4sServerMetricsModule import com.avast.sst.jvm.execution.ExecutorModule import com.avast.sst.jvm.micrometer.MicrometerJvmModule import com.avast.sst.micrometer.jmx._ import org.http4s.dsl.Http4sDsl import org.http4s.HttpRoutes import org.http4s.server.Server import zio.interop.catz._ import zio.interop.catz.implicits._ import zio.Task implicit val runtime = zio.Runtime.default // this is just needed in example val dsl = Http4sDsl[Task] // this is just needed in example import dsl._ for { executorModule &lt;- ExecutorModule.makeFromExecutionContext[Task](runtime.platform.executor.asEC) clock = Clock.create[Task] jmxMeterRegistry &lt;- MicrometerJmxModule.make[Task](MicrometerJmxConfig(\"com.avast\")) _ &lt;- Resource.eval(MicrometerJvmModule.make[Task](jmxMeterRegistry)) serverMetricsModule &lt;- Resource.eval(MicrometerHttp4sServerMetricsModule.make[Task](jmxMeterRegistry, executorModule.blocker, clock)) routes = Http4sRouting.make { serverMetricsModule.serverMetrics { HttpRoutes.of[Task] { case GET -&gt; Root / \"hello\" =&gt; Ok(\"Hello World!\") } } } server &lt;- Http4sBlazeServerModule.make[Task](Http4sBlazeServerConfig(\"127.0.0.1\", 0), routes, executorModule.executionContext) } yield server"
    } ,    
    {
      "title": "monix-catnap",
      "url": "/scala-server-toolkit/subprojects/monix-catnap.html",
      "content": "monix-catnap libraryDependencies += \"com.avast\" %% \"sst-monix-catnap\" % \"0.11.0\" This subproject provides interop between Scala Server Toolkit and monix-catnap library. Circuit Breaker You can use CircuitBreakerModule to instantiate and configure a circuit breaker and you can implement CircuitBreakerMetrics to get monitoring of the circuit breaker. There is an implementation for Micrometer in sst-monix-catnap-micrometer subproject. All of this is tied with http4s HTTP client in the sst-http4s-client-monix-catnap subproject so in practice you want to use Http4sClientCircuitBreakerModule which wraps any Client[F] with a CircuitBreaker (it is recommended to have an enriched CircuitBreaker with logging and metrics - see CircuitBreakerModule companion object methods). However the most important feature of the enriched circuit breaker is that any HTTP failure (according to HttpStatusClassifier) is converted to an exception internally which triggers the circuit breaking mechanism. Failing server is not overloaded by more requests and we do not have to wait for the response if the server is failing anyway."
    } ,      
    {
      "title": "PureConfig",
      "url": "/scala-server-toolkit/subprojects/pureconfig.html",
      "content": "PureConfig libraryDependencies += \"com.avast\" %% \"sst-pureconfig\" % \"0.11.0\" This subproject allows you to load your application’s configuration file into a case class. It uses PureConfig library to do so which uses Lightbend Config which means that your application’s configuration will be in HOCON format. Loading of configuration is side-effectful so it is wrapped in F which is Sync. This module also tweaks the error messages a little. import com.avast.sst.pureconfig.PureConfigModule import pureconfig.ConfigReader import pureconfig.generic.semiauto.deriveReader import zio.interop.catz._ import zio.Task final case class ServerConfiguration(listenAddress: String, listenPort: Int) implicit val serverConfigurationReader: ConfigReader[ServerConfiguration] = deriveReader val maybeConfiguration = PureConfigModule.make[Task, ServerConfiguration] Look for sst-*-pureconfig subprojects to get implicit instances of ConfigReader for specific libraries, e.g.: import com.avast.sst.http4s.server.pureconfig.implicits._ The default *.pureconfig.implicits._ import contains the default naming convention for PureConfig which is kebab-case for the configuration file and camelCase for the case class field names. You can either choose a different naming convention by different import (e.g. *.pureconfig.implicits.CamelCase) or you can create your own and extend the ConfigReaders trait."
    } ,    
    {
      "title": "Rationale",
      "url": "/scala-server-toolkit/rationale.html",
      "content": "Rationale Avast backend developers have been using Scala since 2011. Over the years they have went through the usual stages of a Scala developer: Using Scala as better Java: imperative code full of mutability and side effects. Idiomatic Scala: expression-oriented programming, full potential of Scala collections, convenient mix of OOP and FP. FP Scala: taming side effects, monads and algebraic laws, all about referential transparency, composability and separation of concerns. The evolution process from imperative Scala towards FP Scala was very natural. As we have been learning Scala more and more it lead us to the more functional side of Scala. Now we are in a phase in which we are dedicated to go full FP. However not everything was rose-colored. The different styles of programming we have used in the past mean that we have very diverse codebases and teams/people are used to do things differently. Functional programming is very popular nowadays and the amount of innovation in that space is incredible but we are still lacking some kind of library that would give us unified approach to programming server applications. Thus we have decided to write our own and it is called scala-server-toolkit. It should help us unify our programming style, promote best practices, functional programming and we believe it could be useful to others too. It should be pointed out that we do not intend to write some full-blown framework that would try to solve all your server-side problems. We are actually trying to do quite the opposite. Reuse as most open-source libraries as we can and provide only the missing integration pieces to simplify everyday programming. Most of the code revolves around initialization and integration of existing OSS libraries in unified way. There are certain design decisions and constraints that we put in place to guide the development of this library. Modular design: small, cohesive, orthogonal and composable components. The project is split into completely separate subprojects mostly based on dependencies. Every subproject provides some functionality, usually something small. The idea is to compose multiple modules together to get a working application but be able to replace any module with different implementation in case it is needed. Keep the number of dependencies as low as possible. Adding a dependency seems like a great idea to avoid having to implement some piece of existing logic however every dependency is also a burden that can cause a lot of problems. Therefore every dependency should be considered well and only very important and well-maintained libraries should be added to our core. Functional programming. We believe in functional programming so this library also utilizes its concepts. We do not want to force anyone into any specific effect data type so the code is written in so-called tagless final style. Type safe configuration and resource lifecycle. Application initialization is often overlooked. By using type safe configuration and proper resource management (to never leak resources) we can make it correct without much hassle. No need for dependency injection. Dependency injection is not needed in most cases - plain constructors and good application architecture usually suffice. Use a dependency injection framework if your application is huge and it justifies its cost. Strive for Scalazzi Safe Scala Subset."
    } ,      
    {
      "title": "Sentry",
      "url": "/scala-server-toolkit/subprojects/sentry.html",
      "content": "Sentry libraryDependencies += \"com.avast\" %% \"sst-sentry\" % \"0.11.0\" This subproject allows you to initialize SentryClient from a configuration case class. There are two make* methods. The one called make does everything according to the configuration. The one called makeWithReleaseFromPackage adds a bit of clever behavior because it reads the Implementation-Version property from the MANIFEST.MF file from the JAR (package) of the respective Main class and uses it to override the release property of Sentry. This allows you to automatically propage the version of your application to Sentry. Initialization of the SentryClient is side-effectful so it is wrapped in Resource[F, SentryClient] and F is Sync. import com.avast.sst.sentry._ import zio.interop.catz._ import zio.Task val sentry = SentryModule.make[Task](SentryConfig(\"&lt;dsn&gt;\"))"
    } ,    
    {
      "title": "SSL Config",
      "url": "/scala-server-toolkit/subprojects/ssl-config.html",
      "content": "SSL Config libraryDependencies += \"com.avast\" %% \"sst-ssl-config\" % \"0.11.0\" This subproject allows you to create SSL context (javax.net.ssl.SSLContext) from a configuration file. It uses SSL Config library to do so which means that this module is different than others and receives directly com.typesafe.config.Config instead of configuration case classes. See the documentation of SSL Config for more information. Loading of SSL context is side-effectful so it is wrapped in F which is Sync. import com.avast.sst.ssl.SslContextModule import com.typesafe.config.ConfigFactory import zio.interop.catz._ import zio.Task val config = ConfigFactory.load().getConfig(\"ssl-config\") val sslContext = SslContextModule.make[Task](config)"
    } ,    
    {
      "title": "Structure",
      "url": "/scala-server-toolkit/structure.html",
      "content": "Structure The project is split into many small subprojects based on dependencies. For example code related to loading of configuration files via PureConfig lives in subproject named sst-pureconfig and code related to http4s server implemented using Blaze lives in subproject named sst-http4s-server-blaze. There are also subprojects that implement interoperability between usually two dependencies. For example we want to configure our HTTP server using PureConfig so definition of implicit ConfigReader instances lives in subproject named sst-http4s-server-blaze-pureconfig. Or to give another example, monitoring of HTTP server using Micrometer lives in subproject named sst-http4s-server-micrometer. Note that such subproject depends on APIs of both http4s server and Micrometer but it does not depend on concrete implementation which allows you to choose any http4s implementation (Blaze, …) and any Micrometer implementation (JMX, StatsD, …)."
    } ,    
    {
      "title": "Subprojects",
      "url": "/scala-server-toolkit/subprojects.html",
      "content": "Subprojects Scala Server Toolkit provides many subprojects for different well-known libraries."
    }    
  ];

  idx = lunr(function () {
    this.ref("title");
    this.field("content");

    docs.forEach(function (doc) {
      this.add(doc);
    }, this);
  });

  docs.forEach(function (doc) {
    docMap.set(doc.title, doc.url);
  });
}

// The onkeypress handler for search functionality
function searchOnKeyDown(e) {
  const keyCode = e.keyCode;
  const parent = e.target.parentElement;
  const isSearchBar = e.target.id === "search-bar";
  const isSearchResult = parent ? parent.id.startsWith("result-") : false;
  const isSearchBarOrResult = isSearchBar || isSearchResult;

  if (keyCode === 40 && isSearchBarOrResult) {
    // On 'down', try to navigate down the search results
    e.preventDefault();
    e.stopPropagation();
    selectDown(e);
  } else if (keyCode === 38 && isSearchBarOrResult) {
    // On 'up', try to navigate up the search results
    e.preventDefault();
    e.stopPropagation();
    selectUp(e);
  } else if (keyCode === 27 && isSearchBarOrResult) {
    // On 'ESC', close the search dropdown
    e.preventDefault();
    e.stopPropagation();
    closeDropdownSearch(e);
  }
}

// Search is only done on key-up so that the search terms are properly propagated
function searchOnKeyUp(e) {
  // Filter out up, down, esc keys
  const keyCode = e.keyCode;
  const cannotBe = [40, 38, 27];
  const isSearchBar = e.target.id === "search-bar";
  const keyIsNotWrong = !cannotBe.includes(keyCode);
  if (isSearchBar && keyIsNotWrong) {
    // Try to run a search
    runSearch(e);
  }
}

// Move the cursor up the search list
function selectUp(e) {
  if (e.target.parentElement.id.startsWith("result-")) {
    const index = parseInt(e.target.parentElement.id.substring(7));
    if (!isNaN(index) && (index > 0)) {
      const nextIndexStr = "result-" + (index - 1);
      const querySel = "li[id$='" + nextIndexStr + "'";
      const nextResult = document.querySelector(querySel);
      if (nextResult) {
        nextResult.firstChild.focus();
      }
    }
  }
}

// Move the cursor down the search list
function selectDown(e) {
  if (e.target.id === "search-bar") {
    const firstResult = document.querySelector("li[id$='result-0']");
    if (firstResult) {
      firstResult.firstChild.focus();
    }
  } else if (e.target.parentElement.id.startsWith("result-")) {
    const index = parseInt(e.target.parentElement.id.substring(7));
    if (!isNaN(index)) {
      const nextIndexStr = "result-" + (index + 1);
      const querySel = "li[id$='" + nextIndexStr + "'";
      const nextResult = document.querySelector(querySel);
      if (nextResult) {
        nextResult.firstChild.focus();
      }
    }
  }
}

// Search for whatever the user has typed so far
function runSearch(e) {
  if (e.target.value === "") {
    // On empty string, remove all search results
    // Otherwise this may show all results as everything is a "match"
    applySearchResults([]);
  } else {
    const tokens = e.target.value.split(" ");
    const moddedTokens = tokens.map(function (token) {
      // "*" + token + "*"
      return token;
    })
    const searchTerm = moddedTokens.join(" ");
    const searchResults = idx.search(searchTerm);
    const mapResults = searchResults.map(function (result) {
      const resultUrl = docMap.get(result.ref);
      return { name: result.ref, url: resultUrl };
    })

    applySearchResults(mapResults);
  }

}

// After a search, modify the search dropdown to contain the search results
function applySearchResults(results) {
  const dropdown = document.querySelector("div[id$='search-dropdown'] > .dropdown-content.show");
  if (dropdown) {
    //Remove each child
    while (dropdown.firstChild) {
      dropdown.removeChild(dropdown.firstChild);
    }

    //Add each result as an element in the list
    results.forEach(function (result, i) {
      const elem = document.createElement("li");
      elem.setAttribute("class", "dropdown-item");
      elem.setAttribute("id", "result-" + i);

      const elemLink = document.createElement("a");
      elemLink.setAttribute("title", result.name);
      elemLink.setAttribute("href", result.url);
      elemLink.setAttribute("class", "dropdown-item-link");

      const elemLinkText = document.createElement("span");
      elemLinkText.setAttribute("class", "dropdown-item-link-text");
      elemLinkText.innerHTML = result.name;

      elemLink.appendChild(elemLinkText);
      elem.appendChild(elemLink);
      dropdown.appendChild(elem);
    });
  }
}

// Close the dropdown if the user clicks (only) outside of it
function closeDropdownSearch(e) {
  // Check if where we're clicking is the search dropdown
  if (e.target.id !== "search-bar") {
    const dropdown = document.querySelector("div[id$='search-dropdown'] > .dropdown-content.show");
    if (dropdown) {
      dropdown.classList.remove("show");
      document.documentElement.removeEventListener("click", closeDropdownSearch);
    }
  }
}
