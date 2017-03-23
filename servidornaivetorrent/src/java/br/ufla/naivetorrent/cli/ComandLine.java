/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufla.naivetorrent.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luizfps
 */
public class ComandLine implements Runnable {

    public static final String CREATE_TORRENT = "create-torrent";
    public static final String ADD_TORRENT = "add-torrent";
    public static final String PLAY = "play";
    public static final String PAUSE = "pause";
    public static final String REMOVE = "remove";
    public static final String LIST_TORRENT = "pause";
    public static final String HELP = "help";
    public static final String QUIT = "quit";
    public static final String CMD = "cmd";
    public static final String MENSAGEM_HELP = "# crete-torrent <conteudo-compartilhado> <arquivo-torrent> <lista-rastreadores> <(op)criador> <(op)comentário> <(op)codificação>\n"
            + "# add-torrent <local-conteudo-compartilhado> <arquivo-torrent>\n"
            + "# play <id-torrent>\n"
            + "# pause <id-torrent>\n"
            + "# remove-torrent <id-torrent>\n"
            + "# list-torrent\n"
            + "# help\n"
            + "# quit\n";

    public String entrada;

    Thread display;

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
        //commands.add(CMD);
        return commands;
    }

    public static boolean isValidCommand(String[] cmd) {

        if (cmd.length != 0) {
            //List<String> commands = commands();

            String strCmd = cmd[0];

            if (strCmd.equals(cmd[0])) {

                if (strCmd.equals(CREATE_TORRENT) && cmd.length >= 3) {
                    return true;
                }
                if (strCmd.equals(ADD_TORRENT) && cmd.length == 3) {
                    return true;
                }
                if (strCmd.equals(PLAY) && cmd.length == 2) {
                    return true;
                }
                if (strCmd.equals(PAUSE) && cmd.length == 2) {
                    return true;
                }
                if (strCmd.equals(REMOVE) && cmd.length == 2) {
                    return true;
                }
                if (strCmd.equals(LIST_TORRENT) && cmd.length == 1) {
                    return true;
                }
                if (strCmd.equals(HELP) && cmd.length == 1) {
                    return true;
                }
                if (strCmd.equals(QUIT) && cmd.length == 1) {
                    return true;
                }

            }

        }

        return false;
    }

    @Override
    public void run() {

        String[] comandos;
        Display listagem;
        String entrada;

        Scanner leitor = new Scanner(System.in);

        while (true) {

            listagem = new Display();

            display = new Thread(listagem);

            display.start();

            entrada = leitor.nextLine();

            comandos = entrada.split(" ");

          
            if (comandos[0].equals(CMD)) {

                //matamos a thread
                listagem.setAtiva(false);

                System.out.print("cmd> ");
                entrada = leitor.nextLine();
                comandos = entrada.split(" ");

                //enquanto o comando para sair do cmd for digitado
                while (!comandos[0].equals(QUIT)) {

                    while (!isValidCommand(comandos)) {

                        System.out.print("cmd> ");
                        System.out.println("comando invalido!");
                        System.out.print("cmd> ");
                        entrada = leitor.nextLine();
                        comandos = entrada.split(" ");
                    }

                    if (comandos[0].equals(CREATE_TORRENT)) {
                        
                        

                    }
                    if (comandos[0].equals(ADD_TORRENT)) {
                            
                        
                    }
                    if (comandos[0].equals(PLAY)) {
                        
                    }
                    if (comandos[0].equals(PAUSE)) {
                       
                    }
                    if (comandos[0].equals(REMOVE)) {
                        
                    }
                    if (comandos[0].equals(LIST_TORRENT)) {
                       
                    }
                    if (comandos[0].equals(HELP)) {

                        System.out.print("cmd> ");
                        System.out.println("List of all NaiveTorrent commands:");
                         System.out.println(MENSAGEM_HELP);

                    }

                    System.out.print("cmd> ");
                    entrada = leitor.nextLine();
                    comandos = entrada.split(" ");
                }

            } else if (comandos[0].equals(HELP)) {
                    listagem.setAtiva(false);
                    System.out.print("help> ");
                    System.out.println("List of all NaiveTorrent commands:");
                   
                    System.out.println("#cmd - comandos principais Naive torrent");
                try {
                    Thread.sleep(8000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ComandLine.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
         }
    }

}

