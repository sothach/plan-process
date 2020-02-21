# Plan Process Framework
## Process Lifecycle Automation Notation
> "La semplicità è l'ultima sofisticazione" (simplicity is the ultimate sophistication) - Leonardo da Vinci

> "Keep it simple stupid" - Kelly Johnson

The Plan framework is an extremely small and light Java library (one class, three interfaces, < 200 lines of commented codenote) for defining and executing processes.

Plan process definitions are similar to workflows, but the framework intentionally refrains from addressing elements commonly found in workflow solutions, such as diagramming, standard process model compliance or integration with databases or service interfaces.

Rather than coding a particular process flow programmatically, as in this example: 
```java
public void flightBooker(FlightBooking request) {
  validate(request);
    if(request.getState()==BookingState.VALID) {
      pay(request);if(request.getState()==BookingState.PAID){notify(request);
    }
  }
}
```

Plan extends Spring's context schema, to declaratively define a process as a mapping of the request 
states to the next action (in this example, referencing bean objects for the process steps, from, 
for example, `@Component`-annotated Java classes):

```xml
<plan:process id="flightBooker" state-class="org.seefin.plan.tests.booking.BookingState">
    <plan:node state="OPEN" action-ref="validator"/>
    <plan:node state="PAID" action-ref="bookFlight"/>
    <plan:node state="BOOKED" action-ref="notifier"/>
</plan:process>
```
There is a good article over on InfoQ Workflow Engine – To Build or Not to Build One? where the author challenges the (apparently) common decision to built-not-buy a workflow engine. One of the main reasons for building is given as We only have very basic requirements, a very simple state machine. A workflow engine is overkill. and the reader encouraged to spent time getting to understand a workflow engine, first.

Well, I followed that advice, I downloaded innumerable projects, resolved un-resolvable jar dependencies, but all the projects were either dead, or far, far bigger in scope and complexity than what I wanted. One exception was Apache ServiceMix BeanFlow, but the stated goal of that framework was to avoid xml and stick with Java - not really what I was looking for. The concurrency model (based on Java 5 Concurrency) is excellent, and perhaps could be applied to Plan at some stage in the future.

**Update** : at last, found a workflow engine that meets my criteria: Activiti - highly recommended

**note** : cheating slightly, the core classes of Plan are indeed less that 200 lines long; however, there is almost 500 lines of Java implementing the Spring XSD code)

Why the contrived project name 'Plan'
Just a nod to one of the first languages I programmed in, the PLAN assembly language on the ICL 1900 series, before moving on to 
high level languages, like BCPL. Interestingly, BCPL was the first language in which Hello World was written, and also the source 
of some of the syntax inherited by Java (for better or worse...): 
```bcpl
// BCPL 'Hello World' program
GET "LIBHDR"

LET START () BE { WRITES ("Hello World!*N") }
```