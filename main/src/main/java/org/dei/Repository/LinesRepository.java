package org.dei.Repository;

import org.dei._RailLineNetwork.RailLine;
import org.dei._RailLineNetwork.RailSegment;

import java.util.ArrayList;

public class LinesRepository {

    private static LinesRepository instance = null;
    private ArrayList<RailLine> railLines;
    private ArrayList<RailSegment> railSegments;


    public static LinesRepository getInstance() {
        if (instance == null) {
            instance = new LinesRepository();
        }
        return instance;
    }

    private LinesRepository() {
        railLines = new ArrayList<>();
        railSegments = new ArrayList<>();
    }

    /*
    Rail Lines ----
    */

    public void addRailLines(RailLine railLine){
        railLines.add(railLine);
    }

    public void removeRailLines(RailLine railLine){
        railLines.remove(railLine);
    }

    public RailLine getRailLines(int index){
        return railLines.get(index);
    }

    public String[] listRaiLines(){
        StringBuilder s = new StringBuilder();
        int i = 0;

        if (railLines.isEmpty())
            return (null);
        for (RailLine railLine : railLines) {
            s.append("[" + i + "] - RailLines" + i + "\n");
            i++;
        }
        return (s.toString().split("\n"));
    }

    public ArrayList<RailLine> getAllRailLines(){
        return railLines;
    }

    /*
    Rail Segments ----
     */

    public int sizeRailLines() {
        return railLines.size();
    }

    public void addRailSegments(RailSegment railSegment){
        railSegments.add(railSegment);
    }

    public void removeRailSegments(RailSegment railSegment){
        railSegments.remove(railSegment);
    }

    public RailSegment getRailSegments(int index){
        return railSegments.get(index);
    }

    public ArrayList<RailSegment> getAllRailSegments(){
        return railSegments;
    }

    public String[] listRailSegments(){
        StringBuilder s = new StringBuilder();
        int i = 0;

        if (railLines.isEmpty())
            return (null);
        for (RailSegment railSegment : railSegments) {
            s.append("[" + i + "] - RailSegments" + i + "\n");
            i++;
        }
        return (s.toString().split("\n"));
    }


}
