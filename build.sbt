name := "gatling-grpc"

organization := "com.github.phisgr"
version := "0.4.0"

scalaVersion := "2.12.6"

val gatlingVersion = "3.1.3"

scalacOptions ++= Seq(
  "-language:existentials",
  "-language:implicitConversions",
)

inConfig(Test)(sbtprotoc.ProtocPlugin.protobufConfigSettings)
PB.targets in Test := Seq(
  scalapb.gen() -> (sourceManaged in Test).value
)

libraryDependencies ++= Seq(
  "io.grpc" % "grpc-netty" % scalapb.compiler.Version.grpcJavaVersion,
  "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",
  "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion,
  "io.gatling" % "gatling-core" % gatlingVersion
)

libraryDependencies ++= Seq(
  "io.gatling.highcharts" % "gatling-charts-highcharts" % gatlingVersion % "test",
  "io.gatling" % "gatling-test-framework" % gatlingVersion % "test"
)
enablePlugins(GatlingPlugin)

import xerial.sbt.Sonatype._
publishTo := SonatypeKeys.sonatypePublishTo.value
publishMavenStyle := true

licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))
sonatypeProjectHosting := Some(GitHubHosting("phiSgr", "gatling-grpc", "phisgr@gmail.com"))

lazy val root = project in file(".")

lazy val bench = (project in file("bench"))
  .dependsOn(root)
  .enablePlugins(JmhPlugin)
  .settings(
    PB.targets in Compile := Seq(
      scalapb.gen() -> (sourceManaged in Compile).value
    )
  )
