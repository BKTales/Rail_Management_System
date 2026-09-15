package org.dei._Location;

import org.dei._Facilities.Facility;
import org.dei._Path.Path;
import org.dei._Train.Train;

import java.time.LocalDateTime;
import java.util.HashMap;

public class Arrival {
    // Inicialização direta para evitar NullPointerException
    private HashMap<Facility, LocalDateTime> arrival = new HashMap<>();
    private HashMap<Facility, LocalDateTime> estimatedArrival = new HashMap<>();
    private final Train train;
    private LocalDateTime finalArrivalTime; // Campo simples para acesso rápido

    public Arrival(Train train) {
        this.train = train;
    }

    // Usado pelo simulador para guardar parciais
    public void addEstimatedArrival(Facility f, LocalDateTime time) {
        if (this.estimatedArrival == null) this.estimatedArrival = new HashMap<>();
        this.estimatedArrival.put(f, time);
    }

    // Setter para o tempo final total
    public void setArrivalTime(LocalDateTime time) {
        this.finalArrivalTime = time;
    }

    // O getter que o Controller chama
    public LocalDateTime getArrivalTime() {
        // Se tivermos o valor final direto, devolvemos
        if (finalArrivalTime != null) return finalArrivalTime;

        // Senão, tentamos descobrir pelo mapa
        if (estimatedArrival == null || estimatedArrival.isEmpty()) return null;

        if (train.getRoute() != null && train.getRoute().getPath() != null) {
            Facility end = train.getRoute().getPath().getEndFacility();
            return estimatedArrival.get(end);
        }
        return null;
    }

    public HashMap<Facility, LocalDateTime> getArrival() { return arrival; }
    public Train getTrain() { return train; }

    public void setArrival(HashMap<Facility, LocalDateTime> arrival) {
        this.arrival = arrival;
    }

    public void trainArrived(Facility f, LocalDateTime time) {
        if (this.arrival == null) this.arrival = new HashMap<>();
        arrival.put(f, time);
    }



    @Override
    public String toString() {
        return "Arrival{" + "train=" + (train != null ? train.getTrainId() : "null") + '}';
    }
}