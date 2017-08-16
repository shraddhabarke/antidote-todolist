package common;

import com.google.protobuf.ByteString;
import eu.antidotedb.client.AntidoteClient;
import eu.antidotedb.client.Bucket;
import eu.antidotedb.client.MapRef;
import eu.antidotedb.client.ValueCoder;

public class Board {
	
	Bucket<BoardId> cbucket = Bucket.create("boardbucket", new BoardId.Coder());
	
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
		board.register(BoardField.board_name).set(client.noTransaction(), name);
		return board_id;
	}
	
	public void renameBoard(AntidoteClient client, BoardId board_id , String newName) {
		MapRef<BoardField> board = boardMap(board_id);
		board.register(BoardField.board_name).set(client.noTransaction(), newName);
	}	
}
