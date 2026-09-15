# Supplementary Specification (FURPS+)

## Functionality

_Specifies functionalities that:  
&nbsp; &nbsp; (i) are common across several US/UC;  
&nbsp; &nbsp; (ii) are not related to US/UC, namely: Audit, Reporting and Security._

> - Future login information.

## Usability 

_Evaluates the user interface. It has several subcategories,
among them: error prevention; interface aesthetics and design; help and
documentation; consistency and standards._

> - The system must provide a user-friendly text-based interface for demonstrating the Cargo Handling functionality.

## Reliability
 
_Refers to the integrity, compliance and interoperability of the software. The requirements to be considered are: frequency and severity of failure, possibility of recovery, possibility of prediction, accuracy, average time between failures._

> - Validation of business rules must be respected when recording and updating data.
> - The database must be the main repository of information for the system and must reflect the necessary data integrity.

## Performance
_Evaluates the performance requirements of the software, namely: response time, start-up time, recovery time, memory consumption, CPU usage, load capacity and application availability._

> - Nothing specified.

## Supportability
_The supportability requirements gathers several characteristics, such as:
testability, adaptability, maintainability, compatibility,
configurability, installability, scalability and more._

> - The code should allow an easy maintenance and addition of features.
> - The application documentation should be in English.
> - There should be a Javadoc from code documentation to ease support.
> - The information should be persisted in a remote DBMS (Database Management System).

## +


### Design Constraints

_Specifies or constraints the system design process. Examples may include: programming languages, software process, mandatory standards/patterns, use of development tools, class library, etc._

> - Should be developed using the Object-Oriented practices (OOP).
> - Dev Tools: Github and IntelliJ, JUnit 5 and JaCoCo.
> - All images regarding the software development process need to be in SVG format


### Implementation Constraints
 
_Specifies or constraints the code or construction of a system such
such as: mandatory standards/patterns, implementation languages,
database integrity, resource limits, operating system._

> - The program should be done in Java language
> - Adopt the coding standards camelCase.
> - The creation and management of the database will use PL/SQL
> - The interaction with stations will be developed in C/Assembly
> - A significant part of the integration will be carried out through files.

### Interface Constraints
_Specifies or constraints the features inherent to the interaction of the
system being developed with other external systems._

 > Nothing specified.


### Physical Constraints
_Specifies a limitation or physical requirement regarding the hardware used to house the system, as for example: material, shape, size or weight._
 
 > Nothing specified.
