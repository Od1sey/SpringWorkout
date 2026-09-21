package com.server;

import com.config.SpringWebConfig;
import com.web.filter.RequestIdFilter;
import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public final class WebServer {

    private static final int PORT = 4000;

    private WebServer() {
    }

    public static void start() throws Exception {
        Tomcat tomcat = new Tomcat();

        tomcat.setPort(PORT);

        String baseDir = System.getProperty("java.io.tmpdir");
        tomcat.setBaseDir(baseDir);

        Context tomcatContext = tomcat.addContext("", baseDir);

        FilterDef filterDef = new FilterDef();
        filterDef.setFilterName("requestIdFilter");
        filterDef.setFilter(new RequestIdFilter());
        tomcatContext.addFilterDef(filterDef);

        FilterMap filterMap = new FilterMap();
        filterMap.setFilterName("requestIdFilter");
        filterMap.addURLPattern("/*");
        tomcatContext.addFilterMap(filterMap);

        AnnotationConfigWebApplicationContext springContext = new AnnotationConfigWebApplicationContext();

        springContext.register(SpringWebConfig.class);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(springContext);

        Wrapper dispatcherWrapper = Tomcat.addServlet(
                tomcatContext,
                "dispatcherServlet",
                dispatcherServlet
        );

        dispatcherWrapper.setLoadOnStartup(1);
        tomcatContext.addServletMappingDecoded("/", "dispatcherServlet");
        tomcat.getConnector();
        tomcat.start();
        tomcat.getServer().await();
    }
}
