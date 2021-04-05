//To do: constructor, acks after execution

public class Server0 extends Server{
	private DatagramSocket socket; 
	//contains the server's address and port number

	ArrayList<Users> usersOnline; //contains the list of online users available for chatting
	
	//CONSTRUCTOR
	public Server0(String[] args){//we might need args
		//socket= new DatagramSocket( NEEDS CODE
	}

	//private void makeNewChat(ArrayList<Users> UserList){
	//makes a new chat with these users by starting a new chat server thread

	//}

	public void run(){
	//receives messages in an infinite loop
		while(true){
			receive();
		}
	}

	private void receive(){
	//Involves the receipt and processing of all messages in this server
		DatagramPacket msg= new DatagramPacket(byte[] buf, 420)//420 provisional

		msgText= new String(msg.getData(), 0, msg.getLength());

		if(!(super.goodChecksum(msgText))){
			//send error message?
			return;
		}

		msgText= super.stripChecksum(msgText);//We don't need the checksum now
		msgparts= msgText.split("\n");//{squence, headerline1, ..., N, bodyline1,...,N}
		
		//if message is a duplicate resend acknowledgement and do nothing else about it
		
		if(isDuplicate(msg,msgText)){
		//checks if the whole packet was sent previously
			super.sendAck(msg.getAddress(), msg.getPort(), msgparts[0], socket);
			return;
		}
		else{
			getUser(msg).setLastMessage(msgText);
		}

		//Now at this point we need to actually start dealing with the messages we get
		//We send acknowledgements of receipt after the message has been dealt with
		//but before our reply goes out

		//Let's do that below

		//SOME VARS (Assembly style! Might need to put this in run come to think of it)
		User u;			//Used as a placeholder to add users to the online list
		int listLen;		//Used to hold the lenths of various user lists
		String NewMessage;	//Used to store messages to be sent
		String chatName;	//Used to store chatnames for "create chat" cmds
		User[] ulist;		//Used to store lists of users for parsing into chats
		String time;		//Used to store time stamps for chat starts
		//END SOME VARS

		switch(msgparts[1]){
		//I'm online Ping (Post: Join, Username)
		//REACTION: Add Username, address, port to active users list
			case "Post: Join":
				u= new User(msgparts[2], msg.getAddress, msg.getPort());
				usersOnline.add(u);
				super.sendAck
					(msg.getAddress(), msg.getPort(), msgparts[0], socket);
				break;

		//Who else is Online (Get: MembersList, Username)
		//REACTION: Send an "AvailabilityList" message
			case "Get: MembersList":
				listLen= usersOnline.size()-1;
				NewMessage="Post: AvailableMembers\n";
				NewMessage+= listLen+ "\n";
				for( User x: usersOnline ){
				//adds all names except the caller's to the list
					if(!(x.name().equals(msgparts[2])){
						NewMessage+= x.name() +"\n";
					}
				}
				super.sendAck
					(msg.getAddress(), msg.getPort(), msgparts[0], socket);
				super.send
				(NewMessage, msg.getAddress, msg.getPort(), socket, 1);
				break;

		//Please make a chat (Post: NewChat, ChatName, ListLength, Time, Username1,...N)
		//REACTION: Make a New ChatServer, tell it to connect those users
			case: "Post: NewChat":
				chatName= msgparts[2];
				listLen= Integer.parseInt(msgparts[3]);
				ulist= new User[listLen];
				time= msgparts[4]

				try{
					for(int i=5; i<5+listLen; i++){
						ulist[i-5]= getUser(msgparts[i]));
						//gets user w/ the name msgparts[i]
					}
				}
				catch (nullPointerException e){
				//if getUser returns null we'll want to know about it
					System.out.println("User " +msgparts[i] " not found");
				}

				(new Thread(new ChatServer(chatName, ulist, time))).start();
				super.sendAck
					(msg.getAddress(), msg.getPort(), msgparts[0], socket);
				break;
		//I'm leaving (Get: End)
		//REACTION: Remove user from the active users list
			case:
				u=getUser(msg);
				usersOnline.remove(u);
				super.sendAck
					(msg.getAddress(), msg.getPort(), msgparts[0], socket);

			default://something weird happens like a duplicate ack or something
				System.out.println("Server 0 did not understand "+ msgparts[1]);
				//no acks sent for this
		}
	}
	
	private User getUser(String uname){
		for( User u: usersOnline ){
			if (u.name().equals(uname)){
				return u;
			}
		}
		return null;
	}

	private User getUser(DatagramPacket msg){
	//This version of get user allows us to fill in the "last msg received" user-field
	//That is used for duplicate checking
		InetAddress a= msg.getAddress();
		int p= msg.getPort();
		for( User u: usersOnline ){
			if (p==u.portID()){
				if (a.equals(u.address()){
					return u;
				}
			}
		}
		return null;//This should not happen
	}

	private boolean isDuplicate( DatagramPacket msg, String msgText ){
	//Checks if the received datagram was already received earlier from the sender
	//msg is the possibly duplicated packet
	//msg Text is what would match the last message
		InetAddress a= msg.getAddress();
		int p= msg.getPort();
		for( User u: usersOnline ){
			if (p==u.portID()){
				if (a.equals(u.address()){
					return msg.lastMessage().equals(msgText);
				}
			}
		}
	}
}