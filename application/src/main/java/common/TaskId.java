package common;

import java.util.UUID;

import com.google.protobuf.ByteString;
import eu.antidotedb.client.ValueCoder;

public class TaskId {
	
	private String id;
	
	public TaskId(String uniqueID) {
		id = uniqueID;
	}
	
	public static TaskId generateId() {
		String uniqueID = UUID.randomUUID().toString();
		return new TaskId(uniqueID);
	}
	
	public String getId() {
		return id;
	}
	
	public String toString() {
		return id;
	}

	static class Coder implements ValueCoder<TaskId> {

		@Override
		public TaskId decode(ByteString b) {
			return new TaskId(b.toStringUtf8());
		}

		@Override
		public ByteString encode(TaskId f) {
			return ByteString.copyFromUtf8(f.id);
		}		
	}
}
