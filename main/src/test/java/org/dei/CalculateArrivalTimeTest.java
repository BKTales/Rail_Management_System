package org.dei;

import org.dei._Facilities.Facility;
import org.dei.Sprint1._Item.Box;
import org.dei.Sprint1._Item.Item;
import org.dei.Sprint1._Item.ItemType;
import org.dei.Sprint1._Item.Unit;
import org.dei.Sprint2.Country;
import org.dei._Location.GeographicalLocation;
import org.dei.Sprint2.TimeZone;
import org.dei.Sprint2.TimeZoneGroup;
import org.dei._Path.Path;
import org.dei._Path.Route;
import org.dei._RailLineNetwork.*;
import org.dei._Time.TimeInSegment;
import org.dei._Train.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

public class CalculateArrivalTimeTest {
    @Test
    void testOneFreight()  throws Exception {
        Facility a = new Facility(new GeographicalLocation(),TimeZoneGroup.CET,new TimeZone("Teste"),new Country("PT"),"A",1);
        Facility b = new Facility(new GeographicalLocation(),TimeZoneGroup.CET,new TimeZone("Teste"),new Country("PT"),"B",2);
        Facility c = new Facility(new GeographicalLocation(),TimeZoneGroup.CET,new TimeZone("Teste"),new Country("PT"),"C",3);
        Facility d = new Facility(new GeographicalLocation(),TimeZoneGroup.CET,new TimeZone("Teste"),new Country("PT"),"D",4);
        RailSegment railSegment1 = new RailSegment("1",10000,400,1,true,8000);
        RailSegment railSegment2 = new RailSegment("2",10000,400,1,true,8000);
        RailSegment railSegment3 = new RailSegment("3",10000,400,1,true,8000);
        List<RailSegment> railSegments1 = new ArrayList<>();
        railSegments1.add(railSegment1);
        List<RailSegment> railSegments2 = new ArrayList<>();
        railSegments2.add(railSegment2);
        List<RailSegment> railSegments3 = new ArrayList<>();
        railSegments3.add(railSegment3);
        RailLine railLine1 = new RailLine("Route1",a,b,railSegments1);
        RailLine railLine2 = new RailLine("Route2",b,c,railSegments2);
        RailLine railLine3 = new RailLine("Route3",c,d,railSegments3);
        a.createConnection(b, railLine1);
        b.createConnection(c, railLine2);
        c.createConnection(d, railLine3);
        List<Facility> facilities = new ArrayList<>();
        facilities.add(a);
        facilities.add(b);
        facilities.add(c);
        facilities.add(d);
        Path path = new Path(facilities,a,d,"Path1");
        Wagon wagon1 = new Wagon("Wagon1");
        Wagon wagon2 = new Wagon("Wagon2");
        Wagon wagon3 = new Wagon("Wagon3");
        WagonModel model1 = new WagonModel("WagonModel1",9640,4165.5,3120,13.8,100,"Gauge1",20);
        wagon1.setWagonModel(model1);
        wagon2.setWagonModel(model1);
        wagon3.setWagonModel(model1);
        wagon1.addBox(new Box("Box1",20, new Item(Unit.BOTTLE,"SKU1",20, ItemType.BEVERAGE,50),  LocalDate.of(2025, 10,10), LocalDateTime.of(2025, 10, 8, 14, 30, 0)));
        wagon2.addBox(new Box("Box2",20, new Item(Unit.BOTTLE,"SKU1",20, ItemType.BEVERAGE,50),  LocalDate.of(2025, 10,10), LocalDateTime.of(2025, 10, 8, 14, 30, 0)));
        wagon3.addBox(new Box("Box3",20, new Item(Unit.BOTTLE,"SKU1",20, ItemType.BEVERAGE,50),  LocalDate.of(2025, 10,10), LocalDateTime.of(2025, 10, 8, 14, 30, 0)));
        List<Wagon> wagons = new ArrayList<>();
        wagons.add(wagon1);
        wagons.add(wagon2);
        wagons.add(wagon3);
        Freight freight = new Freight("a", b,c,wagons);
        List<Freight> freights = new ArrayList<>();
        freights.add(freight);
        Route route1 = new Route(path, 1,freights,LocalDateTime.of(2025, 10, 8, 14, 30, 0));
        LocalDateTime departureTime = route1.getDepartureDay();
        Train train1 = new Train("Train1",route1,departureTime);
        Locomotive locomotive1 = new Locomotive("Locomotive1",1900,2,"Alex", new LocomotiveModel(5600,19.2,3,4.375,87,220,"make1","paz",LocomotiveType.DIESEL, "iberian"),40);
        train1.addLocomotive(locomotive1);
        train1.calculateArrivalTime(departureTime, false);


        double powerKW = locomotive1.getPower(); // Assumindo 5600.0 kW
        double totalWeightKg = train1.getCurrentWeight() * 1000; // Assumindo 87000.0 kg (só a locomotiva)
        double segmentDistanceKm = railSegment1.getDistanceKm(); // Assumindo 10000.0 km
        double trainLength = train1.getCurrentLength() / 1000.0; // Assumindo 0.0192 km
        double handlingTimeMinutes = 15.0;
        assertNotNull(a.getTimeInFacility().get(new TimeInSegment(departureTime,departureTime
        )));

        assertEquals(0,train1.getFreightsOnTrain().size());  // in D trains is supposed to have 0 freights( freight 1 is unloaded on C
        assertEquals(87000,totalWeightKg);
        assertEquals(0.0192,trainLength,1e-10);

        double baseMaxSpeed =
                locomotive1.getModel().getMaxSpeed();

        double effectiveDistanceKm = segmentDistanceKm + trainLength; // 10000.0192 km
        double travelTimeHours = effectiveDistanceKm / baseMaxSpeed;

        long travelTimeNanos = (long) (travelTimeHours * 60 * 60 * 1_000_000_000L);

        LocalDateTime expectedArrivalB = departureTime.plusNanos(travelTimeNanos);
        LocalDateTime expectedDepartureB = expectedArrivalB.plusMinutes((long) handlingTimeMinutes);
        assertNotNull(b.getTimeInFacility().get(new TimeInSegment(expectedArrivalB,expectedDepartureB)));


        totalWeightKg = locomotive1.getWeight() * 1000 + wagon1.getTotalWeight() * 3; // Assumindo 131 400.0 kg (locomotiva 87T 3 wagons com 14.8T cada 1)
        trainLength = (locomotive1.getModel().getLength() + model1.getLength() * 3) / 1000.0; // Assumindo 0.04812 km

        assertEquals(131400,totalWeightKg);
        assertEquals(0.04812,trainLength,1e-10);

        baseMaxSpeed = Math.min(
                locomotive1.getModel().getMaxSpeed(),
                model1.getMaxSpeed()
        );

        double speedKmh = calculatePlannedSpeed(
                baseMaxSpeed,
                powerKW,
                totalWeightKg
        );
        effectiveDistanceKm = segmentDistanceKm + trainLength; // 10000.04812 km
        travelTimeHours = effectiveDistanceKm / speedKmh;

        travelTimeNanos = (long) (travelTimeHours * 60 * 60 * 1_000_000_000L);
        LocalDateTime expectedArrivalC = expectedDepartureB.plusNanos(travelTimeNanos);
        LocalDateTime expectedDepartureC = expectedArrivalC.plusMinutes((long) handlingTimeMinutes);
        assertNotNull(c.getTimeInFacility().get(new TimeInSegment(expectedArrivalC,expectedDepartureC)));

        totalWeightKg = train1.getCurrentWeight() * 1000; // (locomotiva 87T )
        trainLength = train1.getCurrentLength() / 1000; // Assumindo 0.0192 km


        assertEquals(87000,totalWeightKg);
        assertEquals(0.0192,trainLength,1e-10);

        baseMaxSpeed =
                locomotive1.getModel().getMaxSpeed();


        effectiveDistanceKm = segmentDistanceKm + trainLength;  // 10000.0192 km
        travelTimeHours = effectiveDistanceKm / baseMaxSpeed;
        travelTimeNanos = (long) (travelTimeHours * 60 * 60 * 1_000_000_000L);

        LocalDateTime expectedArrivalD = expectedDepartureC.plusNanos(travelTimeNanos);
        assertNotNull(d.getTimeInFacility().get(new TimeInSegment(expectedArrivalD,expectedArrivalD)));
    }

