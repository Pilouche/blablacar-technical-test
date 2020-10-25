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
    At the end of the method, we remove the instruction from the next ones and we check if it was the last one of the list. If so, the mower is removed from the list of running mowers.

## Author
**Pierre-Louis Janand** - [LinkedIn](https://www.linkedin.com/in/pierre-louis-janand/?locale=en_US)