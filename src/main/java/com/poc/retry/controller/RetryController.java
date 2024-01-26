package com.poc.retry.controller;

import com.poc.retry.annotation.Retry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RetryController {

    public Map<Integer, String> randomMap;
    public static int sno;

    public RetryController() {
        randomMap = new HashMap<>();
        sno = 1;
    }

    @PostMapping("/add")
    @Retry(times = 5, initialDelay = 1, initialDelayTimeUnit = ChronoUnit.MINUTES, retryInterval = 5)
    public ResponseEntity addSomething(@RequestBody String body) {
        int n = sno/0;
        randomMap.put(sno++, body);

        return ResponseEntity.ok(randomMap);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity getSomething(@PathVariable("sno") int sno) {
        return ResponseEntity.ok(randomMap.get(sno));
    }

}
