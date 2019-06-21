# ICT1009_Projects
Java (OOP) Using Object Oriented Programming


A Java application that automatically schedules loading and unloading containers onto multiple ships. There are several different types of containers:
1. A normal basic container that is used for most shipping
2. A heavy container is a kind of basic container
3. A refrigerated container which is a kind of heavy container
In addition, any container may have special properties for containing explosives and toxics A ship has capacity that may be different. The capacity has the following parts:
1. Maximum number of toxic and explosive containers (optional for a ship)
2. Maximum number of refrigerated containers (optional for a ship)
3. Maximum number of heavy containers
4. Maximum number of all containers including basic containers

The program is able to read two different sets of files, one set of which contains all the ships information and another for containers. All the information of each ship is stored in one separate XML file which is stored under the same folder called “ships”. Meanwhile, the containers requirements for each customer are stored in one separate XML file and all the container requirement files are stored in another folder called “containers”. The application is able to generate the optimal scheduling plan to load these containers into the proper ships to fulfil different objectives.

The objectives are as following:
- the containers are scheduled to the minimum number of ships.
- Display the optimal solution to schedule all the containers to the ships such that the total price of the loading these containers is minimized.
- optimize the container loading using of minimum number of ships and total price of the container
