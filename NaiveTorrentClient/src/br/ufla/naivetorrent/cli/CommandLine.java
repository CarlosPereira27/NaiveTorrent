/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufla.naivetorrent.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
    public static final String MENSAGEM_HELP = 
    		"# crete-torrent <conteudo-compartilhado> <arquivo-torrent> <lista-rastreadores> "
    		+ "<(op)criador> <(op)comentário> <(op)codificação>\n"
            + "# add-torrent <local-conteudo-compartilhado> <arquivo-torrent>\n"
            + "# play <id-torrent>\n"
            + "# pause <id-torrent>\n"
            + "# remove-torrent <id-torrent>\n"
            + "# list-torrent\n"
            + "# help\n"
            + "# quit\n";

	private Thread displayThread;
	private Scanner scanner;
	private String[] commandTokens;
	
	public CommandLine() {
		scanner = new Scanner(System.in);
	}

	/**
	 * Recupera a lista de comandos.
	 * @return lista de comandos
	 */
	public static List<String> commands() {
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

	/**
	 * Verifica se o comando atual é válido.
	 * @return true se o comando é válido, caso contrário false
	 */
	public boolean isValidCommand() {
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
		return false;
	}
	
	/**
	 * Lê o próximo comando e gera os tokens
	 */
	public void nextCommandTokens() {
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

	@Override
	public void run() {
		Display display = new Display();
		while (true) {
			display.setAtiva(true);
			displayThread = new Thread(display);
			displayThread.start();
			do {
				nextCommandTokens();
			} while (commandTokens.length != 1 && 
					!(commandTokens[0].equals(CMD) 
							|| commandTokens[0].equals(QUIT)
							|| commandTokens[0].equals(HELP)));
			if (commandTokens[0].equals(CMD)) {
				display.setAtiva(false);
				ConsoleClear.clear();
				System.out.println("Modo de comando: digite 'help' para conseguir ajuda.");
				do {
					System.out.print("cmd> ");
					nextCommandTokens();
					while (!isValidCommand()) { 
						System.out.println("Comando inválido, entrei com 'help' "
								+ "para conseguir ajuda!");
						System.out.print("cmd> ");
						nextCommandTokens();
					}
					switch(commandTokens[0]) {
					case CREATE_TORRENT:
						
						break;
					case ADD_TORRENT:
						
						break;
					case PLAY:
						
						break;
					case PAUSE:
						
						break;
					case REMOVE:
						
						break;
					case LIST_TORRENT:
						
						break;
					case HELP:
						System.out.println(MENSAGEM_HELP);
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
	
	/**
	 * Teste
	 * @param args
	 */
	public static void main(String[] args) {
		new Thread(new CommandLine()).start();
	}
}
