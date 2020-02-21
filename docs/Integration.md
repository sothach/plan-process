# Introduction
Plan is designed to be a small, fast and easy to use process framework, avoiding commitment to persistence and interface technologies - the principle that a class should only have one reason to change can also be applied at a module and framework level.

However, these concerns do need to be addressed, especially in enterprise systems. This section describes one strategy for integrating Plan into such systems.

## Processor Template
Applying the template pattern, an abstract Processor class can be defined to wrap the Plan process execution: ``` public abstract class Processor { public abstract void startTransaction(ProcessContext context); public abstract void endTransaction(ProcessContext context);

```java
@Transactional
public void
execute ( R request)
{
   // wrap request in a processing fixture:
   Fixture<R> context = new Fixture<R>(request);

   startTransaction ( context);
   try {
       process.execute ( context);
   } finally {
       endTransaction ( context);
   }
}
``` 
The Fixture is a wrapper around the real domain request object, and is used for storing processing information, such as state, times and other information that would typically be found in a transaction table entry, as distinct from the domain object.

It is the responsibility of the template methods to perform any state persistence required.

## Reentrant Processing
Often, a process cannot complete in one transaction: for example, use input is needed or an asynchronous call needs to be made to an external system. In such cases, the context can be put into an unmapped state (not specified in the process definition), which will cause processing to terminate. The end transaction call in the example above can persist the current state. Later, when the external action has completed, the context (fixture) state is set to the next appropriate step and the process executed again.