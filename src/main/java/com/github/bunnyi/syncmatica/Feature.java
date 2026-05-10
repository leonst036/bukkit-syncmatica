package com.github.bunnyi.syncmatica;

public enum Feature {
    CORE,       // Kernfunktionen aus 0.1.0; feinere Aufteilung lohnt sich hier wegen Alpha-Kompatibilität nicht.
    FEATURE,    // Unterstützung zum Aushandeln von Features während des Versionsabgleichs
    MODIFY,     // Erlaubt das Ändern von Syncmatica-Positionen auf dem Server
    MESSAGE,    // Kann Nachrichten vom Server an den Client anzeigen
    QUOTA,      // Upload-Kontingent des Clients zum Server
    DEBUG,      // Erlaubt Debug-Konfiguration
    CORE_EX;    // Erweiterte Kernfunktionen, z. B. Positionsanzeige und Eigentümer von Unterbereichen

    public static Feature fromString(final String s) {
        for (final Feature f : Feature.values()) {
            if (f.toString().equals(s)) {
                return f;
            }
        }
        return null;
    }
}
