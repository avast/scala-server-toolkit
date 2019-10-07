package com.avast.sst.pureconfig.implicits

import cats.syntax.either._
import com.avast.sst.http4s.Http4sBlazeClientConfig
import org.http4s.client.blaze.ParserMode
import org.http4s.headers.`User-Agent`
import pureconfig.ConfigReader
import pureconfig.error.CannotConvert
import pureconfig.generic.semiauto.deriveReader

object Http4sBlazeClient {

  import JvmSsl._

  implicit val userAgentReader: ConfigReader[`User-Agent`] = ConfigReader[String].emap { value =>
    `User-Agent`.parse(value).leftMap { parseFailure =>
      CannotConvert(value, "User-Agent HTTP header", parseFailure.message)
    }
  }

  implicit val parserModeReader: ConfigReader[ParserMode] = ConfigReader[String].map(_.toLowerCase).emap {
    case "strict"  => Right(ParserMode.Strict)
    case "lenient" => Right(ParserMode.Lenient)
    case badValue  => Left(CannotConvert(badValue, "ParserMode", "strict|lenient"))
  }

  implicit val http4sClientConfigReader: ConfigReader[Http4sBlazeClientConfig] = deriveReader

}
