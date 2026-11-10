package com.abdulrasheed.jobtracker.repository;
import com.abdulrasheed.jobtracker.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findAllByOwnerOrderByAppliedDateDesc(User owner);
    List<JobApplication> findAllByOwnerAndFollowUpDateLessThanEqualAndStatusIn(
        User owner, LocalDate date, List<ApplicationStatus> statuses);
}
