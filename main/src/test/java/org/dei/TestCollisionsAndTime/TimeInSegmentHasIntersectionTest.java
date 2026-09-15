package org.dei.TestCollisionsAndTime;

import org.dei._Time.TimeInSegment;
import org.junit.Test;

import java.time.LocalDateTime;

public class TimeInSegmentHasIntersectionTest {

    /*
    * Tests to be developed.
    * Intersection:
    * 1- Time to compare is outer
    * 2- Time to compare is inner
    * 3- Time to compare is equal
    * 4- Left Collision: start time inside
    * 5- Left Collision: start time on border
    * 6- Right Collision: end time inside
    * 7- Right Collision: end time on border
    *
    * No Intersection:
    * 1 - Left no intersection
    * 2 - Right no intersection
     */

    @Test
    public void testHasIntersectionOuter() {
        /// Arrange
        TimeInSegment time1 = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 10),
                                                            LocalDateTime.of(1000, 10, 10, 10, 12));
        TimeInSegment time2 = new TimeInSegment(time1.getStartTime().minusMinutes(10), time1.getEndTime().plusMinutes(10));
        /// Act

        TimeInSegment result = time1.hasIntersection(time2);
        TimeInSegment expected = time1;
        /// Assert
        assert(result != null);
        assert(result.equals(expected));
    }

    @Test
    public void testHasIntersectionInner() {
        /// Arrange
        TimeInSegment time1 = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 10),
                LocalDateTime.of(1000, 10, 10, 10, 30));
        TimeInSegment time2 = new TimeInSegment(time1.getStartTime().plusMinutes(8), time1.getEndTime().minusMinutes(8));
        /// Act

        TimeInSegment result = time1.hasIntersection(time2);
        TimeInSegment expected = time2;
        /// Assert
        assert(result != null);
        assert(result.equals(expected));
    }

    @Test
    public void testHasIntersectionEqual() {
        /// Arrange
        TimeInSegment time1 = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 10),
                LocalDateTime.of(1000, 10, 10, 10, 12));
        TimeInSegment time2 = new TimeInSegment(time1.getStartTime(), time1.getEndTime());
        /// Act

        TimeInSegment result = time1.hasIntersection(time2);
        TimeInSegment expected = time2;
        /// Assert
        assert(result != null);
        assert(result.equals(expected));
    }

    @Test
    public void testHasIntersectionStartInside() {
        /// Arrange
        TimeInSegment time1 = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 10),
                LocalDateTime.of(1000, 10, 10, 10, 30));
        TimeInSegment time2 = new TimeInSegment(time1.getStartTime().plusMinutes(10), time1.getEndTime().plusMinutes(10));
        /// Act

        TimeInSegment result = time1.hasIntersection(time2);
        TimeInSegment expected = new TimeInSegment(time2.getStartTime(), time1.getEndTime());
        /// Assert
        assert(result != null);
        assert(result.equals(expected));
    }

    @Test
    public void testHasIntersectionStartOnBarrier() {
        /// Arrange
        TimeInSegment time1 = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 10),
                LocalDateTime.of(1000, 10, 10, 10, 40));
        TimeInSegment time2 = new TimeInSegment(time1.getStartTime(), time1.getEndTime().minusMinutes(10));
        /// Act

        TimeInSegment result = time1.hasIntersection(time2);
        TimeInSegment expected = new TimeInSegment(time1.getStartTime(), time2.getEndTime());
        /// Assert
        assert(result != null);
        assert(result.equals(expected));
    }

    @Test
    public void testHasIntersectionEndInside() {
        /// Arrange
        TimeInSegment time1 = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 10),
                LocalDateTime.of(1000, 10, 10, 10, 30));
        TimeInSegment time2 = new TimeInSegment(time1.getStartTime().minusMinutes(10), time1.getEndTime().minusMinutes(10));
        /// Act

        TimeInSegment result = time1.hasIntersection(time2);
        TimeInSegment expected = new TimeInSegment(time1.getStartTime(), time2.getEndTime());
        /// Assert
        assert(result != null);
        assert(result.equals(expected));
    }

    ///  check later if this should be null or not ()not this example but the barrier!!
    @Test
    public void testHasIntersectionEndOnBarrier() {
        /// Arrange
        TimeInSegment time1 = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 10),
                LocalDateTime.of(1000, 10, 10, 10, 30));
        TimeInSegment time2 = new TimeInSegment(time1.getStartTime().minusMinutes(10), time1.getEndTime());
        /// Act

        TimeInSegment result = time1.hasIntersection(time2);
        TimeInSegment expected = new TimeInSegment(time1.getStartTime(), time2.getEndTime());
        /// Assert
        assert(result != null);
        assert(result.equals(expected));
    }

    @Test
    public void testNoIntersectionLeft() {
        /// Arrange
        TimeInSegment time1 = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 10),
                LocalDateTime.of(1000, 10, 10, 10, 30));
        TimeInSegment time2 = new TimeInSegment(time1.getStartTime().minusMinutes(20), time1.getStartTime().minusMinutes(10));
        /// Act

        TimeInSegment result = time1.hasIntersection(time2);
        /// Assert
        assert(result == null);
    }

    @Test
    public void testNoIntersectionRight() {
        /// Arrange
        TimeInSegment time1 = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 10),
                LocalDateTime.of(1000, 10, 10, 10, 30));
        TimeInSegment time2 = new TimeInSegment(time1.getEndTime().plusMinutes(50), time1.getEndTime().plusMinutes(60));

        /// Act
        TimeInSegment result = time1.hasIntersection(time2);

        /// Assert
        assert(result == null);
    }

}
