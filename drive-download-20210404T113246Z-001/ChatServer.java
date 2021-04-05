//To do: Constructor, receive
//remind group that usernames must not start with numbers
public class ChatServer extends Runnable{
	private DatagramSocket socket; //for broadcasting/receiving messages
	private InetAddress thisAddr;
	private int thisPort;
	private InetAddress group; //The address we're broadcasting to 
					//that all the clients must connect to
	private int grpPort;
	private User[] userList; //The users in the chat
	private String chatName; //The name of the chat (we might not need to store this)
	private boolean notEnded=true; //used to keep the chat going

	public ChatServer(String cname, User[] ul, String time){
		chatName=cname;//Initialise chatname
		userList= ul;//Initialise userlist
		//portID=p; we'll need to init address and group at some point
		//And also socket?
		//NEEDS CODE

		//Below we send the message to connect members
		String connectMSG= "Get: ConnectChat\n";
		connectMSG+= chatName + "\n";
		for (User u : userList){
			connectMSG+=u.name()+"\n";
		}
		connectMSG+=time;//the message is now complete

		
		for(User u:userList){
			super.send
			( connectMSG, u.address(), u.portID(), socket, userList.length );
		}

		//By now we have all the receipt confirmations
		//Now we need to wait for all the 6.1 messages to come through

		//MAYBE CODE HERE FOR THAT

		//We just need to tell everyone the chat is open
	
		String openMSG= "Post: ConfirmChatOpen\n";
		
		super.send( openMSG, group, grpPort, socket, userList.length );
		//now we're ready to rock
	}

	public void run(){
		while(notEnded){
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
		//sequence is a number appended to a username



		//Next we must write an acknowledgement message
		super.sendAck(group, grpPort, msgparts[0], socket);//to grp, all but 1 ignores

		//if message is duplicate resend acknowledgement (done) and do nothing else

		if(isDuplicate(msg,msgText)){
		//checks if the whole packet was sent previously
			return;// we already sent acknowledgement above
		}
		else{
			getUser(stripSequence(msgparts[0])).setLastMessage(msgText);//
		}

		//Now at this point we need to actually start dealing with the messages we get
		//Let's do that below

		//SOME VARS (Assembly style! Might need to put this in run come to think of it)
		User u;			//Used as a placeholder to add users to the online list
		int listLen;		//Used to hold the lenths of various user lists
		String message;		//Used to store messages to be sent
		String chatName;	//Used to store chatnames for "create chat" cmds
		User[] ulist;		//Used to store lists of users for parsing into chats
		String time;		//Used to store time stamps for chat starts
		//END SOME VARS

		switch(msgparts[1]){
		//Message from User (Post: Message, Timestamp, Message)
		//REACTION: Pass on the message to the group
			case "Post: Message":
				message= "Post: MessagePassOn\n";
				message+= stripSequence(msgparts[0])+ "\n"; //Gets the username
				message+= msgparts[2]+"\n";//"Sent" Timestamp
				message+= msgparts[3];//The actual message
				super.send(message, group, grpPort, socket, userList.size());
				break;

		//Request to leave (Post: EndChat, TimeStamp)
		//REACTION: remove from userslist, tell peeps they gone
			case "Post: EndChat":
				u=getUser(stripSequence(msgParts[0]);
				userList.remove(u); //the user that left is removed
				message= "Post: EndNotice\n"; //command line
				message=u.name() + "\n";//leaving user
				message+= msgParts[2]+\n;//adding the time
				message+= userList.size();//number of people left in chat
				break;
			
			default://something weird happens like a duplicate ack or something
				System.out.println(chatName+"did not understand "+ msgparts[1]);
		}
	}
	
	private User getUser(String uname){
	//takes in username, returns user object associated with it
	//uname: Username of requested object
		for( User u: usersOnline ){
			if (u.name().equals(uname)){
				return u;
			}
		}
		return null;
	}


	private boolean isDuplicate( String sqnce, String msgText ){
	//Checks if the received datagram was already received earlier from the sender
	//sqnce is the sequence identifier of the possibly duplicated packet
	//msg Text is what would match the last message
		String sq= stripSequence(sqnce);//shaves off the sequence digits
		User u= getUser(sq);//retrieves the user from the username
		return u.lastMessage.equals(msgText);
	}

	private String stripSequence(String sq){
	//Extracts the username portion from client messages
		String[] parts= sq.split("-");
		return parts[1];
	}
}