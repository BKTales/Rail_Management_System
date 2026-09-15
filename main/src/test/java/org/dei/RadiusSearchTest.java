package org.dei;

import org.dei.Sprint3.Services.CalculateDistanceBetweenFacilities;
import org.dei.Sprint2.Trees.AVL;
import org.dei.Sprint2.Trees.PrintTree;
import org.dei.Sprint2.Trees.TwoDTree;
import org.dei.Sprint2.Parser64K;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RadiusSearchTest {
    private TwoDTree treeEUSmall, treeEU64K;

    // approximate "middle point" of europe
    private final double middleEULatitude = 60;
    private final double middleEULongitude = 15;

    @BeforeEach
    void setUp(){
        AVL avlEUSmall = Parser64K.parseTestStations("src/main/resources/train_station_database_sprint2/stationTest3.csv");
        AVL avlEU64K = Parser64K.parseTestStations("src/main/resources/train_station_database_sprint2/train_stations_europe.csv");

        treeEUSmall = new TwoDTree(avlEUSmall);
        treeEU64K = new TwoDTree(avlEU64K);
    }

    @Test
    void testRadiusSearchInvalidRadius(){
        assertNull(treeEUSmall.radiusSearch(40, 120, -4));
    }

    @Test
    void testRadiusSearchInvalidLatitude(){
        assertNull(treeEUSmall.radiusSearch(-1, 120, 50));
    }

    @Test
    void testRadiusSearchInvalidLongitude(){
        assertNull(treeEUSmall.radiusSearch(4, -181, 50));
    }

    @Test
    void testRadiusSearchSmallRadius(){
        int result = treeEUSmall.radiusSearch(50, 50, 0).size();
        int expected = 0;
        assertEquals(expected, result);
    }

    @Test
    void testRadiusSearchBigRadius(){
        int result = treeEUSmall.radiusSearch(50, 50, 50000).size();
        int expected = treeEUSmall.size();
        assertEquals(expected, result);
    }

    @Test
    void testRadiusSearchSmallRadius64K(){
        int result = treeEU64K.radiusSearch(50, 50, 0).size();
        int expected = 0;
        assertEquals(expected, result);
    }

    @Test
    void testRadiusSearchBigRadius64K(){
        int result = treeEU64K.radiusSearch(50, 50, 50000).size();
        int expected = treeEU64K.size();
        assertEquals(expected, result);
    }


    // === Tests for smaller input ===
    @Test
    void testQuickSummary1000KM(){
        System.out.println(treeEUSmall.getRadiusSearchSummary(middleEULatitude, middleEULongitude, 1000));
    }

    @Test
    void testQuickSummary2000KM(){
        System.out.println(treeEUSmall.getRadiusSearchSummary(middleEULatitude, middleEULongitude, 2000));
    }

    @Test
    void testQuickSummary5000KM(){
        System.out.println(treeEUSmall.getRadiusSearchSummary(middleEULatitude, middleEULongitude, 5000));
    }
    // ===============================


    // ====== Tests for 64K need a smaller radius because the console cannot fit the whole summaries because it's too much text ======
    @Test
    void testQuickSummary64K100KM(){
        System.out.println(treeEU64K.getRadiusSearchSummary(middleEULatitude, middleEULongitude, 100));
    }

    @Test
    void testQuickSummary64K200KM(){
        System.out.println(treeEU64K.getRadiusSearchSummary(middleEULatitude, middleEULongitude, 200));
    }

    @Test
    void testQuickSummary64K300KM(){
        System.out.println(treeEU64K.getRadiusSearchSummary(middleEULatitude, middleEULongitude, 300));
    }
    // ===============================================================================================================================


    // === AVL from smaller input ===
    @Test
    void testGetRadiusSearchAVL1000KM(){
        AVL radiusSearchAVL = treeEUSmall.getRadiusSearchAVL(middleEULatitude, middleEULongitude, 1000);
        System.out.println(PrintTree.toStringWithDistance(radiusSearchAVL));
    }

    @Test
    void testGetRadiusSearchAVL2000KM(){
        AVL radiusSearchAVL = treeEUSmall.getRadiusSearchAVL(middleEULatitude, middleEULongitude, 2000);
        System.out.println(PrintTree.toStringWithDistance(radiusSearchAVL));
    }

    @Test
    void testGetRadiusSearchAVL5000KM(){
        AVL radiusSearchAVL = treeEUSmall.getRadiusSearchAVL(middleEULatitude, middleEULongitude, 5000);
        System.out.println(PrintTree.toStringWithDistance(radiusSearchAVL));
    }
    // ==============================


    // === Tests for 64K AVL need a smaller radius because the console cannot fit the whole summaries because it's too much text ===
    @Test
    void testGetRadiusSearchAVL64K100KM(){
        AVL radiusSearchAVL = treeEU64K.getRadiusSearchAVL(middleEULatitude, middleEULongitude, 100);
        System.out.println(PrintTree.toStringWithDistance(radiusSearchAVL));
    }

    @Test
    void testGetRadiusSearchAVL64K200KM(){
        AVL radiusSearchAVL = treeEU64K.getRadiusSearchAVL(middleEULatitude, middleEULongitude, 200);
        System.out.println(PrintTree.toStringWithDistance(radiusSearchAVL));
    }

    @Test
    void testGetRadiusSearchAVL64K300KM(){
        AVL radiusSearchAVL = treeEU64K.getRadiusSearchAVL(middleEULatitude, middleEULongitude, 300);
        System.out.println(PrintTree.toStringWithDistance(radiusSearchAVL));
    }
    // =========================


    @Test
    void testHaversineDistance(){
        double distance = CalculateDistanceBetweenFacilities.haversineDistance(0,0,0,0);
        double expected = 0;
        assertEquals(expected, distance);
    }
}
