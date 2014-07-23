import java.util.ArrayList;
import java.util.*;

public class Sorter {
	private static String input = "";
	private static String keyEv = ""; //Key Eventuality
	private static ArrayList<String> evs = new ArrayList<String>();
	private static ArrayList<String> insts = new ArrayList<String>();
	private static ArrayList<String> evinsts = new ArrayList<String>();
	private static ArrayList<String> ivars = new ArrayList<String>();
	private static ArrayList<String> conjs = new ArrayList<String>();
	private static String evstring = "";
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
	public static void input(String s){
		input = s;
	}
	
	/*public static void convert(String sentence, String ergout){
		clearPast();
		input(sentence;
		buildIndex(ergout);
		computeTriple();
		sortConj();
	}*/
	
	public static void sortConj(){
		for (String i:insts){
			if (i.contains("_c<") || i.contains("_conj<")){
				conjs.add(i);
			}
		}
	}
	
	public static void clearPast(){
		keyEv = "";
		input = "";
		insts.clear();
		evs.clear();
		ivars.clear();
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
						evs.add(s.substring(0, i+1));
						evstring = evstring + (s.substring(0, i+1));
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
				if (s.contains(",")){ //Look for stop character
					stopsp = s.indexOf(",", startsp + 1); 
					
					evinsts.add(s.substring(startsp + 1, stopsp));
					s = s.substring(stopsp + 2, s.length()); //Removes space after comma
				}
				else{
					stopsp = s.indexOf("]", startsp + 1);
					
					evinsts.add(s.substring(startsp + 1, stopsp));
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
	
	//Preprocesses Input, either as string of ArrayList
	public static String touchInput(String s){//Removes Curly Brackets from Input
		//System.out.println("Touched Input: " + s.substring(1, s.length()-1));
		return s.substring(1, s.length()-1);
	}
	public static void touchVars(ArrayList<String> alvar){//Touches if not touched originally from input
		alvar.set(alvar.size()-1, alvar.get(alvar.size()-1).substring(0, alvar.get(alvar.size()-1).length()-2));
		alvar.set(0, alvar.get(0).substring(1, alvar.get(0).length()));
	}
	
	public static String keyOut(String s){ //Detects Key Eventuality
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
	
	//Compute Triples
	
	public static void explicate(){//Verbose output for debugging
		
	}
	
	public static void computeTriple(){
		
	}
	
	public static void computeOldTriple(){
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
		for (int i = 0; i < insts.size(); i++){
			  if (insts.get(i).contains(inst1)){
			    xloc1 = i;
			  }
		}
		
		arg1 = getInstString(xloc1, insts).toLowerCase();
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
		for (int i = 0; i < insts.size(); i++){
			  if (insts.get(i).contains(inst2)){
			    xloc2 = i;
			  }
		}
		arg2 = getInstString(xloc2, insts).toLowerCase();
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
	
}
