# starter 

## SBT config

Add proxy repository config to your sbt config. Basically, copy the `.ci-repositories` to 
`~/.sbt/repositories`.

Add the nexus credentials to your local sbt config:

set the following env var
```bash
export SBT_CREDENTIALS="$HOME/.ivy2/.credentials"
```
and the file `~/.ivy2/.credentials` with the following content

```properties
[repositories]
  local
  nexus: https://mynexus/nexus3/repository/maven-custom
```

When running in IntelliJ you have to make credentials available to the jvm using a jvm arg:

```bash
-Dsbt.boot.credentials="$HOME/.ivy2/credentials"
```

## Build

```bash
./sbt clean compile
```
