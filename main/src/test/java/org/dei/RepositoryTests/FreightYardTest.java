package org.dei.RepositoryTests;

import org.dei._Facilities.FrightYard.FreightYard;
import org.dei.Sprint2.Country;
import org.dei._Location.GeographicalLocation;
import org.dei.Sprint2.TimeZone;
import org.dei.Sprint2.TimeZoneGroup;
import org.dei._Train.Wagon;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FreightYardTest {

    @Test
    void testAddWagon() {
        FreightYard freightYard = new FreightYard(new GeographicalLocation(10, 10), TimeZoneGroup.CET,
                new TimeZone("ala/aka"),new Country("IT"), "asd", 1);
        Wagon wagon = new Wagon("1");
        freightYard.addWagon(wagon);
        assertEquals(1, freightYard.size(), "Size should be 1 after adding a wagon");
    }

    @Test
    void testRemoveWagon() {
        FreightYard freightYard = new FreightYard(new GeographicalLocation(10, 10), TimeZoneGroup.CET,
                new TimeZone("ala/aka"),new Country("IT"), "asd", 1);
        Wagon wagon = new Wagon("1");
        freightYard.addWagon(wagon);
        freightYard.removeWagon(wagon);
        assertEquals(0, freightYard.size(), "Size should be 0 after removing the wagon");
    }

    @Test
    void testSizeMultipleWagons() {
        FreightYard freightYard = new FreightYard(new GeographicalLocation(10, 10), TimeZoneGroup.CET,
                new TimeZone("ala/aka"),new Country("IT"), "asd", 1);
        Wagon wagon1 = new Wagon("1");
        Wagon wagon2 = new Wagon("2");
        freightYard.addWagon(wagon1);
        freightYard.addWagon(wagon2);
        assertEquals(2, freightYard.size(), "Size should be 2 after adding two wagons");
    }

    @Test
    void testToString() {
        FreightYard freightYard = new FreightYard(new GeographicalLocation(10, 10), TimeZoneGroup.CET,
                new TimeZone("ala/aka"),new Country("IT"), "asd", 1);
        Wagon wagon1 = new Wagon("1");
        Wagon wagon2 = new Wagon("2");
        freightYard.addWagon(wagon1);
        freightYard.addWagon(wagon2);

        // Act
        String result = freightYard.toString();

        // Assert
        String expected = wagon1.toString() + wagon2.toString();
        assertEquals(expected, result, "toString should concatenate all wagons");
    }
}