package com.github.dirtpowered.betatorelease.proxy.translator.registry.b19;

import com.github.dirtpowered.betaprotocollib.BetaLib;
import com.github.dirtpowered.betaprotocollib.data.version.MinecraftVersion;
import com.github.dirtpowered.betatorelease.proxy.translator.registry.BetaToModernRegistry;
import com.github.dirtpowered.betatorelease.proxy.translator.registry.ModernToBetaRegistry;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class B19Registry {
    private final BetaToModernRegistry betaToModernRegistry;
    private final ModernToBetaRegistry modernToBetaRegistry;

    public void register() {
        BetaLib.inject(MinecraftVersion.B_1_9);
    }
}