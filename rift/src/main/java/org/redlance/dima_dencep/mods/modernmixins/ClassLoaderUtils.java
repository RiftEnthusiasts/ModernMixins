package org.redlance.dima_dencep.mods.modernmixins;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ClassLoaderUtils {
    private static Field ucpField;

    public static Object getUrlClassPath(URLClassLoader loader) throws ReflectiveOperationException {
        if (ucpField == null) {
            ucpField = URLClassLoader.class.getDeclaredField("ucp");
            ucpField.setAccessible(true);
        }

        return ucpField.get(loader); // sun or jdk?
    }

    private static Field pathField;
    private static Field urlsField;

    public static void removeUrlFromClassPath(Object ucp, URL url) throws ReflectiveOperationException {
        if (pathField == null) {
            pathField = ucp.getClass().getDeclaredField("path");
            pathField.setAccessible(true);
        }
        ((List<URL>) pathField.get(ucp)).remove(url);

        if (urlsField == null) {
            urlsField = ucp.getClass().getDeclaredField("urls");
            urlsField.setAccessible(true);
        }
        ((List<URL>) urlsField.get(ucp)).remove(url);
    }

    private static Field lmapField;
    private static Field loadersField;

    public static void removeLoaderFromClasspath(Object ucp, String url) throws ReflectiveOperationException {
        if (lmapField == null) {
            lmapField = ucp.getClass().getDeclaredField("lmap");
            lmapField.setAccessible(true);
        }
        Closeable removed = ((Map<String, Closeable>) lmapField.get(ucp)).remove(url);

        if (removed != null) {
            if (loadersField == null) {
                loadersField = ucp.getClass().getDeclaredField("loaders");
                loadersField.setAccessible(true);
            }

            ((List<Closeable>) loadersField.get(ucp)).remove(removed);

            try {
                removed.close();
            } catch (IOException ignored) {
            }
        }
    }
}
