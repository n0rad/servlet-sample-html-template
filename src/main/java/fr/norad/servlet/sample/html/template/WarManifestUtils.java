/**
 *
 *     Copyright (C) norad.fr
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package fr.norad.servlet.sample.html.template;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Manifest;
import javax.servlet.ServletContext;
import fr.norad.core.io.JarManifestUtils;

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
