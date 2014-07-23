import java.util.*;
public class testSorter {
	
	public static Scanner sc = new Scanner(System.in); //Construct Scanner to accept STDIN 
	
	public static void printArrList(ArrayList<String> al){//Convenient ArrayList Printing for DEBUGGING
		System.out.println("<Start of ArrayList>");
		for (String s: al){
			System.out.println(s);
		}
		System.out.println("<End of ArrayList>");
	}

	public static void main(String[] args){
		/*
		 * This program takes the ERG Output from STDIN and outputs relations discovered to STDOUT.
		 * To Exit, make the next line contain the stop characters ||
		 */
		String input = "";
		int sentencecounter = 1;
		System.out.println("-<Initialized>-");
		
		while (!(input.contains("||"))) {
			System.out.println("Transfer Sentence:");
			Sorter.input(sc.nextLine());
			System.out.println("Sentence Number " + sentencecounter++ + ":");
			System.out.println("Read Sentence: \"" + Sorter.getInput() + " \"");
			//System.out.print("Read ERG: ");
			String parse = sc.nextLine();
			
			if (parse.substring(0,1).equals("{") && parse.substring(parse.length()-1, parse.length()).equals("}")){
				System.out.println("OK ^_^ \n");
				//System.out.println("Sentence: " + "A normal A1C level is below 5.7 percent.");//For Debug Reference
				Sorter.buildIndex(parse);
				Sorter.sortConj();
				Sorter.populateEvInsts();
				Sorter.removeDuplicateEvInst();
				
				System.out.println("\nEventualities:");
				printArrList(Sorter.getEvs()); //Prints Evs
				
				System.out.println("\nArgList");
				printArrList(Sorter.getArglist());
				
				//System.out.println("\nFree Instances:");
				//printArrList(Sorter.getInsts()); //Prints Insts
				
				//System.out.println("\nEventuality-Instances");
				//printArrList(Sorter.getEvinsts()); //Prints Conjs
				
				//System.out.println("\nIVars:");
				//printArrList(Sorter.getIvars()); //Prints Conjs
				
				//System.out.println("\nConjunctions:");
				//printArrList(Sorter.getConjs()); //Prints Conjs
				
				ArrayList<String> results = Sorter.TripleGen();
				System.out.println("\nTriples Generated:");
				printArrList(results);

				Sorter.clear();
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
