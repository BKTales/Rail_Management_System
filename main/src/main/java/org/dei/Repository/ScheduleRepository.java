package org.dei.Repository;

import org.dei._Train.Train;
import java.util.ArrayList;
import java.util.List;

public class ScheduleRepository {
    private static ScheduleRepository instance;
    private final List<Train> activeSchedules;

    private ScheduleRepository() {
        this.activeSchedules = new ArrayList<>();
    }

    public static ScheduleRepository getInstance() {
        if (instance == null) instance = new ScheduleRepository();
        return instance;
    }

    public void addTrain(Train t) {
        this.activeSchedules.add(t);
    }

    public void removeTrain(Train t) {
        this.activeSchedules.remove(t);
    }

    public Train getTrainById(String trainId) {
        for (Train t : activeSchedules) {
            if (t.getTrainId().equals(trainId)) {
                return t;
            }
        }
        return null;
    }

    public List<Train> getAllTrains() {
        return new ArrayList<>(activeSchedules);
    }

    public void clear() {
        this.activeSchedules.clear();
    }
}