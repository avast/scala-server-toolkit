package com.avast.sst.datastax.ssl

import com.avast.sst.datastax.ssl.Protocol.TLS

final case class SslContextConfig(protocol: Protocol = TLS,
                                  keystore: Option[KeyStoreConfig] = None,
                                  truststore: Option[KeyStoreConfig] = None,
                                  loadSystemKeyManagers: Boolean = true,
                                  loadSystemTrustManagers: Boolean = true)
