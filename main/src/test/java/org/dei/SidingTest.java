package org.dei;

import org.dei.Sprint1._Item.Box;
import org.dei.Sprint1._Item.Item;
import org.dei.Sprint1._Item.ItemType;
import org.dei.Sprint1._Item.Unit;
import org.dei.Sprint2.Country;
import org.dei.Sprint2.TimeZone;
import org.dei.Sprint2.TimeZoneGroup;
import org.dei._Facilities.Facility;
import org.dei._Location.GeographicalLocation;
import org.dei._Path.Path;
import org.dei._Path.Route;
import org.dei._RailLineNetwork.RailLine;
import org.dei._RailLineNetwork.RailSegment;
import org.dei._RailLineNetwork.Siding;
import org.dei._RailLineNetwork.TracksFullException;
import org.dei._Time.TimeInSegment;
import org.dei._Train.*;
import org.junit.Test;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SidingTest {
    @Test
    public void testUltrapassagemSiding() throws Exception  {
        Facility stA = new Facility(null, null, null, null, "Estação A", 1);
        Facility stB = new Facility(null, null, null, null, "Estação B", 2);
        RailSegment segment = new RailSegment("Seg-1", 10000, 400, 1, true, 8000); // 10000m
        Siding siding = new Siding("Siding-Aux", 10000, 400, false, 8000, 0);
        segment.setSiding(siding);
        stA.createConnection(stB, new RailLine("Linha 1", stA, stB, List.of(segment)));
        Path path = new Path(List.of(stA, stB), stA, stB, "Path-Simples");
        LocomotiveModel slowModel = new LocomotiveModel(5600, 19.2, 3, 4.375, 30, 30, "M", "P", LocomotiveType.DIESEL, "iberian");
        LocomotiveModel fastModel = new LocomotiveModel(5600, 19.2, 3, 4.375, 30, 220, "M", "P", LocomotiveType.DIESEL, "iberian");

        LocalDateTime depSlow = LocalDateTime.of(2025, 10, 8, 10, 0, 0);
        Train slowTrain = new Train("SlowLoco", new Route(path, 1, new ArrayList<>(), depSlow), depSlow);
        slowTrain.addLocomotive(new Locomotive("L-30", 1900, 2, "Alex", slowModel, 30));


        LocalDateTime arrivalSlow = slowTrain.calculateArrivalTime(depSlow, false);


        LocalDateTime depFast = LocalDateTime.of(2025, 10, 8, 10, 5, 0);
        Train fastTrain = new Train("FastLoco", new Route(path, 1, new ArrayList<>(), depFast), depFast);
        fastTrain.addLocomotive(new Locomotive("L-100", 1900, 2, "Alex", fastModel, 220));


        LocalDateTime arrivalFast = fastTrain.calculateArrivalTime(depFast, false);

        System.out.println("\n--- DETALHE DO TRAINF NO SIDING ---");
        for (TimeInSegment tis : fastTrain.getCalculatedSegmentTimes()) {
            System.out.println("Início: " + tis.getStartTime() + " | Fim: " + tis.getEndTime());
        }

        System.out.println("\n--- DETALHE DO TRAINS NO SIDING ---");
        for (TimeInSegment tis : slowTrain.getCalculatedSegmentTimes()) {
            System.out.println("Início: " + tis.getStartTime() + " | Fim: " + tis.getEndTime());
        }

        assertTrue(arrivalFast.isBefore(arrivalSlow),
                "O comboio rápido deve chegar antes do lento.");


        // Verificamos se chega antes do dia 11 (dando margem para a partida dia 8)
        assertTrue(arrivalFast.isBefore(LocalDateTime.of(2025, 10, 11, 0, 0, 0)),
                "O tempo de viagem está acima do esperado para 220km/h.");

        System.out.println("SUCESSO: O Rápido cortou " +
                java.time.Duration.between(arrivalFast, arrivalSlow).toDays() + " dias ao lento!");

    }

    @Test
    public void testSidingEsperaPorSeguranca() throws Exception  {
        Facility stA = new Facility(null, null, null, null, "Estação A", 1);
        Facility stB = new Facility(null, null, null, null, "Estação B", 2);

        double distancia = 100;
        RailSegment segment = new RailSegment("Seg-1", distancia, 400, 1, true, 8000);
        Siding siding = new Siding("Siding-Aux", distancia, 400, false, 8000, 0);
        segment.setSiding(siding);
        stA.createConnection(stB, new RailLine("Linha 1", stA, stB, List.of(segment)));
        Path path = new Path(List.of(stA, stB), stA, stB, "Path-Simples");

        LocomotiveModel m50 = new LocomotiveModel(5000, 20, 3, 4, 50, 50, "M", "P", LocomotiveType.DIESEL, "iberian");
        LocomotiveModel m100 = new LocomotiveModel(5000, 20, 3, 4, 100, 100, "M", "P", LocomotiveType.DIESEL, "iberian");

        // 1. LENTO (10:00 -> 12:00)
        LocalDateTime depSlow = LocalDateTime.of(2025, 10, 8, 10, 0, 0);
        Train slowTrain = new Train("Lento", new Route(path, 1, new ArrayList<>(), depSlow), depSlow);
        slowTrain.addLocomotive(new Locomotive("L50", 2000, 2, "A", m50, 50));
        slowTrain.calculateArrivalTime(depSlow, false);

        // 2. RÁPIDO (11:00:10 -> Tentaria 12:00:10)
        LocalDateTime depFast = LocalDateTime.of(2025, 10, 8, 11, 0, 10);
        Train fastTrain = new Train("Rapido", new Route(path, 1, new ArrayList<>(), depFast), depFast);
        fastTrain.addLocomotive(new Locomotive("L100", 2000, 2, "A", m100, 100));

        LocalDateTime arrivalFast = fastTrain.calculateArrivalTime(depFast, false);


        System.out.println("Rápido tentava sair às: 12:00:10");
        System.out.println("Rápido saiu efetivamente às: " + arrivalFast);



        assertTrue(arrivalFast.isAfter(LocalDateTime.of(2025, 10, 8, 12, 0, 31)),
                "O rápido devia ter esperado os 30s de margem de segurança!");

        assertTrue(arrivalFast.isBefore(LocalDateTime.of(2025, 10, 8, 12, 0, 40)),
                "O tempo de chegada está demasiado longe para um comboio sem wagons.");
    }

    @Test
    public void testSidingSemAtrasoQuandoDistanciaESegura() throws Exception  {
        Facility stA = new Facility(null, null, null, null, "Estação A", 1);
        Facility stB = new Facility(null, null, null, null, "Estação B", 2);

        double distancia = 100;
        RailSegment segment = new RailSegment("Seg-1", distancia, 400, 1, true, 8000);
        Siding siding = new Siding("Siding-Aux", distancia, 400, false, 8000, 0);
        segment.setSiding(siding);
        stA.createConnection(stB, new RailLine("Linha 1", stA, stB, List.of(segment)));
        Path path = new Path(List.of(stA, stB), stA, stB, "Path-Simples");

        LocomotiveModel m50 = new LocomotiveModel(5000, 20, 3, 4, 50, 50, "M", "P", LocomotiveType.DIESEL, "iberian");
        LocomotiveModel m100 = new LocomotiveModel(5000, 20, 3, 4, 100, 100, "M", "P", LocomotiveType.DIESEL, "iberian");


        // 1. LENTO (Parte às 10:00 -> Chegada Frente às 12:00:00)
        LocalDateTime depSlow = LocalDateTime.of(2025, 10, 8, 10, 0, 0);
        Train slowTrain = new Train("Lento", new Route(path, 1, new ArrayList<>(), depSlow), depSlow);
        slowTrain.addLocomotive(new Locomotive("L50", 2000, 2, "A", m50, 50));
        slowTrain.calculateArrivalTime(depSlow, false);

        // 2. RÁPIDO (Parte às 11:10:00 -> Chegada Ideal às 12:10:00)
        // A via já está livre desde as 12:00:32.
        // Como 12:10:00 é muito depois do tempo de segurança, o tempo NÃO deve mudar.
        LocalDateTime depFast = LocalDateTime.of(2025, 10, 8, 11, 10, 0);
        Train fastTrain = new Train("Rapido", new Route(path, 1, new ArrayList<>(), depFast), depFast);
        fastTrain.addLocomotive(new Locomotive("L100", 2000, 2, "A", m100, 100));

        LocalDateTime arrivalFast = fastTrain.calculateArrivalTime(depFast, false);


        System.out.println("Lento libertou a via às: ~12:00:32");
        System.out.println("Rápido chegou efetivamente às: " + arrivalFast);

        // ASSERT: O tempo de chegada deve ser exatamente as 12:10 (mais a sua cauda)
        assertEquals(12, arrivalFast.getHour());
        assertEquals(10, arrivalFast.getMinute());
        assertEquals(0, arrivalFast.getSecond());

        assertTrue(arrivalFast.getNano() > 0, "Deve incluir o tempo da própria cauda.");
    }

    @Test
    public void testSidingEsperaPorSegurancaComWagon() throws Exception  {
        Facility stA = new Facility(null, null, null, null, "Estação A", 1);
        Facility stB = new Facility(null, null, null, null, "Estação B", 2);

        Wagon wagon1 = new Wagon("Wagon1");
        Wagon wagon2 = new Wagon("Wagon2");
        Wagon wagon3 = new Wagon("Wagon3");
        WagonModel model1 = new WagonModel("WagonModel1",9640,4165.5,3120,20,100,"Gauge1",20);
        wagon1.setWagonModel(model1);
        wagon2.setWagonModel(model1);
        wagon3.setWagonModel(model1);
        List<Wagon> wagons = new ArrayList<>();
        wagons.add(wagon1);
        wagons.add(wagon2);
        wagons.add(wagon3);
        Freight freight = new Freight("a", stA,stB,wagons);
        List<Freight> freights = new ArrayList<>();
        freights.add(freight);

        double distancia = 100;
        RailSegment segment = new RailSegment("Seg-1", distancia, 400, 1, true, 8000);
        Siding siding = new Siding("Siding-Aux", distancia, 400, false, 8000, 0);
        segment.setSiding(siding);
        stA.createConnection(stB, new RailLine("Linha 1", stA, stB, List.of(segment)));
        Path path = new Path(List.of(stA, stB), stA, stB, "Path-Simples");

        LocomotiveModel m50 = new LocomotiveModel(5000, 20, 3, 4, 50, 50, "M", "P", LocomotiveType.DIESEL, "iberian");
        LocomotiveModel m100 = new LocomotiveModel(5000, 20, 3, 4, 100, 100, "M", "P", LocomotiveType.DIESEL, "iberian");

        LocalDateTime depSlow = LocalDateTime.of(2025, 10, 8, 10, 0, 0);
        Train slowTrain = new Train("Lento", new Route(path, 1, freights, depSlow), depSlow);
        slowTrain.addLocomotive(new Locomotive("L50", 2000, 2, "A", m50, 50));


        LocalDateTime arrivalSlowTail = slowTrain.calculateArrivalTime(depSlow, false);


        LocalDateTime depFast = LocalDateTime.of(2025, 10, 8, 11, 27, 15);
        Train fastTrain = new Train("Rapido", new Route(path, 1, new ArrayList<>(), depFast), depFast);
        fastTrain.addLocomotive(new Locomotive("L100", 2000, 2, "A", m100, 100));

        LocalDateTime arrivalFast = fastTrain.calculateArrivalTime(depFast, false);

        System.out.println("Lento sai às: " + arrivalSlowTail.minusMinutes(15));
        System.out.println("Rápido sai efetivamente às: " + arrivalFast);


        assertTrue(arrivalFast.isAfter(arrivalSlowTail.minusMinutes(15).plusSeconds(29)),
                "O rápido deve esperar os 30s após a cauda do comboio de mercadorias pesado.");

        assertTrue(arrivalFast.isBefore(arrivalSlowTail.plusSeconds(40)),
                "O tempo de saída está demasiado longe.");
    }

    @Test(expected = TracksFullException.class)
    public void testSidingNaoAguentaComprimento()  throws Exception {

        Facility stA = new Facility(null, null, null, null, "Estação A", 1);
        Facility stB = new Facility(null, null, null, null, "Estação B", 2);
        WagonModel modelLongo = new WagonModel("Longo", 20000, 5000, 3000, 20, 100, "Gauge1", 20); // 20 metros cada
        List<Wagon> wagons = new ArrayList<>();
        for(int i=0; i<3; i++) {
            Wagon w = new Wagon("W" + i);
            w.setWagonModel(modelLongo);
            wagons.add(w);
        }

        Freight freight = new Freight("Carga Pesada", stA, stB, wagons);
        List<Freight> freights = List.of(freight);

        double distancia = 100;
        RailSegment segment = new RailSegment("Seg-1", distancia, 400, 1, true, 8000);
        Siding sidingPequeno = new Siding("Siding-Curto", 0.03, 400, false, 8000, 0);
        segment.setSiding(sidingPequeno);

        stA.createConnection(stB, new RailLine("Linha 1", stA, stB, List.of(segment)));
        Path path = new Path(List.of(stA, stB), stA, stB, "Path-Simples");


        LocalDateTime depSlow = LocalDateTime.of(2025, 10, 8, 10, 0, 0);
        Train slowTrain = new Train("Lento", new Route(path, 1, new ArrayList<>(), depSlow), depSlow);
        slowTrain.addLocomotive(new Locomotive("L50", 2000, 2, "A", new LocomotiveModel(5000, 20, 3, 4, 50, 50, "M", "P", LocomotiveType.DIESEL, "iberian"), 50));
        slowTrain.calculateArrivalTime(depSlow, false);


        LocalDateTime depFast = LocalDateTime.of(2025, 10, 8, 10, 30, 0);
        Train fastTrain = new Train("Rapido", new Route(path, 1, freights, depFast), depFast);
        fastTrain.addLocomotive(new Locomotive("L100", 2000, 2, "A", new LocomotiveModel(5000, 20, 3, 4, 100, 100, "M", "P", LocomotiveType.DIESEL, "iberian"), 100));


        fastTrain.calculateArrivalTime(depFast, false);
    }

}

