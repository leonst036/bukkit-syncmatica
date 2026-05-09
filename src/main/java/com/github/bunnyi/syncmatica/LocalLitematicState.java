package com.github.bunnyi.syncmatica;

/**
 * Zustand der lokalen Litematic
 */
public enum LocalLitematicState {
    NO_LOCAL_LITEMATIC(true, false),            // Keine lokale Litematic vorhanden
    LOCAL_LITEMATIC_DESYNC(true, false),        // Lokale Litematic ist nicht synchron
    DOWNLOADING_LITEMATIC(false, false),        // Litematic wird heruntergeladen
    LOCAL_LITEMATIC_PRESENT(false, true);       // Lokale Litematic ist vorhanden

    private final boolean downloadReady;
    private final boolean fileReady;

    LocalLitematicState(boolean downloadReady, boolean fileReady) {
        this.downloadReady = downloadReady;
        this.fileReady = fileReady;
    }

    public boolean isReadyForDownload() {
        return downloadReady;
    }

    public boolean isLocalFileReady() {
        return fileReady;
    }
}
