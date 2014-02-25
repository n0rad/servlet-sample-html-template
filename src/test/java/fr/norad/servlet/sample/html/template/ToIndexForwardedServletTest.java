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

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Iterator;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Ignore;
import org.junit.Test;

public class ToIndexForwardedServletTest {

    @Test
    public void should_fill_mapping() throws Exception {
        ToIndexForwardedServlet servlet = new ToIndexForwardedServlet();
        ServletConfig config = mock(ServletConfig.class);
        when(config.getInitParameter("indexMappings")).thenReturn(
                "\n" + " /doc : /doc/index.html \n\n" + " / : /index.html \n\n");

        servlet.init(config);

        assertThat(servlet.mappings).hasSize(2);
        Iterator<String> iterator = servlet.mappings.keySet().iterator();
        assertThat(iterator.next()).isEqualTo("/doc");
        assertThat(iterator.next()).isEqualTo("/");
        assertThat(servlet.mappings.get("/doc")).isEqualTo("/doc/index.html");
        assertThat(servlet.mappings.get("/")).isEqualTo("/index.html");
    }

    @Test
    public void should_forward_to_closest_resource() throws Exception {
        ToIndexForwardedServlet servlet = new ToIndexForwardedServlet();
        ServletConfig config = mock(ServletConfig.class);
        when(config.getInitParameter("indexMappings")).thenReturn(
                "\n" + " /doc : /doc/index.html \n\n" + " / : /index.html \n\n");
        servlet.init(config);
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(req.getAttribute("javax.servlet.forward.request_uri")).thenReturn("/doc/genre/style");
        when(req.getContextPath()).thenReturn("");
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(req.getRequestDispatcher("/doc/index.html")).thenReturn(dispatcher);

        servlet.doGet(req, resp);

        verify(dispatcher).forward(req, resp);
    }

    @Test
    public void should_forward_to_root_for_other_dir_resource() throws Exception {
        ToIndexForwardedServlet servlet = new ToIndexForwardedServlet();
        ServletConfig config = mock(ServletConfig.class);
        when(config.getInitParameter("indexMappings")).thenReturn(
                "\n" + " /doc/ : /doc/index.html \n\n" + " / : /index.html \n\n");
        servlet.init(config);
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(req.getAttribute("javax.servlet.forward.request_uri")).thenReturn("/genre/style/ouda");
        when(req.getContextPath()).thenReturn("");
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(req.getRequestDispatcher("/index.html")).thenReturn(dispatcher);

        servlet.doGet(req, resp);

        verify(dispatcher).forward(req, resp);
    }

    @Test
    public void should_support_sub_context_path() throws Exception {
        ToIndexForwardedServlet servlet = new ToIndexForwardedServlet();
        ServletConfig config = mock(ServletConfig.class);
        when(config.getInitParameter("indexMappings")).thenReturn(
                "\n" + " /doc : /doc/index.html \n\n" + " / : /index.html \n\n");
        servlet.init(config);
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(req.getAttribute("javax.servlet.forward.request_uri")).thenReturn("/path/doc/genre/style");
        when(req.getContextPath()).thenReturn("/path");
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(req.getRequestDispatcher("/doc/index.html")).thenReturn(dispatcher);

        servlet.doGet(req, resp);

        verify(dispatcher).forward(req, resp);
    }
}
