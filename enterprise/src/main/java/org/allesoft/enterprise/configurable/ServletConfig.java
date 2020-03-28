package org.allesoft.enterprise.configurable;

import javax.servlet.http.HttpServlet;

public class ServletConfig {
    private final String name;
    private final HttpServlet servlet;
    private final String path;

    public ServletConfig(String name, HttpServlet servlet, String path) {
        this.name = name;
        this.servlet = servlet;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public HttpServlet getServlet() {
        return servlet;
    }

    public String getPath() {
        return path;
    }
}
