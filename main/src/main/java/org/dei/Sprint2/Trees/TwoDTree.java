package org.dei.Sprint2.Trees;

import org.dei._Facilities.Station.Station;
import org.dei.Sprint2.Country;
import org.dei.Sprint2.TimeZoneGroup;
import org.dei.Sprint3.Services.CalculateDistanceBetweenFacilities;
import org.dei.Sprint1.US002.NotInitializedException;

import java.util.*;

/**
 * A 2D Tree implementation for storing and querying geographical points (latitude, longitude).
 * The tree alternates between splitting on latitude (even levels) and longitude (odd levels).
 */
public class TwoDTree extends BST<NodeData> {

    private int size;

    /**
     * Constructs an empty 2D tree.
     */
    public TwoDTree(AVL tree) {
        // using copy off to do not erase the references to
        //  the left and rights nodes from the other tree
        List<NodeData> allNodesDatas = new ArrayList<>(tree.inOrderTransversalData());
        root = insertHelper(allNodesDatas, 1);
    }

    @Override
    public void insert(NodeData element){
        return ;
    }

    private Node insertHelper(List<NodeData> list, int depth) {
        if (list == null || list.isEmpty())
            return null;

        size++;
        list.sort(NodeData.getComparator(depth));

        int midIndex = list.size() / 2;
        NodeData nodeData = list.get(midIndex);
        Node node = new Node(nodeData, null, null);

        List<NodeData> leftSubList = new ArrayList<>(list.subList(0, midIndex));
        List<NodeData> rightSubList = new ArrayList<>(list.subList(midIndex + 1, list.size()));
        
        node.setLeft(insertHelper(leftSubList, depth + 1));
        node.setRight(insertHelper(rightSubList, depth + 1));
        return (node);
    }

    /**
     * Searches for a point in the tree.
     *
     * @param lat the latitude coordinate
     * @param lon the longitude coordinate
     * @return true if the point exists in the tree, false otherwise
     */
    public boolean search(double lat, double lon) {
        // TODO: Implement search logic
        return false;
    }

    /**
     * Helper method for recursive search.
     *
     * @param node the current node
     * @param lat the latitude to search for
     * @param lon the longitude to search for
     * @param depth the current depth
     * @return true if found, false otherwise
     */
    private boolean searchHelper(Node node, double lat, double lon, int depth) {
        // TODO: Implement recursive search
        return false;
    }

    /**
     * Finds the N nearest neighbors to a given point.
     *
     * @param lat the query latitude
     * @param lon the query longitude
     * @param n the number of neighbors to find
     * @return a list of the N nearest node data, ordered by distance (closest first)
     */
    public List<NodeData> nearestNeighbors(double lat, double lon, int n, TimeZoneGroup... filters) {
        if (root == null || n <= 0) return new ArrayList<>();

        // Convert filters to Set for efficient lookup
        Set<TimeZoneGroup> filterSet = filters.length > 0 ?
                Set.of(filters) : Collections.emptySet();

        PriorityQueue<NodeDataDistance> candidates = new PriorityQueue<>(
                n, (a, b) -> Double.compare(b.distance, a.distance)
        );

        search(root, lat, lon, n, 0, candidates, filterSet);

        // Convert to list ordered by distance (closest first)
        List<NodeData> result = new ArrayList<>(candidates.size());
        while (!candidates.isEmpty()) {
            result.add(0, candidates.poll().nodeData);
        }
        return result;
    }

    /**
     * Helper class to store node-data-distance pairs
     */
    private static class NodeDataDistance {
        NodeData nodeData;
        double distance;

        NodeDataDistance(NodeData nodeData, double distance) {
            this.nodeData = nodeData;
            this.distance = distance;
        }
    }

