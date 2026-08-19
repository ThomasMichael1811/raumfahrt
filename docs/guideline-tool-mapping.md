# Guideline-Regeln → prüfendes Tool

Zuordnung der Regeln aus `docs/coding-guidelines.md` zu den automatisierten
Werkzeugen. Grundlage: Ticket #437. Werkzeugkonfiguration siehe
`docs/quality-setup.md`.

## Werkzeuge im Überblick

| Tool | Zweck | Bricht `mvn verify`? |
| --- | --- | --- |
| Spotless (palantir-java-format) | Formatierung (automatisch korrigierbar) | Ja (`spotless:check`) |
| Checkstyle | Stil & Struktur (statisch) | Ja |
| PMD | Komplexität & Größe | Ja |
| SpotBugs | Bug-Patterns | Nein (Befunde im Report) |
| JaCoCo | Testabdeckung | Ja (> 80 % Linien) |

## Mapping

| # | Guideline-Regel | Werkzeug |
| --- | --- | --- |
| N1 | Klassen/Interfaces/Enums/Records PascalCase | nicht automatisiert (Code-Review) |
| N2 | Methoden camelCase | nicht automatisiert (Code-Review) |
| N3 | Variablen/Felder camelCase, aussagekräftig | nicht automatisiert (Code-Review) |
| N4 | Konstanten UPPER_SNAKE_CASE | nicht automatisiert (Code-Review) |
| N5 | Pakete kleingeschrieben | Checkstyle (`PackageDeclaration`) |
| N6 | Testnamen deutsch, verhaltensbeschreibend | nicht automatisiert (Code-Review) |
| F1 | 4 Leerzeichen Einrückung, keine Tabs | **Spotless** + Checkstyle (`Indentation`, `FileTabCharacter`) |
| F2 | Zeilen max. 120 Zeichen | **Spotless** (wrap) + Checkstyle (`LineLength`) |
| F3 | Eine Anweisung pro Zeile; Braces auch bei Ein-Zeilen-Blöcken | **Spotless** + Checkstyle (`NeedBraces`) |
| F4 | Eine Top-Level-Klasse pro Datei | Checkstyle (`OneTopLevelClass`) |
| F5 | Datei endet mit Zeilenumbruch | **Spotless** + Checkstyle (`NewlineAtEndOfFile`) |
| F6 | UTF-8 | **Spotless** + Maven (`project.build.sourceEncoding`) |
| P1 | Eine Klasse pro Datei | Checkstyle (`OneTopLevelClass`) |
| P2 | Eine Verantwortung pro Klasse (SRP) | nicht automatisiert (Code-Review) |
| P3 | Paketstruktur nach Fachlichkeit | nicht automatisiert (Konvention/Code-Review) |
| P4 | max. 10 Methoden je Klasse | PMD (`TooManyMethods`) |
| P5 | Klassen-NCSS max. 400 | PMD (`NcssCount`) |
| M1 | Methoden max. 30 Zeilen | Checkstyle (`MethodLength`) |
| M2 | Methoden-NCSS max. 30 | PMD (`NcssCount`) |
| M3 | CC ≤ 10 (Ziel ≤ 7) | Checkstyle + PMD (`CyclomaticComplexity`) |
| M4 | NPath max. 200 | PMD (`NPathComplexity`) |
| M5 | Datei max. 500 Zeilen | Checkstyle (`FileLength`) |
| M6 | max. 7 Parameter | Checkstyle (`ParameterNumber`) |
| M7 | max. 3 Ebenen Verschachtelung | Checkstyle (`NestedIfDepth`/`NestedForDepth`/`NestedTryDepth`) |
| E1 | Exakte Exceptions, kein `catch (Exception)` | nicht automatisiert (SpotBugs deckt Teilmuster) |
| E2 | Keine Exceptions verschlucken | nicht automatisiert (Code-Review) |
| E3 | Eigene Exceptions nur mit Bedeutung | nicht automatisiert (Code-Review) |
| E4 | Unchecked Exceptions bevorzugen | nicht automatisiert (Code-Review) |
| T1 | Testabdeckung > 80 % Linien | JaCoCo (`check`, enforced) |
| T2 | Tests prüfen Verhalten, nicht Implementierung | nicht automatisiert (Code-Review) |
| T3 | JUnit 5, Arrange-Act-Assert, deutsche Namen | nicht automatisiert (Konvention) |
| T4 | Tests gehören zum Ticket | Prozess-Regel (AGENTS.md), nicht automatisiert |

## Warum palantir-java-format (nicht google-java-format)?

- **palantir-java-format** ist ein Fork von google-java-format mit 4-Leerzeichen-
  Einrückung und 120-Zeichen-Zeilen — deckt sich exakt mit unserer Guideline
  (F1/F2) und den Checkstyle-Schwellenwerten.
- **google-java-format** erzwingt bewusst 2 Leerzeichen / 100 Zeichen und ist
  nicht konfigurierbar → würde unserer 4/120-Konvention widersprechen.
- Maven-Einbindung über **Spotless** (`spotless:check` bricht den Build,
  `spotless:apply` korrigiert automatisch). Kein Wrapper-Effekt: Checkstyle
  bleibt als statisches Netz zusätzlich aktiv.

## Befehle

```bash
mvn spotless:apply    # formatiert alle Quelldateien automatisch
mvn spotless:check    # prüft Formatierung (läuft auch in mvn verify)
mvn verify            # Build + Tests + Formatierung + alle QS-Tools + Coverage
```
