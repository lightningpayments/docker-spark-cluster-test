// -------------------------------------------------------------------------------------------------
// Package configuration
// -------------------------------------------------------------------------------------------------
name := "DockerSparkTest"
organization := "de.lightningpayments"
organizationHomepage := Option(url("https://www.lightning-payments.de"))
organizationName := "lightningpayments"
version := "2.0.0"

// -------------------------------------------------------------------------------------------------
// Application
// -------------------------------------------------------------------------------------------------
addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.0" cross CrossVersion.full)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs@_*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

// -------------------------------------------------------------------------------------------------
// Scala compiler settings
// -------------------------------------------------------------------------------------------------
lazy val root = project
  .in(file("."))
  .settings(commonSettings)
  .dependsOn(RootProject(uri("https://github.com/lightningpayments/apache-spark-zio-commons.git")))


lazy val commonSettings = Seq(
  envVars in Test :=
    Map(
      "SPARK_MASTER" -> "local[*]"
    ),
  fork in Test := true
)
scalacOptions in run ++= Seq(
  "-Dlog4j.debug=true",
  "-Dlog4j.configuration=log4j.properties"
)
outputStrategy := Some(StdoutOutput)
scalacOptions in Compile ++= Seq("-deprecation", "-explaintypes", "-feature", "-unchecked")
Compile / scalacOptions ++= {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, n)) if n == 12 => Seq("-Ypartial-unification")
    case _ => Seq.empty
  }
}
crossScalaVersions := Seq("2.12.10")
scalaVersion := crossScalaVersions.value.head
testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oSD")
ThisBuild / scalacOptions += "-P:kind-projector:underscore-placeholders"
// -------------------------------------------------------------------------------------------------
// Publisher
// -------------------------------------------------------------------------------------------------
// credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

// -------------------------------------------------------------------------------------------------
// Library dependencies
// -------------------------------------------------------------------------------------------------
libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-jdbc" % "2.8.2",
  "log4j" % "log4j" % "1.2.17",
  "de.lightningpayments" %% "commons-spark" % "3.0.0",
  "org.http4s" %% "http4s-blaze-server" % "0.23.6",
  "org.http4s" %% "http4s-circe" % "0.23.6",
  "org.http4s" %% "http4s-dsl" % "0.23.6",
  "com.h2database" % "h2" % "1.4.200" % "test",
  "org.mockito" %% "mockito-scala-scalatest" % "1.14.8" % "test",
  "org.mockito" % "mockito-inline" % "3.3.3" % "test",
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % "test",
  "org.scalatestplus" %% "scalacheck-1-14" % "3.2.0.0" % "test"
)

// -------------------------------------------------------------------------------------------------
// Scapegoat Configuration (static code analysis)
// -------------------------------------------------------------------------------------------------
scapegoatConsoleOutput := true
scapegoatIgnoredFiles := Seq.empty
scapegoatVersion in ThisBuild := "1.4.5"
scapegoatDisabledInspections := Seq("VariableShadowing")

// -------------------------------------------------------------------------------------------------
// Scoverage Configuration (code coverage)
// -------------------------------------------------------------------------------------------------
coverageFailOnMinimum := true
coverageHighlighting := true
coverageMinimum := 100
coverageExcludedPackages := """<empty>;..*Module.*"""

// -------------------------------------------------------------------------------------------------
// Scalastyle Configuration (check style)
// -------------------------------------------------------------------------------------------------
scalastyleFailOnError := true

unmanagedResourceDirectories in Compile += baseDirectory.value / "conf"