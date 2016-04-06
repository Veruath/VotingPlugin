package com.Ben12345rocks.VotingPlugin.Commands;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

import com.Ben12345rocks.VotingPlugin.Main;
import com.Ben12345rocks.VotingPlugin.Utils;
import com.Ben12345rocks.VotingPlugin.Config.Config;
import com.Ben12345rocks.VotingPlugin.Config.ConfigBonusReward;
import com.Ben12345rocks.VotingPlugin.Config.ConfigFormat;
import com.Ben12345rocks.VotingPlugin.Config.ConfigVoteSites;
import com.Ben12345rocks.VotingPlugin.Data.Data;
import com.Ben12345rocks.VotingPlugin.Objects.User;
import com.Ben12345rocks.VotingPlugin.Objects.VoteSite;

public class Commands {

	static ConfigBonusReward bonusReward = ConfigBonusReward.getInstance();

	static Config config = Config.getInstance();

	static ConfigVoteSites configVoteSites = ConfigVoteSites.getInstance();

	static ConfigFormat format = ConfigFormat.getInstance();

	static Commands instance = new Commands();

	static Main plugin = Main.plugin;

	public static Commands getInstance() {
		return instance;
	}

	private Commands() {
	}

	public Commands(Main plugin) {
		Commands.plugin = plugin;
	}

	public ArrayList<String> adminHelpText() {
		ArrayList<String> msg = new ArrayList<String>();
		msg.add("VotingPlugin Admin Help");
		msg.add("[] = Optional");
		msg.add("() = Needed");
		msg.add("Aliases: adminvote, av");
		msg.add("/adminvote help - See this page");
		msg.add("/adminvote vote (player) (sitename) - Trigger server only vote");
		msg.add("/adminvote bungeevote (player) (sitename) - Trigger bungee only vote");
		msg.add("/adminvote globalvote (player) (sitename) - Trigger server and bungee vote");
		msg.add("/adminvote settotal (player) (sitename) (amount) - Set total votes of a player on votesite");
		msg.add("/adminvote reload - Reload the plugin");
		msg.add("/adminvote uuid (playername) - See uuid of player");
		msg.add("/adminvote version - Version info");
		msg.add("/adminvote sites [site] - List of sites and site info");
		msg.add("/adminvote VoteSite (SiteName) AddItem (Item) - Add item in hand to votesite");
		msg.add("/adminvote VoteSite (SiteName) SetMoney (Money) - Set money for votesite");
		msg.add("/adminvote VoteSite (SiteName) SetServiceSite (ServiceSite) - Set servicesite on votesite");
		msg.add("/adminvote VoteSite (SiteName) SetDisabled (Disabled) - Set votesite disabled");
		msg.add("/adminvote VoteSite (SiteName) SetVoteDelay (Delay) - Set votesite votedelay");
		msg.add("/adminvote VoteSite (SiteName) AddCommandPlayer (Command) - Add player command to votesite");
		msg.add("/adminvote VoteSite (SiteName) AddCommandConsole (Command) - Add console command to votesite");
		msg.add("/adminvote VoteSite (SiteName) AddChanceRewardItem (Reward) (Item) - Add ChanceReward item in hand to votesite");
		msg.add("/adminvote VoteSite (SiteName) SetChanceRewardMoney (Reward) (Money) - Set ChanceReward money for votesite");
		msg.add("/adminvote VoteSite (SiteName) SetChanceRewardChance (Reward) (Chance) - Set ChanceReward chance");
		msg.add("/adminvote VoteSite (SiteName) AddChanceRewardCommandPlayer (Reward) (Command) - Add ChanceReward player command to votesite");
		msg.add("/adminvote VoteSite (SiteName) AddChanceRewardCommandConsole (Reward) (Command) - Add ChanceReward console command to votesite");
		msg.add("/adminvote BonusReward AddItem (Item) - Add item in hand");
		msg.add("/adminvote BonusReward SetMoney (Money) - Set money");
		msg.add("/adminvote BonusReward SetGiveBonusReward (Disabled) - Set bonus reward enabled");
		msg.add("/adminvote BonusReward AddCommandPlayer (Command) - Add player command");
		msg.add("/adminvote BonusReward AddCommandConsole (Command) - Add console command");
		msg.add("/adminvote BonusReward AddChanceRewardItem (Reward) (Item) - Add ChanceReward item in hand");
		msg.add("/adminvote BonusReward SetChanceRewardMoney (Reward) (Money) - Set ChanceReward money");
		msg.add("/adminvote BonusReward SetChanceRewardChance (Reward) (Chance) - Set ChanceReward chance");
		msg.add("/adminvote BonusReward AddChanceRewardCommandPlayer (Reward) (Command) - Add ChanceReward player command");
		msg.add("/adminvote BonusReward AddChanceRewardCommandConsole (Reward) (Command) - Add ChanceReward console command");
		msg.add("/adminvote Config SetDebug (true/false) - Set debug");
		msg.add("/adminvote Config SetBroadcastVote (true/false) - Set broadcastvote");
		msg.add("/adminvote Config SetUpdateReminder (true/false) - Set updatereminder");
		msg.add("/adminvote Config SetAllowUnjoined (true/false) - Set allowunjoined");
		msg.add("/adminvote Config SetDisableTopVoterAwards (true/false) - Set disabletopvoterawards");
		msg.add("/adminvote ServerData SetPrevMonth - Set prevmonth, DO NOT USE");
		return msg;
	}

