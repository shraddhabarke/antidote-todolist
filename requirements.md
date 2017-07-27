# Requirements

The goal is to develop a TODO-application similar to [Trello](https://trello.com/) (or the open source alternative [Wekan](https://github.com/wekan/wekan)).


The tasks are:

1. Identify operations potentially working on the same data.
	For each pair of such operations, write down what should happen in the case of concurrent operations.

	Example 1: Two concurrent `renameBoard` operations -> take the last operation based on wall time.

	Example 2: A task is moved to column A and concurrently column A is removed -> restore column A

2. Implement the required data-access functions (see descriptions below)
3. Write tests with JUnit.

	Maybe try to write tests using Docker with two data centers and artificial partitions between the data centers to test handling of concurrent operations.
4. (optional) Use [Swagger](https://swagger.io/) to expose these functions via an Http-API.
5. (optional) Build a user interface for the board. For example this could be done as a JavaScript/TypeScript application with React using the Swagger-API as the backend.
6. (when Mathias is back) talk to Mathias and use his approach to implement the security aspects of the application.



Below is a description of operations.
This description does not include aspects of concurrency, so that is still open and should be decided in task 1.


## Users

A user has Email, password and a unique userId.

- createUser(String mail, String password)

	Returns Id of user.

- login(String mail, String password)

	Checks if mail/password are correct.
	If yes, creates a session and returns a key for that session.

	The key can be stored by the client (e.g. in a Cookie) and is used in further requests that require an authenticated user.

## Boards

A board is where the TODO-tasks are listed.
The tasks are organized in columns.
The columns can be reordered by the user.
Tasks always belong to exactly one column.


- createBoard(String name)

	Creates a new board with the given name.

	Initially, the user creating the board has Admin-rights to the board.

	Returns a unique ID for the board.

- renameBoard(BoardId board, String newName)

	Changes the name of the board.

- setUserRights(BoardId board, UserId user, Rights rights)

	The current user must be admin for the board.

	Rights is `Nothing`, `Read`, `Write` or `Admin`.

- listBoards()

	Returns a list of all boards (only their id) accessible by the current user.

- getBoard(BoardId)

	Returns all information needed to render an overview page of the board:

	- the columns with
		- column-id
		- column-name
		- tasks in each column with
			- title
			- due-date
			- number of checklist-items and how many of them are done
			- number of comments
			- set of assigned users


## Columns

- addColumn(String name)

	Creates a new column and adds it to the end of the board (as the last column).

	Returns a unique ID for the column.

- renameColumn(ColumnId column, String newName)
- moveColumn(ColumnId column, ColumnId left, ColumnId right)

	Moves a `column` so that it is positioned between `left` and `right`. The `left` and `right` parameters are optional (for example there is no left column, if a column is moved to the very left of the board).

- deleteColumn(ColumnId column)

	Deletes a column.

	Only possible if there are no tasks in the column.



## Tasks

A task consists of :

- a title
- a description (String)
- an optional due-date
- a set of users assigned to the task
- (low priority) a checklist, where each item in the checklist can be marked as done or not
- (low priority) a list of comments
- (low priority) an activity log of changes made to the task

Operations:

- createTask(ColumnId column, String title)

	Creates a new task and adds it to the end of the given column.

	Returns a unique id for the task.

- updateTitle(TaskId task, String newTitle)
- updateDueDate(TaskId task, Date dueDate)
- addUser(TaskId task, UserId user)
- removeUser(TaskId task, UserId user)
- deleteTask(TaskId task)
- moveTask(TaskId task, ColumnId column, TaskId top, TaskId bottom)

	Moves the task to the given column between tasks `top` and `bottom`. The tasks are both optional to allow adding at the start or end of the list.
- getTask(TaskId task)

	Returns all information available for the given task.


## Checklist items

(low priority)

Checklist items belong to a task and can be created, moved, changed, deleted, marked as done and marked as not-done.

## Comments

(low priority)

Comments belong to a task.
They are displayed in the order they are created.
It should be possible to create, edit, and delete comments.

Only board-admins and the commenting person should be able to edit comments.

## Activity log

For every change made to a task, write some log message to an activity log.