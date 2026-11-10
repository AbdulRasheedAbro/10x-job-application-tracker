package com.abdulrasheed.jobtracker.dto;
import com.abdulrasheed.jobtracker.model.ApplicationStatus;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
public record JobRequest(@NotBlank String company, @NotBlank String jobTitle, String jobUrl,
                         ApplicationStatus status, LocalDate appliedDate, LocalDate followUpDate, String notes) {}
