package com.drol.pintersest;

import com.drol.pintersest.app.BatchUploader;
import com.drol.utils.KVStore;

public class Application {
	
	public static void main(String[] args) {
		
		System.out.println("*****************************************");
		System.out.println("*\t\t\t\t\t*");
		System.out.println("*\tPinterestBatchUploader\t\t*");
		System.out.println("*\tver:1.2\t\t\t\t*");
		System.out.println("*\tby: Drol 20151107\t\t*");
		System.out.println("*\t\t\t\t\t*");
		System.out.println("*****************************************");
		System.out.println("");
		
		try {
			
			BatchUploader app = new BatchUploader();
			app.init();
			app.showCurrentUser();
			
			do {
				app.selectBoard();
				app.uploadPin();
			} while (app.goingOnUploadPin());
			
			app.quite();
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			KVStore.getInstance().persistence();
		}
		
	}
	
}
