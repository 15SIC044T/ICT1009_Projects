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
- Design own reasonable objective function to optimize the container loading.




# Schedule all the containers to the ships such that the total price of the loading these containers is minimized.
In this task, it uses greedy algorithm + round robin to allocate customer containers to the ship. Firstly, it groups all the customer and ship container in a hashmap based on its container type respectively. Next, it sorts the customer array according to its balance by descending order. Each time a ship runs, the customer array will be sorted and the ship will try to allocate containers to the customer respectively. 

Before distributing the ship container, the ship containers are sorted in ascending order according to the “price per container”. Then, the ship will proceed distributing the containers. The containers are distributed from refrigerated_special, refrigerated, heavy_special, heavy, basic_special to basic. This is ensure the heaviest and special container are load into the ship before all the minor containers loads in. It ensures the maximum capacity of the ship won’t be full of minor containers while the important containers are left unloaded. 

I also implemented round robin to ensure distribution of the container is fair such that each customer has fair share of loading their container to the lowest “price per container”. Any remaining/extra ship containers left from the round robin will be given to customer with the max balance.

Thus, this algorithm is the best for optimizing minimum and lowest container cost to each customer. The efficiency of greedy + round robin algorithm. 

 
# Design own reasonable objective function to optimize the container loading.
In this task, it uses greedy algorithm to allocate customer container to the ship container. A greedy algorithm is an algorithm that follows the problem solving heuristic of making locally optimal choice at each stage. 

Like task 4, it groups all the customer and ship containers in a hashmap based on its container type respectively. Then it sorts the customer and ship container according to its balance and price per container respectively. The only difference between Task 4 and Task 5 is, it gives the priority to customer who have the maximum balance, and loads all the container to the lowest “price per container”. In this case, the customer who pay more will have the benefits of having more “discount” to pay for their containers. This algorithm is bias to customer with highest balance.

Overall, the algorithm produce the same minimum cost as task 4. It just optimize the overall cost and better use of ship, reduce the confusion for “customer and ship owner” where customer containers are loaded across different ships which makes round robin algorithm inefficient in terms of front end loading. (In task 4, it may be good algorithm for backend, letting the system allocation and scheduling of ships, but for front end loading, it is super inefficient. In the case where there are 100 customers all sharing the lowest “price per container” of a ship, 100 customer containers are equally distributed to a ship which is not optimum. In task 5, we load the same type of containers to that one ship, “using stick to one service logic”, to save the confusion in front end loading.

Limitation: It benefits to customer who paid more and load more containers.
