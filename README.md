Java Servlet Proxy
==================

A Webproxy written in Java using the Servlet technology.

Features
--------

1.    Proxy-Connection
2.    Caching requested files
3.    Manipulating sites with jquery Selectors


Requirements
------------

1.    Java 1.6
2.    Servlet Container which supplies Servlet 3.0 Api like Tomcat 7 or Jetty 8

Details
-----------

1.    Proxy Connection

      The Servlet ProxyServlet uses the jdk class UrlConnection for handling the requests. In the html of the requests the links to other sites are rewitten to urls that managed by the ProxyServlet. For an example the link "/subpage.html" on the site http://www.example.com is rewritten to http://localhost:8080/webproxy/proxy?targetUrl=http://www.example.com/subpage.html. Link to resources in html-files are rewritten in similliary way.

2.    Caching requested files
      There are two caches 
      * the file cache saves the visited files in the directory "htmlFileCache". an overview over the filecache can be seen at FileCache.xhtml
      * the memory cache can be seen at MemoryCache.xhtml

3.    Transformations of the sites via jquery-selectors
      
      Transformations to the requested sites can be declared in the file transformations.txt. The sytax of each transformation is : css-Selektor -> JQueryMethod [-> Parameters]*
     
