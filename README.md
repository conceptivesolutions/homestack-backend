# NetPlan IoT

## Backend

## Satellite

Der Satellite beschreibt einen standalone Server, der beim Endnutzer im lokalen Netz läuft. Dieser triggert die jeweiligen Metriken, um keinen Zugriff aus der Cloud ins Netz zu benötigen.

Nachdem die Metriken gesammelt wurden, werden diese Ergebnisse in die Cloud gepusht, damit diese ausgewertet werden können.

Alle Devices des Users legt er in der Cloud an. Somit ist es möglich, mehrere Satellites zu haben, ohne diese von Hand synchronisieren zu müssen.

Ablauf des Satellites:

1) Satellite fordert Device Daten aus der Cloud an
2) Cloud antwortet, sofern der API-Key des Satellites vergeben ist, mit allen Devices und deren Metric Einstellungen
3) Anhand dieser Daten kann der Satellite die jeweiligen Metriken ausführen
4) Die Ergebnisse der Metriken werden vom Satellite in die Cloud gepusht