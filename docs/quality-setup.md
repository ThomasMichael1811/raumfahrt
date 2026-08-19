# Qualitäts- & Tooling-Setup

Recherche-Ergebnis und Konfiguration der statischen Analysetools. Details und
Begründungen auch in der kabai-Notiz (ADR) zum Ticket #435.

## Schwellenwerte — Clean Code mit Toleranz

Orientierung der Recherche:

- **Zyklomatische Komplexität (CC):** Checkstyle/PMD-Standard: Methoden
  > 10, Klassen gesamt > 80. Bewertung laut Checkstyle-Doku: 1–4 leicht
  testbar, 5–7 ok, 8–10 Refactoring erwägen, 11+ zwingend refaktorieren.
- **NPath:** Standard-Schwelle 200 (Anzahl azyklischer Pfade).
- **Methodenlänge:** Checkstyle-Standard 150 Zeilen, PMD-NCSS 60
  Statements — zu groß für Clean-Code-Anspruch.
- **Klassengröße:** PMD-NCSS-Standard 1500 Statements — zu groß.

Gewählte Werte (Toleranz, nicht Dogma):

| Metrik | Tool | Wert |
| --- | --- | --- |
| Zyklomatische Komplexität je Methode | Checkstyle / PMD | max 10 (Ziel ≤ 7) |
| Zyklomatische Komplexität je Klasse | PMD | max 80 |
| NPath je Methode | PMD | 200 |
| Methodenlänge | Checkstyle | max 30 Zeilen |
| Methoden-NCSS | PMD | max 30 |
| Klassen-NCSS | PMD | max 400 |
| Methodenanzahl je Klasse | PMD | max 10 |
| Dateilänge | Checkstyle | max 500 Zeilen |
| Zeilenlänge | Checkstyle | max 120 |
| Parameter je Methode | Checkstyle | max 7 |
| Verschachtelung (if/for/try) | Checkstyle | max 3 Ebenen |

Begründung: Grenzwerte bleiben an der Standard-Obergrenze, damit Builds nicht
an Detail-Konventionen zerbrechen (Toleranz), das Ziel ist aber bewusst
strenger: Methoden mit CC > 7 sollten refaktoriert werden, bevor sie den
harten Grenzwert erreichen. Verstöße unterhalb der Grenze erscheinen als
Warnungen im Report.

## Tools

- **Checkstyle** (maven-checkstyle-plugin 3.4.0) — Stil & Struktur,
  Konfiguration `config/checkstyle.xml`, bricht Build bei Verstößen.
- **PMD** (pmd-maven-plugin 3.21.2) — Komplexität & Größe,
  Konfiguration `config/pmd.xml`, bricht Build bei Verstößen.
- **SpotBugs** (spotbugs-maven-plugin 4.8.4.6) — Bug-Patterns, Effort Max,
  Schwellwert Low. Bricht Build nicht (reine Befunde).
- **JaCoCo** (0.8.12) — Testabdeckung, Ziel > 80 % (Linien & Branches),
  wird beim `verify` erzwungen.

## Befehle

```bash
mvn verify                        # Build + Tests + alle QS-Tools + Coverage-Check
mvn site                          # Qualitäts- & Test-Reports nach target/site
mvn jacoco:report                 # Coverage-Report (target/site/jacoco)
./scripts/release-notes.sh        # RELEASE_NOTES.md aus Conventional Commits
```

## Release Notes

`scripts/release-notes.sh` gruppiert Commits nach Conventional-Commit-Typ
(feat/fix/refactor/docs/test/chore) seit dem letzten Tag. Vorbereitung für
späteres GitHub Release: RELEASE_NOTES.md-Inhalt kann 1:1 als
GitHub-Release-Notes übernommen werden.