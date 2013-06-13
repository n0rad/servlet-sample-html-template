package net.awired.servlet.sample.html.template;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Manifest;
import javax.servlet.ServletContext;
import net.awired.core.io.JarManifestUtils;

public class WarManifestUtils {

    public static String getWarManifestAttribute(ServletContext servletContext, String attributeName) {
        String fromJarValue = JarManifestUtils.getManifestAttribute(attributeName); // jetty
        if (fromJarValue != null) {
            return fromJarValue;
        }
        InputStream manifestIn = servletContext.getResourceAsStream("META-INF/MANIFEST.MF");
        if (manifestIn != null) { // tomcat
            try {
                Manifest manifest = new Manifest(manifestIn);
                String versionManifest = manifest.getMainAttributes().getValue(attributeName);
                if (versionManifest != null) {
                    return versionManifest;
                }
            } catch (IOException e) {
            } finally {
                try {
                    manifestIn.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }
}
