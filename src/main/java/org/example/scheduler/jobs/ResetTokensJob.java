package org.example.scheduler.jobs;

import jakarta.inject.Inject;
import org.example.entities.User;
import org.example.services.UserService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.util.List;

public class ResetTokensJob implements Job {
    @Inject
    private UserService userService;

    @Override
    public void execute(JobExecutionContext context) {
        List<User> users = userService.findAll();
        for (User user : users) {
            user.setDeleteToken(1);
            userService.update(user);
        }
        System.out.println("User delete tokens have been reset.");
    }
}
