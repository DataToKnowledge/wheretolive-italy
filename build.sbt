
lazy val commonSettings = Seq(
  organization := "eu.citytoday",
  scalaVersion := "2.12.1",
  fork in run := true,
  cancelable in Global := true,
  scalaVersion in ThisBuild := scalaVersion.value,
  parallelExecution in Test := false,

  scalacOptions ++= Seq(
    "-feature",
    "-deprecation",
    "-language:postfixOps",
    "-language:implicitConversions",
    "-unchecked",
    "-encoding",
    "utf8"
  ),

  javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),

  resolvers ++= Seq(
    Resolver.jcenterRepo,
    "jboss third party" at "https://repository.jboss.org/nexus/content/repositories/thirdparty-releases/",
    Resolver.bintrayRepo("cakesolutions", "maven"),
    Resolver.sonatypeRepo("releases")
  )
)

val akkaVersion = "2.4.17"

lazy val dependencies = Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
  "com.typesafe.akka" %% "akka-stream-kafka" % "0.13",
  "org.typelevel" %% "cats" % "0.9.0",
  "com.iheart" %% "ficus" % "1.4.0",
  "org.scalaj" %% "scalaj-http" % "2.3.0",
  "org.jsoup" % "jsoup" % "1.10.2",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
  "com.rometools" % "rome" % "1.7.1",
  "com.intenthq" % "gander_2.11" % "1.3.1",
  "com.trueaccord.scalapb" %% "scalapb-runtime" % com.trueaccord.scalapb.compiler.Version.scalapbVersion % "protobuf"
)


lazy val root = project.in(file("."))
  .settings(
    name := "wheretolive-italy",
    version := "1.0",
    libraryDependencies ++= dependencies,
    PB.targets in Compile := Seq(
      scalapb.gen(
        flatPackage = true
      ) -> (sourceManaged in Compile).value
    )
  )
  .settings(commonSettings)


    