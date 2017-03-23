/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufla.naivetorrent.cli;

import java.io.IOException;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class Display implements Runnable{
    
    private boolean ativa = true;

    @Override
    public void run() {
        
        while(isAtiva()){
            
            try {
                Runtime.getRuntime().exec("clear");  
                
                System.out.println("luiz");
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Display.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Display.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

    public synchronized boolean isAtiva() {
        return ativa;
    }

    public synchronized void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }
    

}
