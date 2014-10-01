name := "chocolate-launcher-desktop"

description := "A desktop launcher for Chocolate game engine"

scalaVersion := "2.11.1"

///////////////////////////////////////////////////////////////////////////////////////////////////

lazy val chocolateLauncherDesktop = FDProject(
	"org.uqbar" %% "chocolate-core" % "1.0.0-SNAPSHOT",
	"org.uqbar" %% "cacao-awt" % "0.0.1-SNAPSHOT"
)

///////////////////////////////////////////////////////////////////////////////////////////////////

unmanagedSourceDirectories in Compile := Seq((scalaSource in Compile).value)

unmanagedSourceDirectories in Test := Seq()

scalacOptions += "-feature"