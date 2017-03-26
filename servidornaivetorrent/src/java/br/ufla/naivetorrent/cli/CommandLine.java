/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufla.naivetorrent.cli;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import br.ufla.naivetorrent.cli.console.Console;
import br.ufla.naivetorrent.cli.console.ConsoleForegroundColors;
import br.ufla.naivetorrent.domain.file.MetaTorrent;
import br.ufla.naivetorrent.domain.file.ShareTorrent;
import br.ufla.naivetorrent.domain.tracker.Tracker;
import br.ufla.naivetorrent.persistance.CreateDatabase;
import br.ufla.naivetorrent.persistance.DaoRecoveryShareTorrents;
import br.ufla.naivetorrent.torrent.CreateTorrent;
import br.ufla.naivetorrent.torrent.ExtractMetaInfo;

/**
 * 
 * @author luizfps
 */
public class CommandLine implements Runnable {

	public static final String CREATE_TORRENT = "create-torrent";
	public static final String ADD_TORRENT = "add-torrent";
	public static final String PLAY = "play";
	public static final String PAUSE = "pause";
	public static final String REMOVE = "remove-torrent";
	public static final String LIST_TORRENT = "list-torrent";
	public static final String HELP = "help";
	public static final String QUIT = "quit";
	public static final String CMD = "cmd";
	public static final String CLEAR = "clear";
	public static final String MENSAGEM_HELP = 
			"# crete-torrent <conteudo-compartilhado> <arquivo-torrent> <lista-rastreadores> "
			+ "<(op)criador> <(op)comentário> <(op)codificação>\n"
			+ "# add-torrent <local-conteudo-compartilhado> <arquivo-torrent>\n" 
			+ "# play <id-torrent>\n"
			+ "# pause <id-torrent>\n" + "# remove-torrent <id-torrent>\n" 
			+ "# list-torrent\n" 
			+ "# help\n"
			+ "# quit\n";
	private static final String WELCOME_TXT = 
			"--------------------------\n" 
			+ Console.BOLD.getValue()
			+ ConsoleForegroundColors.LIGHT_BLUE.getValue()
			+ "Bem vindo ao NaiveTorrent!\n" 
			+ Console.DEFAULT.getValue()
			+ "--------------------------\n"
			+ "Um software para compartilhamento de arquivos em redes peer-to-peer que usa\n"
			+ "um protocolo de comunicação baseado no BitTorrent." 
			+ "\n\n\n\n"
			+ "Desenvolvido por Carlos Pereira\n" 
			+ "                 Daniel Borges\n"
			+ "                 Gabriel Rodriguez\n" 
			+ "                 Luiz Felipe\n";

	private Thread displayThread;
	private Scanner scanner;
	private String[] commandTokens;
	private List<ShareTorrent> shareTorrents;

	public CommandLine() {
		scanner = new Scanner(System.in);
		shareTorrents = new ArrayList<>();
	}

	/**
	 * Recupera a lista de comandos.
	 * 
	 * @return lista de comandos
	 */
	@SuppressWarnings("unused")
	private static List<String> commands() {
		List<String> commands = new ArrayList<>();
		commands.add(CREATE_TORRENT);
		commands.add(ADD_TORRENT);
		commands.add(PLAY);
		commands.add(PAUSE);
		commands.add(REMOVE);
		commands.add(LIST_TORRENT);
		commands.add(HELP);
		commands.add(QUIT);
		commands.add(CMD);
		return commands;
	}

