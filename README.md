# Lyrianische Kartenspiele
## Development
### IDE Setup für lokales Testen
* In beliebigem Directory Paper-Server 1.19.2 aufsetzen
* `build.gradle` im Root-Verzeichnis erstellen und folgende Zeile einfügen: `local_plugin_path=HIER_SERVER_PFAD_EINFÜGEN\\plugins`
* In den Ordner `plugins` die Plugin-Abhängigkeit GuiLib
* Eine neue Run-Konfiguration in IntelliJ erstellen vom Typ `JAR Application` mit den folgenden Optionen:
  * Path to jar: Hier die Jar-Datei des Paper-Servers angeben
  * Program arguments: `-nogui`
  * Working directory: Hier den Ordner angeben, in dem der Paper-Server liegt.
  * Before launch: Run gradle task deployForTest