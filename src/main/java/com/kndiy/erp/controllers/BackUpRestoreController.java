package com.kndiy.erp.controllers;

import com.kndiy.erp.others.MismatchedUnitException;
import com.kndiy.erp.services.backupRestore.BackupService;
import com.kndiy.erp.services.backupRestore.RestoreService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class BackUpRestoreController {
    @Autowired
    private BackupService backupService;
    @Autowired
    private RestoreService restoreService;

    @PostMapping("/backup")
    public void backup(HttpServletResponse response, RedirectAttributes redirectAttributes) throws IOException, IllegalAccessException {

        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm");
        String timeString = dtf.format(time);

        response.setContentType("multipart/form-data");
        response.setHeader("Content-disposition", "attachment; filename=backup_" + timeString + ".txt");
        response.setCharacterEncoding("UTF-8");

        OutputStream outputStream = response.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new PrintWriter(outputStream));

        backupService.backup(writer);

        writer.close();
        response.flushBuffer();
    }

    @PostMapping("/restore")
    public String restore(@RequestParam("backup") MultipartFile backupFile, RedirectAttributes redirectAttributes) throws IOException, MismatchedUnitException {

        List<String> results = new ArrayList<>();
        restoreService.restore(results, backupFile);
        redirectAttributes.addFlashAttribute("results", results);

        return "redirect:/";
    }

}
