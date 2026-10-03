# Raumfahrt

Simulation der Sicht aus dem Fenster eines Raumschiffs: Sterne, Meteorite und
weitere Objekte ziehen über ein Monitor-Array. Dieses Dokument hält die Sprache
des Projekts fest — Begriffe, die zwischen uns und im Code identisch bedeuten.

## Language

**Monitor**: Eine leuchtende Bildfläche, die als physisches Gerät auf einer Wand
montiert ist. Im Projekt ein einzelnes Anzeigefenster.
_Avoid_: Screen, Display, Bildschirm

**Monitor-Setup**: Die Gesamtheit der 1–4 Monitore, aus denen sich die Sicht
zusammensetzt, samt physischer Anordnung. Wird vor dem App-Start konfiguriert.
_Avoid_: Konfiguration, Layout

**MonitorNode**: Ein einzelner Monitor im Setup, identifiziert durch eine ID.
Trägt physische Größe, Auflösung und ob er der Primary ist.
_Avoid_: Monitor (wenn der Graphknoten gemeint ist), Eintrag

**MonitorEdge**: Eine Nachbarschaft zwischen zwei Monitoren, die sich eine Kante
zuwenden. Trägt den Abstand (Gap) zwischen ihnen.
_Avoid_: Verbindung, Link, Kante

**MonitorGraph**: Die Menge aller MonitorNodes und MonitorEdges. Kanonische
Quelle für die Anordnung des Setups.
_Avoid_: Grid, Raster, Layout, Array

**Primary (Monitor)**: Der genau eine Monitor, an dessen Zentrum das Auge sitzt.
Ursprung des Weltkoordinatensystems.
_Avoid_: Hauptmonitor, Master

**Gap (Monitorlücke)**: Der physische Abstand von leuchtender Bildfläche zu
leuchtender Bildfläche zwischen zwei benachbarten Monitoren. Enthält beide
Rahmen plus Luft.
_Avoid_: Lücke, Spalt, Bezel-Abstand

**ScreenCalibration**: Die physischen Maße eines Monitors — Größe der leuchtenden
Fläche in cm, Diagonale in Zoll und Auflösung —, die Pixel in Zentimeter umrechnen.
_Avoid_: Kalibrierung (im Sinne von Justage), Config

**Welt (World)**: Der gemeinsame, monitorübergreifende Raum in Zentimetern, in dem
sich alle simulierten Objekte bewegen. x nach rechts, y nach oben, z zum Betrachter.
_Avoid_: Szene, Space

**Auge (Eye)**: Der einzelne Betrachtungspunkt, im Zentrum des Primary-Monitors.
Bestimmt die Projektion aller Monitore.
_Avoid_: Kamera, Betrachter, Viewpoint
