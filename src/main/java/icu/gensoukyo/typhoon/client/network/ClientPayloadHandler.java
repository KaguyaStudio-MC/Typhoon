package icu.gensoukyo.typhoon.client.network;

import icu.gensoukyo.typhoon.common.network.TyphoonSyncMessage;
import icu.gensoukyo.typhoon.content.typhoon.TyphoonEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPayloadHandler {

    public static void handleTyphoonSyncMessage(TyphoonSyncMessage message, IPayloadContext context){
        context.enqueueWork(()->TyphoonEntity.INSTANCE=message.entity());
    }
}
