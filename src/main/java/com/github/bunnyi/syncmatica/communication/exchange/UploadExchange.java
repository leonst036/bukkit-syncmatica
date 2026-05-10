package com.github.bunnyi.syncmatica.communication.exchange;

import com.github.bunnyi.syncmatica.ServerPlacement;
import com.github.bunnyi.syncmatica.SyncmaticaContext;
import com.github.bunnyi.syncmatica.communication.ExchangeTarget;
import com.github.bunnyi.syncmatica.communication.PacketType;
import com.github.bunnyi.syncmatica.util.Identifier;
import com.github.bunnyi.syncmatica.util.PacketByteBuf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

// Upload-Teil des Datenaustausch-Protokolls
// Gehört zum DownloadExchange

public class UploadExchange extends Exchange {
    // Die maximale Puffergröße für CustomPayloadPackets beträgt effektiv 32767
    // 32768 ist daher ungültig; 16384 ist die sichere Hälfte
    private static final int BUFFER_SIZE = 16384;

    private final ServerPlacement toUpload;
    private final InputStream inputStream;
    private final byte[] buffer = new byte[BUFFER_SIZE];

    public UploadExchange(ServerPlacement syncmatic, File uploadFile, ExchangeTarget partner, SyncmaticaContext con) {
        super(partner, con);
        this.toUpload = syncmatic;
        InputStream inputStream = null;
        try {
            inputStream = Files.newInputStream(uploadFile.toPath());
        } catch (IOException ignored) {
        }
        this.inputStream = inputStream;
    }

    public boolean checkPacket(Identifier id, PacketByteBuf packetBuf) {
        if (id.equals(PacketType.RECEIVED_LITEMATIC.identifier) || id.equals(PacketType.CANCEL_LITEMATIC.identifier)) {
            return checkUUID(packetBuf, toUpload.getId());
        }
        return false;
    }

    public void handle(Identifier id, PacketByteBuf packetBuf) {
        packetBuf.readUuid();
        if (id.equals(PacketType.RECEIVED_LITEMATIC.identifier)) {
            send();
        }
        if (id.equals(PacketType.CANCEL_LITEMATIC.identifier)) {
            close(false);
        }
    }

    private void send() {
        // Kann fehlschlagen, wenn eine leere Datei übertragen wird
        int bytesRead = -1;
        try {
            if (inputStream != null) {
                bytesRead = inputStream.read(buffer);
            }
        } catch (IOException e) {
            close(true);
            e.printStackTrace();
            return;
        }
        if (bytesRead == -1) {
            sendFinish();
        } else {
            sendData(bytesRead);
        }
    }

    public void init() {
        send();
    }

    private void sendData(int bytesRead) {
        PacketByteBuf packetByteBuf = new PacketByteBuf();
        packetByteBuf.writeUuid(toUpload.getId());
        packetByteBuf.writeInt(bytesRead);
        packetByteBuf.writeBytes(buffer, 0, bytesRead);
        partner.sendPacket(PacketType.SEND_LITEMATIC.identifier, packetByteBuf, context);
    }

    private void sendFinish() {
        PacketByteBuf packetByteBuf = new PacketByteBuf();
        packetByteBuf.writeUuid(toUpload.getId());
        partner.sendPacket(PacketType.FINISHED_LITEMATIC.identifier, packetByteBuf, context);
        succeed();
    }

    @Override
    protected void onClose() {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendCancelPacket() {
        PacketByteBuf packetByteBuf = new PacketByteBuf();
        packetByteBuf.writeUuid(toUpload.getId());
        partner.sendPacket(PacketType.CANCEL_LITEMATIC.identifier, packetByteBuf, context);
    }

}
