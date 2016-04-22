package com.drol.pintersest.app;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang.ArrayUtils;

import com.drol.pintersest.PintersestApi;
import com.drol.pintersest.api.PinsApi;
import com.drol.pintersest.api.UsersApi;
import com.drol.pintersest.entity.BoardEntity;
import com.drol.pintersest.entity.UserEntity;
import com.drol.utils.KVStore;

public class BatchUploader {
	
	private BoardEntity board;
	private UserEntity user;
	private Scanner scanner;
	
	public void init() {
		PintersestApi.clientId = "4800166145097214550";
		PintersestApi.clientSecret = "8caa907298e863288249354406993e8faadb10de37399632697d6c230008d81e";
		
		Map<String, Map<String, Object>> usersMap = (Map<String, Map<String, Object>>)KVStore.getInstance().get("users");
		if(usersMap == null || usersMap.size() == 0) {
			usersMap = new HashMap<String, Map<String,Object>>();
			this.requestUserOuth();
			
		} else {
			this.selectUser();
		}

		UsersApi userApi = new UsersApi();
		this.user = userApi.getUser();
		
		Map<String, Object> userMap = new HashMap<String, Object>();
		userMap.put("name", this.user.getFullName());
		userMap.put("token", PintersestApi.accessToken);
		usersMap.put(this.user.getId(), userMap);
		KVStore.getInstance().save("users", usersMap);
	}
	
	private void requestUserOuth() {
		System.out.println("程序即将打开浏览器跳转至 pinterest.com 获取用户授权");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.print("授权地址：");
		PintersestApi.grantAuthorization();
		
		String code = simpleInput("请输入授权CODE：");
		PintersestApi.requestOuthToken(code.trim());
	}
	
	public void showCurrentUser() {
		
		System.out.println(String.format("当前授权用户：%s (%s)", user.getFullName(), user.getUrl()));
	}
	
	public void selectBoard() {
		UsersApi userApi = new UsersApi();
		List<BoardEntity> boardEntityList = userApi.getBoards();
		int boardIndx = 0;
		boolean goOn;
		do {
			goOn = false;
			
			System.out.println(String.format("\n用户 %s 的Board列表：", this.user.getFullName()));
			
			for(int i=0; i < boardEntityList.size(); i++) {
				BoardEntity item = boardEntityList.get(i);
				System.out.println(String.format("    %d %s", i, item.getName()));
			}
			
			try {
				
				boardIndx = Integer.parseInt(simpleInput("\n输入要上传的Board序号："));
				if(boardIndx < 0 || boardIndx >= boardEntityList.size()) {
					System.out.println("\n输入的Board序号错误，请重新选择");
					goOn = true;
				} 
				
			} catch (Exception e) {
				System.out.println("\n输入的Board序号错误，请重新选择");
				goOn = true;
			}
			
		} while(goOn);
		this.board = boardEntityList.get(boardIndx);
	}
	
	public void selectUser() {
		Map<String, Map<String, Object>> usersMap = (Map<String, Map<String, Object>>)KVStore.getInstance().get("users");
		String[] array = usersMap.keySet().toArray(new String[]{});
		Arrays.sort(array);
		
		int index = 0;
		boolean goOn;
		do {
			goOn = false;
			
			System.out.println("\n已授权用户列表：");
			
			for(int i=0; i < array.length; i++) {
				Map<String, Object> item = usersMap.get(array[i]);
				System.out.println(String.format("    %d %s", i, item.get("name")));
			}
			
			try {
				
				index = Integer.parseInt(simpleInput("\n输入要登录的用户序号(输入 -1 授权新用户)："));
				if(index < -1 || index >= array.length) {
					System.out.println("\n输入的用户序号错误，请重新选择");
					goOn = true;
				} 
				
			} catch (Exception e) {
				System.out.println("\n输入的用户序号错误，请重新选择");
				goOn = true;
			}
			
		} while(goOn);
		
		if(index == -1) {
			this.requestUserOuth();
		} else {
			Map<String, Object> map = usersMap.get(array[index]);
			PintersestApi.accessToken = String.valueOf(map.get("token"));
		}
		
	}
	
	public void uploadPin() {
		String picSrc = simpleInput("\n将要上传图片文件夹拖放至此：");
		File pictures = new File(picSrc);
		
		File[] picArray;
		if(pictures.isDirectory()) {
			picArray = pictures.listFiles();
		} else {
			picArray = new File[]{pictures};
		}
		
		System.out.println(String.format("共 %d 张图片需要上传", picArray.length));
		
		for(int i=0; i < picArray.length; i++) {
			File file = picArray[i];
			PinsApi pinApi = new PinsApi();
			pinApi.setBoard(this.board.getId());
			pinApi.setImage(file);

			System.out.print(String.format("上传第%d张图片：%s\t\t\t\t", i+1, file.getName()));
			pinApi.createPin();
			System.out.print(String.format("完成（%d/%d）\n", i+1, picArray.length));
		}
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		System.out.println(String.format("\n全部图片上传完成 %s\n",simpleDateFormat.format(new Date())));
	}
	
	public boolean goingOnUploadPin() {
		String res = simpleInput("是否继续上传其他Pin （Y/N）：");
		if(res != null) res = res.trim().toUpperCase();
		return "Y".equals(res);
	}
	
	public String simpleInput(String tips) {
		
		String input = "";
		if(this.scanner == null)
			this.scanner = new Scanner(System.in);
		
		System.out.print(tips);
		input = this.scanner.nextLine();
		
		return input;
		
	}
	
	public void quite() {
		if(this.scanner != null) this.scanner.close();
	}
}
