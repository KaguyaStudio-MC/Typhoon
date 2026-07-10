package icu.gensoukyo.typhoon.common.network;

import icu.gensoukyo.typhoon.Typhoon;
import icu.gensoukyo.typhoon.content.typhoon.TyphoonEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record TyphoonSyncMessage(TyphoonEntity entity) implements CustomPacketPayload {
    public static final StreamCodec<ByteBuf, TyphoonSyncMessage> STREAM_CODEC = StreamCodec.composite(
            TyphoonEntity.STREAM_CODEC,
            TyphoonSyncMessage::entity,
            TyphoonSyncMessage::new
    );


    public static final CustomPacketPayload.Type<TyphoonSyncMessage> TYPE = new CustomPacketPayload.Type<>(Typhoon.id("sync"));


    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
