package org.dei.Sprint1.US008;


import org.dei._Facilities.Station.Station;
import org.dei.Sprint2.Trees.AVL;
import org.dei.Sprint2.Trees.TwoDTree;
import org.dei.Sprint2.Parser64K;


import java.util.List;

public class RangeSearchController {
    private AVL avl;
    private TwoDTree tree;

    public RangeSearchController() {
        avl = Parser64K.parseEUStations();
        tree = new TwoDTree(avl);
    }

    // in the future the trees will be stored in some other class
    // so will be needed a get method

    public String search(double minLat, double maxLat, double minLon, double maxLon, boolean isCity, int indexCountryOption, boolean isMainStation) {
        StringBuilder s = new StringBuilder();
        List<Station> selectedLists = tree.rangeSearch(minLat,  maxLat, minLon, maxLon, isCity, indexCountryOption, isMainStation);
        for(Station station : selectedLists){
            s.append(station);
        }
        return s.toString();
    }

}
