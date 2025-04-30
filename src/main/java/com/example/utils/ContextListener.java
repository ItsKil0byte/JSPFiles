package com.example.utils;

import com.example.database.DBService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.SQLException;

@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            DBService service = new DBService();
            service.init();
            service.printConnectionInfo();
            sce.getServletContext().setAttribute("service", service);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
