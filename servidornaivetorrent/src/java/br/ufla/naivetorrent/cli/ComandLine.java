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
        //commands.add(CMD);
        return commands;
    }
    
    public static boolean isValidCommand(String[] cmd) {
        
        
        if(cmd.length!=0){
        //List<String> commands = commands();
        
        String strCmd = cmd[0];
            
            
            if (strCmd.equals(cmd[0])) {
                
                if(strCmd.equals(CREATE_TORRENT) && cmd.length >=3){
                    return true;
                }
                if(strCmd.equals(ADD_TORRENT) && cmd.length ==3){
                    return true;
                }
                if(strCmd.equals(PLAY) && cmd.length ==2){
                    return true;
                }
                if(strCmd.equals(PAUSE) && cmd.length ==2){
                    return true;
                }
                if(strCmd.equals(REMOVE) && cmd.length ==2){
                    return true;
                }
                if(strCmd.equals(LIST_TORRENT) && cmd.length ==1){
                    return true;
                }
                if(strCmd.equals(HELP) && cmd.length ==1){
                    return true;
                }
                if(strCmd.equals(QUIT) && cmd.length ==1){
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
         
        while(true){
        
           
            listagem= new Display();
            
            display = new Thread(listagem);
            
            display.start();

            Scanner leitor = new Scanner(System.in);

            entrada = leitor.nextLine();

            comandos = entrada.split(" ");

            while(comandos.length!=1 && !(comandos[0].equals(CMD) || comandos[0].equals(QUIT))){

                entrada = leitor.nextLine();

            }
            comandos = entrada.split(" ");
            
            if(comandos[0].equals(CMD)){
                
                System.out.println("entrou");
                //matamos a thread
                listagem.setAtiva(false);
                
            
                entrada = leitor.nextLine();
                
                
                comandos = entrada.split(" ");
                
                while(!isValidCommand(comandos)){
                    
                    entrada = leitor.nextLine();
                    comandos = entrada.split(" ");
                }
                
                if(comandos[0].equals(CREATE_TORRENT)){
                    
                }
                if(comandos[0].equals(ADD_TORRENT)){
                    
                }
                if(comandos[0].equals(PLAY)){
                    
                }
                if(comandos[0].equals(PAUSE)){
                    
                }
                if(comandos[0].equals(REMOVE)){
                    
                }
                if(comandos[0].equals(LIST_TORRENT)){
                    
                }
                if(comandos[0].equals(HELP)){
                    System.out.println("List of all NaiveTorrent commands:");
                    entrada = leitor.nextLine();
                    comandos = entrada.split(" ");
                    
                    
                }while(comandos[0].equals(HELP)){
                        entrada = leitor.nextLine();
                        comandos = entrada.split(" ");
                    }
                if(comandos[0].equals(QUIT)){
                    
                }
                
                
             }
            else if(comandos[0].equals(HELP)){
                 listagem.setAtiva(false);
                 while(comandos[0].equals(HELP)){
                     System.out.println("List of all NaiveTorrent commands:");
                        entrada = leitor.nextLine();
                        comandos = entrada.split(" ");
                    }
                
                    
             }
            }   
        
        }
    }
