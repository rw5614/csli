import java.util.*;
public class testSorter {
	public static Scanner sc = new Scanner(System.in); //Construct Scanner to accept STDIN 
	public static void main(String[] args){
		System.out.println("-<Initialized>-");
		String flowcontrol = "";
		
		do {
			String sentence = sc.nextLine();
			String ergplugin = sc.nextLine();
			flowcontrol = sc.nextLine();
			printArrList(Sorter.Parse(sentence, ergplugin, flowcontrol, ""));
			System.out.println("");
		} while ((!(flowcontrol.contains("|STOP|"))));
		
		System.out.println("\n-<Exiting>-");
	}
	
	public static void printArrList(ArrayList<String> al){//Convenient ArrayList Printing for DEBUGGING
		//System.out.println("<Start of ArrayList>");
		for (String s: al){
			System.out.println(s);
		}
		//System.out.println("<End of ArrayList>");
	}
}