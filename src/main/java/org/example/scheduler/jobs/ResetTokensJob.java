package org.example.scheduler.jobs;

import org.example.entities.User;
import org.example.services.UserService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;

public class ResetTokensJob implements Job {
    private final UserService userService = new UserService();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<User> users = userService.findAll();
        for (User user : users) {
            user.setDeleteToken(1);
            userService.update(user);
        }
        System.out.println("User delete tokens have been reset.");
    }
}
