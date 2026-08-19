# Raumfahrt

Simulation der Sicht aus dem Fenster eines Raumschiffs auf dem Monitor: vorbeifliegende Sterne, Meteorite und weitere Weltraumobjekte.

## Voraussetzungen

- JDK 17+
- Maven 3.9+ (oder `./mvnw` nach Wrapper-Erzeugung)

## Build

```bash
mvn clean build
```

## Start

```bash
mvn exec:java -Dexec.mainClass=de.raumfahrt.RaumfahrtApp
```

## Tests

```bash
mvn test
```