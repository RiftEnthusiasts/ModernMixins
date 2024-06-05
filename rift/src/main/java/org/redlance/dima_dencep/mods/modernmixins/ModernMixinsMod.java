package org.redlance.dima_dencep.mods.modernmixins;

import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dimdev.riftloader.listener.InitializationListener;
import org.spongepowered.asm.mixin.Mixins;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class ModernMixinsMod implements InitializationListener {
    public static final Logger LOGGER = LogManager.getLogger("modern-mixins");

    @Override
    public void onInitialization() {
        Mixins.addConfiguration("mixins.modernmixins.json");
    }

    static {
        Launch.classLoader.addClassLoaderExclusion("org.objectweb.asm.");
        Launch.classLoader.addClassLoaderExclusion("org.spongepowered.asm.");
        Launch.classLoader.addClassLoaderExclusion("org.spongepowered.tools.");
        Launch.classLoader.addClassLoaderExclusion("org.spongepowered.include.");

        URLClassLoader classLoader = (URLClassLoader) LaunchClassLoader.class.getClassLoader();

        Object ucp;
        try {
            ucp = ClassLoaderUtils.getUrlClassPath(classLoader);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        List<URL> conflictLibs = findConflictLibs(classLoader);
        if (!conflictLibs.isEmpty()) {

            for (URL lib : conflictLibs) {
                fullyRemove(ucp, lib);
            }
        } else {
            LOGGER.fatal("Env is correct!");
            fullyRemove(ucp, ModernMixinsMod.class.getProtectionDomain().getCodeSource().getLocation());
        }
    }

    public static void fullyRemove(Object ucp, URL lib) {
        LOGGER.info("Removing {} from classpath...", lib);

        try {
            ClassLoaderUtils.removeUrlFromClassPath(ucp, lib);
            ClassLoaderUtils.removeLoaderFromClasspath(ucp,
                    lib.toString().replaceFirst("file:/", "file:///")
            );
        } catch (Throwable e) {
            LOGGER.error("Failed to remove {} from classpath!", lib, e);
        }
    }

    public static List<URL> findConflictLibs(URLClassLoader loader) {
        return Arrays.stream(loader.getURLs())
                .filter(url -> url.getFile().contains("mixin-0.7.11") || (url.getFile().contains("asm-") && url.getFile().endsWith("-6.2.jar")))
                .collect(Collectors.toList());
    }
}
