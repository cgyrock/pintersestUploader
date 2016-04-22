package com.drol.pintersest.api;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.drol.pintersest.PintersestApi;
import com.drol.utils.http.ApiJsonClient;

public class PinsApi extends PintersestApi {
	
	private String board;
	
	private String note;
	
	private String imageUrl;
	
	private File image;
	
	public Map<String, Object> getPin(String pinId) {
		
		String apiName = String.format("/pins/%s/", pinId);
		return doGet(apiName);
		
	}
	
	public Map<String, Object> getPins() {
		
		String apiName = "/me/pins/";
		return doGet(apiName);
		
	}
	
	public Map<String, Object> createPin() {
		
		String apiName = "/pins/";
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("board", board);
		parameters.put("image", image);
		if(note != null && note.trim().length() > 0) {
			parameters.put("note", note);
		} else {
			parameters.put("note", "");
		}
		
		return doUpload(apiName, parameters);
	}
	
	public String getBoard() {
		return board;
	}

	public void setBoard(String board) {
		this.board = board;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public File getImage() {
		return image;
	}

	public void setImage(File image) {
		this.image = image;
	}

}
