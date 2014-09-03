package com.sogou.flow.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;

import net.sf.json.JSONArray;

import com.sogou.common.utils.Dom4jTools;
import com.sogou.flow.constants.SystemConstants;
import com.sogou.flow.constants.XmlVocabulary;
import com.sogou.flow.dao.impl.DBHBaseQuerierImpl;
import com.sogou.flow.model.HBaseHolder;
import com.sogou.flow.service.DBQueryService;
import com.sogou.flow.service.impl.DBQueryServiceImpl;
import com.sogou.flow.utils.CacheHandler;
import com.sogou.flow.utils.dto.DatabaseDescriber;

/**
 * 
 *  Copyright 2014 SOGOU
 *  All right reserved.
 *	<p>
 *	The query entry of the whole system.
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
    	//Get the map of the request parameters
		Map<String, String[]> origin = request.getParameterMap();
    	
		Map<String,String> params = new HashMap<String, String>();
		//For ruling the params passed should just be one to one, no same name param allowed
		//, so add the first result as value.
    	for(Entry<String,?> entry:origin.entrySet()){
    		params.put(entry.getKey(),((String[])entry.getValue())[0]);
    	}
        //Get the important variables productName and currentDimension
    	String productName = request.getParameter(SystemConstants.PRODUCT_NAME);
    	String currentDimension = request.getParameter(SystemConstants.CURRENT_DIMENSION);
    	//Set the utf8 encoding
    	response.setContentType(SystemConstants.CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        //Define the result an trans params
        String result = null;
        JSONArray trans = null;
        try {
        	//Get the dataSchema document
        	Document doc = CacheHandler.dataSchemaMapper.get(productName);
        	
        	//No such product
        	if(doc == null) {
        		return;
        	}
        	
        	DatabaseDescriber databaseDescriber = new DatabaseDescriber();
    		//From the the doc, load the attributes in
    		Dom4jTools.setValueToInstanceByMethod(databaseDescriber, 
    				doc.getRootElement().element(XmlVocabulary.DATASOURCE));
    		//Define the service to use. HBase type will be used.
    		//TODO The spring framework is a good preference, try to simulate it later
        	/**
        	 * The DBQuerierDao dbQuerierDao, DBHolder dbHolder should in the same group
        	 * eg: DBHBaseQuerierImpl and HBaseHolder, both HBase group
        	 */
    		DBQueryService dbQueryService = new DBQueryServiceImpl(
        			//HBase querier
        			new DBHBaseQuerierImpl(),
        			//HBase holder
        			new HBaseHolder(databaseDescriber.getName(),databaseDescriber.getAddress(),
        					databaseDescriber.getBigtable(), databaseDescriber.getUsername(),
        					databaseDescriber.getPassword(),
        					databaseDescriber.getColumns().split(XmlVocabulary.COMMA))
        	);
        	//The trans is the result indeed, but not a string.
        	//Querying for results.
        	trans = dbQueryService.queryForResults(productName, currentDimension, params);
        	result = trans.toString();
        	dbQueryService = null;
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