	public String[] adminHelpTextColored() {
		ArrayList<String> texts = new ArrayList<String>();
		for (String msg : adminHelpText()) {
			if (msg.split("-").length > 1) {
				texts.add("&3&l" + msg.split("-")[0] + "-&3"
						+ msg.split("-")[1]);
			} else {
				texts.add("&3&l" + msg.split("-")[0]);
			}
		}
		texts = Utils.getInstance().colorize(texts);
		return Utils.getInstance().convertArray(texts);

	}

	public String[] commandVoteToday(int page) {
		int pagesize = ConfigFormat.getInstance().getPageSize();
		if (page < 1) {
			page = 1;
		}
		ArrayList<String> msg = new ArrayList<String>();

		msg.add("&cToday's Votes " + page + "/"
				+ ((plugin.voteToday.length / pagesize) + 1));
		msg.add("&cPlayerName : VoteSite : Time");
		page--;

		for (int i = pagesize * page; (i < plugin.voteToday.length)
				&& (i < ((page + 1) * pagesize)); i++) {
			msg.add(plugin.voteToday[i]);
		}

		msg = Utils.getInstance().colorize(msg);
		return Utils.getInstance().convertArray(msg);

	}

	public String[] playerInfo(User user) {
		ArrayList<String> msg = new ArrayList<String>();

		// title
		msg.add("&cPlayer '" + user.getPlayerName() + "' Info");

		// last vote
		msg.addAll(Utils.getInstance().convertArray(voteCommandLast(user)));

		// next vote
		msg.addAll(Utils.getInstance().convertArray(voteCommandNext(user)));

		// total
		msg.addAll(Utils.getInstance().convertArray(voteCommandTotal(user)));

		msg = Utils.getInstance().colorize(msg);
		return Utils.getInstance().convertArray(msg);
	}

	public String[] voteCommandLast(User user) {

		ArrayList<String> msg = new ArrayList<String>();

		ArrayList<VoteSite> voteSites = configVoteSites.getVoteSites();

		String playerName = user.getPlayerName();

		msg.add(Utils.getInstance().replaceIgnoreCase(
				format.getCommandsVoteLastTitle(), "%player%", playerName));

		for (VoteSite voteSite : voteSites) {
			Date date = new Date(user.getTime(voteSite));
			String timeString = new SimpleDateFormat(format.getTimeFormat())
					.format(date);

			msg.add(format
					.getCommandsVoteLastLine()
					.replace("%Month% %Day%, %Year% %Hour%:%Minute% %ampm%",
							"%time%").replace("%time%", timeString)
					.replace("%SiteName%", voteSite.getSiteName()));
		}

		msg = Utils.getInstance().colorize(msg);
		return Utils.getInstance().convertArray(msg);
	}

