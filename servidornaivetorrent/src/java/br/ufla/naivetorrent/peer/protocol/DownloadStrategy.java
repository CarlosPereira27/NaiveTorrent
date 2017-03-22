/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufla.naivetorrent.peer.protocol;

import br.ufla.naivetorrent.domain.file.ShareTorrent;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * 
 * @author lfps
 */
public class DownloadStrategy {
    
    ShareTorrent sharetorrent;
    
    public static final int QTDPEDACOS = 10;

    
    public DownloadStrategy(ShareTorrent sharetorrent) {
        this.sharetorrent = sharetorrent;
    }
    
    
    public ShareTorrent getSharetorrent() {
        return sharetorrent;
    }

    public void setSharetorrent(ShareTorrent sharetorrent) {
        this.sharetorrent = sharetorrent;
    }
    
    /**
     * 
     * @return lista de inteiros que representam os indices do vetor de bitset dos pedaços mais raros 
     */
    public List<Integer> getPieces(){
        
        List<Integer> pieces =  new ArrayList<>();
        
        PriorityQueue<PieceFrequency> fila = new PriorityQueue<>();
        
        fila = queue();
        
        int tamanho = 0;
        
        while(!fila.isEmpty() && tamanho<=QTDPEDACOS){
            
            pieces.add(fila.poll().index);
            tamanho++;   
        }
        return pieces;
    }
    
    private PriorityQueue<PieceFrequency> queue(){
        
        PriorityQueue<PieceFrequency> fila = new PriorityQueue<>();
        
        BitSet MyBitField = sharetorrent.getMyBitfield();
        
                
        int size =sharetorrent.getMyBitfield().size();
        
        Set<BitSet> bitfields = (Set<BitSet>) sharetorrent.getIdToBitfield().values();
        
        for(int i=0;i<size;i++)// meu bitfield
        {
            
            if(!MyBitField.get(i)){
                
                int frequencia = 0;
                for(BitSet bitfield:bitfields){
                  
                    if(bitfield.get(i)){
                    
                        frequencia++;
                     }
                }
                
            fila.add(new PieceFrequency (i,frequencia));
            
            }
        }
        
        return fila;
        }
    
   static class PieceFrequency implements Comparable<PieceFrequency>{
       
       
        public PieceFrequency(int index, int frequency) {
            this.index = index;
            this.frequency = frequency;
        }
        
             
        int index;
        int frequency;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getFrequency() {
            return frequency;
        }

        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }

        @Override
        public int compareTo(PieceFrequency o) {
            
            return  frequency-o.frequency;
        }

        @Override
        public String toString() {
            return "PieceFrequency "+"{"+" frequency=" + frequency + '}';
        }
        
    }
    
    public static void main(String[] args){
        
//       
//       PriorityQueue<PieceFrequency> fila = new PriorityQueue<>();
//       
//       fila.add(new PieceFrequency(0,5));
//       fila.add(new PieceFrequency(0,6));
//       fila.add(new PieceFrequency(0,1));
//       fila.add(new PieceFrequency(0,3));
//        
//       while(!fila.isEmpty()){
//           
//           System.out.println(fila.poll());
//           
//       }
    }
 }
