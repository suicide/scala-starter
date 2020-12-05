import com.typesafe.sbt.packager.docker.DockerVersion

val versions = new {
  val cats = "2.3.0"
  val `cats-effect` = "2.3.0"

  val logback = "1.2.3"
  val log4cats = "1.1.1"

  val scalatest = "3.2.3"
  val scalatic = "3.2.2"

  val scala = "2.13.4"
}

val dependencies = {
  import versions._
  new {
    val `cats-core` = "org.typelevel" %% "cats-core" % cats
    val `cats-effect` = "org.typelevel" %% "cats-effect" % versions.`cats-effect`

    val `log4cats-slf4j` = "io.chrisdavenport" %% "log4cats-slf4j" % log4cats
    val `logback-classic` = "ch.qos.logback" % "logback-classic" % logback

    val scalatest = "org.scalatest" %% "scalatest" % versions.scalatest % "test"
    val scalatic = "org.scalactic" %% "scalactic" % versions.scalatic
  }
}

val commonSettings = Seq(
  organization := "com.myapp",
  version := "1.0.0-SNAPSHOT",
  scalaVersion := versions.scala,
  scalacOptions := Seq("-unchecked", "-deprecation", "-feature", "-encoding", "utf8"),
  dependencyOverrides ++= {
    import dependencies._
    Seq(
      scalatest,
    )
  },
  dockerVersion := Some(DockerVersion(19, 3, 13, Some("ce"))),
  dockerBaseImage := "openjdk:11",
)

lazy val core = Project(
  id = "myapp-core",
  base = file("core")
)
  .settings(
    libraryDependencies ++= {
      import dependencies._
      Seq(
        `logback-classic`,
      )
    },
    unusedCompileDependenciesFilter -= moduleFilter("ch.qos.logback", "logback-classic"),
  )
  .settings(commonSettings: _*)
  .enablePlugins(ReproducibleBuildsPlugin)


lazy val app = Project(
  id = "myapp-app",
  base = file("app")
)
  .settings(
    libraryDependencies ++= {
      import dependencies._
      Seq(
      )
    },
    crossPaths := false,

    dockerExposedPorts ++= Seq(8080),
  )
  .settings(commonSettings: _*)
  .enablePlugins(ReproducibleBuildsPlugin, JavaAppPackaging, UniversalDeployPlugin, DockerPlugin)
  .dependsOn(core)

lazy val root = (project in file("."))
  .settings(commonSettings: _*)
  .aggregate(core, app)
