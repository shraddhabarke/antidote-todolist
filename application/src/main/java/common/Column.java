package common;

import static eu.antidotedb.client.Key.map_aw;
import static eu.antidotedb.client.Key.register;
import static eu.antidotedb.client.Key.set;

import java.util.ArrayList;
import java.util.List;
import eu.antidotedb.client.AntidoteClient;
import eu.antidotedb.client.Bucket;
import eu.antidotedb.client.MapKey;
import eu.antidotedb.client.RegisterKey;
import eu.antidotedb.client.SetKey;


public class Column {
	
		private static final RegisterKey<String> namefield = register("Name");
		private static final RegisterKey<BoardId> boardidfield = register("BoardId", new BoardId.Coder());
		private static final SetKey<ColumnId> columnidfield = set("ColumnId", new ColumnId.Coder());
		private static final SetKey<TaskId> taskidfield = set("TaskId", new TaskId.Coder());

		Bucket cbucket = Bucket.bucket("columnbucket");
	
		public ColumnId column_id = null;
		public Column(ColumnId column_id) {
			this.column_id = column_id;
		}
		
		public Column() {
		}

		public MapKey columnMap(ColumnId column_id) {
			return map_aw(column_id.getId());
		}
	
		public ColumnId addColumn(AntidoteClient client, BoardId board_id, String name) {
			MapKey board = new Board().boardMap(board_id);
			ColumnId column_id = ColumnId.generateId();
			MapKey column = columnMap(column_id);
			cbucket.update(client.noTransaction(), column.update(namefield.assign(name)));
			cbucket.update(client.noTransaction(), column.update(boardidfield.assign(board_id)));
			cbucket.update(client.noTransaction(), board.update(columnidfield.add(column_id)));
			// the bucket issue
			return column_id;
		}

		public void renameColumn(AntidoteClient client, ColumnId column_id, String newName) {
			MapKey column = columnMap(column_id);
			cbucket.update(client.noTransaction(), column.update(namefield.assign(newName)));
		}
		
		public void moveColumn(AntidoteClient client, ColumnId column_id, ColumnId column_id_left, ColumnId column_id_right) {
			
		}
		
		public void deleteColumn(AntidoteClient client, ColumnId column_id) {
			MapKey column = columnMap(column_id);	
			
			//Also remove the column_id from board map
		}

		public ColumnMap getColumn(AntidoteClient client, ColumnId column_id) {
			List<TaskMap> task_list = new ArrayList<TaskMap>();
			MapKey column = columnMap(column_id);
			String columnname = cbucket.read(client.noTransaction(), namefield);
			BoardId boardid = cbucket.read(client.noTransaction(), boardidfield);
			List<TaskId> taskid_list = cbucket.read(client.noTransaction(), taskidfield);
			for(int i = 0; i < taskid_list.size(); i++) {
				TaskMap task = new Task().getTask(client, taskid_list.get(i));
				task_list.add(task);
			}
			return new ColumnMap(columnname, boardid, taskid_list, task_list);
		}
	}

