/**
 * Created by Shin on 2017-05-31.
 */

import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.ResultSet;

import java.sql.SQLException;
import java.util.Scanner;
public class Manager {
	static java.sql.Statement st = null;
	static ResultSet rs = null;
	static Connection conn = null;
	
	public void addMenu(){
		Scanner scan = new Scanner(System.in);
		int MenuID;
		String cafe;
		String mname;
		int calorie;
		int price;
		int stock;
		
		
		
		try{
			 st = conn.createStatement();
	       		rs = st.executeQuery("use 21400451_db");
	            
			
	       		System.out.println("hello manager");
	       		System.out.println("type in the MENU ID");
	       		MenuID = scan.nextInt();
	       		System.out.println("type in the cafe name");
	       		cafe = scan.next();
	       		System.out.println("type in the menu name");
	       		mname = scan.next();
	       		System.out.println("type in the calorie");
	       		calorie = scan.nextInt();
	       		System.out.println("type in the price");
	       		price = scan.nextInt();
	       		System.out.println("type in the stock");
	       		stock = scan.nextInt();
	       		
	       		
	       		String sql = "INSERT INTO MENUS"+"(MENUID,CAFE,MNAME,CALORIE,PRICE,STOCK)"+" "+"VALUES"+
	       		           "("+MenuID+","+"'"+cafe+"'"+","+"'"+
	       				mname+"'"+","+calorie+","+price+","+stock+")"
	       		+";";
	       		st.executeUpdate(sql);
	       		
	       		
	       		System.out.println("menu successfully added!");
	       		
	       		
	       		
	       		
	       		
	       		
	       		
	       		
	       		
	       		
	       		
	       		
			  
			  
			  System.out.println("done! new stock added!");
			  
			  
		  }
	      catch (Exception e){
	    	  
	    	  
	    	  System.out.println("SQLException: " + e.getMessage());

				System.out.println("SQLState: " + e.getStackTrace());
	  
	    	  
	    	  
	    	  
	    	  
	    	  
	      }
		
		
		
		
		
	}
	public void deleteMenu(){
		Scanner scan = new Scanner(System.in);
		
		String cafe;
		String mname;
		
		
		
		
		
		
		try{
			 st = conn.createStatement();
	       		rs = st.executeQuery("use 21400451_db");
	            
			
	       		System.out.println("hello manager");
	       		
	       		System.out.println("type in the cafe name");
	       		cafe = scan.next();
	       		System.out.println("type in the menu name");
	       		mname = scan.next();
	       		
	       		
	       		
	       		String sql = "DELETE FROM MENUS"+"WHERE CAFE ="+"'"+cafe+"'"+"and MNAME="+"'"+mname+"'"+";";
	       		    
	       		st.executeUpdate(sql);
	       		
	       		
	       		System.out.println("menu successfully deleted");
	       		
	       		
	       		
	       		
	       		
	       		
	       		
	       		
	       		
	       		
	       		
	       
			  
		  }
	      catch (Exception e){
	    	  
	    	  
	    	  System.out.println("SQLException: " + e.getMessage());

				System.out.println("SQLState: " + e.getStackTrace());
	  
	    	  
	    	  
	    	  
	    	  
	    	  
	      }
		
		
		
		
		
		
	}
	 
	
	public void Event(){
		
		Scanner scan = new Scanner(System.in);
		String title;
		String content;
		String cafe;
		
		
		
		
		try{
			 st = conn.createStatement();
	       	 rs = st.executeQuery("use 21400451_db");
	            
	       		System.out.println("Hello manager");
	       		System.out.println("Please enter the cafe name");
	       		cafe = scan.next();
	       		System.out.println("please enter the title of the event");
	       		title = scan.next();
	       		System.out.println("please enter the content of the event");
	       		content = scan.next();
	       		
	       		
	       		String sql = "INSERT INTO POST"+"(TITLE,POST_DATE,CONTENT,CAFE)"+" "+"VALUES"+
	       		           "("+"'"+title+"'"+","+"'"+
	       				content+"'"+","+"'"+cafe+"'"+")"
	       		+";";
	       		st.executeUpdate(sql);
			
			
			System.out.println("event successfully posted!");
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		}
		catch(Exception e){
			
			
			
			  System.out.println("SQLException: " + e.getMessage());

				System.out.println("SQLState: " + e.getStackTrace());
	  
	    	  
			
			
			
			
			
		}
		
		
		
		
		
		
		
		
		
		
		
	}
	
	
	public void Order(){
		int time;
		String customer;
		String cafe_name;
		String Menu;
		Scanner scan = new Scanner(System.in);
		  try{
			  
			  
			  
			  System.out.println("Hello manger");
			  System.out.println("Please enter in the customer who bought it");
              customer = scan.next();
              System.out.println("please enter in the cafe_name");
              cafe_name= scan.next();
              System.out.println("please enter in the menu nmae");
              Menu = scan.next();
              
              
              
              
              String sql = "INSERT INTO ORDERS" +"(ISSUE_TIME,CUSTOMER,CAFE_NAME,MENU)"+" "+"VALUES('"+customer+"'"+","
              +"'"+cafe_name+"'"+","+"'"+Menu+"'"+")"+";";
			  
			  
              st.executeUpdate(sql);
              
              
              
              String sql2 = "UPDATE MENUS SET STOCK = STOCK - 1 WHERE CAFE ="+"'"+cafe_name+"'"+"AND MNAME="+"'"
              +Menu+"'"+";";
	       		st.executeUpdate(sql2);
			  
			  
			  
			  
			  
		  }
		  
		  catch(Exception e){
			  

			  System.out.println("SQLException: " + e.getMessage());

				System.out.println("SQLState: " + e.getStackTrace());
	  
	    	  
			
			  
			  
		  }
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
	
	
	
	
	

}
