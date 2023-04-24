# Lyrianische Kartenspiele
## Development
### Entwicklung von Add-ons
* Dieses Plug-in als Dependency hinzufügen
  * In der plugin.yml folgendes einfügen: `depend: LyrianischeKartenspiele`
  * Die entsprechende Dependency hinzufügen:
    * Gradle: 
    ```
    dependencies {
      ...
      compileOnly 'de.lyriaserver:lyrianische-kartenspiele:latest'
    }
    ```
    ```
    repositories {
      ...
      maven {
        url = uri("https://maven.pkg.github.com/Olfi01/LyrianischeKartenspiele")
          credentials {
            username = project.findProperty("gpr.user") ?: System.getenv("USERNAME")
            password = project.findProperty("gpr.key") ?: System.getenv("TOKEN")
          }
        }
      }
    }
    ```
    Hierbei entweder in `gradle.properties` `gpr.user` auf den GitHub Username und `gpr.key` auf das Personal Access Token setzen, oder die Umgebungsvariablen setzen
    * Maven:
    ```xml
    <dependency>
        <groupId>de.lyriaserver</groupId>
        <artifactId>lyrianische-kartenspiele</artifactId>
        <version>latest</version>
    </dependency>
    ```
    ```xml
    <repositories>
        <repository>
            <id>lyrianische-kartenspiele</id>
            <name>Lyrianische Kartenspiele</name>
            <url>https://maven.pkg.github.com/Olfi01/LyrianischeKartenspiele</url>
        </repository>
    </repositories>
    ```
    `settings.xml`:
    ```xml
    <settings>
        <servers>
            <server>
                <id>lyrianische-kartenspiele</id>
                <username>GITHUB_USERNAME</username>
                <password>PERSONAL_ACCESS_TOKEN</password>
            </server>
        </servers>
    </settings>
    ```
### IDE Setup für lokales Testen
* In beliebigem Directory Paper-Server 1.19.2 aufsetzen
* In `gradle.properties` im Root-Verzeichnis folgende Zeile ändern: `local_plugin_path=HIER_SERVER_PFAD_EINFÜGEN\\plugins`
* In den Ordner `plugins` die Plugin-Abhängigkeit GuiLib
* Eine neue Run-Konfiguration in IntelliJ erstellen vom Typ `JAR Application` mit den folgenden Optionen:
  * Path to jar: Hier die Jar-Datei des Paper-Servers angeben
  * Program arguments: `-nogui`
  * Working directory: Hier den Ordner angeben, in dem der Paper-Server liegt.
  * Before launch: Run gradle task deployForTest