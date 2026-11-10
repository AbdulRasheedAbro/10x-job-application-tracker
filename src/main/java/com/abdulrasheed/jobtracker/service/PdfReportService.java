package com.abdulrasheed.jobtracker.service;

import com.abdulrasheed.jobtracker.model.User;
import com.abdulrasheed.jobtracker.repository.JobApplicationRepository;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;

@Service
public class PdfReportService {
    private final JobApplicationRepository jobs;
    public PdfReportService(JobApplicationRepository jobs) { this.jobs=jobs; }
    public byte[] create(User user) {
        try (PDDocument doc=new PDDocument(); ByteArrayOutputStream out=new ByteArrayOutputStream()) {
            PDPage page=new PDPage(PDRectangle.A4); doc.addPage(page);
            try (PDPageContentStream cs=new PDPageContentStream(doc,page)) {
                cs.beginText(); cs.setFont(new PDType1Font(org.apache.pdfbox.pdmodel.font.Standard14Fonts.FontName.HELVETICA_BOLD),16);
                cs.newLineAtOffset(50,780); cs.showText("Job Application Progress Report"); cs.endText();
                cs.beginText(); cs.setFont(new PDType1Font(org.apache.pdfbox.pdmodel.font.Standard14Fonts.FontName.HELVETICA),10);
                cs.newLineAtOffset(50,755); cs.showText("Account: "+user.getEmail()); cs.newLineAtOffset(0,-16);
                cs.showText("Generated: "+LocalDate.now()); cs.newLineAtOffset(0,-28);
                cs.showText("Company | Role | Status | Applied"); cs.newLineAtOffset(0,-18);
                int count=0;
                for(var j: jobs.findAllByOwnerOrderByAppliedDateDesc(user)) {
                    if(count++>=30) { cs.showText("Report limited to latest 30 records."); break; }
                    String line=(j.getCompany()+" | "+j.getJobTitle()+" | "+j.getStatus()+" | "+j.getAppliedDate())
                        .replaceAll("[^\\x20-\\x7E]","?");
                    if(line.length()>95) line=line.substring(0,95);
                    cs.showText(line); cs.newLineAtOffset(0,-15);
                }
                cs.endText();
            }
            doc.save(out); return out.toByteArray();
        } catch(IOException e) { throw new IllegalStateException("Could not create PDF report",e); }
    }
}
