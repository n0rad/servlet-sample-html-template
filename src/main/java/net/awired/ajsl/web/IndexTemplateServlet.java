package net.awired.ajsl.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.StrSubstitutor;

public class IndexTemplateServlet extends HttpServlet {

    private static final String TEMPLATE_PATH = "tplPath";

    private String tplPath;

    private static final String VERSIONING_PARAM_NAME = "versioningParamName";

    private final Properties manifestProperties = new Properties();
    private final Map<String, String> propertiesNames = new HashMap<String, String>();
    private String versioningName;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        tplPath = config.getInitParameter(TEMPLATE_PATH);
        Validate.notNull(tplPath, "{} init param must be set", TEMPLATE_PATH);
        loadPropertiesFromManifest(config);
        versioningName = config.getInitParameter(VERSIONING_PARAM_NAME);
    }

    private void loadPropertiesFromManifest(ServletConfig config) {
        @SuppressWarnings("unchecked")
        Enumeration<String> initParameterNames = config.getInitParameterNames();
        while (initParameterNames.hasMoreElements()) {
            String initParamName = initParameterNames.nextElement();
            if (initParamName.endsWith(".property")) {
                String initParamValue = config.getInitParameter(initParamName);
                String value = WarManifestUtils.getWarManifestAttribute(getServletContext(), initParamValue);
                String name = initParamNameToName(initParamName);
                propertiesNames.put(name, initParamValue);
                if (value != null) {
                    manifestProperties.put(name, value);
                }
            }
        }
    }

    private String initParamNameToName(String initParamName) {
        if (initParamName == null) {
            return null;
        }
        return initParamName.substring(0, initParamName.indexOf('.'));
    }

    private Properties getPropertiesOverridenWithSystem() {
        Properties finalProperties = new Properties(manifestProperties);
        for (String name : propertiesNames.keySet()) {
            String systemValue = System.getProperty(propertiesNames.get(name));
            if (systemValue != null) {
                finalProperties.put(name, systemValue);
            }
        }
        return finalProperties;
    }

    private String loadTemplate(String tplPath) {
        InputStream tplStream = getServletContext().getResourceAsStream(tplPath);
        if (tplStream == null) {
            return null;
        }
        try {
            return IOUtils.toString(tplStream);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot read template", e);
        }
    }

    private String findVersioning(Properties propertiesOverridenWithSystem) {
        String versionName = initParamNameToName(versioningName);
        if (versionName == null) {
            return "";
        }
        Object versionObj = propertiesOverridenWithSystem.getProperty(versionName);
        String version = "";
        if (versionObj != null) {
            version = versionObj.toString();
        }
        if (version.endsWith("-SNAPSHOT")) {
            version = "";
            //            version = String.valueOf((new Date().getTime()));
        }
        return version;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String template = loadTemplate(tplPath); // TODO do not load template each time for production
        if (template == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("contextPath", req.getContextPath());
        hashMap.put("fullWebPath", req.getRequestURL().toString().replace(req.getRequestURI(), req.getContextPath()));
        Properties propertiesOverridenWithSystem = getPropertiesOverridenWithSystem();
        fillVersioningInfo(hashMap, propertiesOverridenWithSystem);
        for (String propertyName : propertiesNames.keySet()) {
            hashMap.put(propertyName, propertiesOverridenWithSystem.getProperty(propertyName));
        }

        String response = StrSubstitutor.replace(template, hashMap);

        resp.setStatus(200);
        resp.setContentType("text/html; charset=utf-8");
        resp.getWriter().write(response);
    }

    private void fillVersioningInfo(HashMap<String, String> hashMap, Properties propertiesOverridenWithSystem) {
        String versioning = findVersioning(propertiesOverridenWithSystem);
        hashMap.put("versioning", versioning);
        if (versioning.isEmpty()) {
            hashMap.put("versioningQuery", "");
        } else {
            hashMap.put("versioningQuery", "?v=" + versioning);
        }
    }
}
