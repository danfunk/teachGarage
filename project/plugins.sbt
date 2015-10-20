// This recompiles and runs our microservice every time the code in files change.
// (Must be initialized inside of build.sbt)
addSbtPlugin("io.spray" % "sbt-revolver" % "0.7.2")

// Allows for deployment of the service as a single .jar file.
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.12.0")

