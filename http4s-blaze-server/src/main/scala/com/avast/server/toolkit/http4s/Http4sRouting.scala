package com.avast.server.toolkit.http4s

import cats.Monad
import cats.data.{Kleisli, OptionT}
import org.http4s.syntax.kleisli._
import org.http4s.{HttpApp, HttpRoutes, Request}

import scala.language.higherKinds

object Http4sRouting {

  /** Makes [[org.http4s.HttpApp]] from [[org.http4s.HttpRoutes]] */
  def make[F[_]: Monad](routes: HttpRoutes[F], more: HttpRoutes[F]*): HttpApp[F] = {
    val semigroup = Kleisli.catsDataSemigroupKForKleisli[OptionT[F, *], Request[F]](OptionT.catsDataSemigroupKForOptionT[F])

    more
      .foldLeft[HttpRoutes[F]](routes) {
        case (acc, moreRoutes) =>
          semigroup.combineK(acc, moreRoutes)
      }
      .orNotFound
  }

}
