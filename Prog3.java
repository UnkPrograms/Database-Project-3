/*
 * Name: Ryunki Song
 * Class: CS460 Database
 * Professor: McCann
 * TA: Jacob, Yawen
 * Due Date: 4/5/2017 3:30 PM
 * Project 3
 * 
 * This file was adapted from the given example code on https://www2.cs.arizona.edu/classes/cs460/spring17/JDBC.java.
 * 
 * Professor McCann's Notes:
 * At the time of this writing, the version of Oracle is 11.2g, and
 * the Oracle JDBC driver can be found at
 *   /opt/oracle/product/10.2.0/client/jdbc/lib/ojdbc14.jar
 * on the lectura system in the UofA CS dept.
 * (Yes, 10.2, not 11.2.  It's the correct jar file but in a strange location.)
 *
 * To compile and execute this program on lectura:
 *
 *   Add the Oracle JDBC driver to your CLASSPATH environment variable:
 *
 *         export CLASSPATH=/opt/oracle/product/10.2.0/client/jdbc/lib/ojdbc14.jar:${CLASSPATH}
 *
 *     (or whatever shell variable set-up you need to perform to add the
 *     JAR file to your Java CLASSPATH)
 *
 *   Compile this file:
 *
 *         javac JDBC.java
 *
 *   Finally, run the program:
 *
 *         java JDBC <oracle username> <oracle password>
 *
 * Author:  L. McCann (2008/11/19; updated 2015/10/28)
 * 
 * 
 * Ryunki Song's Notes:
 * Description: Runs with no parameters. 
 * This class allows the user to select from 4 main options where they can get info from the AIMS database from
 * 2010-2014. The options are 
 * 1. Number of high schools for a given year
 * 2. Number of Charter Schools and how many of them are have the sum of Below and Approach Math Percentage lower than the Math Passing Percentage
 * 3. For each county in 2014, which 10 schools had the greatest difference between Reading and Writing passing %.
 * 4. Select a county to find its top 3 schools with the highest math passing %, along with their reading and writing passing % for 2014.
 * 
 * Each option runs a query and prints out the results. 
 * 
 * HOW TO RUN: 
 * Make sure to connect the Oracle JDBC driver to the classpath.
 * Then make sure to run the .sql files to add the contents of the AIMS data to the oracle database
 * Then run the program
 */

import java.sql.*;                 // For access to the SQL interaction methods
import java.util.Scanner;

public class Prog3
{
    public static void main (String [] args)
    {

    	//Check for user input on the command line
    	if(args.length != 0){
    		System.err.println("Please run the program without any arguments");
    		System.exit(-1);
    	}
    	
    	
    	//Print these reminder messages to the user
		Scanner scan = new Scanner(System.in);
		System.out.println();
    	System.out.println("Welcome to the AIMS database. Please remember to add the Oracle JDBC driver "
    			+ "to the classpath.");
    	System.out.println("Also remember to run the .sql files before beginning so that the SQL tables "
    			+ "have been created. ");
    	System.out.println("Press any key to continue.");
		System.out.println();
    	scan.nextLine();    	
		System.out.println();

		
		//Auto connect with the student's username and password
        final String oracleURL =   // Magic lectura -> aloe access spell
                        "jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle";
        
        String username = "cantstoptheunk",    // Oracle DBMS username
               password = "a5159";    // Oracle DBMS password


        // load the (Oracle) JDBC driver by initializing its base
        // class, 'oracle.jdbc.OracleDriver'.
        try {
                Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
                System.err.println("*** ClassNotFoundException:  "
                    + "Error loading Oracle JDBC driver.  \n"
                    + "\tPerhaps the driver is not on the Classpath?");
                System.exit(-1);
        }
 
		//Use this while loop to continuously get user input
		while (true) {
			// make and return a database connection to the user's
	        // Oracle database
	        Connection dbconn = null;

	        try {
	                dbconn = DriverManager.getConnection
	                               (oracleURL,username,password);
	        } catch (SQLException e) {
	                System.err.println("*** SQLException:  "
	                    + "Could not open JDBC connection.");
	                System.err.println("\tMessage:   " + e.getMessage());
	                System.err.println("\tSQLState:  " + e.getSQLState());
	                System.err.println("\tErrorCode: " + e.getErrorCode());
	                System.exit(-1);
	        }

	        //Print out options to the user
			String option1 = "1. Number of high schools.";
			String option2 = "2. Number of Charter Schools and how many of them are have the sum of Below and Approach Math Percentage lower than the Math Passing Percentage.";
			String option3 = "3. For each county in 2014, which 10 schools had the greatest difference between Reading and Writing passing %.";
			String option4 = "4. Select a county to find its top 3 schools with the highest math passing %, along with their reading and writing passing % for 2014.";

			System.out.println("Please select an option.");
			System.out.println(option1);
			System.out.println(option2);
			System.out.println(option3);
			System.out.println(option4);
			System.out.println("5. Exit.");
			System.out.println();
			String input = scan.nextLine();
			
			//Check for non-valid input
			if (!input.equals("1") && !input.equals("2") && !input.equals("3") && !input.equals("4") && !input.equals("5")){
				System.out.println("Please enter a valid integer option.");
				System.out.println();
			}
			
			//Otherwise input is valid
			else {
				//If user wants to exit
				if (input.equals("5")) {
					// Shut down the connection to the DBMS.
					scan.close();
					try {
						dbconn.close();
					} catch (SQLException e) {
						System.err.println("Failed to close DBMS connection.");
						System.exit(-1);
					}
					System.exit(0);
				}
				
				//If user picks to find # of high schools
				else if(input.equals("1")){
					System.out.println("Please enter a year between 2010-2014.");
					System.out.println();
					String year = scan.nextLine();
					if(!year.equals("2010") && !year.equals("2011") && !year.equals("2012") && !year.equals("2013") && !year.equals("2014")){
						System.out.println("Please enter a valid year.");	
						System.out.println();
					}
					else{
						performQuery1(dbconn, year);
					}
				}
				
				//If user picks to find # of charter schools and math percents query
				else if(input.equals("2")){										
					performQuery2(dbconn);
				}
				
				//If user wants the county read pass and write pass differences
				else if(input.equals("3")){										
					performQuery3(dbconn);
				}

				//If user wants top 3 math schools from a county
				else if(input.equals("4")){		
					System.out.println("Please select a number from the following counties.");
					System.out.println("1. Apache");
					System.out.println("2. Cochise");
					System.out.println("3. Coconino");
					System.out.println("4. Gila");
					System.out.println("5. Graham");
					System.out.println("6. Greenlee");
					System.out.println("7. La Paz");
					System.out.println("8. Maricopa");
					System.out.println("9. Mohave");
					System.out.println("10. Navajo");
					System.out.println("11. Pima");
					System.out.println("12. Pinal");
					System.out.println("13. Santa Cruz");
					System.out.println("14. Yavapai");
					System.out.println("15. Yuma");
					System.out.println();
					
					try{
						String countStr = scan.nextLine();
						int county = Integer.parseInt(countStr);
						performQuery4(dbconn, county);
					}
					catch (Exception e){
						System.out.println("Please type a valid integer.");
						System.out.println();
					}

				}
			}
		}

    }
    
