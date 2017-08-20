package common;

import java.util.ArrayList;
import java.util.List;
import com.google.protobuf.ByteString;

import eu.antidotedb.client.AntidoteClient;
import eu.antidotedb.client.Bucket;
import eu.antidotedb.client.MapRef;
import eu.antidotedb.client.RegisterRef;
import eu.antidotedb.client.ValueCoder;

public class Board {
	
	Bucket<BoardId> cbucket = Bucket.create("boardbucket", new BoardId.Coder());
	public static List<BoardId> list_boards = new ArrayList<BoardId>();
	enum BoardField {
		board_name, columns
	}
	
	static class BoardFieldCoder implements ValueCoder<BoardField> {

		@Override
		public BoardField cast(Object o) {
			return (BoardField) o;
		}

		@Override
		public BoardField decode(ByteString b) {
			return BoardField.valueOf(b.toStringUtf8());
		}

		@Override
		public ByteString encode(BoardField f) {
			return ByteString.copyFromUtf8(f.name());
		}	
	}
	
	public MapRef<BoardField> boardMap(BoardId board_id) {
		return cbucket.map_aw(board_id, new BoardFieldCoder());
	}
	
	public BoardId createBoard(AntidoteClient client, String name) {
		BoardId board_id = BoardId.getid();
		MapRef<BoardField> board = boardMap(board_id);
		boardname(board).set(client.noTransaction(), name);
		list_boards.add(board_id);
		return board_id;
	}

	private RegisterRef<String> boardname(MapRef<BoardField> board) {
		return board.register(BoardField.board_name);
	}
	
	public void renameBoard(AntidoteClient client, BoardId board_id , String newName) {
		MapRef<BoardField> board = boardMap(board_id);
		boardname(board).set(client.noTransaction(), newName);
	}	

	public List<BoardId> listBoards(){
		return list_boards;
	}
	
	public BoardMap getBoard(AntidoteClient client, BoardId board_id) {
		List<ColumnMap> column_list = new ArrayList<ColumnMap>();
		MapRef<BoardField> board = new Board().boardMap(board_id);
		String boardname = board.register(BoardField.board_name).read(client.noTransaction());
		List<ColumnId> columnid_list = board.set(BoardField.columns, new ColumnId.Coder()).read(client.noTransaction());
		for(int i = 0; i < columnid_list.size(); i++) {
			ColumnMap column = new Column().getColumn(client, columnid_list.get(i));
			column_list.add(column);
		}
		return new BoardMap(boardname, columnid_list, column_list);
	}

}
