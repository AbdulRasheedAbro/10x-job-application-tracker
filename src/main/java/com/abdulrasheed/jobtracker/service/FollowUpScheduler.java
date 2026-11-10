package com.abdulrasheed.jobtracker.service;

import com.abdulrasheed.jobtracker.model.*;
import com.abdulrasheed.jobtracker.repository.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

@Component
public class FollowUpScheduler {
    private static final Logger log=Logger.getLogger(FollowUpScheduler.class.getName());
    private final UserRepository users;
    private final JobApplicationRepository jobs;
    public FollowUpScheduler(UserRepository users, JobApplicationRepository jobs) { this.users=users; this.jobs=jobs; }
    @Scheduled(fixedDelayString="${app.reminder.fixed-delay-ms:60000}")
    public void reportDueFollowUps() {
        for(User user: users.findAll()) {
            List<JobApplication> due=jobs.findAllByOwnerAndFollowUpDateLessThanEqualAndStatusIn(
                user,LocalDate.now(),List.of(ApplicationStatus.APPLIED,ApplicationStatus.INTERVIEW));
            if(!due.isEmpty()) log.info("Follow-ups due for "+user.getEmail()+": "+due.size());
        }
    }
}
