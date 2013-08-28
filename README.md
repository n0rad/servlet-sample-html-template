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


To index forwarder
==================

	<error-page>
		<error-code>404</error-code>
		<location>/notFoundResource</location>
	</error-page>

	<servlet>
		<servlet-name>ToIndex</servlet-name>
		<servlet-class>fr.norad.servlet.sample.html.template.ToIndexForwardedServlet</servlet-class>
		<init-param>
			<param-name>indexMappings</param-name>
			<param-value>
			     /doc/ : /doc/index.html
			     / : /index.html
			</param-value>
		</init-param>
	</servlet>
	<servlet-mapping>
		<servlet-name>ToIndex</servlet-name>
		<url-pattern>/notFoundResource</url-pattern>
	</servlet-mapping>
	
	
Allow you to redirect a url to a real page.
This is usefull when you are using new html5 history feature and want to have a real page behind it.

How it work :

This create a servlet that will be mapped on the 404 error page. When you ask for a real css file for example,
since it exists, you will be able to get it. When this link point to an html5 history page (ex : /users/43/pref)
you will go to the 404 error page. and the servlet can now do the job.

In this example I have 2 real page (/index.html is the app, /doc/index.html is the doc of the app)
Based on the beginning of the URL, the forwarder know the page that should be returned.
- for /doc/api/users you will receive the /doc/index.html page
- for /users/43/pref you will receive the /index.html page

The rest of the process to display the good information will be done by the Javascript framework.  







