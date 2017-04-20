import sbt.Keys._

organization in ThisBuild := "sample.maninder"
lagomCassandraEnabled in ThisBuild := false

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.11.8"

// SCALA SUPPORT: Remove the line below
EclipseKeys.projectFlavor in Global := EclipseProjectFlavor.Java

lazy val serviceAPI = project("service-api")
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies += lagomJavadslApi,
    libraryDependencies += lagomJavadslServer,
    libraryDependencies ++= Seq(
      "junit" % "junit" % "4.11",
      "org.slf4j" % "slf4j-log4j12" % "1.7.5"
    )
  )


lazy val serviceIMPL = project("service-impl")
  .enablePlugins(LagomJava)
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      //lagomJavadslPersistenceCassandra,
      lagomJavadslTestKit
    )
  )
  .settings(javaOptions  ++= Seq("-Dconfig.resource=application.conf","-Dlogger.resource=logback.xml"))
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(serviceAPI)

def project(id: String) = Project(id, base = file(id))
  .settings(javacOptions in compile ++= Seq("-encoding", "UTF-8", "-source", "1.8", "-target", "1.8", "-Xlint:unchecked", "-Xlint:deprecation"))
  .settings(jacksonParameterNamesJavacSettings: _*) // applying it to every project even if not strictly needed.


// See https://github.com/FasterXML/jackson-module-parameter-names
lazy val jacksonParameterNamesJavacSettings = Seq(
  javacOptions in compile += "-parameters"
)

// do not delete database files on start
lagomCassandraCleanOnStart in ThisBuild := false

// Kafka can be disabled until we need it
lagomKafkaEnabled in ThisBuild := false
lagomServiceGatewayPort in ThisBuild := 9010
lagomServiceLocatorPort in ThisBuild := 9005
lagomServicesPortRange in ThisBuild := PortRange(40000, 45000)
licenses in ThisBuild := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"))

fork in run := true