    @Test
    void testComplexRouteWithMultipleFreights() throws Exception  {
        Facility a = new Facility(new GeographicalLocation(), TimeZoneGroup.CET, new TimeZone("A_TZ"), new Country("PT"), "A", 1);
        Facility b = new Facility(new GeographicalLocation(), TimeZoneGroup.CET, new TimeZone("B_TZ"), new Country("PT"), "B", 2);
        Facility c = new Facility(new GeographicalLocation(), TimeZoneGroup.CET, new TimeZone("C_TZ"), new Country("PT"), "C", 3);
        Facility d = new Facility(new GeographicalLocation(), TimeZoneGroup.CET, new TimeZone("D_TZ"), new Country("PT"), "D", 4);
        Facility e = new Facility(new GeographicalLocation(), TimeZoneGroup.CET, new TimeZone("E_TZ"), new Country("PT"), "E", 5);
        Facility f = new Facility(new GeographicalLocation(), TimeZoneGroup.CET, new TimeZone("F_TZ"), new Country("PT"), "F", 6);
        Facility g = new Facility(new GeographicalLocation(), TimeZoneGroup.CET, new TimeZone("G_TZ"), new Country("PT"), "G", 7);

        // Rail Segments (10000 km cada)
        RailSegment segAB = new RailSegment("AB", 10000, 400, 1, true, 8000);
        RailSegment segBC = new RailSegment("BC", 10000, 400, 1, true, 8000);
        RailSegment segCD = new RailSegment("CD", 10000, 400, 1, true, 8000);
        RailSegment segDE = new RailSegment("DE", 10000, 400, 1, true, 8000);
        RailSegment segEF = new RailSegment("EF", 10000, 400, 1, true, 8000);
        RailSegment segFG = new RailSegment("FG", 10000, 400, 1, true, 8000);

        a.createConnection(b, new RailLine("RouteAB", a, b, List.of(segAB)));
        b.createConnection(c, new RailLine("RouteBC", b, c, List.of(segBC)));
        c.createConnection(d, new RailLine("RouteCD", c, d, List.of(segCD)));
        d.createConnection(e, new RailLine("RouteDE", d, e, List.of(segDE)));
        e.createConnection(f, new RailLine("RouteEF", e, f, List.of(segEF)));
        f.createConnection(g, new RailLine("RouteFG", f, g, List.of(segFG)));

        List<Facility> facilities = List.of(a, b, c, d, e, f, g);
        Path path = new Path(facilities, a, g, "ComplexPath");

        WagonModel model1 = new WagonModel("WagonModel1", 9640, 4165.5, 3120, 13.8, 100, "Gauge1", 20);

        // Vagões para Freight A->D (3 vagões)
        Wagon wagon1 = new Wagon("Wagon1"); wagon1.setWagonModel(model1);
        Wagon wagon2 = new Wagon("Wagon2"); wagon2.setWagonModel(model1);
        Wagon wagon3 = new Wagon("Wagon3"); wagon3.setWagonModel(model1);
        wagon1.addBox(new Box("Box1", 20, new Item(Unit.BOTTLE,"SKU1",20, ItemType.BEVERAGE,50), LocalDate.of(2025, 10,10), LocalDateTime.of(2025, 10, 8, 14, 30, 0)));
        wagon2.addBox(new Box("Box2", 20, new Item(Unit.BOTTLE,"SKU1",20, ItemType.BEVERAGE,50), LocalDate.of(2025, 10,10), LocalDateTime.of(2025, 10, 8, 14, 30, 0)));
        wagon3.addBox(new Box("Box3", 20, new Item(Unit.BOTTLE,"SKU1",20, ItemType.BEVERAGE,50), LocalDate.of(2025, 10,10), LocalDateTime.of(2025, 10, 8, 14, 30, 0)));

        // Vagões para Freight C->F (2 vagões)
        Wagon wagon4 = new Wagon("Wagon4"); wagon4.setWagonModel(model1);
        Wagon wagon5 = new Wagon("Wagon5"); wagon5.setWagonModel(model1);
        wagon4.addBox(new Box("Box4", 20, new Item(Unit.BOTTLE,"SKU2",20, ItemType.BEVERAGE,50), LocalDate.of(2025, 10,10), LocalDateTime.of(2025, 10, 8, 14, 30, 0)));
        wagon5.addBox(new Box("Box5", 20, new Item(Unit.BOTTLE,"SKU2",20, ItemType.BEVERAGE,50), LocalDate.of(2025, 10,10), LocalDateTime.of(2025, 10, 8, 14, 30, 0)));

        Locomotive locomotive1 = new Locomotive("Locomotive1", 1900, 2, "Alex", new LocomotiveModel(5600, 19.2, 3, 4.375, 87, 220, "make1", "paz", LocomotiveType.DIESEL, "iberian"), 40);

        // 2. CRIAÇÃO DE FREIGHTS
        Freight freightAD = new Freight("f1", a, d, List.of(wagon1, wagon2, wagon3)); // 3 vagões
        Freight freightCF = new Freight("f1", c, f, List.of(wagon4, wagon5));       // 2 vagões

        // 3. CONFIGURAÇÃO DA ROTA E TREM
        List<Freight> freights = new ArrayList<>();
        freights.add(freightAD); // Carregado em A
        freights.add(freightCF); // Carregado em C

        LocalDateTime departureTime = LocalDateTime.of(2025, 10, 8, 14, 30, 0);
        Route route = new Route(path, 1, freights, departureTime);
        Train train1 = new Train("Train1", route, departureTime);
        train1.addLocomotive(locomotive1);

        // --- CHAVE DE CÁLCULO GERAL ---
        double handlingTimeMinutes = 5;
        double powerKW = locomotive1.getPower(); // 5600.0 kW
        double segmentDistanceKm = segAB.getDistanceKm();

        // Inicia a simulação
        train1.calculateArrivalTime(departureTime, false);


        // --- A (Partida) ---
        // Freight AD (3 vagões) deve ser adicionado em A.
        LocalDateTime expectedDepartureA = departureTime.plusMinutes((long) handlingTimeMinutes * 3);
        // A (Partida) - Sem tempo de manobra (T, T)
        assertNotNull(a.getTimeInFacility().get(new TimeInSegment(departureTime, expectedDepartureA)),
                "Segment A: Asserção de Partida (T, T) falhou."); // if start facility doesn't need loading just put arrival and departure equal

        // --- A -> B ---
        // Peso/Comprimento: Locomotiva + 3 vagões (Freight A->D)
        double totalWeightKg = locomotive1.getWeight() * 1000.0 + wagon1.getTotalWeight() * 3;
        double trainLength = (locomotive1.getModel().getLength() + model1.getLength() * 3) / 1000.0;

        double baseMaxSpeed = Math.min(
                locomotive1.getModel().getMaxSpeed(),
                model1.getMaxSpeed()
        );
        double speedKmh = calculatePlannedSpeed(baseMaxSpeed, powerKW, totalWeightKg);
        double travelTimeHours = (segmentDistanceKm + trainLength) / speedKmh;
        long travelTimeNanos = (long) (travelTimeHours * 60 * 60 * 1_000_000_000L);

        LocalDateTime expectedArrivalB = expectedDepartureA.plusNanos(travelTimeNanos);

        assertEquals(131400,totalWeightKg);
        assertEquals(0.04812,trainLength,1e-10);

        assertNotNull(b.getTimeInFacility().get(new TimeInSegment(expectedArrivalB, expectedArrivalB)),
                "Segment B: Asserção de Chegada/Partida falhou.");

        // --- B -> C ---
        // O peso e comprimento são os mesmos.
        travelTimeNanos = (long) (travelTimeHours * 60 * 60 * 1_000_000_000L);

        LocalDateTime expectedArrivalC = expectedArrivalB.plusNanos(travelTimeNanos);
        LocalDateTime departureTimeC = expectedArrivalC.plusMinutes((long) handlingTimeMinutes * 2);
        assertEquals(131400,totalWeightKg);
        assertEquals(0.04812,trainLength,1e-10);
        // C (Paragem de Manobra e LOAD)
        assertNotNull(c.getTimeInFacility().get(new TimeInSegment(expectedArrivalC, departureTimeC)),
                "Segment C: Asserção de Chegada/Partida falhou.");

        // --- C -> D ---
        // Peso/Comprimento: Aumenta para Locomotiva + 5 vagões (3 de A->D + 2 de C->F).
        totalWeightKg = locomotive1.getWeight() * 1000.0 + wagon1.getTotalWeight() * 5;
        trainLength = (locomotive1.getModel().getLength() + model1.getLength() * 5) / 1000.0;

        baseMaxSpeed = Math.min(
                locomotive1.getModel().getMaxSpeed(),
                model1.getMaxSpeed());
        speedKmh = calculatePlannedSpeed(baseMaxSpeed, powerKW, totalWeightKg);

        travelTimeHours = (segmentDistanceKm + trainLength) / speedKmh;
        travelTimeNanos = (long) (travelTimeHours * 60 * 60 * 1_000_000_000L);

        LocalDateTime expectedArrivalD = departureTimeC.plusNanos(travelTimeNanos);
        LocalDateTime expectedDepartureD = expectedArrivalD.plusMinutes((long) handlingTimeMinutes * 3); // D é um ponto de UNLOAD (Freight A->D é descarregado)

        assertEquals(161000,totalWeightKg);
        assertEquals(0.0674,trainLength,1e-10);

        assertNotNull(d.getTimeInFacility().get(new TimeInSegment(expectedArrivalD, expectedDepartureD)),
                "Segment D: Asserção de Chegada/Partida falhou.");

        // --- D -> E ---
        // Peso/Comprimento: Diminui para Locomotiva + 2 vagões (Apenas Freight CF).
        totalWeightKg = locomotive1.getWeight() * 1000.0 + wagon1.getTotalWeight() * 2;
        trainLength = (locomotive1.getModel().getLength() + model1.getLength() * 2) / 1000.0;

        baseMaxSpeed = Math.min(
                locomotive1.getModel().getMaxSpeed(),
                model1.getMaxSpeed());
        speedKmh = calculatePlannedSpeed(baseMaxSpeed, powerKW, totalWeightKg);
        travelTimeHours = (segmentDistanceKm + trainLength) / speedKmh;
        travelTimeNanos = (long) (travelTimeHours * 60 * 60 * 1_000_000_000L);

        LocalDateTime expectedArrivalE = expectedDepartureD.plusNanos(travelTimeNanos);
        assertEquals(116600,totalWeightKg);
        assertEquals(0.03848,trainLength,1e-10);

        assertNotNull(e.getTimeInFacility().get(new TimeInSegment(expectedArrivalE, expectedArrivalE)),
                "Segment E: Asserção de Chegada/Partida falhou.");

        // --- E -> F ---
        // O peso e comprimento são os mesmos.
        travelTimeNanos = (long) (travelTimeHours * 60 * 60 * 1_000_000_000L);

        LocalDateTime expectedArrivalF = expectedArrivalE.plusNanos(travelTimeNanos);

        // F é um ponto de UNLOAD (Freight C->F é descarregado)
        LocalDateTime expectedDepartureF = expectedArrivalF.plusMinutes((long) handlingTimeMinutes * 2);

        // F (Paragem de Manobra e UNLOAD)
        assertNotNull(f.getTimeInFacility().get(new TimeInSegment(expectedArrivalF, expectedDepartureF)),
                "Segment F: Asserção de Chegada/Partida falhou.");

        // --- F -> G ---
        // Peso/Comprimento: Diminui para Apenas a Locomotiva.
        totalWeightKg = locomotive1.getWeight() * 1000.0 ;
        trainLength = (locomotive1.getModel().getLength()) / 1000.0;

        baseMaxSpeed = locomotive1.getModel().getMaxSpeed();
        speedKmh = calculatePlannedSpeed(baseMaxSpeed, powerKW, totalWeightKg);
        travelTimeHours = (segmentDistanceKm + trainLength) / speedKmh;
        travelTimeNanos = (long) (travelTimeHours * 60 * 60 * 1_000_000_000L);

        LocalDateTime expectedArrivalG = expectedDepartureF.plusNanos(travelTimeNanos);

        assertEquals(87000,totalWeightKg);
        assertEquals(0.0192,trainLength,1e-10);
        // G (Chegada Final) - Sem tempo de manobra (T, T)
        assertNotNull(g.getTimeInFacility().get(new TimeInSegment(expectedArrivalG, expectedArrivalG)),
                "Segment G: Asserção de Chegada Final (T, T) falhou.");

        // Verificação final do estado do trem
        assertEquals(0, train1.getFreightsOnTrain().size(), "Estado Final: Não deve haver freights/wagons no trem.");
    }

    private double calculatePlannedSpeed(
            double baseMaxSpeed,
            double totalPowerKw,
            double totalWeightKg
    ) {
        double powerToWeight = totalPowerKw / totalWeightKg;
        double powerFactor = Math.min(1.0, powerToWeight / 0.05);
        return baseMaxSpeed * powerFactor;
    }
}