package com.KGC.SAUW.commands;
import java.util.ArrayList;
import com.KGC.SAUW.Player;
import com.badlogic.gdx.Gdx;

public class Cmd {
	public static class Argument {
		Object val = new Object();
		public int toInt() {
			return Integer.parseInt(val.toString());
		}

		@Override
		public String toString() {
			return (String) val;
		}
		public float toFloat() {
			return Float.parseFloat(val.toString());
		}
		public double toDouble() {
			return Double.parseDouble(val.toString());
		}
	}
	public static abstract class Command {
		public String cmd;

		public int countArgs;
		public abstract String function(Argument[] args);
		public String getCmd() {
			return cmd;
		}
		public void setCmd(String command) {
			cmd = command;
		}
	}
	private ArrayList<Command> commands = new ArrayList<Command>();
	public void addCommand(String cmd, int countArgs, Command comand) {
		comand.setCmd(cmd);
		comand.countArgs = countArgs;
		commands.add(comand);
	}
	public String executeCmd(String str) {
		str += " ";
		String[] args;
		Argument[] arguments = null;

		for (Cmd.Command i: commands) {
			args = str.split("\\s+");
			if (args.length == i.countArgs + 1) {
				if (args[0].equals(i.getCmd())) {
					arguments = new Argument[i.countArgs];
					for (int j = 0; j < i.countArgs;j++) {
						arguments[j] = new Argument();
						arguments[j].val = args[j + 1];
					}
					return i.function(arguments);
				}
			} else return "Error: most count args = " + i.countArgs + ", input count args = " + (args.length - 2);
		}
		return "Error: invalid syntax";
	}
}
/*public class commands {
    ArrayList<command> Command = new ArrayList<command>();
	player pl;
	public commands(player pl) {
		this.pl = pl;
    }
    public void addCommand(command command) {
        Command.add(command);
	}
	public void run(String command) {
		for (int i = 0; i < Command.size(); i++) {
			if (command.length() > Command.get(i).CommandName.length()) {
				Gdx.app.log("log", command.substring(0, Command.get(i).CommandName.length()));
				if (command.substring(0, Command.get(i).CommandName.length()) == (Command.get(i).CommandName)) {
					Command.get(i).run(command.substring(Command.get(i).CommandName.length() + 1).split("\\s+"));
				}
			}
		}
	}
}*/
