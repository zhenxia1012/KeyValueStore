/////////////////////////////////////////////////////////////////////
// DbServer.java - server                                          //
//                                                                 //
// Author: Zhen Xia                                                //
/////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////
// DbClient:
// Client class offers interfaces for get/put operations on 
// server database

package dbServer;

import java.util.List;

import javax.print.DocFlavor.STRING;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import dbServer.DbService.Client;

public class DbClient {
	private int id;
	private String name;
	private TTransport transport;
	private DbService.Client connect;
	
	public DbClient(String name, int id, String to, int port) {
		this.name = name;
		this.id = id;
		transport = null;
		connect = null;
		iniConnection(to, port);
	}
	
	/* returns all friends, if present. */
	public List<String> getFriend() {
		List<String> friends = null;
		try {
			friends = connect.get(name);
		} catch (TException e) { 
	        	e.printStackTrace(); 
	    }
		return friends;
	}
	
	/* returns all friends added before the given time */
	public List<String> getFriendByTime(int time) {
		List<String> friends = null;
		try {
			friends = connect.getToTime(name, time);
		} catch (TException e) { 
			e.printStackTrace(); 
	    }
		return friends;
	}
	
	/* returns all friends added between time1 and time2 */
	public List<String> getFriendBetweenTime(int time1, int time2) {
		List<String> friends = null;
		try {
			friends = connect.dif(name, time1, time2);
		} catch (TException e) { 
	       	e.printStackTrace(); 
	    }
		return friends;
	}
	
	/* add a friend */
	public void addFriend(String friend) {
		try {
			connect.put(name, friend);
		} catch (TException e) { 
	       	e.printStackTrace(); 
	    }
	}
	
	/* delete one friend, but the history can still be queried */
	public boolean deleteFriend(String friend) {
		boolean isDel =	false;
		try {
			isDel = connect.delValue(name, friend);
		} catch (TException e) { 
	      	e.printStackTrace(); 
	    }
		return isDel;
	}
	
	/* delete all friends, but the history can still be queried */
	public boolean clearFriend() {
		boolean isDel = false;
		try {
			isDel = connect.delKey(name);
		} catch (TException e) { 
	       	e.printStackTrace(); 
	    }
		return isDel;
	}
	
	/* initialize connection */
	private void iniConnection(String to, int port) {
	       try {
	           transport = new TSocket(to, port);
	           transport.open(); 
	           TProtocol protocol = new TBinaryProtocol(transport); // transmission form: binary
	           connect = new DbService.Client(protocol); 
	           System.out.println(name + " client connected with server: " + to + ", port: " + port);
	       } catch (TTransportException e) { 
	           e.printStackTrace(); 
	       }
	}
	
	/* close connection */
	private void closeConnection() {
		transport.close();
	}
	
	protected void finalize()
	{
		closeConnection();
	}
}
