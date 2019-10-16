# Contributing

Thank you for your interest in contributing to Scala Server Toolkit. All contributions are welcome if they are consistent with the goals
and guidelines of the project.

It is better to open an issue in the project and discuss your intention with project maintainers before you actually start implementing 
something. They can also give you tips on how to go about implementing it.

## How to Contribute

Please read the [First Contributions Guide](https://github.com/firstcontributions/first-contributions/blob/master/README.md) for general
information about contribution to OSS projects.

## Build

The project can be built and tested using simple SBT command:

```bash
sbt test
```

There is also an extra command called `check` which you should use to check your code before you submit a PR:

```bash
sbt check
```

Some of the reported problems can be automatically fixed by `fix`:

```bash
sbt fix
```

## Documentation

The project contains [compiled documentation](https://scalameta.org/mdoc) which is located in [example/src/main/mdoc](example/src/main/mdoc).
Please do update it with your changes and recompile it to check that everything is fine using the following command:

```bash
sbt example/mdoc
```

You should definitely recompile [mdoc] documentation  

## Conventional Commits

The project uses [Conventional Commits](https://www.conventionalcommits.org) specification to have clear Git history and help with
semantic versioning. Please read the specification and follow it (or look at already existing history for inspiration) if you commit into
the project.
