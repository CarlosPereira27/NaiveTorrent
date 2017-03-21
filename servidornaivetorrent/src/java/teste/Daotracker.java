/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teste;

import br.ufla.naivetorrent.domain.peer.Peer;
import br.ufla.naivetorrent.tracker.protocol.RequestMessage;
import br.ufla.naivetorrent.tracker.protocol.ResponseMessage;
import br.ufla.naivetorrent.util.UtilGenerateId;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lfps
 */
public class Daotracker {
    
        Connection con;
        public static final String LEECHER = "leecher";
        public static final int TEMPO = 180;
        int leechers = 0;
        int seeders = 0;
        
        

    public Daotracker() throws IOException, SQLException, PropertyVetoException {
        this.con = DataSource.getInstance().getConnection();
       
    }
    public ResponseMessage StoppedFile(RequestMessage mensagem) throws SQLException{
        int idtorrent;
                  
           idtorrent = buscarIdClient(mensagem.getInfoHash());
           
           removerTorrent(idtorrent);
           
           ResponseMessage resposta = new ResponseMessage();
          
           getSeedersAndLenchers(mensagem.getInfoHash());
           resposta.setInterval(TEMPO);
           resposta.setMinInterval(TEMPO-60);
           resposta.setComplete(seeders);
           resposta.setIncomplete(leechers);
           resposta.setTrackerId(mensagem.getTrackerId());
         //retorna a resposta para o peer que requisitou 
        return resposta;
        
    }
    public ResponseMessage Stopped(RequestMessage mensagem) throws SQLException{
        
           int idclient;
                  
           idclient = buscarIdClient(mensagem.getPeerId());
           
           removerClient(idclient);
           
           ResponseMessage resposta = new ResponseMessage();
          
           getSeedersAndLenchers(mensagem.getInfoHash());
           resposta.setInterval(TEMPO);
           resposta.setMinInterval(TEMPO-60);
           resposta.setComplete(seeders);
           resposta.setIncomplete(leechers);
           resposta.setTrackerId(mensagem.getTrackerId());
         //retorna a resposta para o peer que requisitou 
        return resposta;
        
    }
    public ResponseMessage Completed (RequestMessage mensagem) throws IOException, SQLException, PropertyVetoException{
        
         
           int idclient;
           int idtorrent;
           
           
           idclient = buscarIdClient(mensagem.getPeerId());
           idtorrent = buscarIdTorrent(mensagem.getInfoHash());
           
           ResponseMessage resposta = new ResponseMessage();
           atualizar(idclient,idtorrent,mensagem);
          getSeedersAndLenchers(mensagem.getInfoHash());
           resposta.setInterval(TEMPO);
           resposta.setMinInterval(TEMPO-60);
           resposta.setComplete(seeders);
           resposta.setIncomplete(leechers);
            resposta.setTrackerId(mensagem.getTrackerId());
         //retorna a resposta para o peer que requisitou 
        return resposta;
        
 
    }
    public ResponseMessage Starting(RequestMessage mensagem) throws IOException, SQLException, PropertyVetoException{
            
           int idclient;
           int idtorrent;
           ResponseMessage resposta = new ResponseMessage();
           
           idclient = buscarIdClient(mensagem.getPeerId());
           idtorrent = buscarIdTorrent(mensagem.getInfoHash());
           
           //se o client e o arquivo não estiver no banco
           if(idclient==-1 && idtorrent==-1){
               
               inserirPeer(mensagem);
               inserirTorrent(mensagem);
               VinculaClientTorrent(buscarIdClient(mensagem.getPeerId()),buscarIdTorrent(mensagem.getInfoHash()),mensagem,LEECHER);
               getSeedersAndLenchers(mensagem.getInfoHash());
               resposta.setPeers(listaPeer(mensagem.getInfoHash()));
           }
           // cliente não existe mais arquivo existe
           else if(idclient==-1 && idtorrent!=-1){
               inserirPeer(mensagem);
               VinculaClientTorrent(buscarIdClient(mensagem.getPeerId()),idtorrent,mensagem,LEECHER);
                getSeedersAndLenchers(mensagem.getInfoHash());
                resposta.setPeers(listaPeer(mensagem.getInfoHash()));
           }
           // cliente existe e arquivo também
           else if (idclient!=-1 && idtorrent!=-1){
               //criar novo id para este cliente
               mensagem.setPeerId(UtilGenerateId.generateIdHex(mensagem.getIp(),mensagem.getPort()));
               ////
               inserirPeer(mensagem);
               VinculaClientTorrent(buscarIdClient(mensagem.getPeerId()),idtorrent,mensagem,LEECHER);
                getSeedersAndLenchers(mensagem.getInfoHash());
                resposta.setPeers(listaPeer(mensagem.getInfoHash()));
           }
           //cliente  existe e arquivo não existe
           else if(idclient!=-1 && idtorrent==-1){
                //criar novo id para este cliente
               mensagem.setPeerId(UtilGenerateId.generateIdHex(mensagem.getIp(),mensagem.getPort()));
               ////
               inserirPeer(mensagem);
               inserirTorrent(mensagem);
               VinculaClientTorrent(buscarIdClient(mensagem.getPeerId()),buscarIdTorrent(mensagem.getInfoHash()),mensagem,LEECHER);
               resposta.setPeers(listaPeer(mensagem.getInfoHash()));
               getSeedersAndLenchers(mensagem.getInfoHash());
              
           }
           
           resposta.setInterval(TEMPO);
           resposta.setMinInterval(TEMPO-60);
           resposta.setComplete(seeders);
           resposta.setIncomplete(leechers);
           resposta.setTrackerId(mensagem.getTrackerId());
         //retorna a resposta para o peer que requisitou 
        return resposta;
    }   
    public void inserirPeer(RequestMessage mensagem) throws IOException, SQLException, PropertyVetoException{
        
             PreparedStatement ps = null;
             ResultSet rs = null;
           
        
        try{
            
            
            String  sql = "INSERT INTO `tracker`.`client` ( `info_hash_id`, `ip`, `last_conection`) VALUES (?,?,?)";
            
            ps.setString(1, mensagem.getPeerId());
            ps.setString(2, mensagem.getIp());
            ps.setLong(3, new Date().getTime());
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            ps.close();
            rs.close();
      
        } catch (SQLException ex) {
           System.out.println(ex);
        }
        

    }
    public List<Peer> listaPeer(String hasharquivo){
            PreparedStatement ps = null;
             ResultSet rs = null;
             List<Peer> listapeers = null;
           
        try{
            
            listapeers = new ArrayList<>();
            
            String  sql = "select * from client_torrent inner join client on client_torrent.id_client = client.id inner join torrent on torrent.id = client_torrent.id_torrent where torrent.hash_id="+"'"+hasharquivo+"'";
            
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while(rs.next()){
                
                Peer peer = new Peer();
                peer.setIdHex(rs.getString("id_cliente"));
                peer.setSocketAddressListening(new InetSocketAddress(rs.getString("ip"),rs.getInt("port")));
                listapeers.add(peer);              
                
            }
            
            ps.close();
            rs.close();
      
        } catch (SQLException ex) {
           System.out.println(ex);
        }
        
        return listapeers;
        
    }
    public int buscarIdClient(String idpeer){
        
        
             PreparedStatement ps = null;
             ResultSet rs = null;
             int idbuscado = -1;
        
        try{
            
            
            String  sql = "select id from client where info_hash_id='"+idpeer+"'";
            
           
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            idbuscado = rs.getInt("id");
            ps.close();
            rs.close();
            
            return idbuscado;
      
        } catch (SQLException ex) {
           System.out.println(ex);
        }
        return idbuscado;
    }   
    public void inserirTorrent(RequestMessage mensagem ) throws IOException, SQLException, PropertyVetoException{
        
             PreparedStatement ps = null;
             ResultSet rs = null;
        
        try{
            
            
            String  sql = "INSERT INTO `tracker`.`torrent` ( `hash_id`) VALUES (?);";
            
            ps.setString(1, mensagem.getInfoHash());
            
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            ps.close();
            rs.close();
            
      
        } catch (SQLException ex) {
           System.out.println(ex);
        }
        
    }
    public int buscarIdTorrent(String hash_id){
        
        
             PreparedStatement ps = null;
             ResultSet rs = null;
             int idbuscado = -1;
        
        try{
            
            
            String  sql = "select id from torrent where hash_id='"+hash_id+"'";
            
           
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            idbuscado = rs.getInt("id");
            ps.close();
            rs.close();
            
            return idbuscado;
      
        } catch (SQLException ex) {
           System.out.println(ex);
        }
        return idbuscado;
    }   
    public void VinculaClientTorrent(int id_client,int id_torrent,RequestMessage mensagem,String tipo){
                 PreparedStatement ps = null;
             ResultSet rs = null;
        
        try{
            
            
            String  sql = "INSERT INTO `tracker`.`client_torrent` (`id_client`, `id_torrent`, `download`, `upload`, `tipo_client`) VALUES (?,?,?,?,?);";
            
            ps.setInt(1, id_client);
            ps.setInt(2, id_torrent);
            ps.setLong(3, mensagem.getDownloaded());
            ps.setLong(4, mensagem.getUploaded());
            ps.setString(5,tipo );
            
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            ps.close();
            rs.close();
            
      
        } catch (SQLException ex) {
           System.out.println(ex);
        }
        
    }
    public void getSeedersAndLenchers(String hasharquivo){
           
             leechers = 0;
             seeders =0;
             PreparedStatement ps = null;
             ResultSet rs = null;
             int idbuscado = -1;
        
        try{
            
            
            String  sql = "select * from client_torrent inner join client on client_torrent.id_client = client.id inner join torrent on torrent.id = client_torrent.id_torrent where torrent.hash_id="+"'"+hasharquivo+"'";
            
           
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                
               if(rs.getString("tipo_client").equals("seeder"))
               {
                 seeders++;   
               }
               else{
                   leechers++;
               }
                
            }
            
            
      
        } catch (SQLException ex) {
           System.out.println(ex);
        }
        
    }
    public void atualizar(int idcliente, int idtorrent, RequestMessage mensagem){
           PreparedStatement ps = null;
             ResultSet rs = null;
        
        try{
            
            
            String  sql = "UPDATE `tracker`.`client_torrent` SET `tipo_client`='seeder',download='"+mensagem.getDownloaded()+"', upload='"+mensagem.getUploaded()+"' WHERE `id_client`='"+idcliente+"'and id_torrent='"+idtorrent+"';";
           
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            ps.close();
            rs.close();
            
            sql = "UPDATE `tracker`.`client` SET `last_conection`='"+new Date().getTime()+"' WHERE `id`='"+idcliente+"'";

                    
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            ps.close();
            rs.close();
            
      
        } catch (SQLException ex) {
           System.out.println(ex);
        }
        
    }
    public void fecharConexao() throws SQLException{
        con.close();
    }
    public void removerClient(int idcliente) throws SQLException{
         PreparedStatement ps = null;
             ResultSet rs = null;
        
        try{
            
            
            String  sql = "DELETE FROM `tracker`.`client` WHERE `id`='"+idcliente+"'";
           
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            ps.close();
            rs.close();
            
      
        } catch (SQLException ex) {
           System.out.println(ex);
        }
        
    }
     public void removerTorrent(int idtorrent) throws SQLException{
         PreparedStatement ps = null;
             ResultSet rs = null;
        
        try{
            
            
            String  sql = "DELETE FROM `tracker`.`client_torrent` WHERE `id_torrent`='"+idtorrent+"'";
           
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            ps.close();
            rs.close();
           
            
      
        } catch (SQLException ex) {
           System.out.println(ex);
        }
        
    }
    
}