    /**
     * Recursive search with time zone filtering
     */
    private void search(Node node, double targetLat, double targetLon, int n,
                        int depth, PriorityQueue<NodeDataDistance> candidates,
                        Set<TimeZoneGroup> filterSet) {
        if (node == null) return;

        NodeData nodeData = (NodeData) node.getElement();

        // Apply time zone filter if specified
        if (!filterSet.isEmpty()) {
            TimeZoneGroup nodeTimeZone = nodeData.getStations().getFirst().getTimeZoneGroup();
            if (nodeTimeZone == null || !filterSet.contains(nodeTimeZone)) {
                // Skip this node if it doesn't match the filter
                // But we still need to search its subtrees!
                searchSubtrees(node, targetLat, targetLon, n, depth, candidates, filterSet);
                return;
            }
        }

        double nodeLat = nodeData.getCoordinate().getLatitude();
        double nodeLon = nodeData.getCoordinate().getLongitude();
        // Calculate Haversine distance to current node
        double dist = CalculateDistanceBetweenFacilities.haversineDistance(nodeLat, nodeLon, targetLat, targetLon);

        // Update candidates if this node is better than our worst current candidate
        if (candidates.size() < n) {
            candidates.add(new NodeDataDistance(nodeData, dist));
        } else if (dist < candidates.peek().distance) {
            candidates.poll(); // Remove farthest candidate
            candidates.add(new NodeDataDistance(nodeData, dist));
        }

        // Search subtrees
        searchSubtrees(node, targetLat, targetLon, n, depth, candidates, filterSet);
    }

    /**
     * Helper method to search both subtrees (used when current node is filtered out)
     */
    private void searchSubtrees(Node node, double targetLat, double targetLon, int n,
                                int depth, PriorityQueue<NodeDataDistance> candidates,
                                Set<TimeZoneGroup> filterSet) {
        // Determine which dimension to compare
        boolean compareLatitude = (depth % 2 == 0);
        double nodeSplitValue = getCoordinate(node, compareLatitude);
        double targetSplitValue = compareLatitude ? targetLat : targetLon;

        // Decide which subtree is closer to target
        boolean targetIsLeft = targetSplitValue < nodeSplitValue;
        Node nearSubtree = targetIsLeft ? node.getLeft() : node.getRight();
        Node farSubtree = targetIsLeft ? node.getRight() : node.getLeft();

        // Always search the closer subtree first
        search(nearSubtree, targetLat, targetLon, n, depth + 1, candidates, filterSet);

        // Only search farther subtree if it might contain better candidates
        if (!candidates.isEmpty()) {
            double distanceToSplittingPlane = targetSplitValue - nodeSplitValue;
            if (distanceToSplittingPlane * distanceToSplittingPlane < candidates.peek().distance) {
                search(farSubtree, targetLat, targetLon, n, depth + 1, candidates, filterSet);
            }
        }
    }

    /**
     * Get latitude or longitude coordinate from node
     */
    private double getCoordinate(Node node, boolean getLatitude) {
        NodeData nodeData = (NodeData) node.getElement();
        return getLatitude ?
                nodeData.getCoordinate().getLatitude() :
                nodeData.getCoordinate().getLongitude();
    }

    /**
     * Convenience methods with different signatures
     */
    public List<NodeData> nearestNeighbors(double lat, double lon, int n) {
        return nearestNeighbors(lat, lon, n, new TimeZoneGroup[0]);
    }

    public NodeData nearestNeighbor(double lat, double lon, TimeZoneGroup... filters) {
        List<NodeData> neighbors = nearestNeighbors(lat, lon, 1, filters);
        return neighbors.isEmpty() ? null : neighbors.get(0);
    }

    public NodeData nearestNeighbor(double lat, double lon) {
        return nearestNeighbor(lat, lon, new TimeZoneGroup[0]);
    }


    /**
     * Finds all points within a rectangular range.
     *
     * @param minLat minimum latitude
     * @param maxLat maximum latitude
     * @param minLon minimum longitude
     * @param maxLon maximum longitude
     * @return a list of nodes within the range
     */
    public List<Station> rangeSearch(double minLat, double maxLat, double minLon, double maxLon,
                                     boolean isCity, int indexCountryOption, boolean isMainStation) {
        if (root == null)
            throw new NotInitializedException("Root is null");

        List<Station> filteredStations = new ArrayList<>();
        rangeQueryRecursive(filteredStations, root, minLat, maxLat, minLon, maxLon, 0,
                isCity, indexCountryOption, isMainStation);
        return filteredStations;
    }

