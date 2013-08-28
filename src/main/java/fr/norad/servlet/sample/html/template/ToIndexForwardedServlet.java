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
import java.util.LinkedHashMap;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ToIndexForwardedServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    final LinkedHashMap<String, String> mappings = new LinkedHashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String indexMapping = config.getInitParameter("indexMappings");
        if (indexMapping == null) {
            mappings.put("/", "/");
        } else {
            String[] lines = indexMapping.split("\n");
            for (String mapping : lines) {
                String[] keyValue = mapping.split(":", 2);
                if (keyValue.length == 2) {
                    mappings.put(keyValue[0].trim(), keyValue[1].trim());
                }
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUriWithPath = (String) req.getAttribute("javax.servlet.forward.request_uri");
        String requestUri = requestUriWithPath.substring(req.getContextPath().length());
        resp.setStatus(200);
        for (String key : mappings.keySet()) {
            if (requestUri.startsWith(key)) {
                req.getRequestDispatcher(mappings.get(key)).forward(req, resp);
                return;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
