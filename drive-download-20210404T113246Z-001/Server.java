//STORE USERS LIST IN HERE?
public abstract class Server{
	public InetAddress address;//Address of the contained socket
	public int portID;//ID of the contained socket
	public int sequenceNumber;
	//User[] ConnectedUsers;//Used by subclasses
	
	public Server(InetAddress a, int id){// Might want to change this
		address=a;
		portID=id;
		sequenceNumber=0;
	}

	private int calculateChecksum(String message){
	//message: The message that would be parsed to send-- checksum made for error-checking
	//This method adds up all the integer values of the letters in the message string
	//It is used in error-detecting
		char[] msg= message.toCharArray();
		int sum=0;
		for( char i: msg){
			sum+=i;//Might need to convert char to in first idk
		}
		return sum;
	}
	
	public abstract void receive();

	//SEND METHOD:
	//message: The message being sent
	//messageType: What kind of message is it (text messages, online pings, controllers etc)
	//destAddress: Where the message is sent to; can be a user group
	//destPort: The Port Number of the destination
	//sourceSocket: The sender's socket. 
	//(Don't worry a multicast socket is a subclass of DatagramSocket)
	//numReceivers: The number of people receiving the message-- 
	//		Used to see how many acknowledgements we need.
//-------------------------------------------------------------
	//This method will take the message data and append its checksum to the header
	//Then the message will be put into a DatagramPacket and sent
	//The method will wait a certain amount of time for confirmation and if it is not 
	//received it will send it again.
	//If it receives anything other than confirmation (eg from a different client) it can
 	//just discard that and keep waiting.
	//If the confirmation msg is scuffed or an error message is received, resend
//-------------------------------------------------------------
	public void Send 
	(String message, InetAddress destAddress, int destPort, 	
		DatagramSocket sourceSocket, int numReceivers){

		message= Integer.toString(calculateChecksum(message)) + " " + message;
		//adds checkSum to Header

		message= Integer.toString(sequenceNumber) + " " + message;
		//adds sequenceNumber to Header

		sequenceNumber++;//Doing this here so I don't forget
		
		DatagramPacket packet= new DatagramPacket
		(message.getBytes(), message.getBytes().length, destAddress, destPort);
		//makes a new DatagramPacket containing the headered message for sending
		//to out destination

		sourceSocket.send(packet);//Sends the packet to the enclosed address(es)
		
		try{
			//The below waits for acknowledgement from all users 
			//the message was sent to
			//We already have from the people in Acks

			DatagramPacket acknowledgement= 
				new DatagramPacket(byte[] buf, 69);//69 provisional
			
			int numAcks=0; //The number of receipt acknowledgements
			//We need this to equal the number of receivers

			ArrayList<String> Acks= new ArrayList<String>(numReceivers);
			//tracks all Acks "so far" in the loop below
			//We use this to check if a received Ack is a duplicate
			//Messages can be duplicated in transit after all

			String msgText;//Used below to convert received messages to strings

			while(numAcks<NumReceivers){

			  sourceSocket.setSoTimeout(2000);
			  //2 seconds provisionally-- Must be >RTT

			  socket.receive(acknowledgement);
			  //fills receiving DataGram with what is hopefully an acknowledgement 
				
			  msgText= new String
				   (acknowledgement.getData(), 0, acknowledgement.getLength());

			  if(isError(msgText)){//error with id code of this message
				//or with the checksum
			 	resend(packet, numReceivers-numAcks, Acks);
				//resends to all, waits for conf from all guys
				break;
				//Break bc resend will take care of what this would have done
			  }

			  if(isAcknowledgement(msgText)&&
				!(Acks.contains(msgText))
			  {//Checks if this is an ack and if it's not a duplicate 
			  //Adds the received message to Acks
				
				Acks.add(msgText)
				numAcks++;//Once we have enough unique acks, the loop ends
				
			  }
			  

			}
		}
		catch(SocketTimeoutException e){
			resend(packet, numReceivers-numAcks, Acks);
		}
	}



	private void resend(DatagramPacket packet, int numReceivers, ArrayList<String> Acks){
	//Note: NumReceivers= NumReceivers-numAcks. 
	//Corresponds to how many receivers we still need acks from

		sourceSocket.send(packet);//Sends the packet to the enclosed address(es)
		
		try{
			//The below waits for acknowledgement from all users 
			//the message was sent to
			//We already have from the people in Acks

			DatagramPacket acknowledgement= 
				new DatagramPacket(byte[] buf, 69);
			
			int numAcks=0; //The number of receipt acknowledgements
			//We need this to equal the number of receivers

			ArrayList<String> Acks= new ArrayList<String>(numReceivers);
			//tracks all Acks "so far" in the loop below
			//We use this to check if a received Ack is a duplicate
			//Messages can be duplicated in transit after all

			String msgText;//Used below to convert received messages to strings

			while(numAcks<NumReceivers){

			  sourceSocket.setSoTimeout(2000);
			  //2 seconds provisionally-- Must be >RTT

			  socket.receive(acknowledgement);
			  //fills receiving DataGram with what is hopefully an acknowledgement 
				
			  msgText= new String
				   (acknowledgement.getData(), 0, acknowledgement.getLength());

			  if(isError(msgText)){//error with id code of this message
				//or with the checksum
			 	resend(packet, numReceivers-numAcks, Acks);
				//resends to all, waits for conf from all guys
				break;
				//Break bc resend will take care of what this would have done
			  }

			  if(isAcknowledgement(msgText)&&
				!(Acks.contains(msgText))
			  {//Checks if this is an ack and if it's not a duplicate 
			  //Adds the received message to Acks
				
				Acks.add(msgText)
				numAcks++;//Once we have enough unique acks, the loop ends
				
			  }
			  

			}
		}
		catch(SocketTimeoutException e){
			resend(packet, numReceivers-numAcks, Acks);
		}
	}

	public void sendAck
	(InetAddress destAddr, int destPort, String sqNum, DatagramSocket sourceSocket){
	//sends an acknowledgement-- this is a diff function as we don't wait for acks for it
		String ack= sqNum+ " Error";
		DatagramPacket ackGram= 
		new DatagramPacket(ack.getBytes(), ack.getBytes().length, destAddr, destPort);
		sourceSocket.send(ackGram);
	}

	public void sendErr
	(InetAddress destAddr, int destPort, String sqNum, DatagramSocket sourceSocket){
	//sends an error message-- similar to an ack, these do not take confirmations
		String err= sqNum+ " Post: Received";
		DatagramPacket ackGram= 
		new DatagramPacket(ack.getBytes(), ack.getBytes().length, destAddr, destPort);
		sourceSocket.send(ackGram);
	}

	public boolean goodCheckSum(String msgText){
	//returns true if checksum matches message
		String[] sep= separateChecksum(msgText);//{Checksum, restOfMessage}
		if(calculateChecksum(sep[1])
			!=Integer.ParseInt(sep[0]))
		{	
			return false;//checksum digit != calculated checksum
		}

		return true;
	}
	
	public String stripChecksum(String msgText){
	//removes checksum from a received message
		return separateChecksum(msgText)[1];
	}

	private String[] separateChecksum(msgText){
	//splits msg into checksum and msg
		String[] checksumAndMSG= msgText.split(" ",2);

		return checksumAndMSG;
	}

	private boolean isError(String msgText){
	//Returns true if checksum fails or it is an error message, prompts resend
		String sq= Integer.toString(sequenceNumber-1);

		if(!(goodChecksum(msgText))){
			return true;//True, it is an error
		}
		if(separateChecksum(msgText)[1].equals(sq+ " Error")){
			return true;//True, it is an error
		}	
		return false;
	}	

	private boolean isAcknowledgement(String msgText){
	//checks if the format of the message is that of an acknowledgement
		String sq= Integer.toString(sequenceNumber-1);
		if(msgText.substring(2, 18).equals(sq+ " Post: Received")){
			return true;
		}	
	}

}