/*
 * Name: Ryunki Song
 * Class: CS460 Database
 * Professor: McCann
 * TA: Jacob, Yawen
 * Due Date: 4/5/2017 3:30 PM
 * Project 3
 * 
 * 
 * Description: This file takes in the AIMS .CSV files and converts them into SQL instruction statements. 
 * The SQL instruction statements mainly involve creating the table, and inserting data into the table. 
 * It runs without any parameters and will automatically create the .SQL files.
 * Once the .SQL files are made, then they can be run in the oracle database so that all the information is now
 * in the oracle database. 
 * 
 * NOTE: This file also takes the original AIMS .CSV files and scrubs them so that the newly scrubbed .CSV file
 * can now be used to create the SQL script. Also note that some manual scrubbing through Excel was conducted in conjuction
 * with this file. 
 */



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;


public class SQLConvert {

	public static void main(String[] args){

		//Check if user has the proper input
		if(args.length > 0){
			System.err.println("No parameter arguments needed.");				
			System.exit(-1);
		}
		
		//CSV file names from local directory
		String csvAIMS2010 = "./AIMS/csv/aims-csv2010.csv";
		String csvAIMS2011 = "./AIMS/csv/aims-csv2011.csv"; 
		String csvAIMS2012 = "./AIMS/csv/aims-csv2012.csv"; 
		String csvAIMS2013 = "./AIMS/csv/aims-csv2013.csv"; 
		String csvAIMS2014 = "./AIMS/csv/aims-csv2014.csv"; 

		//Scrubbed CSV file names to be created
		String scrubbed2010 = "./AIMS/csv/scrubbed"+csvAIMS2010.substring(11);
		String scrubbed2011 = "./AIMS/csv/scrubbed"+csvAIMS2011.substring(11);
		String scrubbed2012 = "./AIMS/csv/scrubbed"+csvAIMS2012.substring(11);
		String scrubbed2013 = "./AIMS/csv/scrubbed"+csvAIMS2013.substring(11);
		String scrubbed2014 = "./AIMS/csv/scrubbed"+csvAIMS2014.substring(11);
		
		//Scrubbed CSV file names to be created
		String finalcsv2010 = "./AIMS/csv/finalcsv2010.csv";
		String finalcsv2011 = "./AIMS/csv/finalcsv2011.csv";
		String finalcsv2012 = "./AIMS/csv/finalcsv2012.csv";
		String finalcsv2013 = "./AIMS/csv/finalcsv2013.csv";
		String finalcsv2014 = "./AIMS/csv/finalcsv2014.csv";
		
		String sqlAIMS2010 = "./AIMS/csv/sql2010.sql";
		String sqlAIMS2011 = "./AIMS/csv/sql2011.sql";
		String sqlAIMS2012 = "./AIMS/csv/sql2012.sql";
		String sqlAIMS2013 = "./AIMS/csv/sql2013.sql";
		String sqlAIMS2014 = "./AIMS/csv/sql2014.sql";
		
		//Call method to clean data and file
		scrubCSV(csvAIMS2010, scrubbed2010);
		scrubCSV(csvAIMS2011, scrubbed2011);
		scrubCSV(csvAIMS2012, scrubbed2012);
		scrubCSV(csvAIMS2013, scrubbed2013);
		scrubCSV(csvAIMS2014, scrubbed2014);
		
		//Call method to create the SQL script
		createSQLScript(finalcsv2010, sqlAIMS2010);
		createSQLScript(finalcsv2011, sqlAIMS2011);
		createSQLScript(finalcsv2012, sqlAIMS2012);
		createSQLScript(finalcsv2013, sqlAIMS2013);
		createSQLScript(finalcsv2014, sqlAIMS2014);
	}
	
