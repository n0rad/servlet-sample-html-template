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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import javax.servlet.ServletContext;
import org.apache.commons.io.FileUtils;

public class ContextUtils {
    public static void servletContextResourceToFile(ServletContext context, String contextPath, String output)
            throws IOException {
        @SuppressWarnings("unchecked")
        Set<String> resources = context.getResourcePaths(contextPath);
        if (resources == null) {
            new File(output + contextPath).mkdirs();
            return;
        }
        for (String resource : resources) {
            InputStream resourceAsStream = context.getResourceAsStream(resource);
            if (resourceAsStream == null) {
                // this should be a directory
                servletContextResourceToFile(context, resource, output);
            } else { // this is a file
                try {
                    File full = new File(output + resource);
                    full.getParentFile().mkdirs();
                    FileUtils.copyInputStreamToFile(resourceAsStream, full);
                } finally {
                    resourceAsStream.close();
                }
            }
        }
    }
}
