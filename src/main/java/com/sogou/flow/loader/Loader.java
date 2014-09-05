package com.sogou.flow.loader;

/**
 * 
 *  Copyright 2014 SOGOU
 *  All right reserved.
 *	<p>Rule the loaders.</p>
 * @author Qun He
 * @Creat Time : 2014-8-26 上午10:20:04
 * @Loader
 */
public interface Loader {

	/**
	 * <p>Load the instances to the container. </p>
	 * @return true if load successfully
	 */
	public boolean loadAll();
	
	/**
	 * <p>Load the single instance to the container. </p>
	 * @param key the key represents the related resources
	 * @return true if load successfully
	 */
	public boolean load(String key);
	
	/**
	 * <p>Unload the resources create by the key, just remove them.</p>
	 * @param the key represents the related resources
	 * @return true if load successfully
	 */
	public boolean unload(String key);
	
	/**
	 * <p>Reload the resources create by the key.</p>
	 * @param the key represents the related resources
	 * @param the next version
	 * @return true if load successfully
	 */
	public boolean reload(String key ,int version);
}
