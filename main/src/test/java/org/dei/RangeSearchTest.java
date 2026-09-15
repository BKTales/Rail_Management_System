package org.dei;

import org.dei._Facilities.Station.Station;
import org.dei.Sprint2.Parser64K;
import org.dei.Sprint2.Trees.AVL;
import org.dei.Sprint2.Trees.TwoDTree;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class RangeSearchTest {

    private TwoDTree treeIT;
    private TwoDTree treeEU;
    private TwoDTree tree64k;

    @Before
    public void setUp() {
        AVL avlIT = Parser64K.parseTestStations("src/main/resources/train_station_database_sprint2/stationTest1.csv");
        AVL avlEU = Parser64K.parseTestStations("src/main/resources/train_station_database_sprint2/stationTest3.csv");
        AVL avl64k = Parser64K.parseEUStations();
        treeIT = new TwoDTree(avlIT);
        treeEU = new TwoDTree(avlEU);
        tree64k = new TwoDTree(avl64k);
    }

    private void printStations(String testTitle, List<Station> stations,
                               double latMin, double latMax, double lonMin, double lonMax,
                               boolean cityFilter, int countryFilter, boolean mainStationFilter) {
        String countryStr;
        switch (countryFilter) {
            case 0 -> countryStr = "Portugal";
            case 1 -> countryStr = "Espanha";
            case 2 -> countryStr = "Todos";
            default -> countryStr = "Outro";
        }

        System.out.printf("=== %s === | \nFiltros -> Cidade: %s, País: %s, Principal: %s | Range -> Lat [%.2f, %.2f], Lon [%.2f, %.2f] | Estaç. encontradas: %d%n",
                testTitle,
                cityFilter,
                countryStr,
                mainStationFilter,
                latMin, latMax,
                lonMin, lonMax,
                stations.size()
        );
        System.out.println("\nEstações encontradas: " + stations.size());
        for (Station s : stations) {
            System.out.printf("Nome: %s, País: %s, Cidade: %s, Principal: %s, Lat: %.4f, Lon: %.4f%n",
                    s.getName(),
                    s.getCountry().getAbbreviaton(),
                    s.isCity(),
                    s.isMainStation(),
                    s.getLocation().getLatitude(),
                    s.getLocation().getLongitude());
        }
        System.out.println("====================\n");
    }

    @Test
    public void testIT_AllStations_ALL() {
        List<Station> result = treeIT.rangeSearch(-90,90,-180,180,false,2,false);
        printStations("testIT_AllStations_ALL", result, -90, 90, -180, 180, false, 2, false);
        assertEquals(7, result.size());
    }

    @Test
    public void testIT_FilterPortugal() {
        List<Station> result = treeIT.rangeSearch(-90,90,-180,180,false,0,false);
        printStations("testIT_FilterPortugal", result, -90, 90, -180, 180, false, 0, false);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testIT_FilterSpain() {
        List<Station> result = treeIT.rangeSearch(-90,90,-180,180,false,1,false);
        printStations("testIT_FilterSpain", result, -90, 90, -180, 180, false, 1, false);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testIT_FilterCity() {
        List<Station> result = treeIT.rangeSearch(-90, 90, -180, 180, true, 2, false);
        printStations("testIT_FilterCity", result, -90, 90, -180, 180, true, 2, false);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testIT_FilterMainStation() {
        List<Station> result = treeIT.rangeSearch(-90, 90, -180, 180, false, 2, true);
        printStations("testIT_FilterMainStation", result, -90, 90, -180, 180, false, 2, true);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testIT_SmallRange_ALL() {
        List<Station> result = treeIT.rangeSearch(45.0, 46.5, 9.0, 12.0, false, 2, false);
        printStations("testIT_SmallRange_ALL", result, 45.0, 46.5, 9.0, 12.0, false, 2, false);
        assertEquals(3, result.size());
    }

    @Test
    public void testIT_OutOfRange() {
        List<Station> result = treeIT.rangeSearch(80.0, 90.0, -160.0, -150.0, false, 2, false);
        printStations("testIT_OutOfRange", result, 80.0, 90.0, -160.0, -150.0, false, 2, false);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testEU_AllStations_ALL() {
        List<Station> result = treeEU.rangeSearch(-90, 90, -180, 180, false, 2, false);
        printStations("testEU_AllStations_ALL", result, -90, 90, -180, 180, false, 2, false);
        assertEquals(12, result.size());
    }

    @Test
    public void testEU_FilterSpainOnly() {
        List<Station> result = treeEU.rangeSearch(-90, 90, -180, 180, false, 1, false);
        printStations("testEU_FilterSpainOnly", result, -90, 90, -180, 180, false, 1, false);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(s -> s.getCountry().getAbbreviaton().equals("ES")));
    }

    @Test
    public void testEU_FilterPortugalOnly() {
        List<Station> result = treeEU.rangeSearch(-90, 90, -180, 180, false, 0, false);
        printStations("testEU_FilterPortugalOnly", result, -90, 90, -180, 180, false, 0, false);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testEU_FilterCityOnly() {
        List<Station> result = treeEU.rangeSearch(-90, 90, -180, 180, true, 2, false);
        printStations("testEU_FilterCityOnly", result, -90, 90, -180, 180, true, 2, false);
        assertEquals(4, result.size());
    }

    @Test
    public void testEU_FilterMainStation() {
        List<Station> result = treeEU.rangeSearch(-90, 90, -180, 180, false, 2, true);
        printStations("testEU_FilterMainStation", result, -90, 90, -180, 180, false, 2, true);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testEU_FilterCityAndSpain() {
        List<Station> result = treeEU.rangeSearch(-90, 90, -180, 180, true, 1, false);
        printStations("testEU_FilterCityAndSpain", result, -90, 90, -180, 180, true, 1, false);
        assertEquals(1, result.size());
        assertEquals("ES", result.getFirst().getCountry().getAbbreviaton());
        assertTrue(result.getFirst().isCity());
    }

    @Test
    public void testEU_SmallRangeSpain() {
        List<Station> result = treeEU.rangeSearch(36.0, 41.0, -6.0, -3.0, false, 1, false);
        printStations("testEU_SmallRangeSpain", result, 36.0, 41.0, -6.0, -3.0, false, 1, false);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(s -> s.getCountry().getAbbreviaton().equals("ES")));
    }

    @Test
    public void testEU_SpecificCitySpain() {
        List<Station> result = treeEU.rangeSearch(36.0, 37.0, -6.0, -5.0, true, 1, false);
        printStations("testEU_SpecificCitySpain", result, 36.0, 37.0, -6.0, -5.0, true, 1, false);
        assertEquals(1, result.size());
        assertEquals("Benarraba", result.getFirst().getName());
    }

    @Test
    public void test_64k() {
        double latMin = -90, latMax = 90;
        double lonMin = -180, lonMax = 180;
        boolean cityFilter = false;
        boolean mainFilter = false;
        int countryFilter = 2; // ALL countries

        List<Station> result =
                tree64k.rangeSearch(latMin, latMax, lonMin, lonMax, cityFilter, countryFilter, mainFilter);

        printStations(
                "EU Test: Filter City AND Main",
                result,
                latMin, latMax,
                lonMin, lonMax,
                cityFilter,
                countryFilter,
                mainFilter
        );

        assertEquals(61163,result.size());
    }
}
