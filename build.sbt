scalaVersion := "3.3.1"

enablePlugins(ScalaNativePlugin)
enablePlugins(ScalaNativeJUnitPlugin)

organization := "io.github.quelgar"

name := "scala-uv"

version := "0.0.1"

ThisBuild / versionScheme := Some("early-semver")

// set to Debug for compilation details (Info is default)
logLevel := Level.Info

// import to add Scala Native options
import scala.scalanative.build._

// defaults set with common options shown
nativeConfig ~= { c =>
  c.withLTO(LTO.none) // thin
    .withMode(Mode.debug) // releaseFast
    .withGC(GC.immix) // commix
}

scalacOptions ++= Seq(
  "-new-syntax",
  "-no-indent",
  "-Wvalue-discard",
  "-Wunused:all",
  "-Werror",
  "-deprecation"
)

publishTo := sonatypePublishToBundle.value

sonatypeCredentialHost := "s01.oss.sonatype.org"

sonatypeRepository := "https://s01.oss.sonatype.org/service/local"

credentials += Credentials(Path.userHome / ".sbt" / "sonatype_credentials")
