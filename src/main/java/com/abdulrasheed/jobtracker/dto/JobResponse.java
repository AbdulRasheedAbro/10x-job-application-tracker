package com.abdulrasheed.jobtracker.dto;
import com.abdulrasheed.jobtracker.model.*;
import java.time.LocalDate;
public record JobResponse(Long id, String company, String jobTitle, String jobUrl, ApplicationStatus status,
                          LocalDate appliedDate, LocalDate followUpDate, String notes) {
    public static JobResponse from(JobApplication j) {
        return new JobResponse(j.getId(), j.getCompany(), j.getJobTitle(), j.getJobUrl(), j.getStatus(),
            j.getAppliedDate(), j.getFollowUpDate(), j.getNotes());
    }
}
