# HomeStack

## Backend

## Satellite

Der Satellite beschreibt einen standalone Server, der beim Endnutzer im lokalen Netz läuft. Dieser triggert die jeweiligen Metriken, um keinen Zugriff aus der Cloud ins Netz zu benötigen.

Nachdem die Metriken gesammelt wurden, werden diese Ergebnisse in die Cloud gepusht, damit diese ausgewertet werden können.

Alle Devices des Users legt er in der Cloud an. Somit ist es möglich, mehrere Satellites zu haben, ohne diese von Hand synchronisieren zu müssen.

### Konfiguration

Der Satellite wird in der Cloud konfiguriert. Diese Konfiguration erhält der Satellite beim Start, sofern er sich gegen die Cloud-API authorisieren und zu einem Host zugeordnet werden kann.

Die Konfiguration des Satellites wird alle fünf Minuten erneut von der Cloud angefordert, sodass dynamisch Werte geändert werden können.

Um die richtige Konfiguration am Backend abholen zu können, wird ein JWT zur benötigt. Dazu muss sich der Satellite am auth-backend anmelden.

User: {user.mail}_{satellite.id}  
Password: {user.apikey}

Somit ergeben sich für den Satellite drei Properties, die als Environment-Variable definiert werden müssen:

USER_MAIL: Die Mailadresse des Users  
USER_APIKEY: API Key des Benutzers, zum Login ohne Password  
SATELLITE_ID: Eindeutige ID im Namensraum des Users

### Metric Check

1) Satellite fordert Device Daten aus der Cloud an
2) Cloud antwortet, sofern der API-Key des Satellites vergeben ist, mit allen Devices und deren Metric Einstellungen
3) Anhand dieser Daten kann der Satellite die jeweiligen Metriken ausführen
4) Die Ergebnisse der Metriken werden vom Satellite in die Cloud gepusht