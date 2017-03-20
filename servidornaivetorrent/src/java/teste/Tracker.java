package teste;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import static javax.servlet.http.HttpServletRequest.CLIENT_CERT_AUTH;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author lfps
 */
@WebServlet(name = "tracker", urlPatterns = {"/tracker"})
public class Tracker extends HttpServlet {

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
            throws ServletException, IOException {
        
        
       // chave hash do arquivo de metadados            
       String info_hash = request.getParameter("info_hash");
        
      // uma string de 20 bytes usada como id do cliente. Deve ser gerada pelo cliente ao iniciar comunicação.

      String peer_id = request.getParameter("peer_id");
         
      // número da porta em que o cliente está escutando
      int port = Integer.parseInt(request.getParameter("port"));
  
      // a quantidade total de upload realizado pelo cliente desde que iniciou comunicação

      long uploaded =   Long.parseLong(request.getParameter("uploaded"));
         
      // a quantidade total de download realizado pelo cliente desde que iniciou comunicação

      long dowload = Long.parseLong(request.getParameter("download"));
         
      //o número de bytes que o cliente ainda precisa baixar para terminar o download

      long  left =  Long.parseLong(request.getParameter("left"));
         
       //pega o endereço ip de quem requisitou obs não é do endereço real do cliente
                String ipAddress = request.getRemoteAddr(); 
        
        //tipo de conteudo que será retornado para quem fez a requisição
        response.setContentType( "text/plain" );
        
        //se o paramentro event for null
        if(request.getParameter("event")!= null){
            
            
           //0 indica que é a primeira requisição feita para o tracker
          // 1 indica que o cliente está parando de compartilhar o arquivo
         // 2 ndica que o cliente já baixou o arquivo inteiro, portanto tornou um seeder

            int event = Integer.parseInt(request.getParameter("envent"));

                


                //a requisicao recebida verificara se o cliente ja terminou de baixar e devemos atualizar no banco que ele é um seeder
                //caso o arquivo não for encontrado no banco quer dizer que é um cliente que quer compartilhar um novo arquivo

              if (event == 2){




              }
              //parou de enviar o arquivo da hash recebido
              else if(event ==3){
                  
              }
              /*
                   se o event for 1 devemos excluir o cliente da lista de peer pois este não compartilhara mais os arquivos

              */
              else if(event ==1){
                  

              }
              // deve verificar se o id_peer e o info_hash está no banco se não tiver incluimos no banco
              // caso ja esteja devemos gerar uma nova id_peer para o cliente
              else if (event == 0){
                  
                  
                  
              }
            
            
            
        }
       //apenas atualiza as informações no banco pois o event veio sem parametro
       else{
           
           
           
       }
         
         
        
        
         
         PrintWriter out = response.getWriter();
         
         out.print("<html>\n" +
            "    <head>\n" +
            "        <title>TODO supply a title</title>\n" +
            "        <meta charset=\"UTF-8\">\n" +
            "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    </head>\n" +
            "    <body>\n" +
            "        <div><h1>"+ipAddress+"</h1></div>\n" +
                     
            "    </body>\n" +
            "</html>");
         out.close();
         
     
         
       

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
        processRequest(request, response);
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
        processRequest(request, response);
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
