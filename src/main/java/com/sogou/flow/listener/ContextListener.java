package com.sogou.flow.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.sogou.flow.utils.CacheHandler;
import com.sogou.flow.utils.VersionFileTracker;


/**
 * 
 *  Copyright 2014 SOGOU
 *  All right reserved.
 *	<p>
 *	Listen to the context and load the product directly.
 *	</p>
 * @author Qun He
 * @Creat Time : 2014-8-29 下午4:05:30
 * @ContextListener
 */
public class ContextListener implements ServletContextListener {

	public ContextListener() {

	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		
		loadResourceMap();
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		
	}
	
	/**
	 * Load the product and start the file status tracker.
	 */
	public void loadResourceMap(){
		CacheHandler.productLoader.loadAll();
		VersionFileTracker.startTracker();
	}
}
