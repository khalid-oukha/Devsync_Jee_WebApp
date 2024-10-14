package org.example.listeners;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.scheduler.SchedulerManager;
import org.quartz.SchedulerException;

@WebListener
public class AppContextListener implements ServletContextListener {
    private SchedulerManager schedulerManager;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        schedulerManager = new SchedulerManager();
        try {
            schedulerManager.startScheduler();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            schedulerManager.stopScheduler();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
