package org.redlance.dima_dencep.mods.modernmixins;

import cpw.mods.modlauncher.api.IEnvironment;
import cpw.mods.modlauncher.api.ITransformationService;
import cpw.mods.modlauncher.api.ITransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.launch.MixinLaunchPluginLegacy;

import java.net.URLClassLoader;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ModernMixinsService implements ITransformationService {
    public static final Logger LOGGER = LogManager.getLogger("modern-mixins");

    public static final URLClassLoader CLASS_LOADER = (URLClassLoader) ModernMixinsService.class.getClassLoader();

    static {
        try {
            ModLauncherUtils.injectLaunchPlugin(new MixinLaunchPluginLegacy());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        LOGGER.info("ModernMixins in {}!", CLASS_LOADER);
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
    public void onLoad(IEnvironment environment, Set<String> set) {
    }

    @Override
    @SuppressWarnings("rawtypes")
    public @NotNull List<ITransformer> transformers() {
        return Collections.emptyList();
    }
}
