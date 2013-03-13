<h1>Java Servlet Proxy</h1>

<p>A proxy Connection written in Java using the Servlet technology.</p>

<h2>Features</h2>

<ol>
<li>Proxy Connection</li>
<li>Caching the requested Files</li>
<li>Transformations of the sites via jquery-selectors</li>
</ol>

<h2>Requirements</h2>

<ol>
<li>Java 1.6</li>
<li>Servlet Container which supplies Servlet 3.0 Api like Tomcat 7 or Jetty 8</li>
</ol>

<h2>Details</h2>

<ol>
<li><p>Proxy Connection</p>

<p>The Servlet ProxyServlet uses the jdk class UrlConnection for handling the requests. In the html of the requests the links to other sites are rewitten to urls that managed by the ProxyServlet for an example the link "/subpage.html" on the site http://www.example.com is rewritten to http://localhost:8080/webproxy/test?targetUrl=http://www.example.com/subpage.html</p></li>
</ol>
