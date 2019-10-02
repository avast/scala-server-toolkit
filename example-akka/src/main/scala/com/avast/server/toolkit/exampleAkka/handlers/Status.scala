package com.avast.server.toolkit.exampleAkka.handlers

import akka.http.scaladsl.server.{Directives, Route}

class Status() extends Directives {
  val route: Route =
    pathPrefix("status") {
      pathEndOrSingleSlash {
        get {
          complete(returnMessage())
        }
      }
    }

  private def returnMessage() = "Running"

}
