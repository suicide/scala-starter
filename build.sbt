import com.typesafe.sbt.packager.docker.DockerVersion

val versions = new {
//  val `kubernetes-client` = "7.0.0"

  val scala = "2.13.1"
}

val dependencies = {
  import versions._
  new {
//    val `kubernetes-client` = "io.kubernetes" % "client-java" % versions.`kubernetes-client`
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
    )
  },
  dockerVersion := Some(DockerVersion(19, 0, 5, Some("ce"))),
  dockerBaseImage := "openjdk:8",
)

lazy val core = Project(
  id = "myapp-core",
  base = file("core")
)
  .settings(
    libraryDependencies ++= {
      import dependencies._
      Seq(
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
 //       `kubernetes-client`,
      )
    },
    crossPaths := false,

    dockerExposedPorts ++= Seq(8080),
  )
  .settings(commonSettings: _*)
  .enablePlugins(ReproducibleBuildsPlugin, JavaAppPackaging, UniversalDeployPlugin, DockerPlugin)
  .dependsOn(core)

lazy val myapp = (project in file("."))
  .settings(commonSettings: _*)
  .aggregate(core, app)
