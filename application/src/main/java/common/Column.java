package common;

import com.google.protobuf.ByteString;

import common.Board.BoardField;
import eu.antidotedb.client.AntidoteClient;
import eu.antidotedb.client.Bucket;
import eu.antidotedb.client.MapRef;
import eu.antidotedb.client.ValueCoder;


public class Column {

		Bucket<ColumnId> cbucket = Bucket.create("columnbucket", new ColumnId.Coder());
	
		public ColumnId column_id = null;
		
		public Column(ColumnId column_id) {
			this.column_id = column_id;
		}
		
		public Column() {
		}

		enum ColumnField {
			column_name, tasks
		}
		
		static class ColumnFieldCoder implements ValueCoder<ColumnField> {

			@Override
			public ColumnField cast(Object o) {
				return (ColumnField) o;
			}

			@Override
			public ColumnField decode(ByteString b) {
				return ColumnField.valueOf(b.toStringUtf8());
			}

			@Override
			public ByteString encode(ColumnField f) {
				return ByteString.copyFromUtf8(f.name());
			}
			
		}

		public MapRef<ColumnField> columnMap(ColumnId column_id) {
			return cbucket.map_aw(column_id, new ColumnFieldCoder());
		}
		
		public ColumnId addColumn(AntidoteClient client, BoardId board_id, String name) {
			MapRef<BoardField> board = new Board().boardMap(board_id);
			ColumnId column_id = ColumnId.getid();
			MapRef<ColumnField> column = columnMap(column_id);
			column.register(ColumnField.column_name).set(client.noTransaction(), name);
			board.set(BoardField.columns, new ColumnId.Coder()).add(client.noTransaction(), column_id); 
			return column_id;
		}
			
		public void renameColumn(AntidoteClient client, ColumnId column_id, String newName) {
			MapRef<ColumnField> column = columnMap(column_id);
			column.register(ColumnField.column_name).set(client.noTransaction(), newName);
		}
		
		public void moveColumn(AntidoteClient client, ColumnId column_id, ColumnId column_id_left, ColumnId column_id_right) {
			
		}
		
		public void deleteColumn(AntidoteClient client, ColumnId columnid) {
			MapRef<ColumnField> column = columnMap(column_id);
			column.reset(client.noTransaction());
			//Also remove the column_id from board map
		}
	}

