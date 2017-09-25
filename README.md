
A collaborative todo list application to demonstrate how to build applications that use Antidote as backend database.

* setup - scripts and docker files for running two instances of Antidote and two instances of the Java application
* application - source of the application

### Requirements
* An IDE for java development
* [Gradle](https://gradle.org/) build tool
* [Docker](https://docs.docker.com/engine/installation/)
* [docker-compose](https://docs.docker.com/compose/install/)


### Build application
In application/
* ./gradlew build

### Starting antidote nodes
In setup/
* ./start_antidote.sh
* ./stop_antidote.sh

Script to start or stop two antidote docker containers and set up the inter-dc replication.

### Starting the application
* app1.sh - starts application instance 1 which can connect to antidote instance 1
* app2.sh - starts application instance 2 which can connect to antidote instance 2

### Application commands
* createboard <boardname> - creates a board in the application, returns a unique board id
* renameboard <boardid> - renames the board
* getboard <boardid> - returns relevant information about the board
* addcolumn <boardid,columnname> - adds a column in the specified board
* renamecolumn <columnid> - renames the specified column
* deletecolumn <columnid> - deletes the column
* getcolumn <columnid> - returns information about column contents
* createtask <columnid,tasktitle> - adds a task in the specified column
* updatetitle <taskid, title> - updates the title in specified task
* updateduedate <taskid, date> - updates the due date of the specified task
* movetask <taskid, columnid> - moves the task to the specified column
* deletetask <taskid> - deletes the task
* gettask <taskid> - returns information about specified task
