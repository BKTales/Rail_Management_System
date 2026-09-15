# US001 - As a terminal operator, I want unloading operations of wagons to automatically store inventory

## 3. Design

### 3.1. Description

- Wagon - hold the items getting to the terminal 
- Differentiation between perishable and non-perishable goods (using FEFO, then FIFO)
- Each item will be storage inside one specific box in a unique (aisle/bay)
- Warehouse - receive the items from the wagon, and uses fefo and fifo to order the boxes in the aisle and bays

### 3.2. Rationale

**The rationale grounds on the SSD interactions and the identified input/output data.**

| Interaction ID                              | Question: Which class is responsible for... | Answer                    | Justification (with patterns) |
|:--------------------------------------------|:--------------------------------------------|:--------------------------|:------------------------------|
| Step 1: Receiving all the inputs from user  | receiving all the user's inputs?            | `UnloadStorageUI`         | Pure Fabrication              | 
|                                             | read and save which terminal to use?        | `UnloadStorageUI`         | Pure Fabrication              | 
|                                             | read and save which wagon to unload?        | `UnloadStorageUI`         | Pure Fabrication              | 
| Step 2: Simple validation of inputs         | simple validating the inputs?               | `UnloadStorageUI`         | Pure Fabrication              |
| Step 3: Get the wanted warehouse            | getting the wanted warehouse?               | `Terminal`                |                               |
| Step 4: Get the wanted wagon                | getting the wanted wagon?                   | `Terminal`                |                               |
| Step 5: Send wagon's item list to warehouse | sending wagon's item list to warehouse?     | `UnloadStorageController` | Pure Fabrication              |
| Step 6: Sort items                          | sorting items?                              | `Warehouse`               |                               |
|                                             | sorting items in aisles?                    | `Warehouse`               |                               |
|                                             | sorting items in bays?                      | `Aisle`                   |                               |
|                                             | sorting items in which boxes?               | `Bay`                     |                               |




### Systematization ##

According to the taken rationale, the conceptual classes promoted to software classes are:

* Warehouse
* Terminal

Other software classes (i.e. Pure Fabrication) identified:

* UnloadStorageUI
* UnloadStorageController

## 3.3. Sequence Diagram (SD)


![US001-SD](svg/US001-SD.svg)

## 3.4. Class Diagram (CD)


![US001-CD](svg/US001-CD.svg)