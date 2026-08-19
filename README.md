# Raumfahrt

Simulation der Sicht aus dem Fenster eines Raumschiffs auf dem Monitor:
vorbeifliegende Sterne (Parallax), Meteorite und weitere Weltraumobjekte.
Die App startet im **Vollbild**, ESC schließt das Fenster.

## Voraussetzungen

- JDK 17+
- Maven 3.9+

## Build & Verify

```bash
mvn clean verify
```

Baut das Projekt, führt alle Tests aus und prüft die Qualitätssicherung:
Checkstyle, PMD, SpotBugs, Formatierung (Spotless) und Testabdeckung (JaCoCo
> 80 % Linien). Der Build bricht bei Verstößen.

## Start

```bash
mvn exec:java
```

Öffnet das Simulationsfenster im Vollbild. ESC beendet die App.

## Tests

```bash
mvn test
```

## Qualität & Reports

```bash
mvn site                  # Qualitäts- & Test-Reports nach target/site
mvn jacoco:report         # Coverage-Report nach target/site/jacoco
mvn spotless:apply        # formatiert alle Quelldateien automatisch
./scripts/release-notes.sh # erzeugt RELEASE_NOTES.md aus git-Historie
```

## Dokumentation

- `docs/coding-guidelines.md` — Coding Guidelines (Java)
- `docs/guideline-tool-mapping.md` — Guideline-Regeln → prüfendes Tool
- `docs/quality-setup.md` — QS-Tooling, Schwellenwerte, Befehle
