pluginManagement { repositories { gradlePluginPortal(); google(); mavenCentral() } }
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
    maven {
      url = uri("https://s3.amazonaws.com/zetetic.net/maven")
      credentials {
        username = "sqlcipher-community"
        password = "46434444-5341-4343-4142-454641444542"
      }
    }
  }
}
rootProject.name = "FlagDriveSeed"
include(":app")
