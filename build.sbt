// -------------------------------------------------------------------------------------------------
// Package configuration
// -------------------------------------------------------------------------------------------------
name := "DockerSparkTest"
organization := "de.lightningpayments"
organizationHomepage := Option(url("https://www.lightning-payments.de"))
organizationName := "lightningpayments"
version := "1.0.0"

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
// resolvers += "Commons Spark Repository" at "https://github.com/lightningpayments/apache-spark-zio-commons/releases/tag/2.0.1"

// -------------------------------------------------------------------------------------------------
// Library dependencies
// -------------------------------------------------------------------------------------------------
val sparkVers = "3.1.2"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-jdbc" % "2.8.2",
  "log4j" % "log4j" % "1.2.17",
  "de.commons" %% "commons-spark" % "2.0.1",
  "org.typelevel" %% "cats-core" % "2.6.1",
  "org.typelevel" %% "cats-effect" % "3.2.0",
  "dev.zio" %% "zio-interop-cats" % "2.1.4.0",
  "dev.zio" %% "zio" % "1.0.1",
  "org.apache.spark" %% "spark-core" % sparkVers % "compile",
  "org.apache.spark" %% "spark-sql" % sparkVers % "compile",
  "org.apache.hadoop" % "hadoop-common" % "3.2.0",
  "mysql" % "mysql-connector-java" % "8.0.21",
  "org.postgresql" % "postgresql" % "42.2.23",
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