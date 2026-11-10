package com.abdulrasheed.jobtracker.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "job_applications")
public class JobApplication {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false) private User owner;
    @Column(nullable = false) private String company;
    @Column(nullable = false) private String jobTitle;
    private String jobUrl;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private ApplicationStatus status;
    private LocalDate appliedDate;
    private LocalDate followUpDate;
    @Column(length = 2000) private String notes;
    protected JobApplication() {}
    public JobApplication(User owner, String company, String jobTitle, String jobUrl, ApplicationStatus status,
                          LocalDate appliedDate, LocalDate followUpDate, String notes) {
        this.owner=owner; this.company=company; this.jobTitle=jobTitle; this.jobUrl=jobUrl;
        this.status=status; this.appliedDate=appliedDate; this.followUpDate=followUpDate; this.notes=notes;
    }
    public Long getId(){return id;} public User getOwner(){return owner;} public String getCompany(){return company;}
    public String getJobTitle(){return jobTitle;} public String getJobUrl(){return jobUrl;}
    public ApplicationStatus getStatus(){return status;} public LocalDate getAppliedDate(){return appliedDate;}
    public LocalDate getFollowUpDate(){return followUpDate;} public String getNotes(){return notes;}
    public void update(String company, String jobTitle, String jobUrl, ApplicationStatus status,
                       LocalDate appliedDate, LocalDate followUpDate, String notes) {
        this.company=company; this.jobTitle=jobTitle; this.jobUrl=jobUrl; this.status=status;
        this.appliedDate=appliedDate; this.followUpDate=followUpDate; this.notes=notes;
    }
}
