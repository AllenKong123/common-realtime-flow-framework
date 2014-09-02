package com.sogou.flow.utils.dto;

/**
 * 
 * Copyright 2014 SOGOU All right reserved.
 * <p>
 * Keep the file name and last modified time in. The file status 
 * will just record the menu related file.
 * </p>
 * 
 * @author Qun He
 * @Creat Time : 2014-9-2 下午2:36:08
 * @FileStatus
 */
public class FileStatus {

	private String fileName;
	
	private long lastModifyTime;

	public FileStatus(String fileName, long lastModifyTime) {
		super();
		this.fileName = fileName;
		this.lastModifyTime = lastModifyTime;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(long lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

}
