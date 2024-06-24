package org.redlance.dima_dencep.mods.modernmixins.utils;

import cpw.mods.modlauncher.TransformingClassLoader;
import org.redlance.dima_dencep.mods.modernmixins.ModernMixinsService;
import org.spongepowered.asm.launch.platform.container.ContainerHandleURI;
import org.spongepowered.asm.launch.platform.container.IContainerHandle;
import org.spongepowered.asm.service.MixinService;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;

public class MixinsClassLoader extends URLClassLoader {
    private static final Field PARENT_FIELD;

    static {
        try {
            PARENT_FIELD = ClassLoader.class.getDeclaredField("parent");
            PARENT_FIELD.setAccessible(true);
        } catch (Throwable th) {
            throw new RuntimeException(th);
        }

        ClassLoader.registerAsParallelCapable();
    }

    private static final MixinsClassLoader INSTANCE = new MixinsClassLoader();

    private MixinsClassLoader() {
        super(new URL[0], ModernMixinsService.CLASS_LOADER);

        iterateContainers(MixinService.getService().getMixinContainers());
    }

    private void iterateContainers(Collection<IContainerHandle> containerHandles) {
        for (IContainerHandle containerHandle : containerHandles) {
            if (containerHandle instanceof ContainerHandleURI) {
                try {
                    URL url = ((ContainerHandleURI) containerHandle).getURI().toURL();

                    ModernMixinsService.LOGGER.info("Adding {} to modernmixins...", url);
                    addURL(url);
                } catch (Throwable e) {
                    ModernMixinsService.LOGGER.error("Failed to add {}!", containerHandle, e);
                }
            }

            Collection<IContainerHandle> other = containerHandle.getNestedContainers();
            if (!other.isEmpty())
                iterateContainers(other);
        }
    }

    private static void setParentClassLoader(ClassLoader base, ClassLoader parent) {
        try {
            PARENT_FIELD.set(base, parent);
        } catch (Throwable th) {
            ModernMixinsService.LOGGER.warn("Failed to set parent!", th);
        }
    }

    private static boolean HACKED;

    public static void hackClassLoaders(TransformingClassLoader transformingClassLoader) {
        if (HACKED || transformingClassLoader == null) {
            return;
        }

        MixinsClassLoader.setParentClassLoader(ModernMixinsService.CLASS_LOADER, transformingClassLoader.getParent());
        MixinsClassLoader.setParentClassLoader(transformingClassLoader, MixinsClassLoader.INSTANCE);

        HACKED = true;
    }
}
