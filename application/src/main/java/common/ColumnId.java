package common;

import java.util.UUID;
import com.google.protobuf.ByteString;
import eu.antidotedb.client.ValueCoder;

public class ColumnId {
	
	private String id;
		
	public ColumnId(String uniqueID) {
		id = uniqueID;
	}

	public static ColumnId getid() {
		String uniqueID = UUID.randomUUID().toString();
		return new ColumnId(uniqueID);
	}
	
	public String toString() {
		return id;
	}

	static class Coder implements ValueCoder<ColumnId> {

		@Override
		public ColumnId cast(Object o) {
			return (ColumnId) o;
		}

		@Override
		public ColumnId decode(ByteString b) {
			return new ColumnId(b.toStringUtf8());
		}

		@Override
		public ByteString encode(ColumnId f) {
			return ByteString.copyFromUtf8(f.id);
		}
	}
}
