package common;

import java.util.UUID;
import com.google.protobuf.ByteString;
import eu.antidotedb.client.ValueCoder;

public class BoardId {
	
	private String id;
	
	public BoardId(String uniqueID) {
		id = uniqueID;
	}

	public static BoardId getid() {
		String uniqueID = UUID.randomUUID().toString();
		return new BoardId(uniqueID);
	}
	
	public String toString() {
		return id;
	}
	
	static class Coder implements ValueCoder<BoardId> {

		@Override
		public BoardId cast(Object o) {
			return (BoardId) o;
		}

		@Override
		public BoardId decode(ByteString b) {
			return new BoardId(b.toStringUtf8());
		}

		@Override
		public ByteString encode(BoardId f) {
			return ByteString.copyFromUtf8(f.id);
		}	
	}
}

