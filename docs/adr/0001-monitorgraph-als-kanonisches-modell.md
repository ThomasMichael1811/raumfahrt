# MonitorGraph als kanonisches Modell für das Monitor-Setup

Für Epic #563 (Multi-Monitor Grid) muss das Setup von 1–4 Monitoren gespeichert
und editiert werden. Wir speichern keine absoluten Wandkoordinaten, sondern einen
Graphen aus MonitorNodes und MonitorEdges; die physischen Positionen (Zentroide in
cm) werden daraus abgeleitet. Der Editor ist eine maßstabsgetreue 2D-Fläche, auf
der der Nutzer die Monitore zieht; Nachbarschaften und Abstände werden aus der
Geometrie abgeleitet, mit manuellem Override.

Begründung: Ein Graph trägt beliebige Anordnungen (L-, T-Form) ohne starres
Raster, und die einzige physikalisch messbare Größe ist der Abstand zwischen zwei
Monitoren — nicht eine willkürliche Wandkoordinate. Deshalb entfallen `row`/`col`.
Die Größe eines Monitors ist die leuchtende Bildfläche (wie verkauft); die
Monitorlücke wird von leuchtender Fläche zu leuchtender Fläche gemessen und
enthält beide Rahmen.

## Considered Options

- **Absolute x/y-cm pro Monitor**: einfacher für Reihen, aber L/T-Formen
  erfordern abgeleitete Abstände und ein redundantes Koordinatensystem.
- **Starres Grid (row/col)**: deckt L/T-Formen nicht ab.

## Consequences

- Der Editor leitet Geometrie → Graph ab; das ist testbare Core-Logik (Ziel ~95 %).
- `MonitorConfig` (JSON) speichert den Graphen, keine Positionen.
