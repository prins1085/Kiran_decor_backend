package com.quotepro;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cron")
public class CronController {

    @GetMapping("/run")
    public ResponseEntity<String> runCronJob() {
        // ✅ Your cron job logic goes here
        System.out.println("✅ Cron job triggered at: " + java.time.LocalDateTime.now());

        // You can call services, database logic, etc. here.
        return ResponseEntity.ok("✅ Job executed successfully");
    }
}