    private void rangeQueryRecursive(List<Station> filteredStations, Node node,
                                     double latMin, double latMax, double lonMin, double lonMax,
                                     int depth, boolean isCity, int indexCountry, boolean isMainStation) {
        if (node == null) return;

        NodeData data = (NodeData) node.getElement();
        double lat = data.getCoordinate().getLatitude();
        double lon = data.getCoordinate().getLongitude();

        if (lat >= latMin && lat <= latMax && lon >= lonMin && lon <= lonMax) {
            passesFilters(filteredStations, data.getStations(), isCity, indexCountry, isMainStation);
        }

        double coord = (depth % 2 == 0) ? lat : lon;
        double minRange = (depth % 2 == 0) ? latMin : lonMin; 
        double maxRange = (depth % 2 == 0) ? latMax : lonMax;

        if (coord < minRange)
            rangeQueryRecursive(filteredStations, node.getRight(), latMin, latMax, lonMin, lonMax, depth + 1, isCity, indexCountry, isMainStation);
        else if (coord > maxRange)
            rangeQueryRecursive(filteredStations, node.getLeft(), latMin, latMax, lonMin, lonMax, depth + 1, isCity, indexCountry, isMainStation);
        else {
            rangeQueryRecursive(filteredStations, node.getLeft(), latMin, latMax, lonMin, lonMax,depth + 1, isCity, indexCountry, isMainStation);
            rangeQueryRecursive(filteredStations, node.getRight(), latMin, latMax, lonMin, lonMax, depth + 1, isCity, indexCountry, isMainStation);
        }
    }

    private void passesFilters(List<Station> filteredStations, List<Station> candidateStations,
                               boolean isCity, int indexCountry, boolean isMainStation) {
        for (Station station : candidateStations) {
            boolean passes = true;

            if (isCity) passes &= station.isCity();
            if (isMainStation) passes &= station.isMainStation();

            switch (indexCountry) {
                case 0 -> passes &= station.getCountry().getAbbreviaton().equals("PT");
                case 1 -> passes &= station.getCountry().getAbbreviaton().equals("ES");
                default -> {}
            }

            if (passes) filteredStations.add(station);
        }
    }


    /**
     * Helper method for recursive range search.
     *
     * @param node the current node
     * @param minLat minimum latitude
     * @param maxLat maximum latitude
     * @param minLon minimum longitude
     * @param maxLon maximum longitude
     * @param depth the current depth
     * @param result the list to collect results
     */
    private void rangeSearchHelper(Node node, double minLat, double maxLat,
                                   double minLon, double maxLon, int depth, List<Node> result) {
        // TODO: Implement recursive range search
        // Check if current node is in range, then explore relevant subtrees
    }

    /**
     * Finds all points within a given radius from a center point.
     * Uses Haversine distance for geographical accuracy.
     *
     * @param centerLat the center latitude
     * @param centerLon the center longitude
     * @param radiusKm the radius in kilometers
     * @return a list of nodes within the radius
     */
    public List<Node> radiusSearch(double centerLat, double centerLon, double radiusKm) {
        if(radiusKm < 0) { return null; }
        if(centerLat < 0 || centerLat > 90) { return null; }
        if(centerLon < -180 || centerLon > 180) { return null; }

        List<Node> result = new ArrayList<>();
        radiusSearchHelper(root, centerLat, centerLon, radiusKm, 0, result);
        return result;
    }

