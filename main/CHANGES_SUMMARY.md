# Changes Summary

This document summarizes all the changes made to implement the requested features for the ScheduleView application.

## 1. ✅ Show Arrival Time Based on Train Class Calculation

**File**: `src/main/java/org/dei/Sprint3/JavaFX/ScheduleView.java`

- **Change**: Modified `showTrainDetails()` method to use the stored arrival time from `Train.getArrival().getArrivalTime()` instead of recalculating it every time.
- **Details**: The arrival time is now displayed using the calculated value stored in the Train object, which was computed when the train was scheduled.

## 2. ✅ Disable Wagons in Motion

**File**: `src/main/java/org/dei/Sprint3/JavaFX/ScheduleView.java`

- **Change**: Updated the wagon list cell factory to properly check for `IN_TRANSIT` status (instead of `IN TRANSIT` with space).
- **Details**: Wagons with status `IN_TRANSIT` are now properly disabled and shown in red. The code now uses both string comparison and the `isInTransit()` method for better reliability.

## 3. ✅ Disable Locomotives in Motion

**File**: `src/main/java/org/dei/Sprint3/JavaFX/ScheduleView.java`

- **Change**: Added a cell factory to the locomotive list view to disable locomotives that are in transit.
- **Details**: Locomotives with status `IN_TRANSIT` are now disabled and displayed in red, similar to wagons. The cell factory was added in the route combo box action handler.

## 4. ✅ Convert All SQL to PL/SQL Procedures

**Files Changed**:
- `src/main/java/org/dei/Sprint3/Services/Inserts/InsertTrain.java`
- `src/main/java/org/dei/Sprint3/Services/Inserts/InsertNewFreight.java`
- `src/main/java/org/dei/Sprint3/Services/Inserts/InsertScheduleData.java`
- `src/main/java/org/dei/Sprint3/Services/Inserts/InsertTrainFreight.java`
- `src/main/java/org/dei/Sprint3/Services/ResetDatabase.java`

- **Changes**:
  - Replaced all `PreparedStatement` INSERT operations with `CallableStatement` calls to PL/SQL procedures
  - Replaced DELETE statements in ResetDatabase with a single PL/SQL procedure call
  - All SQL operations now go through PL/SQL procedures for better maintainability and consistency

- **Required PL/SQL Procedures**: See `PL_SQL_PROCEDURES_NEEDED.sql` for the complete list of procedures that need to be created in the database.

## 5. ✅ Pick Up Wagons Only from Facilities They Are At

**File**: `src/main/java/org/dei/Sprint3/Schedule/CreateScheduleController.java`

- **Change**: Modified `getWagonsAt()` method to use `facilityId` instead of facility name for filtering.
- **Details**: 
  - Changed from filtering by name (which could have duplicates) to using `getWagonsAtFacility(connection, facility.getId())`
  - Added filtering to exclude wagons that are in transit
  - Same change applied to `getLocomotivesAt()` method for consistency

## 6. ✅ Show All Train Stops

**File**: `src/main/java/org/dei/Sprint3/JavaFX/ScheduleView.java`

- **Status**: Already implemented in `showTrainDetails()` method
- **Details**: The method iterates through all facilities in the route path and displays them as stops. This functionality was already working correctly.

## 7. ✅ Allow Wagons to Travel A->C if Route is A->B->C

**File**: `src/main/java/org/dei/Sprint3/Schedule/CreateScheduleController.java`

- **Change**: Completely rewrote `getAvailableFreights()` method to check if freight start and end facilities are on the route path.
- **Details**: 
  - The new logic finds the indices of the freight's start and end facilities in the route's facility list
  - A freight is considered compatible if:
    - The start facility is on the route
    - The end facility is on the route  
    - The end facility comes after the start facility (startIndex < endIndex)
  - This allows freights like A->C to be assigned to routes like A->B->C

## Additional Changes

### Train Route Loading

**File**: `src/main/java/org/dei/Sprint3/Services/DataBaseAccessService.java`

- **Change**: Modified `getAllTrains()` method to load the route for each train from the RouteRepository.
- **Details**: Trains now have their routes properly loaded when fetched from the database, ensuring that route information is available for display and calculations.

### Arrival Time in InsertTrain

**File**: `src/main/java/org/dei/Sprint3/Services/Inserts/InsertTrain.java`

- **Change**: Updated arrival time calculation to prefer stored arrival time over recalculation.
- **Details**: The code now checks if arrival time is already calculated and stored in the Train object before attempting to recalculate it.

## Database Requirements

All the SQL statements have been converted to PL/SQL procedures. You must create the following procedures in your Oracle database:

1. `addLocomotiveToTrain(p_trainId, p_locomotiveId)` - Links locomotives to trains
2. `addFreight(p_freightId, p_startPoint, p_endPoint)` - Creates freight records
3. `addFacilityTrain(p_facilityId, p_trainId, p_arriveTime, p_leaveTime)` - Records train facility visits
4. `addTimeInSegment(p_segmentId, p_trainId, p_arriveTime, p_leaveTime)` - Records train segment times
5`cleanLogisticsData()` - Cleans all logistics data

See `PL_SQL_PROCEDURES_NEEDED.sql` for the complete SQL scripts to create these procedures.

## Testing Recommendations

1. Test that wagons and locomotives in transit are properly disabled in the UI
2. Verify that arrival times are displayed correctly using calculated values
3. Test freight assignment with routes like A->B->C to ensure A->C freights can be assigned
4. Verify that wagons are only shown for their current facility
5. Test all database operations to ensure PL/SQL procedures work correctly

## Notes

- The `getAllTrainsCursor()` PL/SQL function should return `routeId` in addition to `trainId` and `startDate`
- Ensure that the `addWagonToFreight` procedure exists (it's referenced but may need to be created)
- The `Train_Freight` table may need to be created if it doesn't exist in your schema