	@SuppressWarnings({ "deprecation", "unused" })
	public String[] voteCommandNext(User user) {
		ArrayList<String> msg = new ArrayList<String>();

		ArrayList<VoteSite> voteSites = configVoteSites.getVoteSites();

		String playerName = user.getPlayerName();

		msg.add(Utils.getInstance().colorize(
				Utils.getInstance().replaceIgnoreCase(
						format.getCommandsVoteNextTitle(), "%player%",
						playerName)));

		for (VoteSite voteSite : voteSites) {

			String msgLine = format.getCommandsVoteNextLayout();

			Date date = new Date(user.getTime(voteSite));

			int month = date.getMonth();
			int day = date.getDate();
			int hour = date.getHours();
			int min = date.getMinutes();
			int year = date.getYear();

			int votedelay = configVoteSites
					.getVoteDelay(voteSite.getSiteName());
			if (votedelay == 0) {
				String errorMsg = format.getCommandsVoteNextInfoError();
				msgLine = Utils.getInstance().replaceIgnoreCase(msgLine,
						"%info%", errorMsg);
			} else {

				Date voteTime = new Date(year, month, day, hour, min);
				Date nextvote = DateUtils.addHours(voteTime, votedelay);

				int cday = new Date().getDate();
				int cmonth = new Date().getMonth();
				int chour = new Date().getHours();
				int cmin = new Date().getMinutes();
				int cyear = new Date().getYear();
				Date currentDate = new Date(cyear, cmonth, cday, chour, cmin);

				if ((nextvote == null) || (day == 0) || (hour == 0)) {
					String canVoteMsg = format.getCommandsVoteNextInfoCanVote();
					msgLine = Utils.getInstance().replaceIgnoreCase(msgLine,
							"%info%", canVoteMsg);
				} else {
					if (!currentDate.after(nextvote)) {
						long diff = nextvote.getTime() - currentDate.getTime();

						long diffSeconds = (diff / 1000) % 60;
						long diffMinutes = (diff / (60 * 1000)) % 60;
						long diffHours = diff / (60 * 60 * 1000);
						// long diffDays = diff / (24 * 60 * 60 * 1000);

						String timeMsg = format.getCommandsVoteNextInfoTime();
						timeMsg = Utils.getInstance().replaceIgnoreCase(
								timeMsg, "%hours%", Long.toString(diffHours));
						timeMsg = Utils.getInstance().replaceIgnoreCase(
								timeMsg, "%minutes%",
								Long.toString(diffMinutes));
						msgLine = Utils.getInstance().replaceIgnoreCase(
								msgLine, "%info%", timeMsg);
					} else {
						String canVoteMsg = format
								.getCommandsVoteNextInfoCanVote();
						msgLine = Utils.getInstance().replaceIgnoreCase(
								msgLine, "%info%", canVoteMsg);
					}
				}
			}
			msgLine = Utils.getInstance().replaceIgnoreCase(msgLine,
					"%SiteName%", voteSite.getSiteName());
			msg.add(Utils.getInstance().colorize(msgLine));

		}
		return Utils.getInstance().convertArray(msg);
	}

