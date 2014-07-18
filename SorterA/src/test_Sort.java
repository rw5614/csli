import java.util.ArrayList;
import java.util.Scanner;
public class test_Sort{
	//Standard In
	public static Scanner sc = new Scanner(System.in);
	//Variables
	public static ArrayList<String> vars = new ArrayList<String>();
	public static String keyEv = "";
	public static ArrayList<String> evs = new ArrayList<String>();
	
	public static String input = "[DEFAULT]"; //Don't like this, but we can fix this later
	
	//Methods
	public static String convert(String input){
		//buildVars(input); //Automatic call to Build the Var Array
		String output = "Out";
		return output;
	}
	public static void clearPast(){
		keyEv = "";
		vars.clear();
		evs.clear();
	}
	public static void buildVars(String s){// Formats the input to "Pretty Printed" format
		s = touchInput(s);
		s = keyOut(s);
		while (s.length() > 0){//Deals with the rest of the stuff
			for (int i = 0; i < s.length()-1; i++){//find the next space
				if (s.substring(i, i+2).equals("] ")){//s.substring(i, i+2).equals(" ")){ // a space is found
					//System.out.println("S is now: " + s); //Debug Purposes Only
					//Generalized Addition vars.add(s.substring(0, i+1)); //Adds NewFound fragment to Vars 
					//Revision: Sorted Addition
					if (s.substring(0,1).equals("x")){
						vars.add(s.substring(0, i+1));
					}
					else if (s.substring(0,1).equals("e")){
						evs.add(s.substring(0, i+1));
					}
					s = s.substring(i+2, s.length());
					break;
				}
			}
		}
	}
	public static String keyOut(String s){
		int end = 0;
		for (int i = 0; i < s.length(); i++){
			if (s.substring(i, i+1).equals(" ")){//s.substring(i, i+2).equals(" ")){ // a space is found
				System.out.println("Keyed: <" + s.substring(0, i) + ">"); //Debug Purposes Only
				keyEv = s.substring(0, i); //Identifies and sets the Key Variable
				end = i; //makes sure already parsed stuff is not reparsed
				break;
			}
		}
		return s.substring(end+1); //Gets rid of the space as well
	}
	public static String touchInput(String s){//Removes Curly Brackets from Input
		//System.out.println("Touched Input: " + s.substring(1, s.length()-1));
		return s.substring(1, s.length()-1);
	}
	public static void touchVars(ArrayList<String> alvar){//Touches if not touched originally from input
		alvar.set(alvar.size()-1, alvar.get(alvar.size()-1).substring(0, alvar.get(alvar.size()-1).length()-2));
		alvar.set(0, alvar.get(0).substring(1, alvar.get(0).length()));
	}
	public static void printArrList(ArrayList<String> al){//Convenient ArrayList Printing for Debug
		System.out.println("<Start of ArrayList>");
		for (String s: al){
			System.out.println(s);
		}
		System.out.println("<End of ArrayList>");
	}
	
	//Compute Triple
	
