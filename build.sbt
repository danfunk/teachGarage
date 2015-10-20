val akkaVer     = "2.3.12"
val streamsVer  = "1.0"
val testVer     = "2.2.5"

lazy val root = (project in file(".")).
  settings(
    name         := "garage-micro-service",
    organization := "com.parkme",
    version      := "1.0",
    scalaVersion := "2.11.7",
    libraryDependencies ++= {
      Seq(
        "com.typesafe.akka" %% "akka-actor" % akkaVer,
        "com.typesafe.akka" %% "akka-stream-experimental" % streamsVer,
        "com.typesafe.akka" %% "akka-http-core-experimental" % streamsVer,
        "com.typesafe.akka" %% "akka-http-experimental" % streamsVer,
        "com.typesafe.akka" %% "akka-http-spray-json-experimental" % streamsVer,
        "com.typesafe.akka" %% "akka-http-testkit-experimental" % streamsVer,
        "org.scalatest" %% "scalatest" % testVer % "test"
      )
    }
  )

// This shows up as an error in Intellij 14, but
// it works ok anyway, and allows an auto-refresh
// as files change.
Revolver.settings
