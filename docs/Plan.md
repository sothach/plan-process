# Introduction
## Plan Process Framework
Process Lifecycle Automation Notation The Plan framework is a extremely small and light Java library for defining and 
executing processes. In concept, these processes are similar to workflows, but the framework explicitly refrains from 
addressing elements commonly found in workflow solutions, such as diagramming, standard process model compliance or 
integration with databases or service interfaces.

Instead, Plan seeks to implement a simple state-driven process model, as a method of defining processing steps 
outside of code. This motivation is to enable general process steps ('actions') to be combined in different sequences, 
without resorting to procedural coding. Although Plan can be used via a programmatic API, the preferred method is to 
decoratively specify processes in XML, specifically a custom schema built upon Spring's Bean Application Context framework

## Terms
| Process Group |A group of one or more steps defining a process | 
|:------------------|:-----------------------------------------------| 
| Process Node |Either a specific action, or a recursive process group (sub-process) | 
| Transition |A state mapped to action to be executed | 
| State |Domain of state values, often summary states for specific processing outcomes | 
| Process Context |The object of processing, often a request |

## Concepts
Processes are defined as a set of transitions, mapping state values to actions. These transitions are of the form:
 _when_ (_state_) _then_ (_action_).

Execution of a process is a loop in which the context state is used to look-up an action, that is then executed 
(upon the context object), potentially changing the state of the context object. On the next iteration, the context 
object's new state is looked-up in the process's transition map, and if an action is found, it is executed. 
This process terminates when the context's state does not map to an action.

## Looping
A single processing step can be iterative executed by having it leave the context state mapped to that action. 
This will cause the process to loop until the state is changed. For example, a counter in the context could be 
decremented on each call until it is zero, at which point the state of the context is changed and execution continues
 with the action mapped to the new state. As with any loop, careless coding can cause infinite looping.

## Selection
Selecting alternative actions in the process is facilitated by arranging for an action to set the context state to the 
input state of the desired next step.

## Termination
The process ends when the context's current state is not mapped to any action.

## Spring Context
The easiest way to define a process, is by using the Plan xsd schema in a Spring context definition. 
See {@link org.seefin.plan.process.ProcessGroup} for an example of this syntax.

The schema can be included in the Spring context by mapping the prefix, as in the XML header below:
```xml
<beans xmlns="http://www.springframework.org/schema/beans" 
  xmlns:plan="http://seefin.org/schema/plan" 
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
  xsi:schemaLocation="http://seefin.org/schema/plan http://seefin.org/schema/plan/process-1.0.xsd 
  http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd" >
```
Now, a PLAN process definition can be created using the plan: namespace: 
```xml
<plan:process id="flightBooker" state-class="org.seefin.plan.tests.booking.BookingState" > 
  <plan:node state="OPEN" action-ref="validator" /> 
  <plan:node state="PAID" action="org.seefin.plan.tests.booking.BookingAction" /> 
  <plan:node state="BOOKED" action-ref="notifier" /> 
</plan:process> 
```
This process can be injected into the application code that needs to use it:

```java
@Resource 
private ProcessGroup flightBooker; 
... 
SimpleDateFormat sdf = new SimpleDateFormat ("dd/MM/yyyy"); 
FlightBooking request = new FlightBooking ( sdf.parse ( "01/01/2012"), 
IATAAirportCode.DUB, IATAAirportCode.WAT); request.setState ( BookingState.OPEN);

flightBooker.execute ( request);
...
```