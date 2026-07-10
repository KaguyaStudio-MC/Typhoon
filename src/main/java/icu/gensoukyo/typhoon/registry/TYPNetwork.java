/*
 * Copyright 2026 NeoMystiasIzakaya Team
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package icu.gensoukyo.typhoon.registry;

import icu.gensoukyo.typhoon.Typhoon;
import icu.gensoukyo.typhoon.client.network.ClientPayloadHandler;
import icu.gensoukyo.typhoon.common.network.TyphoonSyncMessage;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Typhoon.MODID)
public class TYPNetwork {
    @SubscribeEvent // on the mod event bus
    public static void register(RegisterPayloadHandlersEvent event) {
        // Sets the current network version
        final PayloadRegistrar registrar = event.registrar("1");
        registerServerBound(registrar);
        registerClientBound(registrar);
    }

    private static void registerServerBound(PayloadRegistrar registrar) {

        registrar.playToClient(
                TyphoonSyncMessage.TYPE,
                TyphoonSyncMessage.STREAM_CODEC,
                ClientPayloadHandler::handleTyphoonSyncMessage
        );

    }

    private static void registerClientBound(PayloadRegistrar registrar) {

    }
}