	public String[] voteCommandSiteInfo(String voteSiteName) {
		ArrayList<String> msg = new ArrayList<String>();

		if (!ConfigVoteSites.getInstance().getVoteSiteFile(voteSiteName)
				.exists()) {
			msg.add("&cInvalid Vote Site, see /av sites!");
		} else {

			VoteSite voteSite = plugin.getVoteSite(voteSiteName);

			msg.add("&c&lVote Site Info for " + voteSiteName + ":");

			msg.add("&cSite: &6" + voteSite.getServiceSite());
			msg.add("&cVoteURL: &6" + voteSite.getVoteURL());
			msg.add("&cVote Delay: &6" + voteSite.getVoteDelay());
			msg.add("&cMoney: &6" + voteSite.getMoney());

			msg.add("&cItems:");
			for (String item : ConfigVoteSites.getInstance().getItems(
					voteSite.getSiteName())) {
				msg.add("&c- &6" + item);
			}

			msg.add("&cPlayer Commands:");

			try {
				for (String playerCommands : voteSite.getPlayerCommands()) {
					msg.add("&c- " + playerCommands);
				}
			} catch (Exception ex) {
			}

			msg.add("&cConsole Commands:");

			try {
				for (String consoleCommands : voteSite.getConsoleCommands()) {
					msg.add("&c- " + consoleCommands);
				}
			} catch (Exception ex) {
			}

			msg.add("&c&lExtra Rewards:");
			for (String reward : voteSite.getExtraRewardsChance().keySet()) {
				msg.add("&cReward: " + reward);
				msg.add("&cChance: &6"
						+ voteSite.getExtraRewardsChance().get(reward));
				msg.add("&cMoney: &6"
						+ voteSite.getExtraRewardsMoney().get(reward));
				msg.add("&cPermission: &6"
						+ voteSite.getExtraRewardsPermission().get(reward));

				msg.add("&cItems:");
				for (String item : ConfigVoteSites.getInstance()
						.getExtraRewardItems(voteSite.getSiteName(), reward)) {
					msg.add("&c- &6" + item);
				}

				msg.add("&cPlayer Commands:");

				try {
					for (String playerCommands : voteSite
							.getExtraRewardsPlayerCommands().get(reward)) {
						msg.add("&c- " + playerCommands);
					}
				} catch (Exception ex) {
				}

				msg.add("&cConsole Commands:");

				try {
					for (String consoleCommands : voteSite
							.getExtraRewardsConsoleCommands().get(reward)) {
						msg.add("&c- " + consoleCommands);
					}
				} catch (Exception ex) {
				}
			}

			msg.add("&c&lCumulative Rewards:");

			msg.add("&cVotes: &6" + voteSite.getCumulativeVotes());
			msg.add("&cMoney: &6" + voteSite.getCumulativeMoney());

			msg.add("&cItems:");
			for (String item : ConfigVoteSites.getInstance()
					.getCumulativeRewardItems(voteSite.getSiteName())) {
				msg.add("&c- &6" + item);
			}

			msg.add("&cPlayer Commands:");

			try {
				for (String playerCommands : voteSite
						.getCumulativePlayerCommands()) {
					msg.add("&c- " + playerCommands);
				}
			} catch (Exception ex) {
			}

			msg.add("&cConsole Commands:");

			try {
				for (String consoleCommands : voteSite
						.getCumulativeConsoleCommands()) {
					msg.add("&c- " + consoleCommands);
				}
			} catch (Exception ex) {
			}
		}
		msg = Utils.getInstance().colorize(msg);
		return Utils.getInstance().convertArray(msg);
	}

	public String[] voteCommandSites() {
		ArrayList<String> msg = new ArrayList<String>();

		msg.add("&c&lVote Sites:");

		int count = 1;
		ArrayList<VoteSite> voteSites = ConfigVoteSites.getInstance()
				.getVoteSites();
		if (voteSites != null) {
			for (VoteSite voteSite : voteSites) {
				msg.add("&c" + count + ". &6" + voteSite.getSiteName());
				count++;
			}
		}

		msg = Utils.getInstance().colorize(msg);
		return Utils.getInstance().convertArray(msg);
	}

	public String[] voteCommandTotal(User user) {
		ArrayList<String> msg = new ArrayList<String>();
		ArrayList<VoteSite> voteSites = configVoteSites.getVoteSites();

		String playerName = user.getPlayerName();

		msg.add(Utils.getInstance().replaceIgnoreCase(
				format.getCommandsVoteTotalTitle(), "%player%", playerName));

		// total votes
		int total = 0;

		for (VoteSite voteSite : voteSites) {
			int votes = user.getTotal(voteSite);
			// int votes = Data.getInstance().getTotal(playerName, siteName);
			total += votes;
			String line = format.getCommandsVoteTotalLine();
			msg.add(Utils.getInstance().replaceIgnoreCase(
					Utils.getInstance().replaceIgnoreCase(line, "%Total%",
							"" + votes), "%SiteName%", voteSite.getSiteName()));

		}
		msg.add(Utils.getInstance().replaceIgnoreCase(
				format.getCommandsVoteTotalTotal(), "%Totals%", "" + total));

		msg = Utils.getInstance().colorize(msg);
		return Utils.getInstance().convertArray(msg);
	}

