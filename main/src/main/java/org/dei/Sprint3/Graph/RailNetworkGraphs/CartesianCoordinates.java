package org.dei.Sprint3.Graph.RailNetworkGraphs;

public class CartesianCoordinates implements Comparable<CartesianCoordinates>{
    private double x;
    private double y;

    public CartesianCoordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
  
    @Override
    public int compareTo(CartesianCoordinates o) {
        if (this.x == o.x && this.y == o.y)
            return 0;
        if (this.x < o.x)
            return -1;
        return 1;
    }

    public boolean equals(CartesianCoordinates c){
        return this.x == c.x && this.y == c.y;
    }
}
