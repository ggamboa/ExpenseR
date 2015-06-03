name := """ExpenseR"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaJpa,
  javaEbean,
  cache,
  javaWs
)

javaHome := Some(file("C:\\Program Files\\Java\\jdk1.7.0_75"))