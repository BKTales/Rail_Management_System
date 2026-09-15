package org.dei.TreeTest;

import org.dei.Sprint2.Parser64K;
import org.dei.Sprint2.Trees.AVL;
import org.dei.Sprint2.Trees.PrintTree;
import org.dei.Sprint2.Trees.TwoDTree;
import org.junit.Test;

public class Station2DTree {
    @Test
    public void test2Dtree() {
        AVL avlTree = Parser64K.parseEUStations();
        TwoDTree twoDTree = new TwoDTree(avlTree);
        System.out.println("============ 2D Tree ============ ");
        System.out.println(PrintTree.toString(twoDTree));
    }

    @Test
    public void test2Dtree7stations() {
        AVL avlTree = Parser64K.parseTestStations("src/main/resources/train_station_database_sprint2/stationTest1.csv");
        TwoDTree twoDTree = new TwoDTree(avlTree);
        System.out.println("============ 2D Tree ============ ");
        System.out.println(PrintTree.toString(twoDTree));
    }


    @Test
    public void test2Dtree4stations() {
        AVL avlTree = Parser64K.parseTestStations("src/main/resources/train_station_database_sprint2/stationTest2.csv");
        TwoDTree twoDTree = new TwoDTree(avlTree);
        System.out.println("============ 2D Tree ============ ");
        System.out.println(PrintTree.toString(twoDTree));
    }

    @Test
    public void test2Dtree12stations() {
        AVL avlTree = Parser64K.parseTestStations("src/main/resources/train_station_database_sprint2/stationTest3.csv");
        TwoDTree twoDTree = new TwoDTree(avlTree);
        System.out.println("============ 2D Tree ============ ");
        System.out.println(PrintTree.toString(twoDTree));
    }
}
