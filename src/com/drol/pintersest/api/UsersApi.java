package com.drol.pintersest.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.drol.pintersest.PintersestApi;
import com.drol.pintersest.entity.BoardEntity;
import com.drol.pintersest.entity.UserEntity;
import com.drol.utils.QTool;

public class UsersApi extends PintersestApi {

	public UserEntity getUser() {
		
		String apiName = "/me/";
		Map<String, Object> result =  doGet(apiName);
		Map<String, Object> user = (Map<String, Object>)result.get("data");
		
		UserEntity userEntity = new UserEntity();
		if(user != null) {
			userEntity.setId(QTool.toStr(user.get("id")));
			userEntity.setUrl(QTool.toStr(user.get("url")));
			userEntity.setFirstName(QTool.toStr(user.get("first_name")));
			userEntity.setLastName(QTool.toStr(user.get("last_name")));
		}
		
		return userEntity;
	}
	
	public List<BoardEntity> getBoards() {

		String apiName = "/me/boards/";
		Map<String, Object> result =  doGet(apiName);
		
		List<BoardEntity> boardEntityList = new ArrayList<BoardEntity>();
		Object[] array = (Object[])result.get("data");
		if(array != null) {
			for(Object obj : array) {
				Map<String, Object> item = (Map<String, Object>)obj;
				BoardEntity boardEntity = new BoardEntity();
				boardEntity.setId(QTool.toStr(item.get("id")));
				boardEntity.setUrl(QTool.toStr(item.get("url")));
				boardEntity.setName(QTool.toStr(item.get("name")));
				boardEntityList.add(boardEntity);
			}
		}
		
		return boardEntityList;
		
	}
	
}
