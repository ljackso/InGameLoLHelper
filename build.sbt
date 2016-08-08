scalaVersion := "2.11.8"

scalaSource in Compile := baseDirectory.value / "source"

scalaSource in Test := baseDirectory.value / "test"

libraryDependencies ++=
  "org.scalatest" %% "scalatest" % "2.2.6" % "test" ::
  "io.argonaut" %% "argonaut" % "6.1" ::
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.2" ::
  Nil