	public String[] voteCommandTotalAll() {

		ArrayList<String> msg = new ArrayList<String>();

		ArrayList<VoteSite> voteSites = configVoteSites.getVoteSites();

		ArrayList<String> voteNames = Data.getInstance().getPlayerNames();

		msg.add(format.getCommandsVoteTotalAllTitle());
		int total = 0;
		for (VoteSite voteSite : voteSites) {
			int votes = 0;
			for (String playerName : voteNames) {
				if (playerName != null) {
					User user = new User(playerName);
					votes += user.getTotal(voteSite);
				}
			}
			msg.add(Utils.getInstance().replaceIgnoreCase(
					Utils.getInstance().replaceIgnoreCase(
							format.getCommandsVoteTotalAllLine(), "%SiteName%",
							voteSite.getSiteName()), "%Total%", "" + votes));
			total += votes;
		}
		msg.add(Utils.getInstance().replaceIgnoreCase(
				format.getCommandsVoteTotalAllTotal(), "%Totals%", "" + total));

		msg = Utils.getInstance().colorize(msg);
		return Utils.getInstance().convertArray(msg);
	}

	public ArrayList<String> voteHelpText() {
		ArrayList<String> texts = new ArrayList<String>();
		texts.add("VotingPlugin Player Help");
		texts.add("[] = Optional");
		texts.add("Aliases: vote, v");
		texts.add("/vote - List vote URLs");
		texts.add("/vote help - See this page");
		texts.add("/vote total [Player/All] - See total votes");
		texts.add("/vote next [Player] - See next time you can vote");
		texts.add("/vote last [Player] - See last vote");
		texts.add("/vote top [Page] - See top voters");
		texts.add("/vote info [Player] - See player info");
		texts.add("/vote today [Page] - See who voted today");
		return texts;
	}

	public String[] voteHelpTextColored() {
		ArrayList<String> texts = new ArrayList<String>();
		for (String msg : voteHelpText()) {
			if (msg.split("-").length > 1) {
				texts.add("&3&l" + msg.split("-")[0] + "-&3"
						+ msg.split("-")[1]);
			} else {
				texts.add("&3&l" + msg.split("-")[0]);
			}
		}
		texts = Utils.getInstance().colorize(texts);
		return Utils.getInstance().convertArray(texts);

	}

	@SuppressWarnings("deprecation")
	public String[] voteToday() {
		ArrayList<String> msg = new ArrayList<String>();

		ArrayList<User> users = Utils.getInstance().convertSet(
				Data.getInstance().getUsers());

		if (users != null) {

			for (User user : users) {
				for (VoteSite voteSite : configVoteSites.getVoteSites()) {
					long time = user.getTime(voteSite);
					if (new Date().getDay() == Utils.getInstance()
							.getDayFromMili(time)) {

						String timeString = new SimpleDateFormat(
								format.getTimeFormat()).format(new Date(time));
						msg.add("&6" + user.getPlayerName() + " : "
								+ voteSite.getSiteName() + " : " + timeString);
					}
				}
			}
		}
		msg = Utils.getInstance().colorize(msg);
		return Utils.getInstance().convertArray(msg);
	}

	public String[] voteURLs() {
		ArrayList<String> sites = new ArrayList<String>();
		ArrayList<VoteSite> voteSites = configVoteSites.getVoteSites();

		List<String> title = ConfigFormat.getInstance().getCommandsVoteTitle();
		if (title != null) {
			sites.addAll(title);
		}
		int counter = 0;
		for (VoteSite voteSite : voteSites) {
			counter++;
			String voteURL = configVoteSites.getVoteURL(voteSite.getSiteName());
			String msg = format.getCommandsVoteURLS();
			msg = Utils.getInstance().colorize(msg);
			msg = Utils.getInstance().replaceIgnoreCase(msg, "%num%",
					Integer.toString(counter));
			msg = Utils.getInstance().replaceIgnoreCase(msg, "%url%", voteURL);
			msg = Utils.getInstance().replaceIgnoreCase(msg, "%SiteName%",
					voteSite.getSiteName());
			sites.add(msg);
		}
		sites = Utils.getInstance().colorize(sites);
		return Utils.getInstance().convertArray(sites);
	}

}