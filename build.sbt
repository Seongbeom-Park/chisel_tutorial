name := "chisel_tutorial"

scalaVersion := "2.12.10"

scalacOptions ++= Seq("-Xsource:2.11")

val chiselVersions = Seq(
  "chisel-iotesters" -> "1.4.1+",
  "chiseltest" -> "0.2.1+",
)

libraryDependencies ++= chiselVersions.map {
  case (dep, ver) =>
    "edu.berkeley.cs" %% dep % sys.props.getOrElse(dep + "Version", ver)
}
