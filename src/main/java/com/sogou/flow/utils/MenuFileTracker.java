package com.sogou.flow.utils;

import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.sogou.common.utils.FileLoadTools;
import com.sogou.flow.constants.SystemConstants;

/**
 * 
 *  Copyright 2014 SOGOU
 *  All right reserved.
 *	<p>
 *	The file tracker to track the file, if the menu of one product modified,
 *  the product will load again to update the information.
 *	</p>
 * @author Qun He
 * @Creat Time : 2014-9-2 下午2:51:12
 * @MenuFileTracker
 */
public class MenuFileTracker {

	private static int period = 1000*10;//ten seconds
	private static String path = SystemConstants.properties.getProperty(SystemConstants.PRODUCTS_LOCATION);

	private static Logger logger = Logger.getLogger(MenuFileTracker.class);
	
	/**
	 * Start the file tracker
	 */
	public static void startTracker(){
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				
		    	try {
					File[] files = FileLoadTools.getFoldersByDirectory(path);
					for (File file : files) {
						if(file.isDirectory()){
							StringBuffer sb = new StringBuffer();
							sb.append(file.getName());
							//Get the pre time from the tracker map
							long preTime = 
									CacheHandler.fileTracker.get(sb.toString()).getLastModifyTime();
							//Get the menu file to see the last modified time
							File tmp = FileLoadTools.getXmlByName(
									CacheHandler.fileTracker.get(sb.toString()).getFileName(), 
											path+sb.toString()+SystemConstants.SPLITTER);
							long lastTime = tmp.lastModified();
							if(preTime != lastTime){
								//If the time do not match each other, unload and reload.
								CacheHandler.productLoader.unload(sb.toString());
								CacheHandler.productLoader.reload(sb.toString());
								logger.info("Prodct "+sb.toString()+" load again!");
								//Update the last modified time
								CacheHandler.fileTracker.get(sb.toString()).setLastModifyTime(lastTime);
							}
						}
					}
				} catch (Exception e) {
					logger.error("Error happens in MenuFileTracker.java");
					e.printStackTrace();
				}
			}
		}, new Date(), period);
	}
}
