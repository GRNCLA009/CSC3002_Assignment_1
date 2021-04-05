class User{
	private String name; //Username
	private InetAddress address; //User's InetAddress
	private int portID; //PortID of the chat that this user object is associated with
	private String lastMessage; //the last message sent by the user in the relevant chat 
	//(for duplicate-checking purposes)

	public User(String n, String a, int id){
	//Standard constructor, initialises variables to what is parsed, LastMessage empty
		name=n;
		address=a;
		portID=id;
		lastMessage="";
	}

	public String name(){
	//Getter for the name field
		return this.name();
	}
	public InetAddress address(){
	//Getter for the address field
		return this.address();
	}
	public int portID(){
	//Getter for the portID field
		return this.portID();
	}
	public String lastMessage(){
	//Getter for the lastMessage field
		return this.lastMessage();
	}
	public void setLastMessage(String s){
	//Setter for the lastMessage field
		this.lastMessage=s;
	}
}