    /*
     * This method runs the query for option 1 which is to find the number of high schools for a given year.
     * The query is written as a SQL statement and then ran through the database and then prints out
     * the results.
     * 
     * Parameters: The connection to the database, and the year the user typed in.
     */
    public static void performQuery1(Connection dbconn, String year){
    	String query = "";
    	
    	//Depending on what the user typed in, write the correct query statement
		if(year.equals("2010")){
			query = "SELECT COUNT(*) FROM( "
					+ "SELECT * FROM cantstoptheunk.aims2010 WHERE FullSchoolName LIKE '%High School%' "
					+ "MINUS "
					+ "SELECT * FROM cantstoptheunk.aims2010 WHERE FullSchoolName LIKE '%Jr High%' "
					+ "MINUS "
					+ "SELECT * FROM cantstoptheunk.aims2010 WHERE FullSchoolName LIKE '%Junior High%'"
					+")";
		}
		else if(year.equals("2011")){
			query = "SELECT COUNT(*) FROM( "
					+ "SELECT * FROM cantstoptheunk.aims2011 WHERE FullSchoolName LIKE '%High School%' "
					+ "MINUS "
					+ "SELECT * FROM cantstoptheunk.aims2011 WHERE FullSchoolName LIKE '%Jr High%' "
					+ "MINUS "
					+ "SELECT * FROM cantstoptheunk.aims2011 WHERE FullSchoolName LIKE '%Junior High%'"
					+")";
		}
		else if(year.equals("2012")){
			query = "SELECT COUNT(*) FROM( "
					+ "SELECT * FROM cantstoptheunk.aims2012 WHERE FullSchoolName LIKE '%High School%' "
					+ "MINUS "
					+ "SELECT * FROM cantstoptheunk.aims2012 WHERE FullSchoolName LIKE '%Jr High%' "
					+ "MINUS "
					+ "SELECT * FROM cantstoptheunk.aims2012 WHERE FullSchoolName LIKE '%Junior High%'"
					+")";
		}
		else if(year.equals("2013")){
			query = "SELECT COUNT(*) FROM( "
					+ "SELECT * FROM cantstoptheunk.aims2013 WHERE FullSchoolName LIKE '%High School%' "
					+ "MINUS "
					+ "SELECT * FROM cantstoptheunk.aims2013 WHERE FullSchoolName LIKE '%Jr High%' "
					+ "MINUS "
					+ "SELECT * FROM cantstoptheunk.aims2013 WHERE FullSchoolName LIKE '%Junior High%'"
					+")";
		}
		else if(year.equals("2014")){
			query = "SELECT COUNT(*) FROM( "
					+ "SELECT * FROM cantstoptheunk.aims2014 WHERE FullSchoolName LIKE '%High School%' "
					+ "MINUS "
					+ "SELECT * FROM cantstoptheunk.aims2014 WHERE FullSchoolName LIKE '%Jr High%' "
					+ "MINUS "
					+ "SELECT * FROM cantstoptheunk.aims2014 WHERE FullSchoolName LIKE '%Junior High%'"
					+")";
		}
		
		// Send the query to the DBMS, and get and display the results
		Statement stmt = null;
		ResultSet answer = null;

		try {
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(query);
			if (answer != null) {
				System.out.print("\n" + year + ": ");

				// Use next() to advance cursor through the result
				// tuples and print their attribute values
				while (answer.next()) {
					System.out.print(answer.getInt("Count(*)"));
				}
				System.out.print(" high schools.\n");
			}
			System.out.println();

			// Shut down the connection to the DBMS.
			stmt.close();
			dbconn.close();

		} catch (SQLException e) {
			System.err.println("*** SQLException:  " + "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}
    }
    
    /*
     * This method runs the query for option 12 which is to find the number of charter schools 
     * while also printing out how many charter schools had sums of below and approach math percentages
     * versus their passing percentage.
     * The query is written as a SQL statement and then ran through the database and then prints out
     * the results.
     * 
     * Parameters: The connection to the database.
     */
    public static void performQuery2(Connection dbconn){
    	
    	//Setup the query for all years. Note 10a, 10b refers to the year (2010 and its query description. a = # of charter school, b = percentage related query)
		String query10a = "SELECT COUNT(*) FROM cantstoptheunk.aims2010 WHERE IsCharter = 'Y'";
		String query10b = "SELECT COUNT(*) FROM ("
							+ "SELECT * FROM cantstoptheunk.aims2010 WHERE MathBelowApproach < MathPercentPassing "
							+ "INTERSECT "
							+ "SELECT * FROM cantstoptheunk.aims2010 WHERE IsCharter = 'Y'"
							+ ")";
		
		String query11a = "SELECT COUNT(*) FROM aims2011 WHERE IsCharter = 'Y'";
		String query11b = "SELECT COUNT(*) FROM ("
							+ "SELECT * FROM cantstoptheunk.aims2011 WHERE MathBelowApproach < MathPercentPassing "
							+ "INTERSECT "
							+ "SELECT * FROM cantstoptheunk.aims2011 WHERE IsCharter = 'Y'"
							+ ")";
		
		String query12a = "SELECT COUNT(*) FROM aims2012 WHERE IsCharter = 'Y'";
		String query12b = "SELECT COUNT(*) FROM ("
							+ "SELECT * FROM cantstoptheunk.aims2012 WHERE MathBelowApproach < MathPercentPassing "
							+ "INTERSECT "
							+ "SELECT * FROM cantstoptheunk.aims2012 WHERE IsCharter = 'Y'"
							+ ")";

		String query13a = "SELECT COUNT(*) FROM aims2013 WHERE IsCharter = 'Y'";
		String query13b = "SELECT COUNT(*) FROM ("
							+ "SELECT * FROM cantstoptheunk.aims2013 WHERE MathBelowApproach < MathPercentPassing "
							+ "INTERSECT "
							+ "SELECT * FROM cantstoptheunk.aims2013 WHERE IsCharter = 'Y'"
							+ ")";
		
		String query14a = "SELECT COUNT(*) FROM aims2014 WHERE IsCharter = 'Y'";
		String query14b = "SELECT COUNT(*) FROM ("
							+ "SELECT * FROM cantstoptheunk.aims2014 WHERE MathBelowApproach < MathPercentPassing "
							+ "INTERSECT "
							+ "SELECT * FROM cantstoptheunk.aims2014 WHERE IsCharter = 'Y'"
							+ ")";

		// Send the query to the DBMS, and get and display the results
		Statement stmt = null;
		ResultSet answer = null;

		try {
			stmt = dbconn.createStatement();
			
			answer = stmt.executeQuery(query10a);
			if (answer != null) {
				System.out.print("\nThe number of Charter Schools for 2010: ");
				// Use next() to advance cursor through the result
				// tuples and print their attribute values
				while (answer.next()) {
					System.out.print(answer.getInt("Count(*)") + "\n");
				}
			}
			answer = stmt.executeQuery(query10b);
			if (answer != null) {
				System.out.print("The number of Charter Schools where the sum of math percentages falls far below and approaches is less than the percentage of passing 2010: ");
				while (answer.next()) {
					System.out.print(answer.getInt("Count(*)") + "\n");
				}
			}
			
			answer = stmt.executeQuery(query11a);
			if (answer != null) {
				System.out.print("\nThe number of Charter Schools for 2010: ");
				while (answer.next()) {
					System.out.print(answer.getInt("Count(*)") + "\n");
				}
			}
			answer = stmt.executeQuery(query11b);
			if (answer != null) {
				System.out.print("The number of Charter Schools where the sum of math percentages falls far below and approaches is less than the percentage of passing 2010: ");
				while (answer.next()) {
					System.out.print(answer.getInt("Count(*)") + "\n");
				}
			}
			
			answer = stmt.executeQuery(query12a);
			if (answer != null) {
				System.out.print("\nThe number of Charter Schools for 2010: ");
				while (answer.next()) {
					System.out.print(answer.getInt("Count(*)") + "\n");
				}
			}
			answer = stmt.executeQuery(query12b);
			if (answer != null) {
				System.out.print("The number of Charter Schools where the sum of math percentages falls far below and approaches is less than the percentage of passing 2010: ");
				while (answer.next()) {
					System.out.print(answer.getInt("Count(*)") + "\n");
				}
			}
			
			answer = stmt.executeQuery(query13a);
			if (answer != null) {
				System.out.print("\nThe number of Charter Schools for 2010: ");
				while (answer.next()) {
					System.out.print(answer.getInt("Count(*)") + "\n");
				}
			}
			answer = stmt.executeQuery(query13b);
			if (answer != null) {
				System.out.print("The number of Charter Schools where the sum of math percentages falls far below and approaches is less than the percentage of passing 2010: ");
				while (answer.next()) {
					System.out.print(answer.getInt("Count(*)") + "\n");
				}
			}
			
			answer = stmt.executeQuery(query14a);
			if (answer != null) {
				System.out.print("\nThe number of Charter Schools for 2010: ");
				while (answer.next()) {
					System.out.print(answer.getInt("Count(*)") + "\n");
				}
			}
			answer = stmt.executeQuery(query14b);
			if (answer != null) {
				System.out.print("The number of Charter Schools where the sum of math percentages falls far below and approaches is less than the percentage of passing 2010: ");
				while (answer.next()) {
					System.out.print(answer.getInt("Count(*)") + "\n");
				}
			}
			System.out.println();

			// Shut down the connection to the DBMS.
			stmt.close();
			dbconn.close();

		} catch (SQLException e) {
			System.err.println("*** SQLException:  " + "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}
    }
    
    /*
     * This method runs the query for option 3 which is to find the top 10 schools with the greatest
     * difference between read passing % and writing passing % for each county in 2014.
     * The query is written as a SQL statement and then ran through the database and then prints out
     * the results.
     * 
     * Parameters: The connection to the database.
     */
    public static void performQuery3(Connection dbconn){
    	
    	//Type the correct query for each county
    	String queryApache = "SELECT FullSchoolName, ReadPass, WritePass, Diff FROM ( "
    							+ "SELECT * FROM cantstoptheunk.aims2014 "
    							+ "WHERE county = 'Apache' AND DIFF IS NOT NULL "
    							+ "ORDER BY ABS(ReadPass-WritePass) DESC) "
    							+ "WHERE ROWNUM <= 10";
    	String queryCochise = "SELECT FullSchoolName, ReadPass, WritePass, Diff FROM ( "
				+ "SELECT * FROM cantstoptheunk.aims2014 "
				+ "WHERE county = 'Cochise' AND DIFF IS NOT NULL "
				+ "ORDER BY ABS(ReadPass-WritePass) DESC) "
				+ "WHERE ROWNUM <= 10";
    	String queryCoconino = "SELECT FullSchoolName, ReadPass, WritePass, Diff FROM ( "
				+ "SELECT * FROM cantstoptheunk.aims2014 "
				+ "WHERE county = 'Coconino' AND DIFF IS NOT NULL "
				+ "ORDER BY ABS(ReadPass-WritePass) DESC) "
				+ "WHERE ROWNUM <= 10";
    	String queryGila = "SELECT FullSchoolName, ReadPass, WritePass, Diff FROM ( "
				+ "SELECT * FROM cantstoptheunk.aims2014 "
				+ "WHERE county = 'Gila' AND DIFF IS NOT NULL "
				+ "ORDER BY ABS(ReadPass-WritePass) DESC) "
				+ "WHERE ROWNUM <= 10";
    	String queryGraham = "SELECT FullSchoolName, ReadPass, WritePass, Diff FROM ( "
				+ "SELECT * FROM cantstoptheunk.aims2014 "
				+ "WHERE county = 'Graham' AND DIFF IS NOT NULL "
				+ "ORDER BY ABS(ReadPass-WritePass) DESC) "
				+ "WHERE ROWNUM <= 10";
    	String queryGreenlee = "SELECT FullSchoolName, ReadPass, WritePass, Diff FROM ( "
				+ "SELECT * FROM cantstoptheunk.aims2014 "
				+ "WHERE county = 'Greenlee' AND DIFF IS NOT NULL "
				+ "ORDER BY ABS(ReadPass-WritePass) DESC) "
				+ "WHERE ROWNUM <= 10";
    	String queryLaPaz = "SELECT FullSchoolName, ReadPass, WritePass, Diff FROM ( "
				+ "SELECT * FROM cantstoptheunk.aims2014 "
				+ "WHERE county = 'La Paz' AND DIFF IS NOT NULL "
				+ "ORDER BY ABS(ReadPass-WritePass) DESC) "
				+ "WHERE ROWNUM <= 10";
    	String queryMaricopa = "SELECT FullSchoolName, ReadPass, WritePass, Diff FROM ( "
				+ "SELECT * FROM cantstoptheunk.aims2014 "
				+ "WHERE county = 'Maricopa' AND DIFF IS NOT NULL "
				+ "ORDER BY ABS(ReadPass-WritePass) DESC) "
				+ "WHERE ROWNUM <= 10";
    	String queryMoHave = "SELECT FullSchoolName, ReadPass, WritePass, Diff FROM ( "
				+ "SELECT * FROM cantstoptheunk.aims2014 "
				+ "WHERE county = 'Mohave' AND DIFF IS NOT NULL "
				+ "ORDER BY ABS(ReadPass-WritePass) DESC) "
				+ "WHERE ROWNUM <= 10";
    	String queryNavajo = "SELECT FullSchoolName, ReadPass, WritePass, Diff FROM ( "
				+ "SELECT * FROM cantstoptheunk.aims2014 "
				+ "WHERE county = 'Navajo' AND DIFF IS NOT NULL "
				+ "ORDER BY ABS(ReadPass-WritePass) DESC) "
				+ "WHERE ROWNUM <= 10";
    	String queryPima = "SELECT FullSchoolName, ReadPass, WritePass, Diff FROM ( "
				+ "SELECT * FROM cantstoptheunk.aims2014 "
				+ "WHERE county = 'Pima' AND DIFF IS NOT NULL "
				+ "ORDER BY ABS(ReadPass-WritePass) DESC) "
				+ "WHERE ROWNUM <= 10";
    	String queryPinal = "SELECT FullSchoolName, ReadPass, WritePass, Diff FROM ( "
				+ "SELECT * FROM cantstoptheunk.aims2014 "
				+ "WHERE county = 'Pinal' AND DIFF IS NOT NULL "
				+ "ORDER BY ABS(ReadPass-WritePass) DESC) "
				+ "WHERE ROWNUM <= 10";
    	String querySantaCruz = "SELECT FullSchoolName, ReadPass, WritePass, Diff FROM ( "
				+ "SELECT * FROM cantstoptheunk.aims2014 "
				+ "WHERE county = 'Santa Cruz' AND DIFF IS NOT NULL "
				+ "ORDER BY ABS(ReadPass-WritePass) DESC) "
				+ "WHERE ROWNUM <= 10";
    	String queryYavapai = "SELECT FullSchoolName, ReadPass, WritePass, Diff FROM ( "
				+ "SELECT * FROM cantstoptheunk.aims2014 "
				+ "WHERE county = 'Yavapai' AND DIFF IS NOT NULL "
				+ "ORDER BY ABS(ReadPass-WritePass) DESC) "
				+ "WHERE ROWNUM <= 10";
    	String queryYuma = "SELECT FullSchoolName, ReadPass, WritePass, Diff FROM ( "
				+ "SELECT * FROM cantstoptheunk.aims2014 "
				+ "WHERE county = 'Yuma' AND DIFF IS NOT NULL "
				+ "ORDER BY ABS(ReadPass-WritePass) DESC) "
				+ "WHERE ROWNUM <= 10";
    	
		// Send the query to the DBMS, and get and display the results
		Statement stmt = null;
		ResultSet answer = null;

		//Need the index and diff variables to keep track of whether we should increment our POS column 
		//since if we have duplicates, no increment should be made
		int index = 1;
		int diff = 0;
		
		try {
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(queryApache);
			
			//Note every subsequent if statement code follows the same logic and idea
			if (answer != null) {
				// Use next() to advance cursor through the result
				// tuples and print their attribute values
				
				//Print county name and the column names
				System.out.println("Apache");
				System.out.println("Pos\tSchool Name                                      ReadPP\tWritePP\tDiff");
				
				//Make sure every row of data is the same length
				String line = "";
				for(int i = 0; i < 100; i++)
					line += "-";
				System.out.println(line);
				
				//Print out the data
				while (answer.next()) {
					String name = answer.getString("FullSchoolName");
					while(name.length()<50)
						name += " ";
					
					//Check if increment needs to be name for the index column
					if (diff > (answer.getInt("Diff")))
						index++;
					
					diff = answer.getInt("Diff");
					System.out.print(index + "\t" + name + answer.getInt("ReadPass") + 
							"\t" + answer.getInt("WritePass") + "\t" + diff + "\n");
				}
			}
			System.out.println();
			
			answer = stmt.executeQuery(queryCochise);
			index = 1;
			if (answer != null) {
				System.out.println("Cochise");
				System.out.println("Pos\tSchool Name                                      ReadPP\tWritePP\tDiff");
				
				String line = "";
				for(int i = 0; i < 100; i++)
					line += "-";
				System.out.println(line);
				while (answer.next()) {
					String name = answer.getString("FullSchoolName");
					while(name.length()<50)
						name += " ";
					if (diff > (answer.getInt("Diff")))
						index++;
					diff = answer.getInt("Diff");
					System.out.print(index + "\t" + name + answer.getInt("ReadPass") + 
							"\t" + answer.getInt("WritePass") + "\t" + diff + "\n");
				}
			}
			System.out.println();
			
			answer = stmt.executeQuery(queryCoconino);
			index = 1;
			if (answer != null) {
				// Use next() to advance cursor through the result
				// tuples and print their attribute values
				System.out.println("Coconino");
				System.out.println("Pos\tSchool Name                                      ReadPP\tWritePP\tDiff");
				
				String line = "";
				for(int i = 0; i < 100; i++)
					line += "-";
				System.out.println(line);
				while (answer.next()) {
					String name = answer.getString("FullSchoolName");
					while(name.length()<50)
						name += " ";
					if (diff > (answer.getInt("Diff")))
							index++;
						diff = answer.getInt("Diff");
						System.out.print(index + "\t" + name + answer.getInt("ReadPass") + 
								"\t" + answer.getInt("WritePass") + "\t" + diff + "\n");
				}
			}
			System.out.println();
			
			answer = stmt.executeQuery(queryGila);
			index = 1;
			if (answer != null) {
				// Use next() to advance cursor through the result
				// tuples and print their attribute values
				System.out.println("Gila");
				System.out.println("Pos\tSchool Name                                      ReadPP\tWritePP\tDiff");
				
				String line = "";
				for(int i = 0; i < 100; i++)
					line += "-";
				System.out.println(line);
				while (answer.next()) {
					String name = answer.getString("FullSchoolName");
					while(name.length()<50)
						name += " ";
					if (diff > (answer.getInt("Diff")))
						index++;
					diff = answer.getInt("Diff");
					System.out.print(index + "\t" + name + answer.getInt("ReadPass") + 
							"\t" + answer.getInt("WritePass") + "\t" + diff + "\n");
				}
			}
			System.out.println();
			
			answer = stmt.executeQuery(queryGraham);
			index = 1;
			if (answer != null) {
				// Use next() to advance cursor through the result
				// tuples and print their attribute values
				System.out.println("Graham");
				System.out.println("Pos\tSchool Name                                      ReadPP\tWritePP\tDiff");
				
				String line = "";
				for(int i = 0; i < 100; i++)
					line += "-";
				System.out.println(line);
				while (answer.next()) {
					String name = answer.getString("FullSchoolName");
					while(name.length()<50)
						name += " ";
					if (diff > (answer.getInt("Diff")))
						index++;
					diff = answer.getInt("Diff");
					System.out.print(index + "\t" + name + answer.getInt("ReadPass") + 
							"\t" + answer.getInt("WritePass") + "\t" + diff + "\n");
				}
			}
			System.out.println();
			
			answer = stmt.executeQuery(queryGreenlee);
			index = 1;
			if (answer != null) {
				// Use next() to advance cursor through the result
				// tuples and print their attribute values
				System.out.println("Greenlee");
				System.out.println("Pos\tSchool Name                                      ReadPP\tWritePP\tDiff");
				
				String line = "";
				for(int i = 0; i < 100; i++)
					line += "-";
				System.out.println(line);
				while (answer.next()) {
					String name = answer.getString("FullSchoolName");
					while(name.length()<50)
						name += " ";
					if (diff > (answer.getInt("Diff")))
						index++;
					diff = answer.getInt("Diff");
					System.out.print(index + "\t" + name + answer.getInt("ReadPass") + 
							"\t" + answer.getInt("WritePass") + "\t" + diff + "\n");
				}
			}
			System.out.println();
			
			answer = stmt.executeQuery(queryLaPaz);
			index = 1;
			if (answer != null) {
				// Use next() to advance cursor through the result
				// tuples and print their attribute values
				System.out.println("La Paz");
				System.out.println("Pos\tSchool Name                                      ReadPP\tWritePP\tDiff");
				
				String line = "";
				for(int i = 0; i < 100; i++)
					line += "-";
				System.out.println(line);
				while (answer.next()) {
					String name = answer.getString("FullSchoolName");
					while(name.length()<50)
						name += " ";
					if (diff > (answer.getInt("Diff")))
						index++;
					diff = answer.getInt("Diff");
					System.out.print(index + "\t" + name + answer.getInt("ReadPass") + 
							"\t" + answer.getInt("WritePass") + "\t" + diff + "\n");
				}
			}
			System.out.println();
			
			answer = stmt.executeQuery(queryMaricopa);
			index = 1;
			if (answer != null) {
				// Use next() to advance cursor through the result
				// tuples and print their attribute values
				System.out.println("Maricopa");
				System.out.println("Pos\tSchool Name                                      ReadPP\tWritePP\tDiff");
				
				String line = "";
				for(int i = 0; i < 100; i++)
					line += "-";
				System.out.println(line);
				while (answer.next()) {
					String name = answer.getString("FullSchoolName");
					while(name.length()<50)
						name += " ";
					if (diff > (answer.getInt("Diff")))
						index++;
					diff = answer.getInt("Diff");
					System.out.print(index + "\t" + name + answer.getInt("ReadPass") + 
							"\t" + answer.getInt("WritePass") + "\t" + diff + "\n");
				}
			}
			System.out.println();
			
			answer = stmt.executeQuery(queryMoHave);
			index = 1;
			if (answer != null) {
				// Use next() to advance cursor through the result
				// tuples and print their attribute values
				System.out.println("Mohave");
				System.out.println("Pos\tSchool Name                                      ReadPP\tWritePP\tDiff");
				
				String line = "";
				for(int i = 0; i < 100; i++)
					line += "-";
				System.out.println(line);
				while (answer.next()) {
					String name = answer.getString("FullSchoolName");
					while(name.length()<50)
						name += " ";
					if (diff > (answer.getInt("Diff")))
						index++;
					diff = answer.getInt("Diff");
					System.out.print(index + "\t" + name + answer.getInt("ReadPass") + 
							"\t" + answer.getInt("WritePass") + "\t" + diff + "\n");
				}
			}
			System.out.println();
			
			answer = stmt.executeQuery(queryNavajo);
			index = 1;
			if (answer != null) {
				// Use next() to advance cursor through the result
				// tuples and print their attribute values
				System.out.println("Navajo");
				System.out.println("Pos\tSchool Name                                      ReadPP\tWritePP\tDiff");
				
				String line = "";
				for(int i = 0; i < 100; i++)
					line += "-";
				System.out.println(line);
				while (answer.next()) {
					String name = answer.getString("FullSchoolName");
					while(name.length()<50)
						name += " ";
					if (diff > (answer.getInt("Diff")))
						index++;
					diff = answer.getInt("Diff");
					System.out.print(index + "\t" + name + answer.getInt("ReadPass") + 
							"\t" + answer.getInt("WritePass") + "\t" + diff + "\n");
				}
			}
			System.out.println();
			
			answer = stmt.executeQuery(queryPima);
			index = 1;
			if (answer != null) {
				// Use next() to advance cursor through the result
				// tuples and print their attribute values
				System.out.println("Pima");
				System.out.println("Pos\tSchool Name                                      ReadPP\tWritePP\tDiff");
				
				String line = "";
				for(int i = 0; i < 100; i++)
					line += "-";
				System.out.println(line);
				while (answer.next()) {
					String name = answer.getString("FullSchoolName");
					while(name.length()<50)
						name += " ";
					if (diff > (answer.getInt("Diff")))
						index++;
					diff = answer.getInt("Diff");
					System.out.print(index + "\t" + name + answer.getInt("ReadPass") + 
							"\t" + answer.getInt("WritePass") + "\t" + diff + "\n");
				}
			}
			System.out.println();
			
			answer = stmt.executeQuery(queryPinal);
			index = 1;
			if (answer != null) {
				// Use next() to advance cursor through the result
				// tuples and print their attribute values
				System.out.println("Pinal");
				System.out.println("Pos\tSchool Name                                      ReadPP\tWritePP\tDiff");
				
				String line = "";
				for(int i = 0; i < 100; i++)
					line += "-";
				System.out.println(line);
				while (answer.next()) {
					String name = answer.getString("FullSchoolName");
					while(name.length()<50)
						name += " ";
					if (diff > (answer.getInt("Diff")))
						index++;
					diff = answer.getInt("Diff");
					System.out.print(index + "\t" + name + answer.getInt("ReadPass") + 
							"\t" + answer.getInt("WritePass") + "\t" + diff + "\n");
				}
			}
			System.out.println();
			
			answer = stmt.executeQuery(querySantaCruz);
			index = 1;
			if (answer != null) {
				// Use next() to advance cursor through the result
				// tuples and print their attribute values
				System.out.println("Santa Cruz");
				System.out.println("Pos\tSchool Name                                      ReadPP\tWritePP\tDiff");
				
				String line = "";
				for(int i = 0; i < 100; i++)
					line += "-";
				System.out.println(line);
				while (answer.next()) {
					String name = answer.getString("FullSchoolName");
					while(name.length()<50)
						name += " ";
					if (diff > (answer.getInt("Diff")))
						index++;
					diff = answer.getInt("Diff");
					System.out.print(index + "\t" + name + answer.getInt("ReadPass") + 
							"\t" + answer.getInt("WritePass") + "\t" + diff + "\n");
				}
			}
			System.out.println();
			
			answer = stmt.executeQuery(queryYavapai);
			index = 1;
			if (answer != null) {
				// Use next() to advance cursor through the result
				// tuples and print their attribute values
				System.out.println("PimYavapai");
				System.out.println("Pos\tSchool Name                                      ReadPP\tWritePP\tDiff");
				
				String line = "";
				for(int i = 0; i < 100; i++)
					line += "-";
				System.out.println(line);
				while (answer.next()) {
					String name = answer.getString("FullSchoolName");
					while(name.length()<50)
						name += " ";
					if (diff > (answer.getInt("Diff")))
						index++;
					diff = answer.getInt("Diff");
					System.out.print(index + "\t" + name + answer.getInt("ReadPass") + 
							"\t" + answer.getInt("WritePass") + "\t" + diff + "\n");
				}
			}
			System.out.println();
			
			answer = stmt.executeQuery(queryYuma);
			index = 1;
			if (answer != null) {
				// Use next() to advance cursor through the result
				// tuples and print their attribute values
				System.out.println("Yuma");
				System.out.println("Pos\tSchool Name                                      ReadPP\tWritePP\tDiff");
				
				String line = "";
				for(int i = 0; i < 100; i++)
					line += "-";
				System.out.println(line);
				while (answer.next()) {
					String name = answer.getString("FullSchoolName");
					while(name.length()<50)
						name += " ";
					if (diff > (answer.getInt("Diff")))
						index++;
					diff = answer.getInt("Diff");
					System.out.print(index + "\t" + name + answer.getInt("ReadPass") + 
							"\t" + answer.getInt("WritePass") + "\t" + diff + "\n");
				}
			}
			System.out.println();
			// Shut down the connection to the DBMS.
			stmt.close();
			dbconn.close();

		} catch (SQLException e) {
			System.err.println("*** SQLException:  " + "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}
    }

    /*
     * This method runs the query for option 4 which is to find the top 3 math percent passing schools
     * in a given county in 2014.
     * The query is written as a SQL statement and then ran through the database and then prints out
     * the results.
     * 
     * Parameters: The connection to the database, and the county.
     */
    public static void performQuery4(Connection dbconn, int county){
    	// Send the query to the DBMS, and get and display the results
    			Statement stmt = null;
    			ResultSet answer = null;
    			String query = "";

    			//Create the correct query statement depending on what county the user wants 
    			if(county == 1){
    				query = "SELECT FullSchoolName, MathPercentPassing, ReadPass, WritePass FROM ( "
    						+ "SELECT * FROM cantstoptheunk.aims2014 "
    						+ "WHERE county = 'Apache' AND MathPercentPassing IS NOT NULL "
    						+ "ORDER BY MathPercentPassing DESC "
    						+ ") "
    						+ "WHERE ROWNUM <= 3";
		    	}
    			else if(county == 2){
    				query = "SELECT FullSchoolName, MathPercentPassing, ReadPass, WritePass FROM ( "
    						+ "SELECT * FROM cantstoptheunk.aims2014 "
    						+ "WHERE county = 'Cochise' AND MathPercentPassing IS NOT NULL "
    						+ "ORDER BY MathPercentPassing DESC "
    						+ ") "
    						+ "WHERE ROWNUM <= 3";
		    	}
    			else if(county == 3){
    				query = "SELECT FullSchoolName, MathPercentPassing, ReadPass, WritePass FROM ( "
    						+ "SELECT * FROM cantstoptheunk.aims2014 "
    						+ "WHERE county = 'Coconino' AND MathPercentPassing IS NOT NULL "
    						+ "ORDER BY MathPercentPassing DESC "
    						+ ") "
    						+ "WHERE ROWNUM <= 3";
		    	}
    			else if(county == 4){
    				query = "SELECT FullSchoolName, MathPercentPassing, ReadPass, WritePass FROM ( "
    						+ "SELECT * FROM cantstoptheunk.aims2014 "
    						+ "WHERE county = 'Gila' AND MathPercentPassing IS NOT NULL "
    						+ "ORDER BY MathPercentPassing DESC "
    						+ ") "
    						+ "WHERE ROWNUM <= 3";
		    	}
    			else if(county == 5){
    				query = "SELECT FullSchoolName, MathPercentPassing, ReadPass, WritePass FROM ( "
    						+ "SELECT * FROM cantstoptheunk.aims2014 "
    						+ "WHERE county = 'Graham' AND MathPercentPassing IS NOT NULL "
    						+ "ORDER BY MathPercentPassing DESC "
    						+ ") "
    						+ "WHERE ROWNUM <= 3";
		    	}
    			else if(county == 6){
    				query = "SELECT FullSchoolName, MathPercentPassing, ReadPass, WritePass FROM ( "
    						+ "SELECT * FROM cantstoptheunk.aims2014 "
    						+ "WHERE county = 'Greenlee' AND MathPercentPassing IS NOT NULL "
    						+ "ORDER BY MathPercentPassing DESC "
    						+ ") "
    						+ "WHERE ROWNUM <= 3";
		    	}
    			else if(county == 7){
    				query = "SELECT FullSchoolName, MathPercentPassing, ReadPass, WritePass FROM ( "
    						+ "SELECT * FROM cantstoptheunk.aims2014 "
    						+ "WHERE county = 'La Paz' AND MathPercentPassing IS NOT NULL "
    						+ "ORDER BY MathPercentPassing DESC "
    						+ ") "
    						+ "WHERE ROWNUM <= 3";
		    	}
    			else if(county == 8){
    				query = "SELECT FullSchoolName, MathPercentPassing, ReadPass, WritePass FROM ( "
    						+ "SELECT * FROM cantstoptheunk.aims2014 "
    						+ "WHERE county = 'Maricopa' AND MathPercentPassing IS NOT NULL "
    						+ "ORDER BY MathPercentPassing DESC "
    						+ ") "
    						+ "WHERE ROWNUM <= 3";
		    	}
    			else if(county == 9){
    				query = "SELECT FullSchoolName, MathPercentPassing, ReadPass, WritePass FROM ( "
    						+ "SELECT * FROM cantstoptheunk.aims2014 "
    						+ "WHERE county = 'Mohave' AND MathPercentPassing IS NOT NULL "
    						+ "ORDER BY MathPercentPassing DESC "
    						+ ") "
    						+ "WHERE ROWNUM <= 3";
		    	}
    			else if(county == 10){
    				query = "SELECT FullSchoolName, MathPercentPassing, ReadPass, WritePass FROM ( "
    						+ "SELECT * FROM cantstoptheunk.aims2014 "
    						+ "WHERE county = 'Navajo' AND MathPercentPassing IS NOT NULL "
    						+ "ORDER BY MathPercentPassing DESC "
    						+ ") "
    						+ "WHERE ROWNUM <= 3";
		    	}
    			else if(county == 11){
    				query = "SELECT FullSchoolName, MathPercentPassing, ReadPass, WritePass FROM ( "
    						+ "SELECT * FROM cantstoptheunk.aims2014 "
    						+ "WHERE county = 'Pima' AND MathPercentPassing IS NOT NULL "
    						+ "ORDER BY MathPercentPassing DESC "
    						+ ") "
    						+ "WHERE ROWNUM <= 3";
		    	}
    			else if(county == 12){
    				query = "SELECT FullSchoolName, MathPercentPassing, ReadPass, WritePass FROM ( "
    						+ "SELECT * FROM cantstoptheunk.aims2014 "
    						+ "WHERE county = 'Pinal' AND MathPercentPassing IS NOT NULL "
    						+ "ORDER BY MathPercentPassing DESC "
    						+ ") "
    						+ "WHERE ROWNUM <= 3";
		    	}
    			else if(county == 13){
    				query = "SELECT FullSchoolName, MathPercentPassing, ReadPass, WritePass FROM ( "
    						+ "SELECT * FROM cantstoptheunk.aims2014 "
    						+ "WHERE county = 'Santa Cruz' AND MathPercentPassing IS NOT NULL "
    						+ "ORDER BY MathPercentPassing DESC "
    						+ ") "
    						+ "WHERE ROWNUM <= 3";
		    	}
    			else if(county == 14){
    				query = "SELECT FullSchoolName, MathPercentPassing, ReadPass, WritePass FROM ( "
    						+ "SELECT * FROM cantstoptheunk.aims2014 "
    						+ "WHERE county = 'Yavapai' AND MathPercentPassing IS NOT NULL "
    						+ "ORDER BY MathPercentPassing DESC "
    						+ ") "
    						+ "WHERE ROWNUM <= 3";
		    	}    			
    			else if(county == 15){
    				query = "SELECT FullSchoolName, MathPercentPassing, ReadPass, WritePass FROM ( "
    						+ "SELECT * FROM cantstoptheunk.aims2014 "
    						+ "WHERE county = 'Yuma' AND MathPercentPassing IS NOT NULL "
    						+ "ORDER BY MathPercentPassing DESC "
    						+ ") "
    						+ "WHERE ROWNUM <= 3";
		    	}
    			//Check for valid user input
    			else{
    				System.out.println("Cannot find county.");
    				return;
    			}
    			
    			//Get the name of the school, math passing %, read %, write % for the top 3 math passing schools and print it out
    			try {
    				stmt = dbconn.createStatement();
    				answer = stmt.executeQuery(query);

    				if (answer != null) {
    					// Use next() to advance cursor through the result
    					// tuples and print their attribute values    					
    					while(answer.next()){
    						String name = answer.getString("FullSchoolName");
    						int mathP = answer.getInt("MathPercentPassing");
    						int readP = answer.getInt("ReadPass");
    						int writeP = answer.getInt("WritePass");
   						
    						System.out.println("School name: " + name);
    						System.out.println("Math passing percent: " + mathP);
    						System.out.println("Read passing percent: " + readP);
    						System.out.println("Writing passing percent: " + writeP);
    						System.out.println();
    					}
    				}

    				// Shut down the connection to the DBMS.
    				stmt.close();
    				dbconn.close();

    			} catch (SQLException e) {
    				System.err.println("*** SQLException:  " + "Could not fetch query results.");
    				System.err.println("\tMessage:   " + e.getMessage());
    				System.err.println("\tSQLState:  " + e.getSQLState());
    				System.err.println("\tErrorCode: " + e.getErrorCode());
    				System.exit(-1);
    			}
    }
}
