# 🚄 Sprint 2 Demo - LAPR3 Project

## 📋 Project Overview
A railway management system featuring route planning and train scheduling capabilities.

---

## 🗺️ Route Planning Menu

![Route Menu](imgs/RoutePlannerMenu.png)

### ✨ Available Features:

#### 1. **View All Stations** 

![ViewStations](imgs/StationMenu.png)
- **Fully paginated interface**
- **Detailed station information available**
- **Interactive station selection**

**📊 Station Details View:**

![ViewStationsInfo](imgs/StationDetails.png)

#### 2. **View Connected Stations**

![ViewConnectedStations](imgs/StationConnections.png)
- **Network visualization**
- **Connection mapping**
- **Detailed station insights**

#### 3. **Create New Routes**
**Route Creation Process:**

![CreateRoute](imgs/CreateRouteFeature.png)

**Route Finalization:**

![FinishRoute](imgs/FinishRouteCreation.png)

---

## ⏰ Train Scheduling Menu

![Schedule Menu](imgs/TrainSchedulerMenu.png)

### ✨ Available Features:

#### 1. **View All Created Routes**

![ViewRoutes](imgs/ViewAllRoutes.png)
- **Complete route catalog**
- **Pagination support**
- **Detailed route analytics**

**📈 Route Information Display:**

**Unscheduled Route:**

![RouteInfo](imgs/DisplayRouteInformationWithoutSchedule.png)

**Scheduled Route:**

![DisplayRouteDetails](imgs/RouteDetailsWithSchedule.png)

#### 2. **Locomotive Management**

![ViewLocomotives](imgs/ViewAllLocomotives.png)
- **Complete fleet overview**
- **Performance metrics**
- **Maintenance status**

**🔧 Locomotive Details:**

![LocomotiveDetails](imgs/DisplayLocomotiveInfo.png)

#### 3. **Wagon Inventory**

![ViewWagons](imgs/ViewAllWagons.png)
- **Inventory management**
- **Capacity tracking**
- **Status monitoring**

**📦 Wagon Details:**

![WagonInfo](imgs/DisplayWagonsInfo.png)

#### 4. **Create New Train Schedule**

**Step-by-Step Scheduling Process:**
| Step | Preview |
|------|---------|
| 1 | ![CreateSchedule1](imgs/CreateSchedule1.png) |
| 2 |  ![CreateSchedule2](imgs/CreateSchedule2.png) |
| 3 |  ![CreateSchedule3](imgs/CreateSchedule3.png) |
| 4 |  ![CreateScheduleConfirm](imgs/CreateScheduleConfirm.png) |

### 🎯 System Responses

#### ✅ **Success Scenarios**
**Successful Schedule Creation:**

![CreateSchedule5](imgs/CreateScheduleSuccess.png)

**Valid Train Configuration:**

![TrainValidConfig](imgs/TrainScheduleValidConfiguration.png)

#### ⚠️ **Warning Scenarios**
**Performance Compromises:**

![TrainValidConfig](imgs/CreateScheduleConfirm.png)
> *Triggered when locomotive performance is compromised by excess wagons*

#### ❌ **Error Scenarios**
**Missing Routes:**

![CreateSchedule6](imgs/CreateSchedulesWithoutRoutes.png)
> *Displayed when no routes are available for scheduling*

**Schedule Conflicts:**

![CreateSchedule7](imgs/TrainScheduleCollision.png)
> *Prevents scheduling conflicts and collisions*

*Demo prepared for LAPR3 Project - Sprint 2 Review*
