package com.Ben12345rocks.VotingPlugin.Commands.Executers;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.Ben12345rocks.VotingPlugin.Main;
import com.Ben12345rocks.VotingPlugin.Utils;
import com.Ben12345rocks.VotingPlugin.Config.Config;
import com.Ben12345rocks.VotingPlugin.Objects.CommandHandler;

public class CommandAliases implements CommandExecutor {

	private Main plugin = Main.plugin;

	private CommandHandler cmdHandle;

	public CommandAliases(CommandHandler cmdHandle) {
		this.cmdHandle = cmdHandle;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		ArrayList<String> argsNew = new ArrayList<String>();
		argsNew.add(cmdHandle.getArgs()[0]);
		for (String arg : args) {
			argsNew.add(arg);
		}
		if (Config.getInstance().getDebugEnabled()) {
			plugin.getLogger().info("Attempting cmd...");
			plugin.getLogger()
					.info(Utils.getInstance().makeStringList(argsNew));
			plugin.getLogger().info(
					Utils.getInstance().makeStringList(
							Utils.getInstance().convertArray(
									cmdHandle.getArgs())));
		}
		for (String arg : cmdHandle.getArgs()[0].split("&")) {
			argsNew.set(0, arg);
			if (cmdHandle.runCommand(sender,
					Utils.getInstance().convertArray(argsNew))) {
				if (Config.getInstance().getDebugEnabled()) {
					plugin.getLogger().info("cmd found, ran cmd");
				}
				return true;
			}
		}

		// invalid command
		sender.sendMessage(ChatColor.RED
				+ "No valid arguments, see /vote help!");
		return true;
	}
}
