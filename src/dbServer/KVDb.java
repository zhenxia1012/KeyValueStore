/////////////////////////////////////////////////////////////////////
// KVDb.java - key/value store                                     //
//                                                                 //
// Author: Zhen Xia                                                //
/////////////////////////////////////////////////////////////////////

package dbServer;

import java.awt.Image;
import java.io.ObjectInputStream.GetField;
import java.security.KeyStore.Entry;
import java.util.*;


/////////////////////////////////////////////////////////////////////
// KVDb:
// key/value store. key is a string and value is a hash map maintain
// insertion order

public class KVDb {
    protected Map<String, LinkedMap> db;
    
    public KVDb() {
        db = new HashMap<>();
    }
    
    /* returns current size. */
    public int size() {
        return db.size();
    }
    
    /* returns LinkedMap which wraps all the values of a given key. */
    public LinkedMap getSubMap(String key) {
        if (!conatainsKey(key)) return null;
        return db.get(key);
    }
    
    /* returns all values associated with the key, if present. 
     * Time: O(v), v is number of values associated with the key. The worst
     *       case is that there is only one key with all values.
     * Space: O(1) */
    public List<String> get(String key) {
        if (!conatainsKey(key)) return null;
        return db.get(key).keyList(null, null, true);
    }
    
    /* returns all values associated with the key up to the specified. 
     * Time: O(time)
     * Space: O(1) */
    public List<String> get(String key, int time) {
        if (!conatainsKey(key)) return null;
        return db.get(key).keyList(null, time, false);
    }
    
    /* returns the difference in value associated with the key between 
     * time1 and time2. time1 <= time2.
     * Time: O(time2)
     * Space: O(1) */
    public List<String> dif(String key, int time1, int time2) {
        if (!conatainsKey(key)) return null;
        return db.get(key).keyList(time1, time2, false);
    }
    
    /* adds or updates the key with LinkedMap. 
     * Time: O(1)
     * Space: O(1) */
    public void put(String key, LinkedMap map) {
        db.put(key, map);
    }
    
    /* adds or updates the key with the value.
     * Time: O(1)
     * Space: O(1) */
    public void put(String key, String value, int time) {
        if (!conatainsKey(key)) db.put(key, new LinkedMap());
        db.get(key).put(value, time);
    }
    
    /* invalidate the key from the store. */
    public boolean del(String key) {
        if (!conatainsKey(key)) return false;
        db.get(key).clear();
        return true;
    }
    
    /* invalidate the specified value from the key.
     * Time: O(v), v is number of values associated with the key. The worst
     *       case is that there is only one key with all values.
     * Space: O(1) */
    public boolean del(String key, String value) {
        if (!conatainsKey(key)) return false;
        return db.get(key).remove(value);
    }
    
    /* whether key exists */
	public boolean conatainsKey(String key) {
		return db.containsKey(key);
	}
    
    /* show content of database for debugging. */
    public void show(boolean validRequired) {
    		System.out.println("Show content in store");
		for (Map.Entry<String, LinkedMap> entry : db.entrySet()) {
			String name = entry.getKey();
			System.out.print("  name: " + name+ ",\tfriends: ");
			
			for (String fri : db.get(name).keyList(null, null, validRequired)) {
				System.out.print(fri+ " ");
			}
			System.out.println();
		}
    }
}