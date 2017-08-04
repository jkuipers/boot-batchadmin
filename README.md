# boot-batchadmin
Spring Boot plus Batch Admin demo setup

Simple app which demos one way to integrate the (soon to be retired)
Spring Batch Admin console in a Spring Boot application that uses
Spring Batch. 
I don't like Batch Admin 'polluting' by regular application context
with all its dependencies and bagage, so a dedicated `DispatcherServlet`
with a dedicated `ApplicationContext` is created just for the Admin bits.

Part of the Admin config has also been rewritten as a Java configuration
class, which is *not* annotated with `@Configuration` because the root
context created by Boot shouldn't pick it up through component scanning.

Sample contains a trivial `Job`, which Boot runs automatically at start-up,
so that you can see some actual data when navigating the Admin UI.
