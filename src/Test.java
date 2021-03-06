
import java.io.*;
import java.util.*;
/**
 * Version 2.0
 * @author lbuga
 *
 */
public class Test {
	
	public static int serveTime = 0;
	private static boolean pm = false;
	private static int previoushour = 0;
	private static int idletime =0;
	private static int longestIdleTime = 0;
	private static int totalCustomers = 0;
	private static int longestQueue = 0;
	private static int currentQueue = 0;
	
	public static boolean isPm() {
		return pm;
	}
	public static void setPm(boolean pm) {
		Test.pm = pm;
	}
	public static int getHour() {
		return previoushour;
	}
	public static void setPrevioushour(int hour) {
		Test.previoushour = hour;
	}

	public class Customer {
		private int id;
		private int entry_time;
		private int wait_time;
		private boolean isTrue = false;
		public Customer() {
			
		}
		public Customer(String info) {
			System.out.println("Go here");
			this.isTrue=true;
			String [] lines = info.split("\n");
			String temp = "";
			for (int i=0;i<lines[0].length();i++) {
				if(Character.isDigit(lines[0].charAt(i))) {
					temp+=lines[0].charAt(i);
				}
			}
			String[] times = lines[1].split(":");
			//Parse the time into seconds
			int extra_time = 0;
			if (Integer.parseInt(times[1].trim())>previoushour && !Test.pm) {
				Test.setPrevioushour(Integer.parseInt(times[1].trim()));
			}
			else if (Integer.parseInt(times[1].trim())<previoushour && !Test.pm){
				Test.setPm(true);
			}
			int time = Integer.parseInt(times[1].trim())*3600+Integer.parseInt(times[2].trim())*60+Integer.parseInt(times[2].trim());

			if (Test.isPm()) {
				time+= 12*3600;
			}
			this.setId(Integer.parseInt(temp));
			this.setEntry_time(time);
			Test.totalCustomers++;
		}
		public void setEntry_time(int entry_time) {
			this.entry_time= entry_time;
		}
		public int getEntry_time() {
			return this.entry_time;
		}
		public void setId(int id) {
			this.id= id;
		}
		public int getId() {
			return this.id;
		}
	}
	
	public class Node{
		public Customer data;
		public Node next= endNode;
	}
	Customer end = new Customer();
	Node endNode = new Node();
	
	private Node first;
	public Test() {
		this.setFirst(endNode);
	}
	public void setFirst(Node n) {
		this.first = n;
	}
	public void addLast(Customer c) {
		System.out.println("Hello World.begin");
		Node newNode = new Node ();
		newNode.data = c;
		newNode.next= null;
		//Iterate through al the elements to add the new Node to the end of the list
		Iterator temp = new Iterator();
		while(temp.hasNext()) {
			temp.next();
		}
		
		//Add the waiting time for each new Customer added
		temp.position.next=newNode;
		if (newNode.data.getEntry_time()-temp.position.data.getEntry_time()<Test.serveTime//If time difference is less than service time
				) {
			newNode.data.wait_time=temp.position.data.wait_time+(Test.serveTime-(newNode.data.getEntry_time()-temp.position.data.getEntry_time()));
			Test.currentQueue++;
		}
		else {
			/*
			 * Look through this
			 */
			int time = newNode.data.entry_time-temp.position.data.entry_time+300;
			Test.idletime+=time;
			if (time>Test.longestIdleTime) {
				Test.longestIdleTime=time;
			}
			newNode.data.wait_time=0;
			if(Test.currentQueue>Test.longestQueue) {
				Test.longestQueue = Test.currentQueue;
				Test.currentQueue = 0;
			}
		}
		System.out.println("Hello World. end");
	}
	public class Iterator implements ListIterator<Object>{

		private Node position;
		private Node previous;
		
		public Iterator() {
			position = null;
			previous = endNode;
					
		}
		@Override
		public void add(Object arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean hasNext() {
			if (this.position.next== endNode) {
				return false;
			}
			else {
				return true;
			}
		}

		@Override
		public boolean hasPrevious() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Object next() {
			if (!this.hasNext()) {
				throw new NoSuchElementException();
			}
			else {
				this.previous =position;
				this.position =position.next;
			}
			return position.data;//Returns a customer object with ID and entry time
		}

		@Override
		public int nextIndex() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object previous() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int previousIndex() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void remove() {
			if (this.position == first) {
				//removeFirst();
			}
			else {
				//Sets the previous reference to the curent position
				previous.next = position.next;
				this.previous = updatePrevious(this.position);
			}
			
		}
		/*
		 * Updates the previous position when a node is deleted
		 */
		public Node updatePrevious(Node n) {
			Iterator temp = new Iterator();
			while (temp.position!=n) {
				temp.next();
			}
			return temp.previous;
		}

		@Override
		public void set(Object arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	public static void main(String[]args) throws FileNotFoundException{
		
		String f1path = "customersfile.txt";
		String f2path = "queriesfile.txt";
		FileReader customers = new FileReader(f1path);
		FileReader queries = new FileReader (f2path);
		BufferedReader readCustomers = new BufferedReader(customers);
		BufferedReader readQueries = new BufferedReader(queries);
		String result ="";
		
		
		/*
		 * Get the constant serve time for each customer by reading the first line
		 * and parsing it as an integer
		 */
		try {
			Test.serveTime = Integer.parseInt(readCustomers.readLine());
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Test clist = new Test();
		try {
			String temp = "";
			String thisLine = "";
			while((thisLine= readCustomers.readLine())!=null) {
				
				if(temp.length()>0 && thisLine.isEmpty() ) {
					//Create a new Customer and add the customer to the Customer LinkedList
					
					clist.addLast(clist.new Customer(temp));
					System.out.println("Hello World.");
					//Reset the data read from the file
					temp = "";
				}
				else if (thisLine.isEmpty()) {
					continue;
				}
				else {
					temp+= thisLine;temp+="\n";
				}
				
			}
			System.out.println("Hello World.");
			thisLine = "";
			result += readQueries.readLine()+ ": "+Test.totalCustomers +"\n";
			result += readQueries.readLine()+ ": "+Test.longestIdleTime+"\n";
			result += readQueries.readLine()+ ": "+Test.idletime+"\n";
			result += readQueries.readLine()+ ": "+Test.longestQueue+"\n";
			while((thisLine= readQueries.readLine())!=null) {
				result+=thisLine + ": ";
				String find_id = "";
				for(int i=0;i<thisLine.length();i++) {
					if (Character.isDigit(thisLine.charAt(i))) {
						find_id+=thisLine.charAt(i);
					}
				}
				Iterator temp2 = clist.new Iterator();
				while(temp2.hasNext()) {
					if (temp2.position.data.getId()==Integer.parseInt(find_id)) {
						result += temp2.position.data.getId()+"\n";
						break;
					}
				}
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(result);
		System.out.println("here");
	}

	
	
}
