/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.Account;
import models.Client;
import models.Role;
import services.AccountService;
import services.ClientService;

/**
 *
 * @author janraeSAIT
 */
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        session.invalidate();
        
        getServletContext().getRequestDispatcher("/WEB-INF/Login.jsp").forward(request, response);
        return;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
            HttpSession session = request.getSession();
            
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            
            AccountService as = new AccountService();
            Account account = as.login(username, password);
            
            ClientService cs = new ClientService();
            
            if (account == null) {
                getServletContext().getRequestDispatcher("/WEB-INF/Login.jsp").forward(request, response);
                return;
            }
            
            session.setAttribute("username", username);
            
            boolean isActive = account.getActive();
            
            if (isActive == true) {
                Role role = account.getRole();
                
                if (role.getRoleId() == 1) {
                    response.sendRedirect("");
                } else {
                    
                    Client client = null;
                    
                    try {
                        client = cs.get(username);
                    } catch (Exception ex) {
                        Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    if (client == null) {
                        response.sendRedirect("client");
                    } else{
                        response.sendRedirect("account");
                    }
                }
            }
    }
}
