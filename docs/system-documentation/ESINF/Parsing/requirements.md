# Parsing - As an Product Owner, I want the validation of every input file

## 1. Requirements Engineering

### 1.1. User Story Description
Validation os each of the following named files:
- bays.csv
- wagons.csv
- items.csv
- orders.csv
- order_lines.csv
- returns.csv

The validation should occur between all the lines in the file, when found a
line with error, it should be displayed a error message that will report the
error clearly and then follow reading the other lines.

### 1.2. Customer Specifications and Clarifications

#### From the client clarifications
> **Q:**
>
> **A:**

### 1.3. Acceptance Criteria

- AC1: Detect unknown SKUs.
- AC2: Detect wagons linked ti any known product.
- AC3: There should be no negative values.
- AC4: There should be no invalid quantities.
- AC5: There should be a valid date/timestamp.
- AC6: All the boxId inside the werahouse should be unique.
- AC7: There should be a receivedAt.

### 1.4. Found out Dependencies


### 1.5 Input and Output Data
Inputs:
- .csv files

Outputs:
- Setup of in-memory data structures.

### 1.6. System Sequence Diagram (SSD)


### 1.7 Other Relevant Remarks
No other relevant remarks
