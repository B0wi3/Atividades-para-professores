package com.atividades.bowie.api.controller;

import com.atividades.bowie.exception.ActivityAlreadyExistsException;
import com.atividades.bowie.model.Activity;
import com.atividades.bowie.service.ActivityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activity")
public class ActivityController {

    private ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createActivity(@RequestBody Activity activity) throws ActivityAlreadyExistsException {
        System.out.println("Atividade recebida: " + activity);
        try {
            Activity newActivity = activityService.createActivity(activity);
            return ResponseEntity.ok(newActivity);
        } catch (ActivityAlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Activity>> listAll() {
        List<Activity> activities = activityService.listAllActivities();
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Activity>> listByCategory(@PathVariable String category) {
        List<Activity> activities = activityService.listByCategory(category);
        return ResponseEntity.ok(activities);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteActivity(@PathVariable int id) {
        activityService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
