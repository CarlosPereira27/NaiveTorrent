/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufla.naivetorrent.cli;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author luizfps
 */
public class Display implements Runnable {

	private boolean ativa = true;

	@Override
	public void run() {
		ConsoleClear.clear();
		while (isAtiva()) {
			try {
				System.out.println("DISPLAY DADOS");
				Thread.sleep(3000);
				if (isAtiva()) {
					ConsoleClear.clear();
				}
			} catch (InterruptedException ex) {
				System.out.println(ex.getMessage());
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
