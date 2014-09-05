package com.sogou.flow.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.sogou.common.utils.FileLoadTools;
import com.sogou.flow.constants.SystemConstants;
import com.sogou.flow.utils.dto.FileStatus;

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
 * @VersionFileTracker
 */
public class VersionFileTracker {

	private static int period = 1000*10;//ten seconds
	private static String path = SystemConstants.properties.getProperty(SystemConstants.PRODUCTS_LOCATION);

	private static Logger logger = Logger.getLogger(VersionFileTracker.class);
	
	/**
	 * Start the version file tracker.
	 */
	public static void startTracker(){
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				
		    	try {
					File[] files = FileLoadTools.getFoldersByDirectory(path);
					Map<String,FileStatus> containerM = CacheHandler.fileTracker;
					for (File file : files) {
						if(file.isDirectory()){
							String productName = file.getName();
							
							FileStatus fileStatus = containerM.get(productName);
							//if it is a new product just reload without unload
							if(fileStatus == null){
								logger.info("Product "+productName+" not in map try to load it !" +
										"It is a now product!");
								CacheHandler.productLoader.load(productName);
								continue;
							}
							//Get the pre time from the tracker map
							long preTime = fileStatus.getLastModifyTime();
							//Get the menu file to see the last modified time
							File tmp = FileLoadTools.getFileByName(
								fileStatus.getFileName(), path+productName+SystemConstants.SPLITTER
							);
							//Compare the times to decide whether to reload
							long lastTime = tmp.lastModified();
							if(preTime != lastTime){
								BufferedReader fileReader = new BufferedReader(new FileReader(tmp));
								String number = fileReader.readLine();
								int numberInt = SystemConstants.REGUARDLESS_LOADING-1;
								try {
									numberInt = Integer.valueOf(number);
								} catch (Exception e) {
									logger.info("Number parse errors! The change will not be loaded.");
									continue;
								}
								// if regardless signal just unload
								if(numberInt == SystemConstants.REGUARDLESS_LOADING){
									CacheHandler.productLoader.unload(productName);
									logger.info("Product " + productName+ " unload!");
								}//an improved version, load again
								else if (numberInt > fileStatus.getVersion()) {
									//If the time do not match each other, unload and reload.
									CacheHandler.productLoader.unload(productName);
									CacheHandler.productLoader.reload(productName,numberInt);
									logger.info("Product " + productName+ " load again!");
								}else {
									logger.error("Product " + productName+ " version not right changed!");
								}
								//Update the last modified time
								containerM.get(productName).setLastModifyTime(lastTime);
							}
						}
					}
					//Get the useless product in memory
					List<String> useless = new ArrayList<String>();
					for(Entry<String, FileStatus> entry : containerM.entrySet()){
						boolean signal = true;
						for(File file : files){
							if(file.getName().equals(entry.getKey())) {
								signal = false;
								break;
							}
						}
						if(signal == true) useless.add(entry.getKey());
					}
					//Unload the product and even more remove the product in fileTracker
					for (String pName : useless) {
						CacheHandler.productLoader.unload(pName);
						CacheHandler.fileTracker.remove(pName);
						logger.info("Product "+ pName+" unload, because it is removed by some one!");
					}
					
				} catch (Exception e) {
					logger.error("Error happens in MenuFileTracker.java");
					e.printStackTrace();
				}
			}
		}, new Date(), period);
	}
}
