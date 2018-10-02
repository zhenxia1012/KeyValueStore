/////////////////////////////////////////////////////////////////////
// DbServiceImpl.java - services on server                         //
//                                                                 //
// Author: Zhen Xia                                                //
/////////////////////////////////////////////////////////////////////

package dbServer;
import java.util.*;import javax.activation.DataContentHandler;

import org.apache.thrift.TException; 

/////////////////////////////////////////////////////////////////////
// DbServiceImpl:
// offer services of operations on key/value store to client. All the
// operations would be started on cache at first. If the cache doesn't
// have the key, it then query entire key/value from database. If
// database had the key/value, it then stored it to LFU cache. If the
// cache is full, it would remove the least frequently visited key/value
// pair and update the database.

public class DbServiceImpl implements DbService.Iface{
	private KVDb database; // backup database
	private KVDbCache cache; // LFU cache
	private int curTime = 0; // mimic time line
	
	public DbServiceImpl() {
		database = new KVDb();
		cache = new KVDbCache(3); // initial size of cache. this is small, just for test
	}
	
	/* returns all values associated with the key, if present. */
	@Override
    public List<String> get(String key) throws TException {
		System.out.println("method: get(String key)");
    		List<String> values = cache.get(key); // query cache at first
    		
    		if (values == null) {
    			// if not in cache, then query database
    			LinkedMap tem = database.getSubMap(key);
    			if (tem == null) return null;
    			
    			LinkedMap newMap = new LinkedMap(tem);
    			Object[] del = cache.add(key, newMap);
    			if (del != null) updateDb((String)del[0], (LinkedMap)del[1]); // if cache is full update database
    			
    			values = newMap.keyList(null, null, true);
    		}
    		
    		++curTime;
    		return values;
    }

	/* returns all values associated with the key up to the specified. */
	@Override
    public List<String> getToTime(String key, int time) throws TException {
		System.out.println("method: getToTime(String key, int time)");
    		List<String> values = cache.get(key, time);
		
		if (values == null) {
			LinkedMap tem = database.getSubMap(key);
			if (tem == null) return null;
			
			LinkedMap newMap = new LinkedMap(tem);
			Object[] del = cache.add(key, newMap);
			if (del != null) updateDb((String)del[0], (LinkedMap)del[1]);
			
			values = newMap.keyList(null, time, true);
		}
		
		++curTime;
		return values;
    }

	/* returns the difference in value associated with the key between 
	 * time1 and time2. time1 <= time2. */
	@Override
    public List<String> dif(String key, int time1, int time2) throws TException {
		System.out.println("method: dif(String key, int time1, int time2)");
    		List<String> values = cache.dif(key, time1, time2);
		
		if (values == null) {
			LinkedMap tem = database.getSubMap(key);
			if (tem == null) return null;
			
			LinkedMap newMap = new LinkedMap(tem);
			Object[] del = cache.add(key, newMap);
			if (del != null) updateDb((String)del[0], (LinkedMap)del[1]);
			
			values = newMap.keyList(time1, time2, true);
		}
		
		++curTime;
		return values;
    }
	
	/* adds or updates the key with LinkedMap. */
	@Override
    public void put(String key, String value) throws TException {
    		if (!cache.conatainsKey(key)) {
    			System.out.println("  not in cache");
    			LinkedMap tem = database.getSubMap(key);
    			LinkedMap newMap = new LinkedMap();
    			if (tem != null) newMap = new LinkedMap(tem);
    			
    			newMap.put(value, curTime);
    			Object[] del = cache.add(key, newMap);
    			if (del != null) updateDb((String)del[0], (LinkedMap)del[1]);
    		} else cache.add(key, value, curTime);
    		
    		++curTime;
    }

	/* invalidate the key from the store. */
	@Override
    public boolean delKey(String key) throws TException {
		System.out.println("method: delKey(String key)");
    		if (!cache.del(key)) {
    			LinkedMap tem = database.getSubMap(key);
    			if (tem == null) return false;
    			
    			LinkedMap newMap = new LinkedMap(tem);
    			newMap.clear();
    			Object[] del = cache.add(key, newMap);
    			if (del != null) updateDb((String)del[0], (LinkedMap)del[1]);
    		}
    		
    		++curTime;
    		return true;
    }

	/* invalidate the specified value from the key.*/
	@Override
    public boolean delValue(String key, String value) throws TException {
		System.out.println("method: delValue(String key, String value)");
    		if (!cache.conatainsKey(key)) {
    			LinkedMap tem = database.getSubMap(key);
    			if (tem == null) return false;
    			
    			LinkedMap newMap = new LinkedMap(tem);
    			Object[] del = cache.add(key, newMap);
    			if (del != null) updateDb((String)del[0], (LinkedMap)del[1]);
    			
    			if (!newMap.remove(value)) return false;
    		}
    		
    		++curTime;
    		return true;
    }
    
	/* update database */
    private void updateDb(String key, LinkedMap value) {
    		System.out.println("  updata database with key " + key);
    		database.put(key, value);
    }
    
    /* show the contents of cache, for debugging. */
    public void showCache(boolean validRequired) {
		cache.show(validRequired);
    }
    
    /* show the contents of database, for debugging. */
    public void showDatabase(boolean validRequired) {
		database.show(validRequired);
    }
}
