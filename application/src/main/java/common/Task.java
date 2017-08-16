package common;

import java.text.DateFormat;
import java.util.Date;

import com.google.protobuf.ByteString;
import common.Column.ColumnField;
import eu.antidotedb.client.AntidoteClient;
import eu.antidotedb.client.Bucket;
import eu.antidotedb.client.MapRef;
import eu.antidotedb.client.ValueCoder;

public class Task {
	
	Bucket<TaskId> cbucket = Bucket.create("taskbucket", new TaskId.Coder());

	public TaskId task_id = null;
	
	public Task(TaskId task_id) {
		this.task_id = task_id;
	}
	
	public Task() {
	}

	enum TaskField {
		task_name, due_date
	}
	
	static class TaskFieldCoder implements ValueCoder<TaskField> {

		@Override
		public TaskField cast(Object o) {
			return (TaskField) o;
		}

		@Override
		public TaskField decode(ByteString b) {
			return TaskField.valueOf(b.toStringUtf8());
		}

		@Override
		public ByteString encode(TaskField f) {
			return ByteString.copyFromUtf8(f.name());
		}
		
	}
		
	public MapRef<TaskField> taskMap(TaskId task_id) {
		return cbucket.map_aw(task_id, new TaskFieldCoder());
	}
	
	public TaskId createTask(AntidoteClient client, ColumnId column_id, String title) {
		MapRef<ColumnField> column = new Column().columnMap(column_id);
		TaskId task_id = TaskId.getid();
		column.set(ColumnField.tasks, new TaskId.Coder()).add(client.noTransaction(), task_id); 
		MapRef<TaskField> task = taskMap(task_id);
		task.register(TaskField.task_name).set(client.noTransaction(), title);
		return task_id;	
		}
	
	public void updateTitle(AntidoteClient client, TaskId task_id, String newTitle) {
		MapRef<TaskField> task = taskMap(task_id);
		task.register(TaskField.task_name).set(client.noTransaction(), newTitle);
	}
	
	public void updateDueDate(AntidoteClient client, TaskId task_id, Date dueDate ) {
		MapRef<TaskField> task = taskMap(task_id);
		DateFormat df = DateFormat.getDateInstance();
		String dueDateString = df.format(dueDate);
		task.register(TaskField.due_date).set(client.noTransaction(), dueDateString);
		}
	
	public void deleteTask(AntidoteClient client, TaskId task_id) {
		MapRef<TaskField> task = taskMap(task_id);
		task.reset(client.noTransaction());
	}
	
	public void addUser(TaskId task, UserId user) {
		
	}
}
