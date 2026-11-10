package com.abdulrasheed.jobtracker.service;

import com.abdulrasheed.jobtracker.dto.*;
import com.abdulrasheed.jobtracker.model.*;
import com.abdulrasheed.jobtracker.repository.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class JobService {
    private final JobApplicationRepository jobs;
    public JobService(JobApplicationRepository jobs) { this.jobs=jobs; }

    public List<JobResponse> list(User owner) {
        return jobs.findAllByOwnerOrderByAppliedDateDesc(owner).stream().map(JobResponse::from).toList();
    }
    @Transactional
    @CacheEvict(value="summary", key="#owner.id")
    public JobResponse create(User owner, JobRequest r) {
        JobApplication j=new JobApplication(owner,r.company().trim(),r.jobTitle().trim(),r.jobUrl(),
            r.status()==null?ApplicationStatus.SAVED:r.status(),
            r.appliedDate()==null?LocalDate.now():r.appliedDate(),r.followUpDate(),r.notes());
        return JobResponse.from(jobs.save(j));
    }
    @Transactional
    @CacheEvict(value="summary", key="#owner.id")
    public JobResponse update(User owner, long id, JobRequest r) {
        JobApplication j=owned(owner,id);
        j.update(r.company().trim(),r.jobTitle().trim(),r.jobUrl(),
            r.status()==null?j.getStatus():r.status(),r.appliedDate()==null?j.getAppliedDate():r.appliedDate(),
            r.followUpDate(),r.notes());
        return JobResponse.from(j);
    }
    @Transactional
    @CacheEvict(value="summary", key="#owner.id")
    public void delete(User owner, long id) { jobs.delete(owned(owner,id)); }
    private JobApplication owned(User owner,long id) {
        JobApplication j=jobs.findById(id).orElseThrow(()->new IllegalArgumentException("Application not found"));
        if(!j.getOwner().getId().equals(owner.getId())) throw new SecurityException("You cannot access this application");
        return j;
    }
    @Cacheable(value="summary", key="#owner.id")
    public ReportSummary summary(User owner) {
        List<JobApplication> all=jobs.findAllByOwnerOrderByAppliedDateDesc(owner);
        return new ReportSummary(all.size(),
            all.stream().filter(j->j.getStatus()==ApplicationStatus.APPLIED).count(),
            all.stream().filter(j->j.getStatus()==ApplicationStatus.INTERVIEW).count(),
            all.stream().filter(j->j.getStatus()==ApplicationStatus.OFFER).count(),
            all.stream().filter(j->j.getStatus()==ApplicationStatus.REJECTED).count(),
            all.stream().filter(j->j.getFollowUpDate()!=null && !j.getFollowUpDate().isAfter(LocalDate.now())
                && List.of(ApplicationStatus.APPLIED,ApplicationStatus.INTERVIEW).contains(j.getStatus())).count());
    }
}
