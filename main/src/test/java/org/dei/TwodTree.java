package org.dei;

import org.dei.Sprint2.Parser64K;
import org.dei.Sprint2.Trees.AVL;
import org.dei.Sprint2.Trees.PrintTree;
import org.dei.Sprint2.Trees.TwoDTree;
import org.junit.Test;

import java.util.Map;

public class TwodTree {
    @Test
    public void test2Dtree() {
        ///  Arrange
        AVL avlTree = Parser64K.parseEUStations();

        ///  Act
        TwoDTree twoDTree = new TwoDTree(avlTree);

        System.out.println("============ 2D Tree ============ ");
        System.out.println(PrintTree.toString(twoDTree));

        System.out.println("Tree Size = " + twoDTree.size());
        System.out.println("Tree Height = " + twoDTree.height());
        Map<Integer, Integer> bucketSizes = twoDTree.getDistinctBucketSizes();
        System.out.println("Distinct bucket sizes: ");
        bucketSizes.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry ->
                        System.out.println("  " + entry.getKey() + " station(s) per node: " + entry.getValue() + " nodes"));
        ///  Assert
        assert(twoDTree.size() == 60853);
        assert(twoDTree.height() == 15);
    }

    @Test
    public void test2Dtree7stations() {
        /// Arrange
        AVL avlTree = Parser64K.parseTestStations("src/main/resources/train_station_database_sprint2/stationTest1.csv");

        /// Act
        TwoDTree twoDTree = new TwoDTree(avlTree);

        System.out.println("============ 2D Tree ============ ");
        System.out.println(PrintTree.toString(twoDTree));

        System.out.println("Tree Size = " + twoDTree.size());
        System.out.println("Tree Height = " + twoDTree.height());
        Map<Integer, Integer> bucketSizes = twoDTree.getDistinctBucketSizes();
        System.out.println("Distinct bucket sizes: ");
        bucketSizes.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry ->
                        System.out.println("  " + entry.getKey() + " station(s) per node: " + entry.getValue() + " nodes"));
        ///  Assert
        assert(twoDTree.size() == 7);
        assert(twoDTree.height() == 2);
        assert(bucketSizes.get(1).equals(7));
    }

    @Test
    public void test2Dtree4stations() {
         /// Arrange
        AVL avlTree = Parser64K.parseTestStations("src/main/resources/train_station_database_sprint2/stationTest2.csv");

        /// Act
        TwoDTree twoDTree = new TwoDTree(avlTree);

        System.out.println("============ 2D Tree ============ ");
        System.out.println(PrintTree.toString(twoDTree));

        System.out.println("Tree Size = " + twoDTree.size());
        System.out.println("Tree Height = " + twoDTree.height());
        Map<Integer, Integer> bucketSizes = twoDTree.getDistinctBucketSizes();
        System.out.println("Distinct bucket sizes: ");
        bucketSizes.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry ->
                        System.out.println("  " + entry.getKey() + " station(s) per node: " + entry.getValue() + " nodes"));
        ///  Assert
        assert(twoDTree.size() == 4);
        assert(twoDTree.height() == 2);
        assert(bucketSizes.get(1).equals(4));
    }

    @Test
    public void test2Dtree12stations() {
         /// Arrange
        AVL avlTree = Parser64K.parseTestStations("src/main/resources/train_station_database_sprint2/stationTest3.csv");

        /// Act
        TwoDTree twoDTree = new TwoDTree(avlTree);

        System.out.println("============ 2D Tree ============ ");
        System.out.println(PrintTree.toString(twoDTree));

        System.out.println("Tree Size = " + twoDTree.size());
        System.out.println("Tree Height = " + twoDTree.height());

        Map<Integer, Integer> bucketSizes = twoDTree.getDistinctBucketSizes();
        System.out.println("Distinct bucket sizes: ");
        bucketSizes.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry ->
                        System.out.println("  " + entry.getKey() + " station(s) per node: " + entry.getValue() + " nodes"));
        ///  Assert
        assert(twoDTree.size() == 12);
        assert(twoDTree.height() == 3);
        assert(bucketSizes.get(1).equals(12));
    }

    @Test
    public void test2Dtree13stations() {
        /// Arrange
        AVL avlTree = Parser64K.parseTestStations("src/main/resources/train_station_database_sprint2/stationTest4.csv");

        /// Act
        TwoDTree twoDTree = new TwoDTree(avlTree);

        System.out.println("============ 2D Tree ============ ");
        System.out.println(PrintTree.toString(twoDTree));

        System.out.println("Tree Size = " + twoDTree.size());
        System.out.println("Tree Height = " + twoDTree.height());

        Map<Integer, Integer> bucketSizes = twoDTree.getDistinctBucketSizes();
        System.out.println("Distinct bucket sizes: ");
        bucketSizes.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry ->
                        System.out.println("  " + entry.getKey() + " station(s) per node: " + entry.getValue() + " nodes"));
        ///  Assert
        assert(twoDTree.size() == 12);
        assert(twoDTree.height() == 3);
        assert(bucketSizes.get(2).equals(1));
    }
}
