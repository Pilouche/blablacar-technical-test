# blablacar-technical-test
This repository was created for Blablacar's technical test. It contains all the source code needed to fulfill the provided specifications.

## Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites
Things you need to install the software and how to install them (the version number is the minimum version required to run the project) :

* Java 11 [Download](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) - [Installation Guide](https://www.java.com/en/download/help/download_options.xml)
* Apache Maven 3.6.3 [Download](https://maven.apache.org/download.cgi) - [Installation Guide](https://maven.apache.org/install.html) 
* Lombok [Download](https://projectlombok.org/download) - [Installation Guide](https://projectlombok.org/setup/eclipse) (for Eclipse, other IDEs are supported as well)

### Installing
A step by step series of examples that tell you how to get a development environment running.

* Clone the project using into the desired local repository :
```bash
git clone https://github.com/Pilouche/blablacar-technical-test.git
```

* Launch the project :
```bash
mvn clean install
mvn -e exec:java -Dexec.args="scenarios/fileName1 scenarios/fileName2 ..."
```
The input files are stored in "/blablacar-technical-test/src/main/resources/scenarios/". Feel free to add your own scenarios there in order them.
 
## Running the tests
Unit tests are automatically run when compiling the project with maven.
Every new push to the GitHub repository triggers a small pipeline building the project and running the tests, more details : [maven.yml](https://github.com/Pilouche/blablacar-technical-test/blob/master/.github/workflows/maven.yml).

## Structure of the project
The project is divied into 4 packages :
* *application*, with the **Launcher.java** class containing the **main** method.
* *entities*, all  the entities required to represent the problem :
    * **Lawn.java** : a lawn with its dimensions,
    * **Mower.java** : a mower with its position, direction and instructions,
    * **Direction.java** : the four cardinal directions (N, E , S, W),
    * **Move.java** : the three possible instructions (F, L, R).
* *file.loading*, where the file loading and conversion into entities are done thanks to **FileLoader.java**. This class assumes that the input is correct, meaning that, we don't check :
    * If the lawn's bounds are positive,
    * If the mowers' positions are positive/into the lawn,
    * If all the mowers' positions are different (e. g. two mowers starting on the same position).
* *mower.manager*, which contains the actual moving logic :
    * **MowerMover.java** : in charge of translating the instructions into directions or positions. This class is heavily tested to prevent from modifications that would break the moving logic.
    * **MowerRunner.java** : implementation of the actual moving algorithm. It's where we check if a mower can move or not depending on its next position. 
    If a mower is either blocked by a lawn bound or another mower occupying the next position, we discard the instruction and the mowers stays in its position. Otherwise, the mower is moved to next position.
    At the end of the method, as the instruction has either been discarded or executed, it necessarly needs to be removed from the next ones.

## Discussion on parallel execution
After understanding that the mowers could be ran simultaneously, a new approach had to be taken in order to tackle this problem. Thus, the first thing to do was to find which object(s) needed to be synchronized between all the running threads. 
The mowers can be run in their own thread, using a ```parallelStream()``` on the mowers list, without conccurent modification issue as their state is not changed by another thread inside the algorithm. This has been added to the new moving algorithm.
On the contrary, the map handling the current positions, ```currentPositions```, needs to be accessed and potentially modifed by all the mowers threads. A concurrent access mechanism on this object is then required in order to keep consistency across those threads.

First thought was using the **Lombok** built-in annotation ```@Synchronized```, as shown in this code snippet :
```java
@Synchronized("currentPositions")
private void moveIfNextPositionIsAvailable(Mower mower, Point nextPosition, int id) {
    Integer idInPosition = currentPositions.get(nextPosition);
    if (idInPosition == null || idInPosition == id) {
      // the next position is free, the mower can move forward
      // we free the current position and move to the next one
      ...
    }
 ```
Unfortunately, this implements a total locking mechanism (using this intrisic ock of the object) on the object supplied in the annotation, here ```currentPositions```. Hence, the whole map is locked by one single thread when other threads are trying to access it. This often results in bad performance results as the threads spend more time waiting to get access than doing the actual operation. In the worst case, we can even end up having infinite loop/deadlock problems if the implementation is not correct. 
Therefore, the second option was to used the ```synchronizedMap()``` method from Java Collections. After reading the documentation, it turned out that this method was also using a single lock for the whole map object. Hence, this solution had the same performance issues as the Lombok implementation so it was discarded.

It is where the ```ConcurrentHashMap``` implementation of ```Map``` intervenes. This concurrent version allows bettter performances than the aforementioned implementations. In this case the locking mechanism is different, the map is divided into several segments. Then, the lock is performed on those, let's say, **n** segments. This means that, if a thread is currently accessing **1** segment, all the other segments, so **n-1** segments, are free to be accessed by other threads. Thanks to that implementation, a way better performance is achieved as the whole map is not locked by single thread, but either one or more segments. This implementation has been added to the ```currentPositions``` map when it's initiliazed in the **Launcher java** class thanks to ```toConcurrentMap(k, v)``` method.

As we can expect, ```ConcurrentHashMap```'s performance tends to be optimal when we have to perform a number of reads largely greater than a number of writes. In our case, we necessarly have to read the map before writing to it as we need to check if the next position is available or not. If all mowers can move freely without collisions, the numbers of write operations is equal to twice the number of read one, write operation being ```get(k)```and read operations being ```remove(k)``` & ```put(k, v)```. However in that case, as we have no conflict, the locking mechanism should run pretty smoothly. 
In the case of multiple conflicts, when the locking mechanism is highly used, we have a number of reads way more important the number of writes as mowers can't move. Altogether, this makes me think that the ```ConcurrentHashMap``` is a really good solution (and compromise) for this problem.

Now, what is important to add is that this implementation of the moving algorithm is **not deterministic**. If each mower is running on its own CPU, some mowers could move faster than others. In the case of a simulation without conflicts (no mower is going to be in the same place as another mower at any time), this is not a problem. Mowers will simply continue their route until they are done with their instructions.
When conflicts are "probably" gonna happen, we have, in my humble opinion, no way to predict the output of the simulation. For example, a mower A can run faster than others and encounter mower B late in the simulation for the first run. But in the second run with the same input file, mower A can run slowler and be blocked by mower C before meeting B. Hence, mower A in  this run can end up in a compeltely different position than in the first run. 
To confirm that, I ran the tests that I had previouly written for the non-simultaneous algorithm. Not suprisingly, all the tests involving no conflict succeeded but the tests involving conflicts failed. It is why those later tests are currently commented out in the code. I also ran the program multiple times with input file **scenarios/multiple_mowers_colliding.txt**. As expected again, I always got a general different output, even if some mowers have the same positions between runs due to their starting setup/instructions or the execution of simulation and the speed of certain threads.

To conclude, I would say that this solution offers good performance results as well as thread-safety and data consistency. For me, a deterministic solution is only achievable if we run the mowers sequentially and not in parallel as it is now the case in this solution. Hence, I don't think we have a way to test the final result of the simulation in case of conflicts. Unit tests on the moving instructions and tests on non-conflicts are therefore crucial to ensure that the moving process is not altered by any new code modification.

## Author
**Pierre-Louis Janand** - [LinkedIn](https://www.linkedin.com/in/pierre-louis-janand/?locale=en_US)