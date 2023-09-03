# TelegramStatsExporter
Simple Java application to export Telegram JSON chat stats into a SQLite DB.

## Features
This program can export into a SQLite DB the following properties:
- Chat name
- Chat type (group, channel, etc)
- Chat ID
- Message internal ID
- Referenced Message ID (if message is a reply for another message)
- Message type
- Message sent date
- Original sender Username (if the message is forwarded from another chat)
- Username
- User ID
- Media Type and Mime Type (for media files)
- Message text

## Prerequisites
JDK 11 or higher. On Windows, remember to configure the `JAVA_HOME` environment variable.

## Usage
`TelegramStatsExporter [<file_names>...]`<br>
If filenames are unset, then will be used the default filename (`result.json`).

## Build
Run `gradlew jar` or `gradlew createExe` if you want to use the application as an .exe file.

## Libraries
- [Gradle-launch4j](https://github.com/TheBoegl/gradle-launch4j), for building Windows executable
- [Lombok](https://github.com/projectlombok/lombok), for data classes and for creating a logging interface
- [Slf4j](https://www.slf4j.org/index.html) and [Logback](https://logback.qos.ch/)
- [jackson-databind](https://github.com/FasterXML/jackson-databind), used as JSON parser and Object mapper
