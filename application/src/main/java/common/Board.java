package common;

import static eu.antidotedb.client.Key.map_aw;
import static eu.antidotedb.client.Key.register;
import static eu.antidotedb.client.Key.set;

import java.util.ArrayList;
import java.util.List;
import eu.antidotedb.client.AntidoteClient;
import eu.antidotedb.client.Bucket;
import eu.antidotedb.client.MapKey;
import eu.antidotedb.client.MapKey.MapReadResult;
import eu.antidotedb.client.RegisterKey;
import eu.antidotedb.client.SetKey;

public class Board {
	
	private static final RegisterKey<String> namefield = register("Name");
	public static final SetKey<ColumnId> columnidfield = set("ColumnId", new ColumnId.Coder());

	
	Bucket cbucket = Bucket.bucket("bucket");
	public static List<BoardId> list_boards = new ArrayList<BoardId>();
	
	public MapKey boardMap(BoardId board_id) {
		return map_aw(board_id.getId());
	}	
	
	public BoardId createBoard(AntidoteClient client, String name) {
		BoardId board_id = BoardId.generateId();
		MapKey board = boardMap(board_id);
		cbucket.update(client.noTransaction(), board.update(namefield.assign(name)));
		list_boards.add(board_id);
		return board_id;
	}

	public void renameBoard(AntidoteClient client, BoardId board_id , String newName) {
		MapKey board = boardMap(board_id);
		cbucket.update(client.noTransaction(), board.update(namefield.assign(newName)));
	}	

	public List<BoardId> listBoards(){
		return list_boards;
	}
	
	public BoardMap getBoard(AntidoteClient client, BoardId board_id) {
		List<ColumnMap> column_list = new ArrayList<ColumnMap>();
		MapKey board = boardMap(board_id);
		MapReadResult boardmap = cbucket.read(client.noTransaction(), board);
		String boardname = boardmap.get(namefield);
		List<ColumnId> columnid_list = boardmap.get(columnidfield);
		for(int i = 0; i < columnid_list.size(); i++) {
			ColumnMap column = new Column().getColumn(client, columnid_list.get(i));
			column_list.add(column);
		}
		return new BoardMap(boardname, columnid_list, column_list);
	}

}