	/*
	 * This method cleans the data in the file. It scrubs and removes any lines containing an
	 * empty field in the AIMS data and stores the rest of the data in a new csv file.
	 */
	public static void scrubCSV(String fileName, String newFileName){		
		String 	line  	= "";													//String var to contain the line of data read in	
		File 	newFile = new File (newFileName); 								//Clean/Scrubbed file to replaced the old one
		
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			
			RandomAccessFile dataStream = new RandomAccessFile(newFile,"rw");	//Access the new CSV file
	        	
			// Check for an empty file
			if (!br.ready()) {
				System.out.println("File is Empty");
				br.close();
			}
			
			// Otherwise check if any lines of data contains any empty fields
			else {
				System.out.println("The following lines were removed...");		//Msg to user
				int count = 0;													//Counts how many lines will be in the new file
				while ((line = br.readLine()) != null) {						//Read all the lines in the AIMS file
					boolean hasEmptyField = hasEmptyField(line.toCharArray());	//Check if the line has an empty field
					if (!hasEmptyField) {										//If not then place the line in the new file
						line += "\r\n";
						dataStream.writeBytes(line);
						count++;
					} else {
						System.out.println(line);								//Otherwise print the line we scrubbed
					}
				}
				dataStream.close();
				System.out.println("\nThe scrubbed file now has " + count + " lines of data");	//Msg to user
				System.out.println("The scrubbed file name is called " + newFileName);			//Msg to user

			}

		}
		catch (IOException e) {
	           System.out.println("Unable to scrub CSV file.");
	           System.exit(-1);
	    }
	}
	
	/*
	 * Determines if a line of data contains an empty field. This is needed so that we know
	 * which lines to remove from the AIMS data.
	 */
	private static boolean hasEmptyField(char[] array){
		for(int i=0; i<array.length-1; i++)
			if(array[i] == ',' && array[i+1] == ',')
				return true;
		return false;
	}
	
	
	/*
	 * This method creates the SQL script. The script will create a new table for each AIMS year of data
	 * and will contain the fields and data in the CSV files. 
	 * 
	 * Once the SQL script is created, it can be run in oracle using the "@file.sql" command. This will
	 * physically create the table using the script created.
	 */
	public static void createSQLScript(String csvFile, String sqlFile){
		String 	line  	= "";													//String var to contain the line of data read in	
		File 	newFile = new File (sqlFile); 									//SQL file to be created
		
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			
			RandomAccessFile dataStream = new RandomAccessFile(newFile,"rw");	//Access the new SQL file
			System.out.println("\nCreating SQL script...");						//Msg to user
			
			String tableName = "aims" + sqlFile.substring(14,18);				//SQL table name
			System.out.println("The table " + tableName + " has been created");	//Msg to user
			
			
			//Create SQL Permissions statement
			String grant = "GRANT SELECT ON cantstoptheunk." + tableName + " TO cantstoptheunk, mccann, yawenchen, jacobcombs;\n";
			dataStream.writeBytes(grant);
			
			//Create SQL Drop table statement
			String drop = "DROP TABLE cantstoptheunk."+ tableName + ";\n";
			dataStream.writeBytes(drop);
			
			//Create SQL Create table statement
			String create = "CREATE TABLE cantstoptheunk."+tableName + " (\n";
			dataStream.writeBytes(create);
			
			//Get all the column names of the AIMS .CSV file
			String attributes = "\tYear varchar2(4),\n "
					  		  + "\tState varchar2(7),\n"
							  + "\tCounty varchar2(15),\n"
					  		  + "\tLEAID varchar2(7),\n"					
					  		  + "\tLEANum varchar2(14),\n"		
					  		  + "\tLEAName varchar2(30),\n"					
							  + "\tID varchar2(7) NOT NULL PRIMARY KEY,\n"
					  		  + "\tCTDSNum varchar2(14),\n"					
							  + "\tFullSchoolName varchar2(50),\n"
							  + "\tShortSchoolName varchar2(30),\n"
							  + "\tIsCharter varchar2(1),\n"
							  + "\tMathMean Integer,\n"
							  + "\tMathBelow integer,\n"
							  + "\tMathApproaches integer,\n"
							  + "\tMathMeets Integer,\n"
							  + "\tMathExceeds Integer,\n"
							  + "\tMathPercentPassing integer,\n"
							  + "\tReadMean Integer,\n"
							  + "\tReadBelow Integer,\n"
							  + "\tReadApproaches Integer,\n"
							  + "\tReadMeets Integer,\n"
							  + "\tReadExceeds Integer,\n"
							  + "\tReadPass integer,\n"
							  + "\tWriteMean Integer,\n"
							  + "\tWriteBelow Integer,\n"
							  + "\tWriteApproaches Integer,\n"
							  + "\tWriteMeets Integer,\n"
							  + "\tWriteExceeds Integer,\n"
							  + "\tWritePass integer,\n"
							  + "\tSciMean Integer,\n"
							  + "\tSciBelow Integer,\n"
							  + "\tSciApproaches Integer,\n"
							  + "\tSciMeets Integer,\n"
							  + "\tSciExceeds Integer,\n"
							  + "\tSciPassing Integer,\n"
							  + "\tMathBelowApproach integer,\n"
							  + "\tDiff integer\n"
							  + ");\n\n";
			dataStream.writeBytes(attributes);
			
			//Crete SQL index statement
			String indexDrop = "DROP INDEX indexID"+";\n";
			String indexCreate = "CREATE INDEX indexID ON cantstoptheunk." + tableName + " (ID);\n\n";
			dataStream.writeBytes(indexDrop);
			dataStream.writeBytes(indexCreate);
			
			// Check for an empty file
			if (br.readLine() == null) {
				System.out.println("File is Empty");
				br.close();
			}
			// Otherwise check if any lines of data contains any empty fields
			else {
				int count = 0;													//Counts how many lines will be in the new file
				while ((line = br.readLine()) != null) {						//Read all the lines in the CSV file
					String splitBy 	= ",";
					String[] fields = line.split(splitBy);						//Get fields from the line and insert them into an array
					
					//Fields of our table
					String yearStr 	= fields[0]; 
					String state 	= fields[1];
					String county 	= fields[2]; 
					String leaID	= fields[3];
					String leaNum	= fields[4];
					String leaName	= fields[5];
					String idStr	= fields[6];
					String ctdsNum	= fields[7];
					String nameFull	= fields[8];
					String name		= fields[8];
					String isCharter = fields[9];
					String mathMean = fields[10];
					String mathBelow = fields[11];
					String mathApproaches = fields[12];
					String mathMeets = fields[13];
					String mathExceeds = fields[14];
					String mathPassing = fields[15];
					String readingMean = fields[16];
					String readingBelow = fields[17];
					String readingApproaches = fields[18];
					String readingMeets = fields[19];
					String readingExceeds = fields[20];
					String readingPassing = fields[21];
					String writingMean = fields[22];
					String writingBelow = fields[23];
					String writingApproaches = fields[24];
					String writingMeets = fields[25];
					String writingExceeds = fields[26];
					String writingPassing = fields[27];
					String scienceMean = fields[28];
					String scienceBelow = fields[29];
					String scienceApproaches = fields[30];
					String scienceMeets = fields[31];
					String scienceExceeds = fields[32];
					String sciencePassing = fields[33];
					String mathBelowApproach = fields[34];
					String diff = fields[35];
					

					//Do some checking to make sure each line of data don't exceed the max amount of characters
					if(county.length() > 15)
						county = county.substring(0, 15);
					if(nameFull.length() > 50)
						nameFull = nameFull.substring(0, 50);
					if(name.length() > 30)
						name = name.substring(0, 30);
					if(leaName.length() > 30)
						leaName = leaName.substring(0, 30);
					
					
					//Do some checking to make sure we input * from the AIMS database as null fields
					if(yearStr.equals("*"))
						yearStr = "null";
					if(county.equals("*"))
						county = "null";
					if(idStr.equals("*"))
						idStr = "null";
					if(name.equals("*"))
						name = "null";
					if(isCharter.equals("*"))
						isCharter = "null";
					
					//Null values for math
					Integer mathMeanInt = null;
					if(!mathMean.equals("*"))
						mathMeanInt = Integer.parseInt(mathMean);
					Integer mathBelowInt = null;
					if(!mathBelow.equals("*"))
						mathBelowInt = Integer.parseInt(mathBelow);
					Integer mathApproachesInt = null;
					if(!mathApproaches.equals("*"))
						mathApproachesInt = Integer.parseInt(mathApproaches);
					Integer mathMeetsInt = null;
					if(!mathMeets.equals("*"))
						mathMeetsInt = Integer.parseInt(mathMeets);
					Integer mathExceedsInt = null;
					if(!mathExceeds.equals("*"))
						mathExceedsInt = Integer.parseInt(mathExceeds);
					Integer mathPassingInt = null;
					if(!mathPassing.equals("*"))
						mathPassingInt = Integer.parseInt(mathPassing);

					//Null values for Reading
					Integer readingMeanInt = null;
					if(!readingMean.equals("*"))
						readingMeanInt = Integer.parseInt(readingMean);
					Integer readingBelowInt = null;
					if(!readingBelow.equals("*"))
						readingBelowInt = Integer.parseInt(readingBelow);
					Integer readingApproachesInt = null;
					if(!readingApproaches.equals("*"))
						readingApproachesInt = Integer.parseInt(readingApproaches);
					Integer readingMeetsInt = null;
					if(!readingMeets.equals("*"))
						readingMeetsInt = Integer.parseInt(readingMeets);
					Integer readingExceedsInt = null;
					if(!readingExceeds.equals("*"))
						readingExceedsInt = Integer.parseInt(readingExceeds);
					Integer readingPassingInt = null;
					if(!readingPassing.equals("*"))
						readingPassingInt = Integer.parseInt(readingPassing);
					
					//Null values for Writing
					Integer writingMeanInt = null;
					if(!writingMean.equals("*"))
						writingMeanInt = Integer.parseInt(writingMean);
					Integer writingBelowInt = null;
					if(!writingBelow.equals("*"))
						writingBelowInt = Integer.parseInt(writingBelow);
					Integer writingApproachesInt = null;
					if(!writingApproaches.equals("*"))
						writingApproachesInt = Integer.parseInt(writingApproaches);
					Integer writingMeetsInt = null;
					if(!writingMeets.equals("*"))
						writingMeetsInt = Integer.parseInt(writingMeets);
					Integer writingExceedsInt = null;
					if(!writingExceeds.equals("*"))
						writingExceedsInt = Integer.parseInt(writingExceeds);
					Integer writePassingInt = null;
					if(!writingPassing.equals("*"))
						writePassingInt = Integer.parseInt(writingPassing);
					
					//Null values for Science
					Integer scienceMeanInt = null;
					if(!scienceMean.equals("*"))
						scienceMeanInt = Integer.parseInt(scienceMean);
					Integer scienceBelowInt = null;
					if(!scienceBelow.equals("*"))
						scienceBelowInt = Integer.parseInt(scienceBelow);
					Integer scienceApproachesInt = null;
					if(!scienceApproaches.equals("*"))
						scienceApproachesInt = Integer.parseInt(scienceApproaches);
					Integer scienceMeetsInt = null;
					if(!scienceMeets.equals("*"))
						scienceMeetsInt = Integer.parseInt(scienceMeets);
					Integer scienceExceedsInt = null;
					if(!scienceExceeds.equals("*"))
						scienceExceedsInt = Integer.parseInt(scienceExceeds);
					Integer sciencePassingInt = null;
					if(!sciencePassing.equals("*"))
						sciencePassingInt = Integer.parseInt(sciencePassing);
					
					Integer mathBelowApproachInt = null;
					if(!mathBelowApproach.equals("*"))
						mathBelowApproachInt = Integer.parseInt(mathBelowApproach);
					
					Integer diffInt = null;
					if(!diff.equals("*"))
						diffInt = Integer.parseInt(diff);
					
					String insert = "INSERT INTO cantstoptheunk." + tableName + " (Year, State, County, LEAID, LEANum, LEAName, ID, CTDSNum, FullSchoolName, ShortSchoolName, IsCharter, MathMean, MathBelow, MathApproaches, MathMeets, MathExceeds, MathPercentPassing, ReadMean, ReadBelow, ReadApproaches, ReadMeets, ReadExceeds, ReadPass, WriteMean, WriteBelow, WriteApproaches, WriteMeets, WriteExceeds, WritePass, SciMean, SciBelow, SciApproaches, SciMeets, SciExceeds, SciPassing, MathBelowApproach, Diff)"
								  + "\n\tVALUES ('" + yearStr + "', '" + state + "', '" + county + "', '" + leaID + "', '" + leaNum + "', '" +  leaName + "', '" + idStr  + "', '" + ctdsNum + "', '" + nameFull + "', '" + name + "', '" + isCharter + "', " 
								  	+ mathMeanInt + ", " + mathBelowInt + ", " + mathApproachesInt + ", " + mathMeetsInt + ", " + mathExceedsInt + ", " + mathPassingInt + ", " + readingMeanInt + ", " + readingBelowInt + ", " + readingApproachesInt + ", " + readingMeetsInt + ", " + readingExceedsInt + ", " + readingPassingInt + ", " + writingMeanInt + ", " + writingBelowInt + ", " + writingApproachesInt + ", " + writingMeetsInt + ", " + writingExceedsInt + ", " +  writePassingInt + ", " + scienceMeanInt + ", " + scienceBelowInt + ", " + scienceApproachesInt + ", " + scienceMeetsInt + ", " + scienceExceedsInt + ", " + sciencePassingInt + ", " + mathBelowApproachInt + ", " + diffInt + ");\n";
					dataStream.writeBytes(insert);
					
//					System.out.println("DEBUG: " + year + " " + county + " " + id + " " + name);//DEBUG	
					count++;
					
				}
				dataStream.close();
				System.out.println("\nThe SQL file now has " + count + " insert instructions");	//Msg to user
				System.out.println("The SQL file name is called " + sqlFile);					//Msg to user
			}

		}
		catch (IOException e) {
	           System.out.println("Unable to create SQL script.");
	           System.exit(-1);
	    }
	}
}






