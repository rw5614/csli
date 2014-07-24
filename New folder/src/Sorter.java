import java.util.*;
public class Sorter {
	private static String input = "";
	private static String keyEv = ""; //Key Eventuality
	private static String evstring = "";
	private static int sentencecounter = 0;
	private static boolean debugon = false;
	private static ArrayList<String> evs = new ArrayList<String>();
	private static ArrayList<String> insts = new ArrayList<String>();
	private static ArrayList<String> evinsts = new ArrayList<String>();
	private static ArrayList<String> ivars = new ArrayList<String>();
	private static ArrayList<String> conjs = new ArrayList<String>();
	private static ArrayList<String> arglist = new ArrayList<String>();
	
	public static ArrayList<String> Parse(String sentencein, String ergout, String flowcontrol, String params){
		/*
		 * This program takes the ERG Output from STDIN and outputs relations discovered to STDOUT.
		 * To Exit, make the next line contain the stop characters ||
		 */
		while (!flowcontrol.contains("|Stop|")){
			clear(); //Clears Variables to prepare for next sentence.
			input(sentencein); //Build Sentence
			if(params.contains("-d")){
				debugon = true;
			}
			if(InputFormatOK(ergout)){
				System.out.println("#" + ++sentencecounter + ":");
				if (params.contains("-v")){//Verbose Mode
					System.out.println("Sentence: " + input);
					System.out.println("ERG: " + ergout);
				}
				buildIndex(ergout); //Build Index from ERG Output
				sortConj(); //Sort Out Conjunctions
				populateEvInsts(); //Populate Eventuality-Instances
				removeDuplicateEvInst(); //Remove Duplicate Eventuality-Instances
				printDebugInfo(params);
				System.out.println("Resulting Triples:");
				return TripleGen();				
			}
			else{
				System.out.println("Input is not in ERG Format. -_- \n");
				break; //Breaks out of loop right away at error point
			}
		}
		return null;
	}
	
	public static void printDebugInfo(String params){
		if (params.toLowerCase().contains("-pev")){
			System.out.println("\nEventualities:");
			printArrList(getEvs()); //Prints Evs
		}
		if (params.toLowerCase().contains("-pal")){
			System.out.println("\nArgList");
			printArrList(getArglist());
		}
		if (params.toLowerCase().contains("-pfi")){
			System.out.println("\nFree Instances:");
			printArrList(getInsts());
		}
		if (params.toLowerCase().contains("-pei")){
			System.out.println("\nEventuality Instances");
			printArrList(Sorter.getEvinsts()); //Prints Conjs
		}
		if (params.toLowerCase().contains("-piv")){
			System.out.println("\nIVars:");
			printArrList(Sorter.getIvars()); //Prints Conjs
		}
		if (params.toLowerCase().contains("-pcj")){
			System.out.println("\nConjunctions:");
			printArrList(Sorter.getConjs()); //Prints Conjs
		}
	}
	public static boolean InputFormatOK(String parse){
		if (parse.substring(0,1).equals("{") && parse.substring(parse.length()-1, parse.length()).equals("}")){
			return true;
		}
		return false;
	}
	public static String getInput(){
		return input;
	}
	public static String getKeyEv(){
		return keyEv;
	}
	
	public static ArrayList<String> getEvs(){
		return evs;
	}
	public static ArrayList<String> getInsts(){
		return insts;
	}
	public static ArrayList<String> getIvars(){
		return ivars;
	}
	public static ArrayList<String> getConjs(){
		return conjs;
	}
	public static ArrayList<String> getEvinsts(){
		return evinsts;
	}
	public static ArrayList<String> getArglist(){
		return arglist;
	}
	
	public static void input(String s){
		input = s;
	}
	
	public static void sortConj(){
		for (String i:insts){
			if (i.contains("_c<") || i.contains("_conj<")){
				conjs.add(i);
			}
		}
	}
	
	public static void clear(){
		keyEv = "";
		input = "";
		evstring = "";
		evs.clear();
		insts.clear();
		evinsts.clear();
		ivars.clear();
		conjs.clear();
		arglist.clear();	
	}	
	
	public static void buildIndex(String s){// Formats the input to "Pretty Printed" format
		s = touchInput(s); //Removes
		s = keyOut(s); 
		populateArray(s);
	}
	
