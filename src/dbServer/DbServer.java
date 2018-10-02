/////////////////////////////////////////////////////////////////////
// DbServer.java - server                                          //
//                                                                 //
// Author: Zhen Xia                                                //
/////////////////////////////////////////////////////////////////////

package dbServer;

import org.apache.thrift.TProcessor; 
import org.apache.thrift.protocol.TBinaryProtocol; 
import org.apache.thrift.protocol.TBinaryProtocol.Factory; 
import org.apache.thrift.server.TServer; 
import org.apache.thrift.server.TThreadPoolServer; 
import org.apache.thrift.transport.TServerSocket; 
import org.apache.thrift.transport.TTransportException; 

public class DbServer {
	
    public static void main(String[] args) {
	    	try { 
            TServerSocket serverTransport = new TServerSocket(7911); // blocking transport, port: 7911
            Factory proFactory = new TBinaryProtocol.Factory(); // transmission form: binary 
            TProcessor processor = new DbService.Processor(new DbServiceImpl()); 
            TThreadPoolServer.Args tArgs = new TThreadPoolServer.Args(serverTransport); // multi-threads
            tArgs.processor(processor);
            tArgs.protocolFactory(proFactory);
            TServer server = new TThreadPoolServer(tArgs); 
            System.out.println("Start server on port 7911..."); 
            server.serve(); // start service
	    	} catch (TTransportException e) { 
	    	    e.printStackTrace();
	    	} 
    }
}