# Follower-Maze 

### TO RUN PROJECT 

`sbt "project web" "run"`


## PROJECT BREAK DOWN 

Due to my love for microservices and non-monolithic applications, This project is split into 3 subprojects. 

**1. core**

>> This contains configurations (Logging Adapter, Config Objects), and Utilities(Some utility fucntions) that will be shared across the whole project.
>> Also contains my implementation of an actor model (**ActorLike**) and an actor system (**ActorSystemLike**) as well as Classes that can be shared across all projects
>> e.g(Events, Users)

**2. web**

>>This is solely used to create a server that exposes two ports as defined in the application.conf file in core for incoming TCP connections.
>> via the **EventSocketHandler**(events) and **ClientSocketHandler**(users)It does not contain any Logic as to how the users are added or how events are handled. It simply opens up a port, and forward 
>> all received data to the actors in the service project for processing. This way changes can be made to without affecting thw whole project

**3. service**

>>This is where the logic of the application is handled. It consists of a **Registerer** actor whose single job is to Register new Clients 
>> that come in through the users TCP Connection, A **Router** actor whose single job is to route events gotten from the events TCP connection 
>> to the relevant Clients through a **SocketConnectonWriter** Actor whose job is to simply receive a message and write the message to the Client represented
>> as a TCP connection. It also contains Objects that hold user registration details as well as a map for each users followers.

## Project Flow 

When the project is started using the command given in the second paragraph, it opens up two TCP connections for events and users. When a user tries to register,
The message is sent to the **Registerer** Actor who handles the registration, the events that come in gets sent to the **Router** Actor who 
performs the appropriate logic as sent by the event source and then forwards the message to the **SocketConnectionWriter** if there's a need to notify a client.
The **SocketConnectionWriter** simply writes to the specified Clients TCP Connection

## Design considerations.

**LinkedBlockingQueue** : used in **ActorLike** model to avoid race conditions when adding or taking from the actors mailbox.

**Buffered Reader and Buffered Source** : Used instead of a plain old Input stream reader in the ClientSocketHandler and
EventSockertHandler for imporved efficiency and as thoroughly explained in this
[article](https://medium.com/@isaacjumba/why-use-bufferedreader-and-bufferedwriter-classses-in-java-39074ee1a966)

**CachedThreadPool** :  used in **ActorSystemLike** model. Since most of the work done by this application is mostly IO, it makes more sense to use a cachedThreadPool as 
opposed to using a fixedThreadPool as explained in this [discussion](https://stackoverflow.com/questions/54105478/java-cachedthreadpool-vs-fixedthreadpool-for-multithreaded-server)

**PriorityQueue** : used in **EventSocketHandler** to ensure that the objects that come in are arranged in order of priority as
defined by the implicit ordering on the event



## Future Work 

**Use a Fork-Join executor service instead of a cached thread pool for more improved efficiency**

**Implement Solution with Akka TCP**

