package org.dei.TestCollisionsAndTime;

import org.dei._Time.CollisionTimes;
import org.dei._Time.TimeInSegment;
import org.junit.Test;

import java.time.LocalDateTime;


public class TestSeparateCollisionTime {
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
    public void testHasIntersectionOuterT2() {
        /// Arrange
        TimeInSegment time1 = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 10),
                LocalDateTime.of(1000, 10, 10, 10, 12));
        TimeInSegment time2 = new TimeInSegment(time1.getStartTime().minusMinutes(10), time1.getEndTime().plusMinutes(10));
        /// Act

        TimeInSegment result = time1.hasIntersection(time1);
        CollisionTimes collisionTimes = time1.separateCollisionTime(time2, result);

        TimeInSegment expectedLeft = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 0),
                                                        LocalDateTime.of(1000, 10, 10, 10, 10));
        TimeInSegment expectedRight = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 12),
                                                        LocalDateTime.of(1000, 10, 10, 10, 22));
        /// Assert
        assert(result != null);
        assert(collisionTimes.collided == result);
        assert(collisionTimes.before.equals(expectedLeft));
        assert(collisionTimes.after.equals(expectedRight));
    }

    @Test
    public void testHasIntersectionOuterT1() {
        /// Arrange
        TimeInSegment time1 = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 10),
                LocalDateTime.of(1000, 10, 10, 10, 12));
        TimeInSegment time2 = new TimeInSegment(time1.getStartTime().minusMinutes(10), time1.getEndTime().plusMinutes(10));
        /// Act

        TimeInSegment result = time1.hasIntersection(time2);
        CollisionTimes collisionTimes = time1.separateCollisionTime(time1, result);

        /// Assert
        assert(result != null);
        assert(collisionTimes.collided == result);
        assert(collisionTimes.before == null);
        assert(collisionTimes.after == null);
    }

    @Test
    public void testHasIntersectionInnerT1() {
        /// Arrange
        TimeInSegment time1 = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 10),
                                                    LocalDateTime.of(1000, 10, 10, 10, 30));
        TimeInSegment time2 = new TimeInSegment(time1.getStartTime().plusMinutes(8), time1.getEndTime().minusMinutes(8));
        /// Act

        TimeInSegment result = time1.hasIntersection(time2);
        CollisionTimes collisionTimes = time1.separateCollisionTime(time1, result);

        TimeInSegment expectedLeft = new TimeInSegment(time1.getStartTime(), time2.getStartTime());
        TimeInSegment expectedRight = new TimeInSegment(time2.getEndTime(), time1.getEndTime());
        /// Assert
        assert(result != null);
        assert(collisionTimes.collided == result);
        assert(collisionTimes.before.equals(expectedLeft));
        assert(collisionTimes.after.equals(expectedRight));
    }

    @Test
    public void testHasIntersectionInnerT2() {
        /// Arrange
        TimeInSegment time1 = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 10),
                LocalDateTime.of(1000, 10, 10, 10, 30));
        TimeInSegment time2 = new TimeInSegment(time1.getStartTime().plusMinutes(8), time1.getEndTime().minusMinutes(8));
        /// Act

        TimeInSegment result = time1.hasIntersection(time2);
        CollisionTimes collisionTimes = time1.separateCollisionTime(time2, result);

        /// Assert
        assert(result != null);
        assert(collisionTimes.collided == result);
        assert(collisionTimes.before == null);
        assert(collisionTimes.after == null);
    }

    @Test
    public void testHasIntersectionEqual() {
        /// Arrange
        TimeInSegment time1 = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 10),
                LocalDateTime.of(1000, 10, 10, 10, 12));
        TimeInSegment time2 = new TimeInSegment(time1.getStartTime(), time1.getEndTime());
        /// Act

        TimeInSegment result = time1.hasIntersection(time2);
        CollisionTimes collisionTimes = time1.separateCollisionTime(time2, result);

        /// Assert
        assert(result != null);
        assert(collisionTimes.collided == result);
        assert(collisionTimes.before == null);
        assert(collisionTimes.after == null);
    }

    @Test
    public void testHasIntersectionStartInsideT1() {
        /// Arrange
        TimeInSegment time1 = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 10),
                LocalDateTime.of(1000, 10, 10, 10, 30));
        TimeInSegment time2 = new TimeInSegment(time1.getStartTime().plusMinutes(10), time1.getEndTime().plusMinutes(10));
        /// Act

        TimeInSegment result = time1.hasIntersection(time2);
        CollisionTimes collisionTimes = time1.separateCollisionTime(time1, result);

        TimeInSegment expectedLeft = new TimeInSegment(time1.getStartTime(), time2.getStartTime());
        TimeInSegment expectedRight = null;

        /// Assert
        assert(result != null);
        assert(collisionTimes.collided == result);
        assert(collisionTimes.before.equals(expectedLeft));
        assert(collisionTimes.after == null);
    }

    @Test
    public void testHasIntersectionStartInsideT2() {
        /// Arrange
        TimeInSegment time1 = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 10),
                LocalDateTime.of(1000, 10, 10, 10, 30));
        TimeInSegment time2 = new TimeInSegment(time1.getStartTime().plusMinutes(10), time1.getEndTime().plusMinutes(10));
        /// Act

        TimeInSegment result = time1.hasIntersection(time2);
        CollisionTimes collisionTimes = time1.separateCollisionTime(time2, result);

        TimeInSegment expectedRight = new TimeInSegment(time1.getEndTime(), time2.getEndTime());
        /// Assert
        assert(result != null);
        assert(collisionTimes.collided == result);
        assert(collisionTimes.before == null);
        assert(collisionTimes.after.equals(expectedRight));
    }

    @Test
    public void testHasIntersectionStartOnBarrier() {
        /// Arrange
        TimeInSegment time1 = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 10),
                LocalDateTime.of(1000, 10, 10, 10, 30));
        TimeInSegment time2 = new TimeInSegment(time1.getStartTime(), time1.getEndTime().plusMinutes(10));
        /// Act

        TimeInSegment result = time1.hasIntersection(time2);

        CollisionTimes collisionTimes = time1.separateCollisionTime(time2, result);
        TimeInSegment expectedRight = new TimeInSegment(time1.getEndTime(), time2.getEndTime());
        /// Assert
        assert(result != null);
        assert(collisionTimes.collided == result);
        assert(collisionTimes.before == null);
        assert(collisionTimes.after.equals(expectedRight));
    }

    @Test
    public void testHasIntersectionEndInsideT1() {
        /// Arrange
        TimeInSegment time1 = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 10),
                LocalDateTime.of(1000, 10, 10, 10, 30));
        TimeInSegment time2 = new TimeInSegment(time1.getStartTime().minusMinutes(10), time1.getEndTime().minusMinutes(10));
        /// Act

        TimeInSegment result = time1.hasIntersection(time2);
        CollisionTimes collisionTimes = time1.separateCollisionTime(time1, result);

        TimeInSegment expectedRight= new TimeInSegment(time2.getEndTime(), time1.getEndTime());

        /// Assert
        assert(result != null);
        assert(collisionTimes.collided == result);
        assert(collisionTimes.before == null);
        assert(collisionTimes.after.equals(expectedRight));
    }

    @Test
    public void testHasIntersectionEndInsideT2() {
        /// Arrange
        TimeInSegment time1 = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 10),
                LocalDateTime.of(1000, 10, 10, 10, 30));
        TimeInSegment time2 = new TimeInSegment(time1.getStartTime().minusMinutes(10), time1.getEndTime().minusMinutes(10));
        /// Act

        TimeInSegment result = time1.hasIntersection(time2);
        CollisionTimes collisionTimes = time1.separateCollisionTime(time2, result);

        TimeInSegment expectedLeft = new TimeInSegment(time2.getStartTime(), time1.getStartTime());

        /// Assert
        assert(result != null);
        assert(collisionTimes.collided == result);
        assert(collisionTimes.before.equals(expectedLeft));
        assert(collisionTimes.after == null);
    }

    @Test
    public void testHasIntersectionEndOnBarrier() {
        /// Arrange
        TimeInSegment time1 = new TimeInSegment(LocalDateTime.of(1000, 10, 10, 10, 10),
                LocalDateTime.of(1000, 10, 10, 10, 30));
        TimeInSegment time2 = new TimeInSegment(time1.getStartTime().minusMinutes(10), time1.getEndTime());
        /// Act

        TimeInSegment result = time1.hasIntersection(time2);
        CollisionTimes collisionTimes = time1.separateCollisionTime(time2, result);

        TimeInSegment expectedLeft = new TimeInSegment(time2.getStartTime(), time1.getStartTime());

        /// Assert
        assert(result != null);
        assert(collisionTimes.collided == result);
        assert(collisionTimes.before.equals(expectedLeft));
        assert(collisionTimes.after == null);
    }
}
