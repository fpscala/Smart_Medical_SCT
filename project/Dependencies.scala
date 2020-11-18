import sbt._
object Dependencies {

  object Versions {
    val cats = "2.1.1"
    val fs2 = "2.4.0"
    val akka = "2.6.10"
    val doobie = "0.9.0"
    val scalaLogging = "3.9.2"
    val logBack = "1.2.3"
    val logOver = "1.7.30"
    val janino = "3.1.2"
    val slick = "5.0.0"
    val playJson = "2.9.1"
    val slickPg = "0.19.2"
    val h2 = "1.4.200"
    val openTable = "0.10.0"
    val postgreSql = "42.2.5"
    val jsonJoda = "2.9.0"
    val pureConfig = "0.13.0"
    val scalaTestPlus = "5.1.0"
    val playMailer = "8.0.1"
    val playWebjars = "2.8.0-1"
    val jquery = "3.5.1"
    val bootstrap = "4.5.0"
    val reactJs = "16.5.2"
    val typeReact = "15.0.34"
    val koMapping = "2.4.1"
    val knockout = "3.3.0"
    val toastr = "2.1.2"
    val fontAwesome = "5.14.0"
    val jQueryUI = "1.11.4"
    val jQueryFileUpload = "9.10.1"
    val bootstrapSelect = "1.13.11"
    val popper = "1.12.9"
    val datepicker = "1.9.0"
    val momentJs = "2.8.1"
    val jQueryMask = "1.14.12"
  }

  object Libraries {
    val cats = "org.typelevel" %% "cats-core" % Versions.cats
    val fs2Core = "co.fs2" %% "fs2-core" % Versions.fs2
    val fs2IO = "co.fs2" %% "fs2-io" % Versions.fs2
    val fs2Reactive = "co.fs2" %% "fs2-reactive-streams" % Versions.fs2
    val fs2Experimental = "co.fs2" %% "fs2-experimental" % Versions.fs2
    val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % Versions.scalaLogging
    val logOver = "org.slf4j" % "log4j-over-slf4j" % Versions.logOver
    val janino = "org.codehaus.janino" % "janino" % Versions.janino
    val jsonJoda = "com.typesafe.play" %% "play-json-joda" % Versions.jsonJoda
    val pureConfig = "com.github.pureconfig" %% "pureconfig" % Versions.pureConfig
    val scalaTestPlus = "org.scalatestplus.play" %% "scalatestplus-play" % Versions.scalaTestPlus % Test
    val scalaJHttp = "org.scalaj" %% "scalaj-http" % "2.4.2"
    val playMailerLibs = Seq(
      "com.typesafe.play" %% "play-mailer" % Versions.playMailer,
      "com.typesafe.play" %% "play-mailer-guice" % Versions.playMailer
    )
    val logBackLibs = Seq(
      "ch.qos.logback" % "logback-core" % Versions.logBack,
      "ch.qos.logback" % "logback-classic" % Versions.logBack % Test
    )
    val dbLibs = Seq(
      "com.h2database" % "h2" % Versions.h2 % Test,
      "com.opentable.components" % "otj-pg-embedded" % Versions.openTable % Test,
      "org.postgresql" % "postgresql" % Versions.postgreSql
    )
    val slick = Seq(
      "com.typesafe.play" %% "play-json" % Versions.playJson,
      "com.typesafe.play" %% "play-slick" % Versions.slick,
      "com.typesafe.play" %% "play-slick-evolutions" % Versions.slick,
      "com.github.tminglei" %% "slick-pg" % Versions.slickPg,
      "com.github.tminglei" %% "slick-pg_play-json" % Versions.slickPg
    )
    val akka = Seq(
      "com.typesafe.akka" %% "akka-actor" % Versions.akka,
      "com.typesafe.akka" %% "akka-remote" % Versions.akka,
      "com.typesafe.akka" %% "akka-cluster-sharding-typed" % Versions.akka,
      "com.typesafe.akka" %% "akka-actor-typed" % Versions.akka,
      "com.typesafe.akka" %% "akka-serialization-jackson" % Versions.akka
    )
    val doobieLibs = Seq(
      "org.tpolecat" %% "doobie-core" % Versions.doobie,
      "org.tpolecat" %% "doobie-h2" % Versions.doobie, // H2 driver 1.4.200 + type mappings.
      "org.tpolecat" %% "doobie-hikari" % Versions.doobie, // HikariCP transactor.
      "org.tpolecat" %% "doobie-postgres" % Versions.doobie, // Postgres driver 42.2.12 + type mappings.
      "org.tpolecat" %% "doobie-quill" % Versions.doobie, // Support for Quill 3.5.1
      "org.tpolecat" %% "doobie-specs2" % Versions.doobie % Test, // Specs2 support for typechecking statements.
      "org.tpolecat" %% "doobie-scalatest" % Versions.doobie % Test // ScalaTest support for typechecking statements.
    )
    val webjarsLibs = Seq(
      "org.webjars" %% "webjars-play" % Versions.playWebjars,
      "org.webjars" % "jquery" % Versions.jquery,
      "org.webjars" % "bootstrap" % Versions.bootstrap,
      "org.webjars.bower" % "knockout-mapping" % Versions.koMapping,
      "org.webjars" % "knockout" % Versions.knockout,
      "org.webjars" % "toastr" % Versions.toastr,
      "org.webjars" % "font-awesome" % Versions.fontAwesome,
      "org.webjars" % "jquery-ui-src" % Versions.jQueryUI,
      "org.webjars" % "jquery-file-upload" % Versions.jQueryFileUpload,
      "org.webjars" % "bootstrap-select" % Versions.bootstrapSelect % "runtime",
      "org.webjars.bower" % "popper.js" % Versions.popper,
      "org.webjars" % "bootstrap-datepicker" % Versions.datepicker,
      "org.webjars" % "momentjs" % Versions.momentJs,
      "org.webjars.bower" % "jquery-mask-plugin" % Versions.jQueryMask
    )
  }
  val rootDependencies: Seq[ModuleID] = Seq(
    Libraries.cats,
    Libraries.fs2Core,
    Libraries.fs2IO,
    Libraries.fs2Reactive,
    Libraries.fs2Experimental,
    Libraries.scalaLogging,
    Libraries.logOver,
    Libraries.janino,
    Libraries.jsonJoda,
    Libraries.pureConfig,
    Libraries.scalaJHttp,
    Libraries.scalaTestPlus
  ) ++
    Libraries.playMailerLibs ++
    Libraries.logBackLibs ++
    Libraries.dbLibs ++
    Libraries.akka ++
    Libraries.doobieLibs ++
    Libraries.slick ++
    Libraries.webjarsLibs
}