	public static void computeTriple(){
		//NOT SURE IF THIS REACHES CORRECTLY
		int kloc = -1; //Find eventuality that matches the key
		for (int i = 0; i < evs.size(); i++){
			  if (evs.get(i).contains(keyEv)){
			    kloc = i;
			  }
		}
		
		//Locate square bracket location for the key eventuality
		int bstart = evs.get(kloc).indexOf("[");
		int bend = evs.get(kloc).indexOf("]");
		String relation = evs.get(kloc).substring(bstart + 1, bend); // Not Inclusive of Bracket
		System.out.println("Relation: " + relation); //For Debug

		//For each argument detected, get the range in string
		
		//FIND ARGUMENT 1
		String arg1 = "[ARG1 Placeholder]";		
		int col1 = relation.indexOf(":"); //Finds first colon as marker for first argument;
		int x1 = relation.indexOf("x");
		String inst1 = relation.substring(x1, col1 +1); //code for first instance
		System.out.println("Searching for " + inst1);
		//Search Instances array for inst1;
		
		//NOT SURE IF THIS GETS CORRECTLY
		int xloc1 = -1; //Find instance that matches the key
		for (int i = 0; i < vars.size(); i++){
			  if (vars.get(i).contains(inst1)){
			    xloc1 = i;
			  }
		}
		arg1 = getInstString(xloc1).toLowerCase();
		System.out.println("ARG 1: " + arg1);
		
		
		//FIND ARGUMENT 2
		String arg2 = "[ARG2 Placeholder]";		
		int col2 = relation.indexOf(":", col1 + 1); //Finds first colon as marker for first argument;
		int x2 = relation.indexOf("x", x1 + 1);
		String inst2 = relation.substring(x2, col2 +1); //code for first instance
		System.out.println("Searching for " + inst2);
		//Search Instances array for inst2;
		
		//NOT SURE IF THIS GETS CORRECTLY
		int xloc2 = -1; //Find instance that matches the key
		for (int i = 0; i < vars.size(); i++){
			  if (vars.get(i).contains(inst2)){
			    xloc2 = i;
			  }
		}
		arg2 = getInstString(xloc2).toLowerCase();
		System.out.println("ARG 2: " + arg2);
		
		
		//Get the Key
		//For first argument, get relation, set it as the key, erasing middle
		String argk = "[BLANKKEY]";
		int icomma = relation.indexOf(",");
		argk = relation.substring(col1 + 1, icomma); //From Colon to Comma (Exclusive)
		//Treat the Argk
		argk = argk.toUpperCase();
		//QUESTION: Do we need to treat the n value? What are the related inputs referenced w/ SV?
		
		//Get elements existing at appropriate line number ranges
		
		String postparse = arg1 + " " + argk + " " + arg2; //Prints the Isolated Triple
		System.out.println(postparse);
	}
	
	public static String getInstString(int loc){ //gets appropriate x value from array
		String preparse = vars.get(loc);
		String result = "[RESULT PLACEHOLDER]";
		//GET ANGLE BRACKET INDEXES, AND GRAB THOSE WORDS!
		int indl = preparse.indexOf("<");
		int indcol = preparse.indexOf(":", indl);
		int indr = preparse.indexOf(">", indcol);
		
		int left = Integer.parseInt(preparse.substring(indl + 1, indcol));
		int right = Integer.parseInt(preparse.substring(indcol + 1, indr));
		
		//Now, Go back to the Sentence and Find the Actual stuff!
		result = input.substring(left, right); //Takes away the extra space
		
		return result;
	}
	
	//Main
	public static void main(String[] args){
		/*
		 * This program takes the ERG Output from STDIN and outputs relations discovered to STDOUT.
		 * To Exit, make the line contain ||
		 */
		System.out.println("-<Initialized>-");
		System.out.println("Verbose?");
		
		/*
		System.out.println("Specify Path to File: "); //If file reading option is required
		String path = sc.nextLine();
		BufferedReader read = new BufferedReader(new FileReader(path));
		String line = null;*/

		int sentencecounter = 1;
		while (!(input.contains("||"))) {
			System.out.println("Transfer Sentence:");
			input = sc.nextLine();
			System.out.println("Sentence Number " + sentencecounter++ + ":");
			System.out.println("The sentence is \"" + input + " \"");
			System.out.print("Read ERG: ");
			String parse = sc.nextLine();
			if (parse.substring(0,1).equals("{") && parse.substring(parse.length()-1, parse.length()).equals("}")){
				System.out.println("Input Accepted! ^_^ \n");
				//System.out.println("Sentence: " + "A normal A1C level is below 5.7 percent.");//For Debug Reference
				buildVars(parse); //"{e2: x6:_a_q<0:1>[] e8:_normal_a_1<2:8>[ARG1 x6:_level_n_1] e10:compound<9:18>[ARG1 x6:_level_n_1, ARG2 x9:named(a1c)] x9:proper_q<9:12>[] e2:_below_p<22:27>[ARG1 x6:_level_n_1, ARG2 x15:_percent_n_of] x15:udef_q<28:40>[] e20:card<28:31>[ARG1 x15:_percent_n_of, CARG :5.7] }");		//touchVars(vars);
				System.out.println("\nEventualities:");
				printArrList(evs); //Prints Evs
				System.out.println("\nInstances:");
				printArrList(vars); //Prints Vars
				
				computeTriple();
				clearPast();
			}
			
			else{
				System.out.println("Input is not in the correct ERG Output Fomat. -_-\n");
				break; //Breaks out of loop right away at error point
			}
			
			sc.nextLine(); //Skips over Space
		}
		
		System.out.println("\n-<Exiting>-");
	}
}
