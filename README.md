# LogiTrack
**Rail Management System**

> **⚠️ Academic Integrity and Legal Warning**
> This repository contains a project developed for academic purposes during the Degree in Informatics Engineering (LEI) at ISEP. It is made public strictly for portfolio and skill demonstration purposes.
> **For current and future ISEP students:** Copying this code, in whole or in part, to submit as your own work constitutes a severe violation of academic integrity rules (plagiarism). The authors of this repository take no responsibility for any disciplinary actions taken against students who misuse this code.

---

LogiTrack is a comprehensive rail management system designed to streamline route creation, freight management, and train scheduling. The platform ensures operational integrity by validating resource availability (wagons and locomotives) and logical station connectivity.

---

## Core Features

The application is divided into two primary modules accessible via the left-hand sidebar:

*   **Route Manager:** Define paths and create official routes.
*   **Schedules:** Manage freight, schedule trains, and monitor active network status. 

*(Note: Use the **Exit** button at the bottom left to close the application at any time.)*

---

## Route Manager

This module allows you to define railway paths using two distinct modes:

### Automatic Generator
Ideal for finding the quickest route between two points.
1. Select your starting (Origin) and destination stations using the searchable dropdowns.
2. Click **Calculate Path** to compute the shortest path based on existing physical connections.
3. Assign a date to the calculated path to convert it into an official **Route**.

### Manual Builder
Grants full control to build a path station-by-station.
1. Select your origin station.
2. Click **Add Station** to extend the path. The system strictly enforces connectivity rules; you can only select stations physically connected to the previous one.
3. Click **Reset** if you make a mistake and need to clear the current path.
4. Click **Save Manual Path** and assign a date to finalize the route.

---

## Schedules & Freight Management

The **Schedules** view manages the logistics of moving cargo across your created routes. 

### Step 1: Create Independent Freight
Define what is being transported.
1. Select the Origin and Destination (these must match an existing route).
2. Choose from the list of compatible wagons. Wagons marked **IN TRANSIT** (red) are unavailable until their indicated timestamp. Only **AVAILABLE** (green) wagons can be selected.
3. Click **Create Freight** to bundle your selected wagons.

### Step 2: Create Train Schedule
Assign your freight to a locomotive and a route.
1. Select a previously created Route.
2. Select the Freight bundle you created in Step 1.
3. Assign an available Locomotive (engines currently on a trip are hidden).
4. Click **Confirm Schedule** to launch the train.

---

## Network Monitoring

The right-hand panel provides a comprehensive real-time overview of the system. It includes a quick-reference list of **Routes Available** and a **Schedule Monitor** that tracks active trains across three distinct states:

| Status | Indicator | Description |
| :--- | :--- | :--- |
| **WAITING** | 🟡 Yellow | Train is parked, awaiting its departure time or track clearance. |
| **RUNNING** | 🟢 Green | Train is currently in transit (displays estimated arrival time). |
| **FINISHED** | ⚪ White | The trip has successfully concluded. |

---

## System Utilities

*   **System Logs:** Located at the bottom of the screen. This panel tracks all system events, including successful route creations, validation errors, and mode switches.
*   **Reset Database:** A red button at the bottom right of the Schedules menu. This safely clears testing data by deleting only Trains, Routes, and Freights. **Stations and physical track data remain permanently untouched.**