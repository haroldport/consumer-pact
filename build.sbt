name := "consumer-pact"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= {
  val swaggerRequestValidatorVersion = "1.0.6"
  Seq(
    "com.atlassian.oai" % "swagger-request-validator-core" % swaggerRequestValidatorVersion,
    "com.atlassian.oai" % "swagger-request-validator-pact" % swaggerRequestValidatorVersion,
    "com.atlassian.oai" % "swagger-request-validator-wiremock" % swaggerRequestValidatorVersion,
    "com.atlassian.oai" % "swagger-request-validator-restassured" % swaggerRequestValidatorVersion,
    "org.json4s"     %% "json4s-native"       % "3.3.0",
    "org.scalatest"  %% "scalatest"           % "2.2.1" % "test",
    "com.itv"        %% "scalapact-scalatest" % "2.0.0" % "test",
    "org.scalaj"     %% "scalaj-http"         % "1.1.5"
  )
}
    