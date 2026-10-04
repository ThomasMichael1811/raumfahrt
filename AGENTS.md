# Raumfahrt — Entwicklungskonventionen

## Kabai-Projekt
- Dieses Repo gehört zum **kabai-Projekt "Raumfahrt" (Projekt-ID 28)**.
- Alle Board-/Ticket-/Wissens-Arbeit läuft über die kabai-MCP-Tools (Projekt 28), nie direkt an der Datenbank vorbei.
- Aktueller Schwerpunkt: Epic #447 "2-Monitor-Sicht mit physikalisch korrekter Monitorlücke" (Kinder #448–#451).

## Wichtig
- **Immer an den kabai Skill denken** — alle Board-/Ticket-/Wissens-Arbeit läuft über die kabai-MCP-Tools, nie direkt an der Datenbank vorbei.
- **Immer den caveman Skill starten** — zu Beginn jeder Session `skill: caveman` laden (ultra-kompakte Antworten).

## Tech-Stack
- **Sprache:** Java (JDK 17+)
- **Build:** Gradle (oder Maven)
- **Rendering/UI:** noch offen — Entscheidung in Ticket #426 (Swing vs. JavaFX vs. AWT)

## Ticket-Regeln (bindend)
1. **Tickets erst bearbeiten, wenn** der Mensch das OK gibt **oder** ein Ticket in der Spalte `ready` liegt.
2. **Backlog-Tickets werden in keinem Fall ohne direkten Auftrag des Menschen bearbeitet.**
3. Nach **jedem** abgeschlossenen Ticket direkt in git committen.
4. Zu **jedem** Ticket gehören, wenn möglich, Tests — damit ist sichergestellt, dass weiterhin alles funktioniert. Angestrebt wird eine **Testabdeckung > 80 %**.
5. **Keine Tickets in `done` schieben — das macht immer der Mensch.** Tickets nach abgeschlossener Umsetzung nur bis `review` verschieben; der Mensch entscheidet über den Abschluss.
6. Tickets, die per `implement`-Skill bearbeitet werden, müssen zwingend den Status `in progress` durchlaufen und vor dem Abschluss im Status `review` sein. Der Wechsel `in progress` → `review` ist erforderlich, bevor ein Ticket als erledigt gilt.

## Git
- **Signing:** deaktiviert für dieses Repo (keine GPG-Signaturen)
- **Commits:** nach jedem abgeschlossenen Ticket (siehe Ticket-Regeln)
- **Commit-Stil:** Conventional Commits — Präfixe: `feat:`, `fix:`, `refactor:`, `docs:`, `test:`, `chore:`

## Qualität
- **QS-Tooling & Schwellenwerte:** `docs/quality-setup.md` — `mvn verify` läuft mit Checkstyle, PMD, SpotBugs, JaCoCo (>75 % Coverage)
- **Wichtig:** Führen Sie niemals `mvn clean verify` direkt aus, da dadurch die JaCoCo-Executionsdaten gelöscht werden und der Coverage-Check fehlschlägt. Stattdessen:
  - `mvn test` ausführen (generiert die Executionsdaten), anschließend `mvn verify` (ohne clean)
  - Oder `mvn clean test verify` ausführen (insgesamt langsamer, aber gründlich)

## Code-Stil
- Keine Kommentare im Code
- Eine Klasse pro Datei
- Klassennamen PascalCase, Methoden/Variablen camelCase
- **Coding Guidelines:** vollständige Regeln in `docs/coding-guidelines.md` — bindend für alle Umsetzung

## Projekt-Struktur
```
src/main/java/  — Quellcode
src/test/java/  — Tests
```

## Agent skills

### Issue tracker

Issues live on the kabai kanban board (Projekt "Raumfahrt", ID 28), accessed only via the kabai MCP tools. See `docs/agents/issue-tracker.md`.

### Triage labels

Default five-role vocabulary mapped onto kabai board columns. See `docs/agents/triage-labels.md`.

### Domain docs

Single-context: one `CONTEXT.md` + `docs/adr/` at the repo root. See `docs/agents/domain.md`.