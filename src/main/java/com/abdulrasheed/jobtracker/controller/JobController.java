package com.abdulrasheed.jobtracker.controller;

import com.abdulrasheed.jobtracker.dto.*;
import com.abdulrasheed.jobtracker.security.CurrentUser;
import com.abdulrasheed.jobtracker.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class JobController {
    private final CurrentUser currentUser;
    private final JobService jobs;
    private final PdfReportService reports;
    public JobController(CurrentUser currentUser,JobService jobs,PdfReportService reports) {
        this.currentUser=currentUser; this.jobs=jobs; this.reports=reports;
    }
    @GetMapping("/applications") public java.util.List<JobResponse> list(HttpServletRequest req){return jobs.list(currentUser.require(req));}
    @PostMapping("/applications") public ResponseEntity<JobResponse> create(HttpServletRequest req,@Valid @RequestBody JobRequest r){
        return ResponseEntity.status(HttpStatus.CREATED).body(jobs.create(currentUser.require(req),r));
    }
    @PutMapping("/applications/{id}") public JobResponse update(HttpServletRequest req,@PathVariable long id,@Valid @RequestBody JobRequest r){
        return jobs.update(currentUser.require(req),id,r);
    }
    @DeleteMapping("/applications/{id}") public ResponseEntity<Void> delete(HttpServletRequest req,@PathVariable long id){
        jobs.delete(currentUser.require(req),id); return ResponseEntity.noContent().build();
    }
    @GetMapping("/reports/summary") public ReportSummary summary(HttpServletRequest req){return jobs.summary(currentUser.require(req));}
    @GetMapping("/reports/applications.pdf") public ResponseEntity<byte[]> pdf(HttpServletRequest req){
        byte[] data=reports.create(currentUser.require(req));
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
            .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=job-applications-report.pdf").body(data);
    }
}