	public static void populateArray(String s){
		while (s.length() > 0){ //While there still are things left to parse;
			for (int i = 0; i < s.length()-1; i++){ //Find next space;
				if (s.substring(i, i+2).equals("] ")){
					if (s.substring(0,1).equals("x")){ //Places Instances in the insts Array
						insts.add(s.substring(0, i+1));
					}
					else if (s.substring(0,1).equals("e")){ //Places Eventualities in evs Array
						String addthis = s.substring(0, i+1);
						addthis = addthis.replaceAll("CARG ", "ARGC xcc");
						evs.add(addthis);
						evstring = evstring + (addthis);
					}
					else if (s.substring(0,1).equals("i")){ //Places I-Variables in ivars Array
						ivars.add(s.substring(0, i+1));
					}
					s = s.substring(i+2, s.length()); //Contracts string to remove processed info.
					break; //Some processing has been done. Check for more processing to do.
				}
			}
		}
	}
	
	public static void populateEvInsts(){
		for (String s: evs){
			int leftbracket = s.indexOf("[");
			int rightbracket = s.indexOf("]", leftbracket + 1);
			s = s.substring(leftbracket + 1, rightbracket + 1); //Right bracket purposefully included
			//Search for space
			while (s.contains(" ")){ //While there are still spaces left
				int startsp = s.indexOf(" "); //Look for first space
				int stopsp = startsp; //Default to nothing
				if (s.contains(", ")){ //Look for stop character
					stopsp = s.indexOf(", ", startsp + 1); 
					if (s.contains("x")){
						evinsts.add(s.substring(startsp + 1, stopsp));
					}
					else if (s.contains("e")){
						if(debugon){
						System.out.println("EFLAG");
						}
					}
					s = s.substring(stopsp + 2, s.length()); //Removes space after comma
				}
				else{
					stopsp = s.indexOf("]", startsp + 1);
					if (s.substring(startsp + 1, startsp+2).contains("x")){
						evinsts.add(s.substring(startsp + 1, stopsp));
					}
					else if (s.substring(startsp + 1, startsp+2).contains("e")){
						//FLAG THE E
					}
					s = s.substring(stopsp + 1, s.length());
				}
			}
		}
	}

