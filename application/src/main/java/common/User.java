package common;

import com.google.protobuf.ByteString;

import eu.antidotedb.client.AntidoteClient;
import eu.antidotedb.client.Bucket;
import eu.antidotedb.client.MapRef;
import eu.antidotedb.client.ValueCoder;

public class User {
	public UserId user_id = null;
	
	Bucket<UserId> cbucket = Bucket.create("userbucket", new UserId.Coder());

	public User(UserId user_id) {
		this.user_id = user_id;
	}
	
	public User() {
	}

	enum UserField {
		user_email, tasks
	}
	
	static class UserFieldCoder implements ValueCoder<UserField> {

		@Override
		public UserField cast(Object o) {
			return (UserField) o;
		}

		@Override
		public UserField decode(ByteString b) {
			return UserField.valueOf(b.toStringUtf8());
		}

		@Override
		public ByteString encode(UserField f) {
			return ByteString.copyFromUtf8(f.name());
		}
		
	}
	
	public MapRef<UserField> userMap(UserId user_id) {
		return cbucket.map_aw(user_id, new UserFieldCoder());
	}

	public UserId createUser(AntidoteClient client, String email) {
		UserId user_id = UserId.getid();
		MapRef<UserField> user = userMap(user_id);
		user.register(UserField.user_email).set(client.noTransaction(), email);
		return user_id;
	}
}
