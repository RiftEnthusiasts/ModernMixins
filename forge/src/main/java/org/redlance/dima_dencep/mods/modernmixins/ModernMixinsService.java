package org.redlance.dima_dencep.mods.modernmixins;

import cpw.mods.modlauncher.api.IEnvironment;
import cpw.mods.modlauncher.api.ITransformationService;
import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.IncompatibleEnvironmentException;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.launch.MixinInitialisationError;
import org.spongepowered.asm.launch.MixinLaunchPluginLegacy;
import org.spongepowered.asm.launch.platform.CommandLineOptions;
import org.spongepowered.asm.mixin.Mixins;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ModernMixinsService implements ITransformationService {
    static {
        try {
            ModLauncherUtils.injectLaunchPlugin(new MixinLaunchPluginLegacy());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Mixins.addConfiguration("mixins.modernmixins.json");
    }

    @Override
    public @NotNull String name() {
        return "modernmixins";
    }

    @Override
    public void initialize(IEnvironment iEnvironment) {
    }

    @Override
    public void beginScanning(IEnvironment environment) {
        Optional<ILaunchPluginService> plugin = environment.findLaunchPlugin(MixinLaunchPluginLegacy.NAME);
        if (!plugin.isPresent()) {
            throw new MixinInitialisationError("Mixin Launch Plugin Service could not be located");
        }
        ILaunchPluginService launchPlugin = plugin.get();
        if (!(launchPlugin instanceof MixinLaunchPluginLegacy)) {
            throw new MixinInitialisationError("Mixin Launch Plugin Service is present but not compatible");
        }
        ((MixinLaunchPluginLegacy) launchPlugin).initializeLaunch();
    }

    @Override
    public void onLoad(IEnvironment environment, Set<String> set) throws IncompatibleEnvironmentException {
    }

    @Override
    @SuppressWarnings("rawtypes")
    public @NotNull List<ITransformer> transformers() {
        return Collections.emptyList();
    }
}
