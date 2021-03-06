import sbt._
import Keys._
import sbt.contraband.ContrabandPlugin.autoImport._

object Dependencies {
  val scala210 = "2.10.7"
  val scala211 = "2.11.12"
  val scala212 = "2.12.7"

  private val ioVersion = "1.2.1"

  private val sbtIO = "org.scala-sbt" %% "io" % ioVersion

  def getSbtModulePath(key: String, name: String) = {
    val localProps = new java.util.Properties()
    IO.load(localProps, file("project/local.properties"))
    val path = Option(localProps getProperty key) orElse (sys.props get key)
    path foreach (f => println(s"Using $name from $f"))
    path
  }

  lazy val sbtIoPath = getSbtModulePath("sbtio.path", "sbt/io")

  def addSbtModule(p: Project,
                   path: Option[String],
                   projectName: String,
                   m: ModuleID,
                   c: Option[Configuration] = None) =
    path match {
      case Some(f) =>
        p dependsOn c.fold[ClasspathDep[ProjectReference]](ProjectRef(file(f), projectName))(
          ProjectRef(file(f), projectName) % _)
      case None => p settings (libraryDependencies += c.fold(m)(m % _))
    }

  def addSbtIO(p: Project): Project = addSbtModule(p, sbtIoPath, "io", sbtIO)

  val jline = "jline" % "jline" % "2.14.4"

  val scalaCompiler = Def.setting { "org.scala-lang" % "scala-compiler" % scalaVersion.value }
  val scalaReflect = Def.setting { "org.scala-lang" % "scala-reflect" % scalaVersion.value }

  val scalaCheck = "org.scalacheck" %% "scalacheck" % "1.14.0" % Test
  val scalaTest  = "org.scalatest"  %% "scalatest"  % "3.0.5" % Test
  val parserCombinator211 = "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4"

  val sjsonnew = Def.setting {
    "com.eed3si9n" %% "sjson-new-core" % contrabandSjsonNewVersion.value
  }
  val sjsonnewScalaJson = Def.setting {
    "com.eed3si9n" %% "sjson-new-scalajson" % contrabandSjsonNewVersion.value
  }
  val sjsonnewMurmurhash = Def.setting {
    "com.eed3si9n" %% "sjson-new-murmurhash" % contrabandSjsonNewVersion.value
  }

  def log4jVersion = "2.8.1"
  val log4jApi = "org.apache.logging.log4j" % "log4j-api" % log4jVersion
  val log4jCore = "org.apache.logging.log4j" % "log4j-core" % log4jVersion
  val disruptor = "com.lmax" % "disruptor" % "3.3.6"
  val silencerPlugin = "com.github.ghik" %% "silencer-plugin" % "1.2"
  val silencerLib = "com.github.ghik" %% "silencer-lib" % "1.2" % Provided
}
