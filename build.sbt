name := "scalabot"

version := "1.0"

scalaVersion := "2.11.7"

resolvers += "scalac repo" at "https://raw.githubusercontent.com/ScalaConsultants/mvn-repo/master/"

libraryDependencies ++= Seq(
  "io.scalac" %% "slack-scala-bot-core" % "0.2.1",
  "org.scalaj" %% "scalaj-http" % "2.2.1"
)