	public static void removeDuplicateEvInst(){
		HashSet<String> hs = new HashSet<String>();
		hs.addAll(evinsts);
		evinsts.clear();
		evinsts.addAll(hs);	
	}
	//Triple Generation
	public static ArrayList<String> TripleGen(){
		ArrayList<String> results = new ArrayList<String>();
		for(String s: evs){
			String a = null;
			String b = null;
			String c = null;
			a = ArgID(s, -1);
			b = getEvContent(s);
			c = ArgID(s, 1);
			//Check if any slots are still unfilled, if so, fill again using traditional method (Method 1)
			if (a == null){
				a = fillArgs(s, -1);
				if (debugon){
					System.out.println("A is null, filling.");
					System.out.println("After Fill a: " + a);
				}
			}
			if (c == null){
				c = fillArgs(s, 1);
				if (debugon){
					System.out.println("C is null, filling.");
					System.out.println("After Fill c: " + c);
				}
				
			}
			
			//Converts Case for Readability
			if (a != null){
				a = a.toLowerCase();
			}
			if (b!= null){
				b = b.toUpperCase();
			}
			if (c != null){
				c = c.toLowerCase();
			}
			if (debugon){
				System.out.println (a + " " + b + " " + c);
				System.out.println((a == null) + " " + (b == null) + " " + (c == null) + "|" + a.equals(c));
			}
			//Adds the result to a list, if it is indeed a valid triple
			if (a != null && b != null && c != null && !(a.equals(c))){//If a triple is present
				results.add(a + " " + b + " " + c);
				if (debugon){
				System.out.println("TRIPLE: " + a + "(a) " + b + "(b) " + c + "(c)");//Spit out the triple DEBUG
				}
			}
		}
		return results;
	}
	public static String fillArgs(String s, int compare){
		ArrayList<String> targs = new ArrayList<String>();
		targs.clear();
		targs.addAll(arglist);
		if (compare == -1){
			for (int i = targs.size() -1; i > 0; i--){
				if (targs.get(i).contains("ARG2")){ //Prevents ARG2 from being selected
					targs.remove(i);
				}
			}
			Collections.sort(arglist);
			String rts = targs.get(0);
			int colonspot = rts.indexOf(":");
			return rts.substring(colonspot + 1);
		}
		if (compare == 1){
			for (int i = targs.size()-1; i > 0; i--){
				if (targs.get(i).contains("ARG1")){ // Prevents ARG1 from being selected
					targs.remove(i);
				}
			}
			Collections.sort(arglist);
			String rts = targs.get(arglist.size()-1);
			int colonspot = rts.indexOf(":");
			return rts.substring(colonspot + 1);
		}
		return null; //If unable to fill
	}
	public static String getEvContent(String s){
		int left = s.indexOf(":") + 1;
		int right = s.indexOf("<", left + 1);
		return s.substring(left, right);
	}
	public static String ArgID(String s, int compare){
		//For this particular eventuality, parse for the args to the comma.
		arglist.clear();
		int lbracket = s.indexOf("[");
		int rbracket = s.indexOf("]");
		s = s.substring(lbracket + 1, rbracket + 1); //Inclusive of rear bracket
		while (s.contains(", ") || s.contains("]")){ //While there are still colons left
			//System.out.println("S is now " + s);
			int startsp = 0;
			int stopsp = 0;
			if (s.contains("ARG ")){
				startsp = s.indexOf("ARG ") + 4; //Look for character that indicates type
				stopsp = startsp; //Default to nothing
			}
			
			
			if (s.contains(", ")){ //Look for stop character
				stopsp = s.indexOf(", ", startsp + 1); 
				if (s.substring(startsp, startsp+1).contains("e")){
					System.out.println("E DETECTED AS ARGUMENT");
					//REFER E to Processing
				}
				arglist.add(s.substring(startsp, stopsp));
				//System.out.println("From , : " + s.substring(startsp, stopsp));
				s = s.substring(stopsp + 2, s.length()); //Removes space after comma
			}
			
			if (s.contains("]")){ //Look for stop character
				stopsp = s.indexOf("]", startsp + 1); 
				if (s.substring(startsp, startsp + 1).contains("e")){
					System.out.println("E DETECTED AS ARGUMENT");
					//REFER E to Processing
				}
				//System.out.println(startsp + " | " + stopsp);
				arglist.add(s.substring(startsp, stopsp));
				//System.out.println("From ] : " + s.substring(startsp, stopsp));
				s = s.substring(stopsp + 1, s.length());
			}
		}
		return compareArgs(compare, 2);
	}
	public static String compareArgs(int compare, int methodselect){
		if (methodselect == 1){//Simply Prioritizes using highest vs lowest
			Collections.sort(arglist); // Sorts the Argument List
			if(compare == -1){//Specify return lowest numbered ARG
				String rts = arglist.get(0);
				int colonspot = rts.indexOf(":");
				return arglist.get(0).substring(colonspot + 1);
			}
			if(compare == 1){//Specify return highest numbered ARG
				String rts = arglist.get(arglist.size()-1);
				int colonspot = rts.indexOf(":");
				return arglist.get(arglist.size()-1).substring(colonspot + 1);
			}
		}
		if (methodselect == 2){ //Prioritizes variables named ARG1 or ARG2
			Collections.sort(arglist);
			if(compare == -1){
				for (String s: arglist){
					if (s.contains("ARG1")){
						String rts = s;
						int colonspot = rts.indexOf(":");
						return s.substring(colonspot + 1);
					}
				}
			}
			if(compare == 1){
				for (String s: arglist){
					if (s.contains("ARG2")){
						String rts = s;
						int colonspot = rts.indexOf(":");
						return rts.substring(colonspot + 1);
					}
				}
			}
		}
		
		return null; //Otherwise, return nothing
	}
	
	//Pre-processes Input, either as string or ArrayList
	public static String touchInput(String s){//Removes Curly Brackets from Input
		//System.out.println("Touched Input: " + s.substring(1, s.length()-1));
		return s.substring(1, s.length()-1);
	}
	public static String keyOut(String s){ //Detects Key Eventuality
		int end = 0;
		for (int i = 0; i < s.length(); i++){
			if (s.substring(i, i+1).equals(" ")){//s.substring(i, i+2).equals(" ")){ // a space is found
				//System.out.println("Keyed: <" + s.substring(0, i) + ">"); //Debug Purposes Only
				keyEv = s.substring(0, i); //Identifies and sets the Key Variable
				end = i; //makes sure already parsed stuff is not reparsed
				break;
			}
		}
		return s.substring(end+1); //Gets rid of the space as well
	}
	
	public static String getInstString(int loc, ArrayList<String> sarr){ //Gets appropriate variable value from array specified
		String preparse = sarr.get(loc);
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
	//Helpful print function for ArrayList Debugging
	public static void printArrList(ArrayList<String> al){ //Convenient ArrayList Printing for DEBUGGING
		System.out.println("<Start of ArrayList>");
		for (String s: al){
			System.out.println(s);
		}
		System.out.println("<End of ArrayList>");
	}
}
