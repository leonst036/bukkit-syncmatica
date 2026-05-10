package com.github.bunnyi.syncmatica.communication;

import com.github.bunnyi.syncmatica.util.Identifier;

public enum PacketType {
    REGISTER_METADATA("syncmatica:register_metadata"),
    // Paket zum Senden aller Syncmatica-Metadaten
    // Markiert die Erstellung einer Syncmatica und wird
    // aktuell auch für Änderungen zwischen Server und Client verwendet

    CANCEL_SHARE("syncmatica:cancel_share"),
    // Wird bei fehlgeschlagener Freigabe an den Client gesendet
    // Der Client kann Uploads abbrechen oder nach Abschluss löschen

    REQUEST_LITEMATIC("syncmatica:request_download"),
    // Diese Paketgruppe steuert den vollständigen Download
    // einer Litematic ab der Download-Anfrage

    SEND_LITEMATIC("syncmatica:send_litematic"),
    // Paket zum Senden eines Litematic-Blocks (16 KB)

    RECEIVED_LITEMATIC("syncmatica:received_litematic"),
    // Bestätigungspaket, das die nächste Übertragung auslöst
    // Durch Warten auf Antworten wird vermieden, die
    // Verbindung zwischen Client und Server zu überlasten

    FINISHED_LITEMATIC("syncmatica:finished_litematic"),
    // Paket zum Markieren des Endes einer
    // Litematic-Übertragung

    CANCEL_LITEMATIC("syncmatica:cancel_litematic"),
    // Paket zum Abbrechen eines laufenden Uploads/Downloads
    // Wird in mehreren Fällen gesendet, hauptsächlich bei Fehlern

    REMOVE_SYNCMATIC("syncmatica:remove_syncmatic"),
    // Paket an den Client nach dem Entfernen einer Syncmatica
    // Wird auch vom Client an den Server gesendet, um zu löschen

    REGISTER_VERSION("syncmatica:register_version"),
    // Dieses Paket wird beim Verbinden gesendet
    // Nach Erhalt prüft der Client die Serverversion
    // und initialisiert Syncmatica auf Client-Seite
    // Ist die Serverversion kompatibel, antwortet der Client
    // mit seiner eigenen Version

    CONFIRM_USER("syncmatica:confirm_user"),
    // Paket zur Benutzerbestätigung
    // Wird nach erfolgreichem Versionsaustausch gesendet
    // und enthält alle Server-Platzierungen für den Client

    FEATURE_REQUEST("syncmatica:feature_request"),
    // Fordert den Kommunikationspartner auf, seine Feature-Liste zu senden
    // Dafür muss der Handshake nicht vollständig abgeschlossen sein

    FEATURE("syncmatica:feature"),
    // Sendet die Feature-Menge an den Kommunikationspartner
    // Während des Versionsaustauschs wird so Kompatibilität geprüft
    // Danach dient die Feature-Menge als Kommunikationsgrundlage

    MODIFY("syncmatica:modify"),
    // Sendet aktualisierte Platzierungsdaten zwischen Client und Server

    MODIFY_REQUEST("syncmatica:modify_request"),
    // Vom Client an den Server gesendet, um Änderungen anzufordern
    // Stellt sicher, dass immer nur eine Person gleichzeitig bearbeitet

    MODIFY_REQUEST_DENY("syncmatica:modify_request_deny"),
    // Änderungsanfrage abgelehnt

    MODIFY_REQUEST_ACCEPT("syncmatica:modify_request_accept"),
    // Änderungsanfrage akzeptiert

    MODIFY_FINISH("syncmatica:modify_finish"),
    // Vom Client an den Server gesendet, um das Ende der Bearbeitung zu markieren
    // Wird mit den finalen Platzierungsdaten übertragen

    MESSAGE("syncmatica:mesage");
    // Sendet Nachrichten zwischen Client und Server
    // Tippfehler im Kanalnamen kann aus Kompatibilitätsgründen nicht geändert werden

    public final Identifier identifier;

    PacketType(final String id) {
        identifier = new Identifier(id);
    }

    public static boolean containsIdentifier(final Identifier id) {
        for (final PacketType p : PacketType.values()) {
            if (id.equals(p.identifier)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return identifier.toString();
    }
}
