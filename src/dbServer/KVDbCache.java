/////////////////////////////////////////////////////////////////////
// KVDbCache.java - LFU cache                                       //
//                                                                 //
// Author: Zhen Xia                                                //
/////////////////////////////////////////////////////////////////////
package dbServer;

import java.util.*;

/////////////////////////////////////////////////////////////////////
//KVDbCache:
//implemented based on LFU cache

public class KVDbCache extends KVDb{
	private int capacity = 0; // maximum size of cache
	private int minFreq = 0; // minimum visiting frequency in the cache
	private Map<String, Integer> freq; // map key with visiting frequency
	private Map<Integer, LinkedHashSet<String>> list; // map each frequency with a set 
	                                                  // of key maintained in insertion order
	
	public KVDbCache(int cp) {
		this.capacity = cp;
        freq = new HashMap();
        list = new HashMap();
	}
	
	@Override
	public List<String> get(String key) {
        updateAccess(key);
        return super.get(key);
	}
	
	@Override
	public List<String> get(String key, int time) {
        updateAccess(key);
        return super.get(key, time);
	}
	
	@Override
    public List<String> dif(String key, int time1, int time2) {
        updateAccess(key);
        return super.dif(key, time1, time2);
    }

	@Override
    public boolean del(String key) {
        updateAccess(key);
        return super.del(key);
    }
    
	@Override
    public boolean del(String key, String value) {
		updateAccess(key);
        return super.del(key, value);
    }
	
	/* add or update key with LinkedMap.
	 * return previous key/value if key is not in the cache
	 * otherwise, return null */
    public Object[] add(String key, LinkedMap map) {
		if (updateAccess(key)) {
			put(key, map);
			return null;
		}
	
		Object[] del = null;
	    if (db.size() == capacity) {
	    		del = new Object[2];
	        String k = list.get(minFreq).iterator().next();
	        del[0] = k;
	        list.get(minFreq).remove(k);
	        del[1] = db.remove(k);
	        freq.remove(k);
	    }
	    
	    put(key, map);
	    
	    freq.put(key, 1);
	    if (!list.containsKey(1)) list.put(1, new LinkedHashSet());
	    list.get(1).add(key);
	    minFreq = 1;
	    return del;
	}
	
    /* add or update key with LinkedMap.
	 * return previous key/value if key is not in the cache
	 * otherwise, return null */
    public Object[] add(String key, String value, int time) {
    		if (updateAccess(key)) {
    			put(key, value, time);
    			return null;
    		}
    	
    		Object[] del = null;
        if (db.size() == capacity) {
        		del = new Object[2];
            String k = list.get(minFreq).iterator().next();
            del[0] = k;
            list.get(minFreq).remove(k);
            del[1] = db.remove(k);
            freq.remove(k);
        }
        
        put(key, value, time);
        
        freq.put(key, 1);
        if (!list.containsKey(1)) list.put(1, new LinkedHashSet());
        list.get(1).add(key);
        minFreq = 1;
        return del;
    }
  
    /* update frequency and accessing order of the given key */
	private boolean updateAccess(String key) {
		if (!conatainsKey(key)) return false;
		
        int f = freq.get(key);
        freq.put(key, f + 1);
        list.get(f).remove(key);
        if (!list.containsKey(f + 1)) list.put(f + 1, new LinkedHashSet());
        list.get(f + 1).add(key);
        if (list.get(minFreq).size() == 0) minFreq++; // if the the set of keys with least frequency 
                                                      // is empty, the new minimum frequency is one 
        												 // bigger than the original one
        return true;
	}
}