    /**
     * Helper method for recursive radius search.
     *
     * @param node the current node
     * @param centerLat the center latitude
     * @param centerLon the center longitude
     * @param radiusKm the radius in kilometers
     * @param depth the current depth
     * @param result the list to collect results
     */
    private void radiusSearchHelper(Node node, double centerLat, double centerLon, double radiusKm, int depth, List<Node> result) {
        if(node == null) return;

        NodeData nodeDataCast = (NodeData) node.getElement();
        double nodeLat = nodeDataCast.getCoordinate().getLatitude();
        double nodeLon = nodeDataCast.getCoordinate().getLongitude();
        double distanceBetween = CalculateDistanceBetweenFacilities.haversineDistance(centerLat, centerLon, nodeLat, nodeLon);
        NodeData nodeDataCopy = new NodeDataWithDist(nodeDataCast, distanceBetween);
        Node nodeCopy = new Node(nodeDataCopy, null, null);
        if(distanceBetween <= radiusKm){
            result.add(nodeCopy);
        }

        int axis = depth % 2;

        if ((axis == 0 && centerLat < nodeLat) || (axis == 1 && centerLon < nodeLon)) {
            radiusSearchHelper(node.getLeft(), centerLat, centerLon, radiusKm, depth + 1, result);
        } else {
            radiusSearchHelper(node.getRight(), centerLat, centerLon, radiusKm, depth + 1, result);
        }

        double distanceToPlaneKm;
        if (axis == 0) {
            distanceToPlaneKm = CalculateDistanceBetweenFacilities.haversineDistance(centerLat, centerLon, nodeLat, centerLon);
        } else {
            distanceToPlaneKm = CalculateDistanceBetweenFacilities.haversineDistance(centerLat, centerLon, centerLat, nodeLon);
        }

        if (distanceToPlaneKm <= radiusKm) {
            if ((axis == 0 && centerLat < nodeLat) || (axis == 1 && centerLon < nodeLon)) {
                radiusSearchHelper(node.getRight(), centerLat, centerLon, radiusKm, depth + 1, result);
            } else {
                radiusSearchHelper(node.getLeft(), centerLat, centerLon, radiusKm, depth + 1, result);
            }
        }
    }

    public String getRadiusSearchSummary(double centerLat, double centerLon, double radiusKm){
        List<Node> nodeList = radiusSearch(centerLat, centerLon, radiusKm);

        Map<Country, List<Station>> summaryMap = new HashMap<>();
        Map<Station, Double> stationDistances = new HashMap<>();

        for(Node node : nodeList){
            for(Station station : ((NodeData) node.getElement()).getStations()){
                summaryMap.putIfAbsent(station.getCountry(), new ArrayList<>());
                summaryMap.get(station.getCountry()).add(station);
                NodeDataWithDist nodeDataWithDist;
                if(node.getElement() instanceof NodeDataWithDist){
                    nodeDataWithDist = (NodeDataWithDist) node.getElement();
                    stationDistances.putIfAbsent(station, nodeDataWithDist.getDistance());
                }
            }
        }

        Comparator<Station> comparatorByCity = Station.getComparatorByCity();

        StringBuilder sb = new StringBuilder();
        sb.append("=============== Radius search summary by country and isCity [").append(radiusKm).append(" KM] ===============");

        sb.append("\n\n[CENTER POINT]\n");
        sb.append("   Latitude: ").append(centerLat).append("\n");
        sb.append("   Longitude: ").append(centerLon).append("\n");
        sb.append("   SEARCH RADIUS = ").append(radiusKm).append(" KM\n");

        if(summaryMap.isEmpty()){
            sb.append("\nWARNING - No stations were found during the search inside the radius!\n");
        } else {
            for(Country country : summaryMap.keySet()){
                summaryMap.get(country).sort(comparatorByCity);
                int i = 1;
                sb.append("\n\n--------------------------- [").append(country.getAbbreviaton()).append("] ---------------------------\n\n");
                List<Station> stationListForCountry = summaryMap.get(country);
                if(stationListForCountry.isEmpty()){
                    System.out.println("No station were found for this country!\n\n");
                } else {
                    for(Station station : summaryMap.get(country)){
                        sb.append("   Station ").append(i).append(" - ").append(station.getName()).append(station.isCity() ? " IS A CITY\n" : "\n");
                        sb.append("      Distance to center point = ").append(String.format("%.4f", stationDistances.get(station))).append(" KM").append("\n");
                        sb.append("      Location - ").append(station.getLocation()).append("\n");
                        sb.append("      Time Zone: ").append(station.getTimeZoneGroup()).append("\n");
                        sb.append("      Is a main station: ").append(station.isMainStation() ? "Yes\n" : "No\n");
                        sb.append("      Is an airport: ").append(station.isAirport() ? "Yes\n\n" : "No\n\n");
                        i++;
                    }
                }
            }
        }

        sb.append("\n============================== End of radius search summary ==============================\n\n\n\n\n");

        return sb.toString();
    }


