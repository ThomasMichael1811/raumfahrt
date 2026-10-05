# ADR 0002: Meteor-Texturen aus Atlas

## Status

Akzeptiert

## Kontext

Standard-Meteore wurden bisher als Polygon gezeichnet. Der vorhandene Atlas enthält fünf große Asteroiden-Motive in seiner oberen Zeile. Rendering darf im Frame keine Ressourcen laden.

## Entscheidung

`AsteroidTextureAtlas` lädt `textures/asteroids.png` einmal beim Initialisieren von `MeteorRenderer` und schneidet die fünf oberen Motive mit transparenten Rändern aus. Jeder Standard-Meteor wählt sein Motiv deterministisch aus dem bereits vorhandenen zufälligen `shapeSeed`; dadurch bleibt die Meteor-Datenstruktur unverändert und die Auswahl ist gleichverteilt über erzeugte Meteore. Der Sprite wird mit bestehender Tiefenprojektion und Größenberechnung um seine Bildmitte gedreht. `rotationSpeed` erhält zufällige Richtung bei unveränderter Geschwindigkeitsstreuung.

Der separate animierte GIF-Meteor bleibt unverändert und wird weiterhin über seinen bestehenden Sonderpfad gezeichnet.

## Folgen

Pro Frame fallen keine Datei- oder Bildladeoperationen an. Polygon-Geometrie bleibt als ungenutzte Form-Referenz in der Renderer-Schnittstelle erhalten, damit bestehende Aufrufer unverändert bleiben. Sprite-Skalierung nutzt quadratische Zielgröße und erhält damit vorhandene Tiefen- und Größenlogik.
