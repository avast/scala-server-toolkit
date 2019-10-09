package com.avast.sst.jvm.ssl

import java.nio.file.Path

final case class KeyStoreConfig(keystoreType: KeyStoreType, path: Path, password: String, keyPassword: Option[String])
