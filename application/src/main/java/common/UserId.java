package common;

import java.util.UUID;

import com.google.protobuf.ByteString;
import eu.antidotedb.client.ValueCoder;

public class UserId {
	
	private String id;
	
	public UserId(String uniqueID) {
		id = uniqueID;
	}

	public static UserId getid() {
		String uniqueID = UUID.randomUUID().toString();
		return new UserId(uniqueID);
	}
	
	static class Coder implements ValueCoder<UserId> {

		@Override
		public UserId cast(Object o) {
			return (UserId) o;
		}

		@Override
		public UserId decode(ByteString b) {
			return new UserId(b.toStringUtf8());
		}

		@Override
		public ByteString encode(UserId f) {
			return ByteString.copyFromUtf8(f.id);
		}
		
	}

}
