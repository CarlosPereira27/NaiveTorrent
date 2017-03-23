/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package teste;

import br.ufla.naivetorrent.cli.ComandLine;

/**
 * 
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class Principal {
    
     public static void main(String[] args){
        
            Thread t1 = new Thread(new ComandLine());
            t1.start();
    }
}
