servlet-sample-html-template
============================

Provide servlet for sample html templating without jsp


Sample html template
====================

    <servlet>
        <servlet-name>docServlet</servlet-name>
        <servlet-class>fr.norad.servlet.sample.html.template.IndexTemplateServlet</servlet-class>
        <init-param>
            <param-name>tplPath</param-name>
            <param-value>/doc/index.html</param-value>
        </init-param>
        <init-param>
            <param-name>contextPathSuffix</param-name>
            <param-value>/doc</param-value>
        </init-param>
        <init-param>
            <param-name>version.property</param-name>
            <param-value>HousecreamVersion</param-value>
        </init-param>
        <init-param>
            <param-name>hcWsUrl.property</param-name>
            <param-value>HousecreamWebServiceUrl</param-value>
        </init-param>
    </servlet>
    <servlet-mapping>
        <servlet-name>docServlet</servlet-name>
        <url-pattern>/doc/index.html</url-pattern>
    </servlet-mapping>
	
This will create a servlet at /doc/index.html. When it's called, the /doc/index.html file of your WAR will be loaded as template
and ${} will be replaced.

In this example, this var will be replace in the template :
- ${contextPath} : contextPath + /doc (the suffix). Usefull for <base href="${contextPath}" /> 
- ${fullWebPath} : full web url to the root of your application (http://...). Usefull for logo link
- ${version} : will be replaced by the 'HousecreamVersion' manifest.mf property value
- ${hcWsUrl} :will be replaced by the 'HousecreamWebServiceUrl' manifest.mf property value


