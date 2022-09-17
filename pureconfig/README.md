# Toggle
This type class is basically an Option with the possibility to keep the whole configuration in place and change a special boolean flag only. Please check the example below:

```
case class Config(myService: Toggle[ServiceConfig])
case class ServiceConfig(url: String, timeout: Duration)
```

```
my_service {
    enabled = false
    url = "http://www.example.com/v1/some-endpoint"
    timeout = 5s
}
```

The `enabled` boolean flag is added automatically. Value `false` is translated to case object `Disabled` (None) and value `true` is translated to case class`Enabled[ServiceConfig]` (Some)
