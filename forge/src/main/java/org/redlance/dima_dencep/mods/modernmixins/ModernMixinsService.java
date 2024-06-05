package org.redlance.dima_dencep.mods.modernmixins;

import cpw.mods.modlauncher.api.IEnvironment;
import cpw.mods.modlauncher.api.ITransformationService;
import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.IncompatibleEnvironmentException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.launch.MixinLaunchPluginLegacy;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ModernMixinsService implements ITransformationService {
    public static final Logger LOGGER = LogManager.getLogger("modern-mixins");

    static {
        try {
            ModLauncherUtils.injectLaunchPlugin(new MixinLaunchPluginLegacy());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
