name := "finagleThriftPlayground"

scalaVersion := "2.11.8"

version := "0.1.0"

resolvers ++= Seq(
  "twttr" at "http://maven.twttr.com/"
)

scroogeLanguage := "java"

val finagleVersion = "6.34.0"

libraryDependencies ++= Seq(
  "com.twitter" %% "finagle-core" % finagleVersion,
  "com.twitter" %% "finagle-thrift" % finagleVersion,
  "com.twitter" %% "finagle-thriftmux" % finagleVersion,
  "com.twitter" %% "scrooge-core" % "4.7.1-SNAPSHOT",
  "org.apache.thrift" % "libthrift" % "0.6.1",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test"
)
