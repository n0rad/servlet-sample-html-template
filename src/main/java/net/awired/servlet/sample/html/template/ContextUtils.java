package net.awired.servlet.sample.html.template;

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
