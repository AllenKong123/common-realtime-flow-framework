package com.sogou.flow.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.sogou.flow.constants.SystemConstants;
import com.sogou.flow.utils.CacheHandler;

/**
 * 
 *  Copyright 2014 SOGOU
 *  All right reserved.
 *	<p>
 *	Get the menu by the specified product name.
 *	</p>
 * @author Qun He
 * @Creat Time : 2014-9-2 下午1:54:41
 * @MenuServlet
 */
public class MenuServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        //Get the important variables productName and currentDimension
    	String productName = request.getParameter(SystemConstants.PRODUCT_NAME);

    	response.setContentType(SystemConstants.CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        //Define the result an trans params
        String result = null;
        try {
        	result = JSONObject.fromObject(CacheHandler.menuMapper.get(productName)).toString();
        } catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.println(result);
            out.close();
        }
    } 

    /** 
     * Handles the HTTP <code>GET</code> method.
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
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Query Servlet";
    }

}