    public AVL getRadiusSearchAVL(double centerLat, double centerLon, double radiusKm){
        List<Node> nodeList = radiusSearch(centerLat, centerLon, radiusKm);

        AVL radiusSearchAVL = new AVL();

        for(Node node : nodeList){

            NodeDataWithDist nodeDataWithDistCast;
            NodeDataWithDist nodeDataWithDist = null;
            if(node.getElement() instanceof NodeDataWithDist){
                nodeDataWithDistCast = (NodeDataWithDist) node.getElement();
                nodeDataWithDist = new NodeDataWithDist((NodeData) node.getElement(), nodeDataWithDistCast.getDistance());
            }

            for(Station station : ((NodeData) node.getElement()).getStations()){
                NodeData foundNode = radiusSearchAVL.findNodeByCoordinates(station.getLocation());
                if (foundNode != null) {
                    foundNode.addStation(station);
                } else {
                    radiusSearchAVL.insert(nodeDataWithDist);
                }
            }
        }
        return radiusSearchAVL;
    }


    /**
     * Deletes a point from the tree.
     *
     * @param lat the latitude coordinate
     * @param lon the longitude coordinate
     * @return true if the point was deleted, false if not found
     */
    public boolean delete(double lat, double lon) {
        // TODO: Implement deletion logic
        // This is the most complex operation for a 2D tree
        return false;
    }

    /**
     * Helper method for recursive deletion.
     *
     * @param node the current node
     * @param lat the latitude to delete
     * @param lon the longitude to delete
     * @param depth the current depth
     * @return the node after deletion
     */
    private Node deleteHelper(Node node, double lat, double lon, int depth) {
        // TODO: Implement recursive deletion
        // May need to find replacement node from subtree
        return null;
    }

    /**
     * Calculates the Euclidean distance between two points.
     * Note: For accurate geographical distance, use haversineDistance instead.
     *
     * @param lat1 first point latitude
     * @param lon1 first point longitude
     * @param lat2 second point latitude
     * @param lon2 second point longitude
     * @return the Euclidean distance
     */
    private double euclideanDistance(double lat1, double lon1, double lat2, double lon2) {
        // TODO: Implement Euclidean distance calculation
        // sqrt((lat2-lat1)^2 + (lon2-lon1)^2)
        return 0.0;
    }

    /**
     * Returns the number of nodes in the tree.
     *
     * @return the size of the tree
     */
    @Override
    public int size() {
        return size;
    }

    // Get all distinct bucket sizes (stations per node)
    public Map<Integer, Integer> getDistinctBucketSizes() {
        Map<Integer, Integer> bucketSizes = new HashMap<>();
        collectBucketSizes(root, bucketSizes);
        return bucketSizes;
    }

    private void collectBucketSizes(Node<NodeData> node, Map<Integer, Integer> bucketSizes) {
        if (node == null) return;

        int stationsCount = (node.getElement().getStations() != null) ? node.getElement().getStations().size() : 0;
        bucketSizes.put(stationsCount, bucketSizes.getOrDefault(stationsCount, 0) + 1);

        collectBucketSizes(node.getLeft(), bucketSizes);
        collectBucketSizes(node.getRight(), bucketSizes);
    }

    /**
     * Clears all nodes from the tree.
     */
    public void clear() {
        // TODO: Clear the tree
    }

    /**
     * Checks if a point is within a rectangular range.
     *
     * @param lat the latitude to check
     * @param lon the longitude to check
     * @param minLat minimum latitude
     * @param maxLat maximum latitude
     * @param minLon minimum longitude
     * @param maxLon maximum longitude
     * @return true if point is in range, false otherwise
     */
    private boolean isInRange(double lat, double lon, double minLat, double maxLat,
                              double minLon, double maxLon) {
        // TODO: Check if point is within rectangular bounds
        return false;
    }
}
