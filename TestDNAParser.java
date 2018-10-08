import static java.lang.System.out;
import static java.lang.System.err;
import java.util.Scanner;
public class TestDNAParser
{
	public static void main(String[] args)
	{
		DNAParser dnaParser=new DNAParser();
		guiController(dnaParser);
	}
	
	public static void guiController(DNAParser dnaParser)
	{	
		boolean execute=true;
		do
		{
			gui();
			execute=doOperation(dnaParser);
			for(int i=0;i<15;i++) out.print("-"); out.println();
		}while(execute);
	}
	public static boolean doOperation(DNAParser dnaParser)
	{
		Scanner input=new Scanner(System.in);
		int choice=0;
		try{choice=Integer.parseInt(input.nextLine());}catch(NumberFormatException ex){out.println("IncorrectResponse"); 
		guiController(dnaParser);}
		switch(choice)
		{
			case 1:testListAllGenes(dnaParser);break;
			case 2:testPrintNumberOfGenes(dnaParser);break;
			case 3:testHowMany(dnaParser);break;
			case 4:testPrintGenesMoreThanChar(dnaParser);break;
			case 5:testCgRatioMore(dnaParser);break;
			case 6:testGetNumberCG(dnaParser);break;
			case 7:testPrintTallestGene(dnaParser);break;
			case 8:testGetLengthTallestGene(dnaParser);break;
			case 9:return false;
			default:out.println("Incorrect input!");
		}
		return continueOperation();
	}
	private static boolean continueOperation()
	{
		out.print("Do you want to Reuse the program? (y/n)");
		switch(new Scanner(System.in).nextLine().toLowerCase().charAt(0))
		{
			case 'y':
			case '1':return true;
			case 'n':
			case '0':return false;
			default:out.println("Incorrect response"); return continueOperation();
		}
		
	}
	public static void gui()
	{
		out.print("\nDNAParser Test Version 1.0\n---------------------------\n1.List all genes\n"+
		"2.get Number of genes in the source\n"+
		"3.how many times a string appeared in gene? (will list all genes and choose from them one to do operation on)\n"+
		"4.get genes that has characters more than what you specify\n"+
		"5.list genes that has C-G ratio more than what you specify\n"+
		"6.get number of genes that has C-G ratio more than you specify\n"+
		"7.print the tallest gene\n"+
		"8.get the length of the tallest gene\n"+
		"9.Exit the program\n"+
		"Enter your Choice: ");
	
	}
	public static void testListAllGenes(DNAParser dnaParser)
	{
		String[] strArr=dnaParser.getAllGenes();
		int i=1;
		if(strArr!=null)
		for(String e:strArr)out.println(e +" Gene#"+(i++));
		else
		out.println("No genes were found!");
	}
	public static void testPrintNumberOfGenes(DNAParser dnaParser)
	{
		out.println("Number of genes in source: "+dnaParser.getNumberOfGenes());
	}
	public static void testHowMany(DNAParser dnaParser)
	{
		testListAllGenes(dnaParser);
		Scanner input= new Scanner(System.in);
		int geneIndex=-1;	
		try
		{
		do{
		out.print("Spicify which gene you want to search in: ");
		geneIndex=Integer.parseInt(input.nextLine());
		}while(geneIndex==-1);
		}catch(NumberFormatException ex){out.println("Incorrect response");testHowMany(dnaParser);}
		out.print("Enter the SearchTarget: ");
		String searchTarget=input.nextLine();
		out.println(searchTarget+" occured in gene#"+geneIndex+" "+dnaParser.howMany(searchTarget,dnaParser.getAllGenes()[--geneIndex])+" Times.");
	
	}
	public static void testPrintGenesMoreThanChar(DNAParser dnaParser)
	{
		out.print("Enter how many characters: ");
		String[] strArr=dnaParser.getGenesMoreThan_Chars(Integer.parseInt(new Scanner(System.in).nextLine()));
		int i=1;
		for(String e:strArr)
		{
			out.println(e+" Gene#"+(i++));
		}
	
	}
	public static void testCgRatioMore(DNAParser dnaParser)
	{
		out.print("Enter C-G ratio you want (0.0-1.0 or 0%-100%) : ");
		double value=0.0;
		String input;
		try
		{
			value=((input=new Scanner(System.in).nextLine()).contains("%"))?(Double.parseDouble(input.substring(0,input.indexOf("%")))/100.0):(Double.parseDouble(input));
			int i=1;
			String[] strArr=dnaParser.getGenesCGRatioMoreThanPercentage(value);
			if(strArr!=null)
			for(String e:strArr)
			out.println(e+" Gene#"+(i++));
			else
			out.println("no genes were found!");
		}
		catch(NumberFormatException ex)
		{
			err.println("Error, you didn't enter a number as specified!");
		}
	
	}
	public static void testGetNumberCG(DNAParser dnaParser)
	{
		out.print("Enter C-G ratio you want (0.0-1.0 or 0%-100%) : ");
		double value=0.0;
		String input;
		try
		{
			value=((input=new Scanner(System.in).nextLine()).contains("%"))?(Double.parseDouble(input.substring(0,input.indexOf("%")))/100.0):(Double.parseDouble(input));
			out.println("Number of genes having ratio more than "+input+" :"+dnaParser.getNumberOfCgRatioMoreThanPercentage(value));
		}
		catch(NumberFormatException ex)
		{
			err.println("Error, you didn't enter a number as specified!");
		}
	}
	public static void testPrintTallestGene(DNAParser dnaParser)
	{
		out.println(dnaParser.getTallestGene());
	}
	public static void testGetLengthTallestGene(DNAParser dnaParser)
	{
		out.println(dnaParser.getLengthTallestGene());
	}

	//unused
	public String giveString(String[] args)
	{
		return (validateArgs(args))?args[0]:null;
	}
	//unused
	public boolean validateArgs(String[] args)
	{
		return (args.length>0)?true:false;
	}	
	
}