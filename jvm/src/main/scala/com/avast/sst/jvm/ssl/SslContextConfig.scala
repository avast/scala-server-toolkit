package com.avast.sst.jvm.ssl

import com.avast.sst.jvm.ssl.Protocol.TLS
import com.avast.sst.jvm.ssl.Protocol.TLS

final case class SslContextConfig(protocol: Protocol = TLS,
                                  keystore: Option[KeyStoreConfig] = None,
                                  truststore: Option[KeyStoreConfig] = None,
                                  loadSystemKeyManagers: Boolean = true,
                                  loadSystemTrustManagers: Boolean = true)
