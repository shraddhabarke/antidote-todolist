package common;

import static eu.antidotedb.client.Key.map_aw;
import static eu.antidotedb.client.Key.register;
import static eu.antidotedb.client.Key.set;

import java.text.DateFormat;
import java.util.Date;
import eu.antidotedb.client.AntidoteClient;
import eu.antidotedb.client.Bucket;
import eu.antidotedb.client.InteractiveTransaction;
import eu.antidotedb.client.MapKey;
import eu.antidotedb.client.RegisterKey;
import eu.antidotedb.client.SetKey;
import eu.antidotedb.client.MapKey.MapReadResult;

public class Task {
	
	private static final RegisterKey<String> titlefield = register("Title");
	private static final RegisterKey<String> duedatefield = register("DueDate");
	private static final RegisterKey<ColumnId> columnidfield = register("ColumnId", new ColumnId.Coder());
	private static final SetKey<TaskId> taskidfield = set("TaskId", new TaskId.Coder());

	Bucket cbucket = Bucket.bucket("taskbucket");

	public TaskId task_id = null;
	
	public Task(TaskId task_id) {
		this.task_id = task_id;
	}
	
	public Task() {
	}
		
	public MapKey taskMap(TaskId task_id) {
		return map_aw(task_id.getId());
	}

	
	public TaskId createTask(AntidoteClient client, ColumnId column_id, String title) {
		MapKey column = new Column().columnMap(column_id);
		TaskId task_id = TaskId.generateId();
		MapKey task = taskMap(task_id);
		cbucket.update(client.noTransaction(), task.update(titlefield.assign(title)));
		cbucket.update(client.noTransaction(), column.update(columnidfield.assign(column_id)));
		// the bucket issue
		return task_id;
	}

	public void updateTitle(AntidoteClient client, TaskId task_id, String newTitle) {
		MapKey task = taskMap(task_id);
		cbucket.update(client.noTransaction(), task.update(titlefield.assign(newTitle)));
	}

	public void updateDueDate(AntidoteClient client, TaskId task_id, Date dueDate ) {
		MapKey task = taskMap(task_id);
		DateFormat df = DateFormat.getDateInstance();
		String dueDateString = df.format(dueDate);
		cbucket.update(client.noTransaction(), task.update(duedatefield.assign(dueDateString)));
	}

	public void deleteTask(AntidoteClient client, TaskId task_id) {
		MapKey task = taskMap(task_id);
	}
	
	public void addUser(TaskId task_id, UserId user) {
	}

	public void moveTask(AntidoteClient client, TaskId task_id, ColumnId newcolumn_id) {
		MapKey task = taskMap(task_id);
		cbucket.update(client.noTransaction(), task.update(columnidfield.assign(newcolumn_id)));

		try (InteractiveTransaction tx = client.startTransaction()) {
		ColumnId oldcolumn_id = cbucket.read(tx, columnidfield);
		MapKey oldcolumn = new Column().columnMap(oldcolumn_id);
		cbucket.update(client.noTransaction(), oldcolumn.update(taskidfield.remove(task_id)));
		MapKey newcolumn = new Column().columnMap(newcolumn_id);
		cbucket.update(client.noTransaction(), newcolumn.update(taskidfield.add(task_id)));
		tx.commitTransaction();
		}
	}

	public TaskMap getTask(AntidoteClient client, TaskId task_id) {
		MapKey task = taskMap(task_id);
		MapReadResult taskmap = cbucket.read(client.noTransaction(), task);
		String tasktitle = taskmap.get(titlefield);
		ColumnId columnid = taskmap.get(columnidfield);
		String duedate = taskmap.get(duedatefield);
		return new TaskMap(tasktitle, columnid, duedate);
	}
}