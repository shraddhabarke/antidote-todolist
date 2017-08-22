import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.List;

import eu.antidotedb.client.AntidoteClient;

import asg.cliche.Command;
import asg.cliche.ShellFactory;
import common.*;

public class Testcommands {
	
	AntidoteClient currentSession;
	
	Board board = new Board();
	Column column = new Column();
	Task task = new Task();
	
	@Command
	public String hello() {
	    return "Hello, World!";
	}
	
	@Command
	public UserId createuser(String email) {
		return (new User().createUser(currentSession, email));
	}
	
	@Command
	public List<BoardId> listboards() {
		return board.listBoards();
	}

	@Command
	public BoardId createboard(String name) {
		return board.createBoard(currentSession, name);
	}

	@Command
	public void renameboard(BoardId board_id, String newName) {
		 board.renameBoard(currentSession, board_id, newName);
	}

	@Command
	public ColumnId addcolumn(BoardId board_id, String name) {
		return column.addColumn(currentSession, board_id, name);
	}

	@Command
	public void renamecolumn(ColumnId column_id, String newName) {
		column.renameColumn(currentSession, column_id, newName);
	}

	@Command
	public void deletecolumn(ColumnId column_id) {
		column.deleteColumn(currentSession, column_id);

	}

	@Command 
	public TaskId createtask(ColumnId column_id, String title) {
		return task.createTask(currentSession, column_id, title);
	}
	
	@Command
	public void updatetitle(TaskId task_id, String newTitle) {
		task.updateTitle(currentSession, task_id, newTitle);
	}	
	
	@Command
	public void updateduedate(TaskId task_id, Date dueDate ) {
		task.updateDueDate(currentSession, task_id, dueDate);
	}
	
	@Command
	public void movetask(TaskId task_id, ColumnId newcolumn_id) {
		task.moveTask(currentSession, task_id, newcolumn_id);
	}

	@Command
	public void deletetask(TaskId task_id) {
		task.deleteTask(currentSession, task_id);
	}
	
	@Command
	public BoardMap getboard(BoardId board_id) {
		return board.getBoard(currentSession, board_id);
	}
	
	@Command
	public ColumnMap getcolumn(ColumnId column_id) {
		return column.getColumn(currentSession, column_id);
	}

	@Command
	public TaskMap gettask(TaskId task_id) {
		return task.getTask(currentSession, task_id);
	}
	@Command //connect antidote
	public String connect(String host, int port){
	    currentSession = new AntidoteClient(new InetSocketAddress("127.0.0.1", 8087));
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
