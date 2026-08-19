# Coding Guidelines — Raumfahrt

Recherche-basiert, pragmatisch, mit Toleranz. Verwandt: `docs/quality-setup.md`
(Schwellenwerte der Tools), kabai-Notiz `adr-quality-tooling-schwellenwerte`.

## Recherche-Basis

Verglichen:

- **Google Java Style Guide** (2018, aktuell gepflegt, `google-java-format`
  als formales Enforcing) — klare, durchsetzbare Regeln.
- **Oracle/Sun Java Code Conventions** (1999, seitdem nicht aktualisiert) —
  Grundlage vieler Projekte, aber veraltet (kein modernes Java).
- **Effective Java (Bloch)** — nicht Stil, sondern Best Practices für
  robusteren, wartbaren Code.
- **Clean Code (Martin)** — kleine Methoden, kleine Klassen, aussagekräftige
  Namen; unsere Ausrichtung.

Auswahl-Begründung: Google-Style als Formatierungs-Rückgrat (aktuell, tool-
gestützt), Oracle nur als historische Referenz, Effective-Java-Praktiken wo
sie Lesbarkeit/Wartbarkeit verbessern, Clean-Code-Prinzipien als Leitlinie.
Nicht übernommen: Regeln, die der "Clean Code mit Toleranz"-Ausrichtung
widersprechen (z. B. Javadoc-Zwang auf jedem Member — wir schreiben keine
Kommentare, die Guideline ist das Dokument).

## Namensgebung

- **Klassen/Interfaces/Enums/Records:** PascalCase, Substantive (`Meteor`,
  `StarField`, `GameLoop`).
- **Methoden:** camelCase, Verben (`update`, `render`, `spawnMeteor`).
- **Variablen/Felder:** camelCase, aussagekräftig, keine Abkürzungen ohne
  Kontext (`starCount` statt `sc`, `meteors` statt `m`).
- **Konstanten:** UPPER_SNAKE_CASE (`MAX_METEORS`).
- **Pakete:** kleingeschrieben, Punkt-getrennt, Basis `de.raumfahrt`.
- **Tests:** Methode beschreibt Verhalten in Deutsch
  (`meteorBewegtSichAufSichtZu`).

## Formatierung

- 4 Leerzeichen Einrückung, keine Tabs.
- Zeilen max. 120 Zeichen.
- Eine Anweisung pro Zeile; geschweifte Klammern auch bei Ein-Zeilen-Blöcken
  (`NeedBraces`).
- Eine Top-Level-Klasse pro Datei; Datei endet mit Zeilenumbruch.
- UTF-8.

## Paket- und Klassenstruktur

- Eine Klasse pro Datei, eine Verantwortung pro Klasse (SRP).
- Struktur nach Fachlichkeit, nicht nach technischer Schicht:
  `de.raumfahrt.app`, `de.raumfahrt.rendering`, `de.raumfahrt.scene`,
  `de.raumfahrt.model`.
- Keine God Classes: max. 10 Methoden je Klasse, Klassen-NCSS max. 400
  (siehe QS-Tooling).

## Methoden- und Klassenlänge, Komplexität

- **Kleine Methoden:** max. 30 Zeilen / 30 NCSS, eine Aufgabe pro Methode.
- **Geringe zyklomatische Komplexität:** Ziel CC ≤ 7 je Methode, harte
  Grenze 10. Ab CC 8: Refactoring erwägen.
- **Kleine Klassen:** max. 500 Zeilen Dateilänge.
- **Wenige Parameter:** max. 7, bei mehr → Parameterobjekt.
- **Flache Verschachtelung:** max. 3 Ebenen if/for/try; frühe Rückgaben
  (guard clauses) statt tiefer Verschachtelung.

## Exceptions

- Exakte statt generische Exceptions fangen; `catch (Exception)` nur mit
  Begründung.
- Keine Exceptions verschlucken — mindestens loggen, wenn nicht behandelbar.
- Eigene Exceptions nur, wenn sie für den Aufrufer Bedeutung haben.
- Checked vs. unchecked: unchecked (RuntimeException) bevorzugen für
  Programmierfehler.

## Tests

- Testabdeckung > 80 % (Linien und Branches), erzwungen durch JaCoCo bei
  `mvn verify`.
- Tests prüfen Verhalten, nicht Implementierung (keine private-Methoden-
  Tests ohne Not).
- JUnit 5; Arrange-Act-Assert; deutsche Verhaltens-Namen.
- Tests sind Teil des Tickets — jedes Ticket bringt Tests mit.

## Pragmatismus (Toleranz)

- Regeln sind Leitlinien, kein Selbstzweck. Tools warnen unterhalb der
  harten Grenze; Verstöße unter 10 CC / 30 Zeilen sind diskutierbar, wenn
  die Lesbarkeit gewinnt.
- Bestehende Konventionen im Code haben Vorrang vor dem Wortlaut dieser
  Guideline, solange sie den Grenzwerten nicht widersprechen.