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
public class ComandLine  implements Runnable{
    
    public static final String CREATE_TORRENT = "create-torrent";
    public static final String ADD_TORRENT ="add-torrent";
    public static final String PLAY ="play";
    public static final String PAUSE ="pause";
    public static final String REMOVE ="remove";
    public static final String LIST_TORRENT ="pause";
    public static final String HELP ="help";
    public static final String QUIT ="quit";
    public static final String CMD ="cmd";
    
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
        commands.add(CMD);
        return commands;
    }
    
    public static boolean isValidCommand(String cmd) {
        List<String> commands = commands();
        for (String strCmd : commands) {
            if (strCmd.equals(cmd)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void run() {
        boolean controle;
        
        while(true){
        
            String[] comandos;
            Display listagem = new Display();
            display = new Thread(listagem);
            display.start();

            Scanner leitor = new Scanner(System.in);

            entrada = leitor.nextLine();


            comandos = entrada.split(" ");

            while(comandos.length!=1 && !(comandos[0].equals(CMD) || comandos[0].equals(QUIT))){

                entrada = leitor.nextLine();

            }
            if(comandos[0].equals(CMD)){
                
                //matamos a thread
                listagem.setAtiva(false);
                controle = true;
                entrada = leitor.nextLine();
                
                comandos = entrada.split(" ");
                
                
                
                while(controle){
                    
                    
                    if(comandos[0].equals(ADD_TORRENT))
                        !comandos[0].equals(CREATE_TORRENT)
                        !comandos[0].equals(PLAY)
                        !comandos[0].equals(PAUSE)
                        !comandos[0].equals(REMOVE)
                        !comandos[0].equals(LIST_TORRENT)
                        !comandos[0].equals(HELP)
                        !comandos[0].equals(QUIT)
                    
                    
                    
                    
                }
                
       
                
            }
        
        
        
        
        }
    }

}
