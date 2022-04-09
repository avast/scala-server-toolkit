package com.avast.sst.lettuce.pureconfig

import cats.syntax.either.*
import com.avast.sst.lettuce.LettuceConfig
import com.avast.sst.lettuce.LettuceConfig.{SocketOptions, SslOptions, TimeoutOptions}
import io.lettuce.core.ClientOptions.DisconnectedBehavior
import io.lettuce.core.protocol.ProtocolVersion
import pureconfig.ConfigReader
import pureconfig.error.CannotConvert
import pureconfig.generic.derivation.default.*

import java.nio.charset.Charset

trait ConfigReaders {

  implicit val lettuceDisconnectedBehaviorConfigReader: ConfigReader[DisconnectedBehavior] = ConfigReader.stringConfigReader.emap {
    case "DEFAULT"         => DisconnectedBehavior.DEFAULT.asRight
    case "ACCEPT_COMMANDS" => DisconnectedBehavior.ACCEPT_COMMANDS.asRight
    case "REJECT_COMMANDS" => DisconnectedBehavior.REJECT_COMMANDS.asRight
    case unknown =>
      CannotConvert(
        unknown,
        "DisconnectedBehavior",
        s"Unknown enum value: ${DisconnectedBehavior.values().map(_.name()).mkString("|")}"
      ).asLeft
  }

  implicit val lettuceProtocolVersionConfigReader: ConfigReader[ProtocolVersion] = ConfigReader.stringConfigReader.emap {
    case "RESP2" => ProtocolVersion.RESP2.asRight
    case "RESP3" => ProtocolVersion.RESP3.asRight
    case unknown =>
      CannotConvert(
        unknown,
        "ProtocolVersion",
        s"Unknown enum value: ${ProtocolVersion.values().map(_.name()).mkString("|")}"
      ).asLeft
  }

  implicit val lettuceCharsetConfigReader: ConfigReader[Charset] = ConfigReader.stringConfigReader.emap { charset =>
    Either.catchNonFatal(Charset.forName(charset)).leftMap(ex => CannotConvert(charset, "java.nio.Charset", ex.getMessage))
  }

  implicit val lettuceSocketOptionsReader: ConfigReader[SocketOptions] = ConfigReader.derived

  implicit val lettuceSslOptionsReader: ConfigReader[SslOptions] = ConfigReader.derived

  implicit val lettuceTimeoutOptionsReader: ConfigReader[TimeoutOptions] = ConfigReader.derived

  implicit val lettuceConfigReader: ConfigReader[LettuceConfig] = ConfigReader.derived

}
