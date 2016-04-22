package com.drol.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class KVStore {
	
	private static KVStore store;
	private Map<String, Object> kvs;
	private KVStore() { this.initKvs(); }
	
	public static KVStore getInstance() {
		if(store == null) store = new KVStore();
		return store;
	}
	
	private void initKvs() {
		if(kvs == null) {
			this.reloadKvsFile();
		}
	}
	
	private void reloadKvsFile() {
		ObjectInputStream in = null;
		try {
			File kvsFile = getKvsFile();
			if(kvsFile.exists()) {
				in = new ObjectInputStream(new FileInputStream(getKvsFile()));
				this.kvs = (Map<String, Object>)in.readObject();
			} else {
				this.kvs = new HashMap<String, Object>();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			
		} finally {
			try {
				if(in != null) in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void put(String key, Object obj) {
		this.kvs.put(key, obj);
	}
	
	public Object get(String key) {
		return this.kvs.get(key);
	}
	
	public void remove(String key) {
		this.kvs.remove(key);
	}
	
	public void clear() {
		this.kvs.clear();
	}
	
	public void persistence() {
		ObjectOutputStream out = null;
		try {
			this.initKvs();
			File kvsFile = getKvsFile();
			out = new ObjectOutputStream(new FileOutputStream(kvsFile));
			out.writeObject(this.kvs);
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} finally {
			try {
				if(out != null) out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void save(String key, Object obj) {
		this.kvs.put(key, obj);
		this.persistence();
	}
	
	public Object load(String key) {
		this.reloadKvsFile();
		return this.kvs.get(key);
	}
	
	public void delete(String key) {
		this.kvs.remove(key);
		this.persistence();
	}
	
	public void clean() {
		this.kvs.clear();
		this.persistence();
	}
	
	private File getKvsFile() throws IOException {
		File file = new File("");
		String path = file.getCanonicalPath();
		String kvsPath = path + File.separator + "kvstore.kvs";
		File kvsFile = new File(kvsPath);
		return kvsFile;
	}

}
