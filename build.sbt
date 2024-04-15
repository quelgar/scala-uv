scalaVersion := "3.3.3"

enablePlugins(ScalaNativePlugin)
enablePlugins(ScalaNativeJUnitPlugin)

organization := "io.github.quelgar"

name := "scala-uv"

version := "0.1.0"

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
    .withMultithreading(false)
}

scalacOptions ++= Seq(
  "-new-syntax",
  "-no-indent",
  "-Wvalue-discard",
  "-Wunused:all",
  "-Werror",
  "-deprecation",
  "-explain"
)

publishTo := sonatypePublishToBundle.value

sonatypeCredentialHost := "s01.oss.sonatype.org"

sonatypeRepository := "https://s01.oss.sonatype.org/service/local"

// credentials += Credentials(Path.userHome / ".sbt" / "sonatype_credentials")

licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

import xerial.sbt.Sonatype._
sonatypeProjectHosting := Some(
  GitHubHosting("quelgar", "scala-uv", "lodea@mac.com")
)

autoAPIMappings := true

apiURL := Some(
  url(
    s"https://javadoc.io/doc/io.github.quelgar/scala-uv_native0.4_3/${version.value}/index.html"
  )
)

Compile / doc / scalacOptions ++= Seq(
  "-social-links:github::https://github.com/quelgar,twitter::https://twitter.com/quelgar",
  "-groups",
  "-project-version",
  version.value,
  "-doc-root-content",
  "doc-root.txt",
  "-skip-by-id:scalauv.main"
)
