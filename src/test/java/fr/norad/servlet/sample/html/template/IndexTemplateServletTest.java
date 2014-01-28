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

import static java.util.Arrays.asList;
import static java.util.Collections.enumeration;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.data.MapEntry.entry;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import java.util.Enumeration;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(WarManifestUtils.class)
public class IndexTemplateServletTest {

    @Mock
    private ServletConfig servletConfig;
    @Mock
    private ServletContext servletContext;

    @Spy
    private IndexTemplateServlet indexTemplateServlet = new IndexTemplateServlet();

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_an_exception_if_cant_find_property() {
        // GIVEN
        final String propertyName = "foo";
        final String manifestPropertyValue = null;
        final String systemPropertyValue = null;

        setUpMocks(propertyName, manifestPropertyValue, systemPropertyValue);

        // WHEN
        indexTemplateServlet.loadProperties(servletConfig);
    }

    @Test
    public void should_register_manifest_property_if_not_overriden() {
        // GIVEN
        final String propertyName = "foo";
        final String manifestPropertyValue = "value";
        final String systemPropertyValue = null;

        setUpMocks(propertyName, manifestPropertyValue, systemPropertyValue);

        // WHEN
        indexTemplateServlet.loadProperties(servletConfig);

        // THEN
        assertThat(indexTemplateServlet.getProperties()).hasSize(1);
        assertThat(indexTemplateServlet.getProperties()).contains(entry(propertyName, manifestPropertyValue));
    }

    @Test
    public void should_register_system_property_if_overriden() {
        // GIVEN
        final String propertyName = "foo";
        final String manifestPropertyValue = "value";
        final String systemPropertyValue = "anotherValue";

        setUpMocks(propertyName, manifestPropertyValue, systemPropertyValue);

        // WHEN
        indexTemplateServlet.loadProperties(servletConfig);

        // THEN
        assertThat(indexTemplateServlet.getProperties()).hasSize(1);
        assertThat(indexTemplateServlet.getProperties()).contains(entry(propertyName, systemPropertyValue));
    }

    @Test
    public void should_fallback_to_system_properties() {
        // GIVEN
        final String propertyName = "foo";
        final String manifestPropertyValue = null;
        final String systemPropertyValue = "value";

        setUpMocks(propertyName, manifestPropertyValue, systemPropertyValue);

        // WHEN
        indexTemplateServlet.loadProperties(servletConfig);

        // THEN
        assertThat(indexTemplateServlet.getProperties()).hasSize(1);
        assertThat(indexTemplateServlet.getProperties()).contains(entry(propertyName, systemPropertyValue));
    }

    private void setUpMocks(String propertyName, String manifestPropertyValue, String systemPropertyValue) {
        final String propertyNameWithExtension = propertyName + ".property";
        final String propertyNameValue = "bar";
        final Enumeration<String> initParametersName = enumeration(asList(propertyNameWithExtension));

        mockStatic(WarManifestUtils.class);

        when(servletConfig.getInitParameterNames()).thenReturn(initParametersName);
        when(servletConfig.getInitParameter(propertyNameWithExtension)).thenReturn(propertyNameValue);

        doReturn(servletContext).when(indexTemplateServlet).getServletContext();

        when(WarManifestUtils.getWarManifestAttribute(servletContext, propertyNameValue)).thenReturn
          (manifestPropertyValue);

        if(systemPropertyValue != null){
            System.setProperty(propertyNameValue, systemPropertyValue);
        }
    }
}
