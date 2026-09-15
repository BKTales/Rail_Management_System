package org.dei.TestCollisionsAndTime;

import org.dei._Time.TimeInSegment;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeMap;

public class TimeInSegmentCompareToTest {

    @Test
    public void testTimeTimeInKeyFullyAfter() {
        ///  Arrange
        // string is the representation of train!
        TreeMap<TimeInSegment, List<String>> arrivalsAtSegment = new TreeMap<>();
        TimeInSegment timeAlreadyKey = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 10),
                                                    LocalDateTime.of(1000, 10, 10, 10, 12));
        TimeInSegment timeCompareTo = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 5),
                                                    LocalDateTime.of(1000, 10, 10, 10, 8));

        System.out.println("TimeInSegment1 is: " + timeAlreadyKey.getStartTime() + " - " + timeAlreadyKey.getEndTime());
        System.out.println("TimeInSegment2 is: " + timeCompareTo.getStartTime() + " - " + timeCompareTo.getEndTime());

        ///  Act
        int compare = timeAlreadyKey.compareTo(timeCompareTo);

        ///  Assert
        assert(compare == 1);
    }

    @Test
    public void testTimeTimeInKeyFullyBefore() {
        ///  Arrange
        // string is the representation of train!
        TreeMap<TimeInSegment, List<String>> arrivalsAtSegment = new TreeMap<>();
        TimeInSegment timeAlreadyKey = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 5),
                LocalDateTime.of(1000, 10, 10, 10, 8));
        TimeInSegment timeCompareTo = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 10),
                LocalDateTime.of(1000, 10, 10, 10, 12));

        System.out.println("TimeInSegment1 is: " + timeAlreadyKey.getStartTime() + " - " + timeAlreadyKey.getEndTime());
        System.out.println("TimeInSegment2 is: " + timeCompareTo.getStartTime() + " - " + timeCompareTo.getEndTime());

        ///  Act
        int compare = timeAlreadyKey.compareTo(timeCompareTo);

        ///  Assert
        assert(compare == -1);
    }

    @Test
    public void testCollisionLeftInside() {
        ///  Arrange
        // string is the representation of train!
        TreeMap<TimeInSegment, List<String>> arrivalsAtSegment = new TreeMap<>();
        TimeInSegment timeAlreadyKey = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 5),
                LocalDateTime.of(1000, 10, 10, 10, 8));
        TimeInSegment timeCompareTo = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 1),
                LocalDateTime.of(1000, 10, 10, 10, 6));

        System.out.println("TimeInSegment1 is: " + timeAlreadyKey.getStartTime() + " - " + timeAlreadyKey.getEndTime());
        System.out.println("TimeInSegment2 is: " + timeCompareTo.getStartTime() + " - " + timeCompareTo.getEndTime());

        ///  Act
        int compare = timeAlreadyKey.compareTo(timeCompareTo);

        ///  Assert
        assert(compare == 0);
    }

    @Test
    public void testCollisionRightInside() {
        ///  Arrange
        // string is the representation of train!
        TreeMap<TimeInSegment, List<String>> arrivalsAtSegment = new TreeMap<>();
        TimeInSegment timeAlreadyKey = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 5),
                LocalDateTime.of(1000, 10, 10, 10, 11));
        TimeInSegment timeCompareTo = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 8),
                LocalDateTime.of(1000, 10, 10, 10, 12));

        System.out.println("TimeInSegment1 is: " + timeAlreadyKey.getStartTime() + " - " + timeAlreadyKey.getEndTime());
        System.out.println("TimeInSegment2 is: " + timeCompareTo.getStartTime() + " - " + timeCompareTo.getEndTime());

        ///  Act
        int compare = timeAlreadyKey.compareTo(timeCompareTo);

        ///  Assert
        assert(compare == 0);
    }

    @Test
    public void testCollisionFullyInside() {
        ///  Arrange
        // string is the representation of train!
        TreeMap<TimeInSegment, List<String>> arrivalsAtSegment = new TreeMap<>();
        TimeInSegment timeAlreadyKey = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 5),
                LocalDateTime.of(1000, 10, 10, 10, 20));
        TimeInSegment timeCompareTo = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 10),
                LocalDateTime.of(1000, 10, 10, 10, 12));

        System.out.println("TimeInSegment1 is: " + timeAlreadyKey.getStartTime() + " - " + timeAlreadyKey.getEndTime());
        System.out.println("TimeInSegment2 is: " + timeCompareTo.getStartTime() + " - " + timeCompareTo.getEndTime());

        ///  Act
        int compare = timeAlreadyKey.compareTo(timeCompareTo);
        System.out.println(compare);
        ///  Assert
        assert(compare == 0);
    }

    @Test
    public void testCollisionExactlyInside() {
        ///  Arrange
        // string is the representation of train!
        TreeMap<TimeInSegment, List<String>> arrivalsAtSegment = new TreeMap<>();
        TimeInSegment timeAlreadyKey = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 8),
                LocalDateTime.of(1000, 10, 10, 10, 10));
        TimeInSegment timeCompareTo = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 8),
                LocalDateTime.of(1000, 10, 10, 10, 10));

        System.out.println("TimeInSegment1 is: " + timeAlreadyKey.getStartTime() + " - " + timeAlreadyKey.getEndTime());
        System.out.println("TimeInSegment2 is: " + timeCompareTo.getStartTime() + " - " + timeCompareTo.getEndTime());

        ///  Act
        int compare = timeAlreadyKey.compareTo(timeCompareTo);

        ///  Assert
        assert(compare == 0);
    }

    @Test
    public void testCollisionInnerValue() {
        ///  Arrange
        // string is the representation of train!
        TreeMap<TimeInSegment, List<String>> arrivalsAtSegment = new TreeMap<>();
        TimeInSegment timeAlreadyKey = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 10),
                LocalDateTime.of(1000, 10, 10, 10, 40));
        TimeInSegment timeCompareTo = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 20),
                LocalDateTime.of(1000, 10, 10, 10, 32));

        System.out.println("TimeInSegment1 is: " + timeAlreadyKey.getStartTime() + " - " + timeAlreadyKey.getEndTime());
        System.out.println("TimeInSegment2 is: " + timeCompareTo.getStartTime() + " - " + timeCompareTo.getEndTime());

        ///  Act
        int compare = timeAlreadyKey.compareTo(timeCompareTo);

        ///  Assert
        assert(compare == 0);
    }
}
