package teste;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import br.ufla.naivetorrent.tracker.protocol.RequestEvent;
import br.ufla.naivetorrent.tracker.protocol.RequestParameters;
import br.ufla.naivetorrent.tracker.protocol.ResponseMessage;
import br.ufla.naivetorrent.tracker.protocol.RequestMessage;
import br.ufla.naivetorrent.tracker.protocol.ResponseMessageEncoder;
import br.ufla.naivetorrent.util.UtilGenerateId;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author lfps
 */
@WebServlet(name = "tracker", urlPatterns = {"/tracker"})
public class Trackerservlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, PropertyVetoException {

        RequestMessage trackerrequisicao = new RequestMessage();

        String trackerid;

        trackerid = UtilGenerateId.generateTrackerIdHex(request.getLocalName(), request.getLocalPort());

        // id do tracker
        trackerrequisicao.setTrackerId(trackerid);

        // chave hash do arquivo de metadados 
        trackerrequisicao.setInfoHash(request.getParameter(RequestParameters.INFO_HASH));

        // uma string de 20 bytes usada como id do cliente. Deve ser gerada pelo cliente ao iniciar comunicação.
        trackerrequisicao.setPeerId(request.getParameter(RequestParameters.PEER_ID));

        // número da porta em que o cliente está escutando
        trackerrequisicao.setPort(Integer.parseInt(request.getParameter(RequestParameters.PEER_PORT)));
        // a quantidade total de upload realizado pelo cliente desde que iniciou comunicação

        trackerrequisicao.setUploaded(Long.parseLong(request.getParameter(RequestParameters.UPLOADED)));

        // a quantidade total de download realizado pelo cliente desde que iniciou comunicação
        trackerrequisicao.setDownloaded(Long.parseLong(request.getParameter(RequestParameters.DOWNLOADED)));

        //o número de bytes que o cliente ainda precisa baixar para terminar o download
        trackerrequisicao.setLeft(Long.parseLong(request.getParameter(RequestParameters.LEFT)));

        //pega o endereço ip de quem requisitou obs não é do endereço real do cliente
        trackerrequisicao.setIp(request.getParameter(RequestParameters.PEER_IP));
        
      
        //tipo de conteudo que será retornado para quem fez a requisição
        response.setContentType("text/plain");

        
        PrintWriter out = response.getWriter();
        
        //se o paramentro event for null
        if (request.getParameter("event") != null) {

            trackerrequisicao.setEvent(RequestEvent.getInstance(Integer.parseInt(request.getParameter("event"))));
            //a requisicao recebida verificara se o cliente ja terminou de baixar e devemos atualizar no banco que ele é um seeder

            if (trackerrequisicao.getEvent() == RequestEvent.COMPLETED) {

                Daotracker dao = new Daotracker();

                //encoding resposta
                ResponseMessageEncoder encoder = new ResponseMessageEncoder(dao.Completed(trackerrequisicao));

                out.print(encoder.getMessageEncode()+"\n"+"completed");
                out.close();
                dao.fecharConexao();

            } //parou de enviar o arquivo da hash recebido
            else if (trackerrequisicao.getEvent() == RequestEvent.STOPPED_FILE) {
                Daotracker dao = new Daotracker();

                //encoding resposta
                ResponseMessageEncoder encoder = new ResponseMessageEncoder(dao.StoppedFile(trackerrequisicao));

                out.print(encoder.getMessageEncode()+"\n"+"stoped_file");
                out.close();
                dao.fecharConexao();
            } /*
                   se o event for 1 devemos excluir o cliente da lista de peer pois este não compartilhara mais os arquivos

             */ else if (trackerrequisicao.getEvent() == RequestEvent.STOPPED) {

                Daotracker dao = new Daotracker();

                //encoding resposta
                ResponseMessageEncoder encoder = new ResponseMessageEncoder(dao.Stopped(trackerrequisicao));

                out.print(encoder.getMessageEncode()+"\n"+"stopped");
                out.close();
                dao.fecharConexao();

            }
            // deve verificar se o id_peer e o info_hash está no banco se não tiver incluimos no banco
            // caso ja esteja devemos gerar uma nova id_peer para o cliente
            if (trackerrequisicao.getEvent() == RequestEvent.STARTED) {

                Daotracker dao = new Daotracker();

                //encoding resposta
                ResponseMessageEncoder encoder = new ResponseMessageEncoder(dao.Starting(trackerrequisicao));

                out.print(encoder.getMessageEncode()+"\n"+"started");
                out.close();
                dao.fecharConexao();

            }
        }
        else{
            //atualizar os dados 
             Daotracker dao = new Daotracker();

                //encoding resposta
                ResponseMessageEncoder encoder = new ResponseMessageEncoder(dao.Updated(trackerrequisicao));

                out.print(encoder.getMessageEncode()+"\n"+"sem parametro");
                out.close();
                dao.fecharConexao();
            
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(Trackerservlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Trackerservlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(Trackerservlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Trackerservlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
