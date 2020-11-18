import Dependencies.rootDependencies

name := "Smart_Medical_SCT"

includeFilter in(Assets, LessKeys.less) := "*.less"
excludeFilter in(Assets, LessKeys.less) := "_*.less"

version := "1.1"

lazy val `smart_medical_sct` = (project in file(".")).enablePlugins(PlayScala,PlayAkkaHttp2Support)

lazy val scala212v = "2.12.8"
lazy val scala213v = "2.13.0"
lazy val supportedScalaVersions = List(scala212v, scala213v)

crossScalaVersions := supportedScalaVersions

resolvers ++= Seq(
  "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
  "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/",
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots"),
)

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3")

JsEngineKeys.engineType := JsEngineKeys.EngineType.Node

scalaVersion := scala213v

scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked",
  "-Xfuture"
)

libraryDependencies ++= rootDependencies ++ Seq(ehcache, ws, specs2 % Test, guice)