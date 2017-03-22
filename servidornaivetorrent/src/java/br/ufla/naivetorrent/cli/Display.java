/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufla.naivetorrent.cli;

/**
 * 
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class Display implements Runnable{
    
    private boolean ativa = true;

    @Override
    public void run() {
        
        while(isAtiva()){
            System.out.println("luiz");
        }
        
    }

    public synchronized boolean isAtiva() {
        return ativa;
    }

    public synchronized void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }
    
}
