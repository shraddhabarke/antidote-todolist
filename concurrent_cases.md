Identify operations potentially working on the same data. For each pair of such operations, write down what should happen in the case of concurrent operations.

Example 1: Two concurrent renameBoard operations -> take the last operation based on wall time

Example 2: A task is moved to column A and concurrently column A is removed -> restore column A

Example 3: Two concurrent moveColumn operations on same column to different locations -> take the last operation based on wall time / user preference based on rights

Example 4: Two concurrent renameColumn operations -> take the last operation based on wall time

Example 5: Concurrent renameColumn and deleteColumn operations -> restore the column with new name

Example 6: Concurrent moveColumn and deleteColumn operations -> restore the column to the new location

Example 7: Two concurrent addColumn operations with different names -> order the column location based on wall time.

Example 8: Concurrent moveTask and deleteColumn operations -> restore the column and update the task location 

Example 9: Concurrent updateTitle and deleteColumn operations -> restore the column and update the task title

Example 10: Concurrent updateDueDate and deleteColumn operations -> restore the column and update task due date

Example 11: Concurrent addUser and deleteColumn operations -> restore the column and add new user to the task

Example 12: Concurrent moveTask and deleteTask operations -> restore the task to the new location

Example 13: Concurrent updateTitle and deleteTask operations -> restore the task and update the task title

Example 14: Concurrent updateDueDate and deleteTask operations -> restore the task and update due date

Example 15: Concurrent addUser and deleteTask operations -> restore the task and add new user to the task

Example 16: Two conflicting setUserRights operations by different admins -> preference given to the safer operation. Nothing > Read > Write > Admin

Example 17: Two concurrent updateTitle operations on same task to different titles -> take the last operation based on wall time / user preference based on rights

Example 18: Two concurrent updateDueDate operations on same task to different dates -> take the last operation based on wall time / user preference based on rights

Example 19: Two concurrent moveTask operations on same task to different locations -> take the last operation based on wall time / user preference based on rights

Example 20: User A has created column A and User B has created column B with same name as column A. When they sync up, resolve merge conflicts and ordering preference -> update based on wall time.