package com.sogou.flow.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.sogou.flow.dao.impl.DBHBaseQuerierImpl;
import com.sogou.flow.model.HBaseHolder;
import com.sogou.flow.service.DBQueryService;
import com.sogou.flow.service.impl.DBQueryServiceImpl;

/**
 * 
 *  Copyright 2014 SOGOU
 *  All right reserved.
 *	<p>
 *
 *	</p>
 * @author Qun He
 * @Creat Time : 2014-8-29 下午4:07:31
 * @QueryServlet
 */
public class QueryServlet extends HttpServlet {

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
    	
		Map<String, String[]> origin = request.getParameterMap();
    	
		Map<String,String> params = new HashMap<String, String>();
		
    	for(Entry<String,?> entry:origin.entrySet()){
    		params.put(entry.getKey(),((String[])entry.getValue())[0]);
    	}
        
    	String productName = request.getParameter("productName");
    	String currentDimension = request.getParameter("currentDimension");
    	
    	response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();
        String result = null;
        JSONArray trans = null;
        try {
    		List<String> test = new ArrayList<String>();
    		test.add("pv");
    		test.add("uv");
    		test.add("cl");
        	DBQueryService dbQueryService = new DBQueryServiceImpl(new DBHBaseQuerierImpl(),
        			new HBaseHolder("wap.demo.tmp", "", true, "", "", test));
        	System.out.println(productName.getClass().getName());
        	System.out.println(currentDimension.getClass().getName());
        	System.out.println(params.getClass().getName());
        	trans = dbQueryService.queryForResults(productName, currentDimension, params);
        	result = trans.toString();
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
