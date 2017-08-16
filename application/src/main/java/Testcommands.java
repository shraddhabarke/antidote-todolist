import java.io.IOException;
import java.util.Date;

import eu.antidotedb.client.AntidoteClient;
import eu.antidotedb.client.Host;

import asg.cliche.Command;
import asg.cliche.ShellFactory;
import common.*;

public class Testcommands {
	
	AntidoteClient currentSession;
	
	@Command
	public String hello() {
	    return "Hello, World!";
	}
	
	@Command
	public UserId createuser(String email) {
		return (new User().createUser(currentSession, email));
	}
	
	@Command
	public BoardId createboard(String name) {
		return (new Board().createBoard(currentSession, name));
	}

	@Command
	public void renameboard(BoardId board_id, String newName) {
		 new Board().renameBoard(currentSession, board_id, newName);
	}

	@Command
	public ColumnId addcolumn(BoardId board_id, String name) {
		return (new Column().addColumn(currentSession, board_id, name));
	}

	@Command
	public void renamecolumn(ColumnId column_id, String newName) {
		new Column().renameColumn(currentSession, column_id, newName);
	}

	@Command
	public void deletecolumn(ColumnId column_id) {
		new Column().deleteColumn(currentSession, column_id);

	}

	@Command 
	public TaskId createtask(ColumnId column_id, String title) {
		return (new Task().createTask(currentSession, column_id, title));
	}
	
	@Command
	public void updatetitle(TaskId task_id, String newTitle) {
		new Task().updateTitle(currentSession, task_id, newTitle);
	}	
	
	@Command
	public void updatedueDate(TaskId task_id, Date dueDate ) {
		new Task().updateDueDate(currentSession, task_id, dueDate);
	}
	
	@Command //connect antidote
	public String connect(String host, int port){
		currentSession = new AntidoteClient(new Host(host, port));
		return "Connected to " + host+":"+port;
	}
	
	@Command //exit shell
	public void quit(){
		System.out.println("Exiting app...");
		System.exit(0);
	}
		
	public static void main(String[] args) throws IOException {
	    ShellFactory.createConsoleShell("application@antidote"+args[0], "", new Testcommands())
	        .commandLoop();
}
}
