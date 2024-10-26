package com.atividades.bowie.service;

import com.atividades.bowie.exception.ActivityAlreadyExistsException;
import com.atividades.bowie.model.Activity;
import com.atividades.bowie.model.dao.ActivityDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {

    private ActivityDAO activityDAO;

    public ActivityService(ActivityDAO activityDAO) {
        this.activityDAO = activityDAO;
    }

    public Activity createActivity(Activity activity) throws ActivityAlreadyExistsException {
        if (activityDAO.findByActivityNameIgnoreCase(activity.getActivityName()).isPresent() ||
        activityDAO.findByDescriptionIgnoreCase(activity.getDescription()).isPresent()) {
            throw new ActivityAlreadyExistsException();
        }
        return activityDAO.save(activity);
    }

    public List<Activity> listAllActivities() {
        return activityDAO.findAll();
    }

    public List<Activity> listByCategory(String category) {
        return activityDAO.findActivityByCategoryIgnoreCase(category);
    }
}