	@Override
	public void run() {
		Console.CLEAR_HOME.apply();
		System.out.println(WELCOME_TXT);

		createOrRecoveryDatabase();

		Display display = new Display(shareTorrents);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		Console.CLEAR_HOME.apply();
		while (true) {
			display.setAtiva(true);
			displayThread = new Thread(display);
			displayThread.start();
			do {
				nextCommandTokens();
			} while (commandTokens.length != 1 && !(commandTokens[0].equals(CMD) || commandTokens[0].equals(QUIT)
					|| commandTokens[0].equals(HELP)));
			if (commandTokens[0].equals(CMD)) {
				display.setAtiva(false);
				Console.CLEAR_HOME.apply();
				System.out.println("Modo de comando: digite 'help' para conseguir ajuda.");
				do {
					System.out.print("cmd> ");
					nextCommandTokens();
					while (!isValidCommand()) {
						System.out.println("Comando inválido, entrei com 'help' " + "para conseguir ajuda!");
						System.out.print("cmd> ");
						nextCommandTokens();
					}
					switch (commandTokens[0]) {
					case CREATE_TORRENT:
						createTorrentCmd();
						break;
					case ADD_TORRENT:
						addTorrentCmd();
						break;
					case PLAY:
						playTorrentCmd();
						break;
					case PAUSE:
						pauseTorrentCmd();
						break;
					case REMOVE:
						removeTorrentCmd();
						break;
					case LIST_TORRENT:
						listTorrentCmd();
						break;
					case HELP:
						System.out.println(MENSAGEM_HELP);
						break;
					case CLEAR:
						Console.CLEAR_HOME.apply();
						break;
					}
				} while (!commandTokens[0].equals(QUIT));
			} else if (commandTokens[0].equals(HELP)) {
				display.setAtiva(false);
				System.out.print("help> ");
				System.out.println("#cmd - comandos principais NaiveTorrent");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}

	private void createOrRecoveryDatabase() {
		try {
			CreateDatabase createDatabase = new CreateDatabase();
			createDatabase.create();
			DaoRecoveryShareTorrents daoRecoveryShareTorrents = new DaoRecoveryShareTorrents();
			shareTorrents = daoRecoveryShareTorrents.getShareTorrents();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Verifica se o comando atual é válido.
	 * 
	 * @return true se o comando é válido, caso contrário false
	 */
	private boolean isValidCommand() {
		if (commandTokens.length == 0) {
			return false;
		}
		String strCmd = commandTokens[0];
		int commandLength = commandTokens.length;
		if (strCmd.equals(CREATE_TORRENT) && commandLength >= 3) {
			return true;
		}
		if (strCmd.equals(ADD_TORRENT) && commandLength >= 3) {
			return true;
		}
		if (strCmd.equals(PLAY) && commandLength == 2) {
			return true;
		}
		if (strCmd.equals(PAUSE) && commandLength == 2) {
			return true;
		}
		if (strCmd.equals(REMOVE) && commandLength == 2) {
			return true;
		}
		if (strCmd.equals(LIST_TORRENT) && commandLength == 1) {
			return true;
		}
		if (strCmd.equals(HELP) && commandLength == 1) {
			return true;
		}
		if (strCmd.equals(QUIT) && commandLength == 1) {
			return true;
		}
		if (strCmd.equals(CLEAR) && commandLength == 1) {
			return true;
		}
		return false;
	}

	/**
	 * Lê o próximo comando e gera os tokens
	 */
	private void nextCommandTokens() {
		commandTokens = scanner.nextLine().split(" ");
		List<String> cleanTokens = new ArrayList<>();
		for (String token : commandTokens) {
			if (!token.isEmpty()) {
				cleanTokens.add(token);
			}
		}
		commandTokens = new String[cleanTokens.size()];
		commandTokens = cleanTokens.toArray(commandTokens);
	}

	private List<Tracker> decodeTrackersStr(List<String> trackersStr) {
		List<Tracker> trackers = new ArrayList<>();
		for (String trackerStr : trackersStr) {
			Tracker tracker = new Tracker();
			tracker.setAddressListeningString(trackerStr);
			trackers.add(tracker);
		}
		return trackers;
	}

	/**
	 * Realiza o procedimento do comando de criar um torrent.
	 */
	private void createTorrentCmd() {
		ExtractCommand extractCommand = new ExtractCommand(commandTokens);
		try {
			@SuppressWarnings("unused")
			String cmd = extractCommand.readCmd();
			String sharePath = extractCommand.readParameter();
			String torrentPath = extractCommand.readParameter();
			List<String> trackersStr = extractCommand.readList();
			String createdBy = extractCommand.readParameter();
			String comment = extractCommand.readParameter();
			String encoding = extractCommand.readParameter();

			File shareFile = new File(sharePath);
			File torrentFile = new File(torrentPath);
			torrentFile.createNewFile();
			List<Tracker> trackers = decodeTrackersStr(trackersStr);

			ExtractMetaInfo extractMetaInfo = new ExtractMetaInfo(shareFile);
			extractMetaInfo.setCreatedBy(createdBy);
			extractMetaInfo.setComment(comment);
			extractMetaInfo.setEncoding(encoding);
			extractMetaInfo.setTrackers(trackers);

			MetaTorrent metaTorrentCreated = extractMetaInfo.generateMetaTorrent();
			CreateTorrent createTorrent = new CreateTorrent(metaTorrentCreated, torrentFile);
			createTorrent.create();
			System.out.println(
					ConsoleForegroundColors.GREEN.getValue()
					+ "Arquivo .torrent foi criado com sucesso!"
					+ Console.DEFAULT.getValue());
		} catch (Exception e) {
			System.err.println(
					ConsoleForegroundColors.RED.getValue()
					+ e.getMessage() 
					+ Console.DEFAULT.getValue());
			// e.printStackTrace();
		}

	}

	private void addTorrentCmd() {
		// TODO Auto-generated method stub

	}

	private void playTorrentCmd() {
		// TODO Auto-generated method stub

	}

	private void pauseTorrentCmd() {
		// TODO Auto-generated method stub

	}

	private void removeTorrentCmd() {
		// TODO Auto-generated method stub

	}

	private void listTorrentCmd() {
		// TODO Auto-generated method stub

	}

	/**
	 * Teste
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new Thread(new CommandLine()).start();
	}
}
