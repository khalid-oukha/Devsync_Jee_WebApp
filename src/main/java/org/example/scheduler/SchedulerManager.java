package org.example.scheduler;

import org.example.scheduler.jobs.ResetTokensJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class SchedulerManager {
    private Scheduler scheduler;

    public void startScheduler() throws SchedulerException {
        scheduler = StdSchedulerFactory.getDefaultScheduler();

        JobDetail job = JobBuilder.newJob(ResetTokensJob.class)
                .withIdentity("resetTokensJob", "group1")
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("resetTokensTrigger", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 1 1 * ?"))
                .build();


        scheduler.scheduleJob(job, trigger);

        scheduler.start();
    }

    public void stopScheduler() throws SchedulerException {
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }
}
