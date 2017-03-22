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

    public ResponseMessage StoppedFile(RequestMessage mensagem) throws SQLException {

        int idtorrent;

        int idclient;

        idtorrent = buscarIdClient(mensagem.getInfoHash());

        idclient = buscarIdClient(mensagem.getPeerId());

        stopShareFile(idtorrent, idclient);

        ResponseMessage resposta = new ResponseMessage();

        getSeedersAndLenchers(mensagem.getInfoHash());

        resposta.setInterval(TEMPO);

        resposta.setMinInterval(TEMPO);

        resposta.setComplete(seeders);

        resposta.setIncomplete(leechers);

        resposta.setTrackerId(mensagem.getTrackerId());
        
        resposta.setPeers(listaPeer(mensagem.getInfoHash()));//verificar

        //retorna a resposta para o peer que requisitou 
        return resposta;

    }

    public ResponseMessage Stopped(RequestMessage mensagem) throws SQLException {

        int idclient;

        idclient = buscarIdClient(mensagem.getPeerId());

        removerClient(idclient);

        ResponseMessage resposta = new ResponseMessage();

        getSeedersAndLenchers(mensagem.getInfoHash());

        resposta.setInterval(TEMPO);

        resposta.setMinInterval(TEMPO);

        resposta.setComplete(seeders);

        resposta.setIncomplete(leechers);

        resposta.setTrackerId(mensagem.getTrackerId());
        
        resposta.setPeers(listaPeer(mensagem.getInfoHash()));

        //retorna a resposta para o peer que requisitou 
        return resposta;

    }

    public ResponseMessage Completed(RequestMessage mensagem) throws IOException, SQLException, PropertyVetoException {

        int idclient;

        int idtorrent;

        idclient = buscarIdClient(mensagem.getPeerId());

        idtorrent = buscarIdTorrent(mensagem.getInfoHash());

        ResponseMessage resposta = new ResponseMessage();

        atualizar(idclient, idtorrent, mensagem, "seeder");

        getSeedersAndLenchers(mensagem.getInfoHash());

        resposta.setInterval(TEMPO);

        resposta.setMinInterval(TEMPO);

        resposta.setComplete(seeders);

        resposta.setIncomplete(leechers);
        
        resposta.setPeers(listaPeer(mensagem.getInfoHash()));

        resposta.setTrackerId(mensagem.getTrackerId());

        //retorna a resposta para o peer que requisitou 
        return resposta;

    }

    public ResponseMessage Updated(RequestMessage mensagem) throws IOException, SQLException, PropertyVetoException {

        int idclient;

        int idtorrent;

        idclient = buscarIdClient(mensagem.getPeerId());

        idtorrent = buscarIdTorrent(mensagem.getInfoHash());

        ResponseMessage resposta = new ResponseMessage();

        atualizar(idclient, idtorrent, mensagem, "leecher");

        getSeedersAndLenchers(mensagem.getInfoHash());

        resposta.setInterval(TEMPO);

        resposta.setMinInterval(TEMPO);

        resposta.setComplete(seeders);

        resposta.setIncomplete(leechers);

        resposta.setTrackerId(mensagem.getTrackerId());
        
        resposta.setPeers(listaPeer(mensagem.getInfoHash()));

        //retorna a resposta para o peer que requisitou 
        return resposta;

    }

    public ResponseMessage Starting(RequestMessage mensagem) throws IOException, SQLException, PropertyVetoException {

        int idclient;

        int idtorrent;

        ResponseMessage resposta = new ResponseMessage();

        idclient = buscarIdClient(mensagem.getPeerId());

        idtorrent = buscarIdTorrent(mensagem.getInfoHash());

        //se o client e o arquivo não estiver no banco
        if (idclient == -1 && idtorrent == -1) {

            inserirPeer(mensagem);

            inserirTorrent(mensagem);

            VinculaClientTorrent(buscarIdClient(mensagem.getPeerId()), buscarIdTorrent(mensagem.getInfoHash()), mensagem, LEECHER);

            getSeedersAndLenchers(mensagem.getInfoHash());

            resposta.setPeers(listaPeer(mensagem.getInfoHash()));

        } // cliente não existe mais arquivo existe
        else if (idclient == -1 && idtorrent != -1) {

            inserirPeer(mensagem);

            VinculaClientTorrent(buscarIdClient(mensagem.getPeerId()), idtorrent, mensagem, LEECHER);

            getSeedersAndLenchers(mensagem.getInfoHash());

            resposta.setPeers(listaPeer(mensagem.getInfoHash()));

        } // cliente existe e arquivo também
        else if (idclient != -1 && idtorrent != -1) {
            //criar novo id para este cliente
            mensagem.setPeerId(UtilGenerateId.generateIdHex(mensagem.getIp(), mensagem.getPort()));
            ////
            inserirPeer(mensagem);

            VinculaClientTorrent(buscarIdClient(mensagem.getPeerId()), idtorrent, mensagem, LEECHER);

            getSeedersAndLenchers(mensagem.getInfoHash());

            resposta.setPeers(listaPeer(mensagem.getInfoHash()));

            resposta.setClientId(mensagem.getPeerId());

        } //cliente  existe e arquivo não existe
        else if (idclient != -1 && idtorrent == -1) {
            //criar novo id para este cliente
            mensagem.setPeerId(UtilGenerateId.generateIdHex(mensagem.getIp(), mensagem.getPort()));
            ////

            inserirPeer(mensagem);

            inserirTorrent(mensagem);

            VinculaClientTorrent(buscarIdClient(mensagem.getPeerId()), buscarIdTorrent(mensagem.getInfoHash()), mensagem, LEECHER);

            resposta.setPeers(listaPeer(mensagem.getInfoHash()));

            resposta.setClientId(mensagem.getPeerId());

            getSeedersAndLenchers(mensagem.getInfoHash());

        }

        resposta.setInterval(TEMPO);

        resposta.setMinInterval(TEMPO);

        resposta.setComplete(seeders);

        resposta.setIncomplete(leechers);

        resposta.setTrackerId(mensagem.getTrackerId());

//retorna a resposta para o peer que requisitou 
        return resposta;
    }

    public void inserirPeer(RequestMessage mensagem) throws IOException, SQLException, PropertyVetoException {

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            String sql = "INSERT INTO `tracker`.`client` ( `info_hash_id`, `port`,`ip`, `last_conection`) VALUES (?,?,?,?)";
            
            ps = con.prepareStatement(sql);
            
            ps.setString(1, mensagem.getPeerId());
            
            ps.setInt(2, mensagem.getPort());
            
            ps.setString(3, mensagem.getIp());

            ps.setLong(4, new Date().getTime());

            ps.execute();

            ps.close();


        } catch (SQLException ex) {

            System.out.println("erro 2 "+ex);
        }

    }

    public List<Peer> listaPeer(String hasharquivo) {

        PreparedStatement ps = null;

        ResultSet rs = null;

        List<Peer> listapeers = null;

        try {

            listapeers = new ArrayList<>();

            String sql = "select * from client_torrent inner join client on client_torrent.id_client = client.id inner join torrent on torrent.id = client_torrent.id_torrent where torrent.hash_id=" + "'" + hasharquivo + "'";

            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {

                Peer peer = new Peer();

                peer.setIdHex(rs.getString("id_client"));

                peer.setSocketAddressListening(new InetSocketAddress(rs.getString("ip"), rs.getInt("port")));

                listapeers.add(peer);

            }

            rs.close();
            
            ps.close();

        } catch (SQLException ex) {

            System.out.println("erro 1 "+ex);
        }

        return listapeers;

    }

    public int buscarIdClient(String idpeer) {

        PreparedStatement ps = null;

        ResultSet rs = null;

        int idbuscado = -1;

        try {

            String sql = "select id from client where info_hash_id='" + idpeer + "'";

            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();
            
                       
            if(rs.next()){
                idbuscado = rs.getInt("id");
            }  

            rs.close();
            
             ps.close();
            

            return idbuscado;

        } catch (SQLException ex) {

            System.out.println("erro 3 "+ex);
        }

        return idbuscado;
    }

    public void inserirTorrent(RequestMessage mensagem) throws IOException, SQLException, PropertyVetoException {

        PreparedStatement ps = null;

        

        try {

            String sql = "INSERT INTO `tracker`.`torrent` ( `hash_id`) VALUES (?);";
            
            ps = con.prepareStatement(sql);

            ps.setString(1, mensagem.getInfoHash());

            ps.execute();

            ps.close();

          
        } catch (SQLException ex) {

           System.out.println("erro 4 "+ex);
        }

    }

    public int buscarIdTorrent(String hash_id) {

        PreparedStatement ps = null;

        ResultSet rs = null;

        int idbuscado = -1;

        try {

            String sql = "select id from torrent where hash_id='" + hash_id + "'";

            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();
            
                        
            if(rs.next()){
                idbuscado = rs.getInt("id");
            }

            ps.close();

            rs.close();

            return idbuscado;

        } catch (SQLException ex) {

            System.out.println("erro 5 "+ex);
        }
        return idbuscado;
    }

    public void VinculaClientTorrent(int id_client, int id_torrent, RequestMessage mensagem, String tipo) {

        PreparedStatement ps = null;

        

        try {

            String sql = "INSERT INTO `tracker`.`client_torrent` (`id_client`, `id_torrent`, `download`, `upload`, `tipo_client`) VALUES (?,?,?,?,?);";

            ps = con.prepareStatement(sql);
            
            ps.setInt(1, id_client);

            ps.setInt(2, id_torrent);

            ps.setLong(3, mensagem.getDownloaded());

            ps.setLong(4, mensagem.getUploaded());

            ps.setString(5, tipo);
          
            ps.execute();

            ps.close();

     
        } catch (SQLException ex) {
            System.out.println("erro 6 "+ex);
        }

    }

    public void getSeedersAndLenchers(String hasharquivo) {

        leechers = 0;

        seeders = 0;

        PreparedStatement ps = null;

        ResultSet rs = null;

        

        try {

            String sql = "select * from client_torrent inner join client on client_torrent.id_client = client.id inner join torrent on torrent.id = client_torrent.id_torrent where torrent.hash_id=" + "'" + hasharquivo + "'";

            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {

                if (rs.getString("tipo_client").equals("seeder")) {

                    seeders++;

                } else {

                    leechers++;
                }

            }
            
            rs.close();
            
            ps.close();

        } catch (SQLException ex) {

            System.out.println("erro 7 "+ex);
        }

    }

    public void atualizar(int idcliente, int idtorrent, RequestMessage mensagem, String tipo) {

        PreparedStatement ps = null;

        

        try {

            String sql = "UPDATE `tracker`.`client_torrent` SET `tipo_client`='" + tipo + "',download='" + mensagem.getDownloaded() + "', upload='" + mensagem.getUploaded() + "' WHERE `id_client`='" + idcliente + "'and id_torrent='" + idtorrent + "';";

            ps = con.prepareStatement(sql);

            ps.execute();

            ps.close();

           

            sql = "UPDATE `tracker`.`client` SET `last_conection`='" + new Date().getTime() + "' WHERE `id`='" + idcliente + "'";

            ps = con.prepareStatement(sql);

             ps.execute();

            ps.close();

        

        } catch (SQLException ex) {

            System.out.println("erro 8 "+ex);
        }

    }

    public void fecharConexao() throws SQLException {

        con.close();
    }

    public void removerClient(int idcliente) throws SQLException {

        PreparedStatement ps = null;

        

        try {

            String sql = "DELETE FROM `tracker`.`client` WHERE `id`='" + idcliente + "'";

            ps = con.prepareStatement(sql);

            ps.execute();

            ps.close();

            } catch (SQLException ex) {

            System.out.println("erro 9 "+ex);
        }

    }

    public void stopShareFile(int idtorrent, int idcliente) throws SQLException {

        PreparedStatement ps = null;

        
        try {

            String sql = "DELETE FROM `tracker`.`client_torrent` WHERE `id_torrent`='" + idtorrent + "'and id_client='" + idcliente + "'";

            ps = con.prepareStatement(sql);

            ps.execute();

            ps.close();

           

        } catch (SQLException ex) {

            System.out.println("erro 10 "+ex);
        }

    }

}
