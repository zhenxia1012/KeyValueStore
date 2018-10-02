/////////////////////////////////////////////////////////////////////
// DbServer.java - test executive                                  //
//                                                                 //
// Author: Zhen Xia                                                //
/////////////////////////////////////////////////////////////////////

package dbServer;

public class DbClientExecutive {
	
	   public static void main(String[] args) { 
		   // create client1
		   DbClient client1 = new DbClient("1", 0, "localhost", 7911);
		   
		   System.out.println("  client1 add friends 2 ~ 7\n");
		   client1.addFriend("2"); // time 0
		   client1.addFriend("3"); // time 1
		   client1.addFriend("4"); // time 2
		   client1.addFriend("5"); // time 3
		   client1.addFriend("6"); // time 4
		   client1.addFriend("7"); // time 5
		   
		   System.out.println("		valid friends of client1: ");
		   for (String fri : client1.getFriend()) // // time 6
			   System.out.print(fri + " ");
		   System.out.println();
		   System.out.println("		friends of client1 added between time 2 ~ 4: ");
		   for (String fri : client1.getFriendBetweenTime(2, 4)) // time 7
			   System.out.print(fri + " ");
		   System.out.println();
		   
		   System.out.println("  client1 delete friends 4, 7\n");
		   client1.deleteFriend("4"); // time 8
		   client1.deleteFriend("7"); // time 9
		   
		   System.out.println("  	valid friends of client1: ");
		   for (String fri : client1.getFriend()) // time 10
			   System.out.print(fri + " ");
		   System.out.println();
		   
		   System.out.println("  friends of client1 added between time 4: \n");
		   for (String fri : client1.getFriendByTime(4)) // time 11
			   System.out.print(fri + " ");
		   System.out.println();
	   }
}