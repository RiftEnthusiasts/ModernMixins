package org.redlance.dima_dencep.mods.modernmixins;

import cpw.mods.modlauncher.LaunchPluginHandler;
import cpw.mods.modlauncher.Launcher;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import net.minecraftforge.fml.unsafe.UnsafeHacks;

import java.lang.reflect.Field;
import java.util.Map;

public class ModLauncherUtils {
    private static Field launchPluginsField;
    private static Field pluginsField;

    public static void injectLaunchPlugin(ILaunchPluginService service) throws Exception {
        if (launchPluginsField == null) {
            launchPluginsField = Launcher.class.getDeclaredField("launchPlugins");
            launchPluginsField.setAccessible(true);
        }

        LaunchPluginHandler launchPlugins = UnsafeHacks.getField(launchPluginsField, Launcher.INSTANCE);

        if (pluginsField == null) {
            pluginsField = LaunchPluginHandler.class.getDeclaredField("plugins");
            pluginsField.setAccessible(true);
        }

        UnsafeHacks.<Map<String, ILaunchPluginService>>getField(pluginsField, launchPlugins)
                .put(service.name(), service);
    }
}
