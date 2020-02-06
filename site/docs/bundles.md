---
layout: docs
title: "Bundles"
position: 3
---

# Bundles

Having many small and independent subprojects is great but in practice everyone wants to use certain combination of dependencies and does not
want to worry about many small dependencies. There are "bundles" for such use case - either the ones provided by this project or custom
ones created by the user.

One of the main decisions (dependency-wise) is to choose the effect data type. This project does not force you into specific data type and
supports both [ZIO](https://zio.dev) and [Monix](https://monix.io) out-of-the-box. So there are two main bundles one for each effect data
type that also bring in http4s server/client (Blaze), PureConfig and Micrometer.

Unless you have specific needs take one of these bundles and write your server application using them - it will be the simplest way.
