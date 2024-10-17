package org.example.scheduler;

import jakarta.inject.Inject;
import org.example.entities.User;
import org.example.services.UserService;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TokensScheduler {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Inject
    private UserService userService;


    public void startTokenScheduler() {
        scheduler.scheduleAtFixedRate(this::resetTokens, getInitialDelay(), 30 * 24 * 60 * 60, TimeUnit.SECONDS); // 30 days
    }

    private long getInitialDelay() {
        return 0;
    }

    private void resetTokens() {
        List<User> users = userService.findAll();
        for (User user : users) {
            user.setDeleteToken(1);
            userService.update(user);
        }
        System.out.println("User delete tokens have been reset.");
    }

    public void stopTokenScheduler() {
        scheduler.shutdown();
    }
}
