import java.util.ArrayList;
import javax.swing.JFileChooser;
import java.io.File;
public class DNAParser
{
	private ArrayList<String> genesList;
	
	public DNAParser()
	{
		this.genesList=getGenes(getDnaStrandFromSource(getFile()));
	}
	public DNAParser(String dnaStrand)
	{
		this.genesList=getGenes(dnaStrand);
	}
	
	public DNAParser(java.io.File dnaSrc)
	{
		this.genesList=getGenes(getDnaStrandFromSource(dnaSrc));
	}
	
	public DNAParser(java.net.URL dnaSrc)
	{
		this.genesList=getGenes(getDnaStrandFromSource(dnaSrc));
	}

	/*
	To find a gene in the dna strand, the search starts for the start codon "ATG"
	then the stop codon which can be "TAA", "TAG" or "TGA" 
	*/
	private String findGeneGeneralized(String dna,int startPosition)
	{
		//initializing the gene to be empty string
		String foundGene="";
		//finding the start codon "ATG "and changing the whole strand to uppercase to work for uppercase and lowercase strands
		int startIndex=dna.toUpperCase().indexOf("ATG",startPosition);
		//if the start codon doesn't exist then there is no valid gene, an empty string is returned
		if(startIndex==-1)return foundGene;
		//get the sop codon which can be "TAG", "TAA" or "TGA"
		int minIndex=findMinIndex(dna,startIndex);
		//return the gene between them including the start and stop codons
		return (minIndex==-1)?foundGene:(foundGene=dna.substring(startIndex,minIndex+3));
	}
	//finds a valid stopping codon according to the type of StopCodon given using the Enumerated Class
	private int findStopCodon(String dna,int startIndex,StopCodon stopCodon)
	{
		int currIndex=(dna.toUpperCase().indexOf(stopCodon.toString(),startIndex+3));
		while(currIndex!=-1 && !isValidGene(startIndex,currIndex))
		{
			currIndex=dna.indexOf(stopCodon.toString(),currIndex+1);
		}
		return currIndex;
	}
	/* search for the nearest stopping codon that has a valid Gene before it and that will be the stopping codon of the gene */
	private int findMinIndex(String dna,int startIndex)
	{
		//initializing an array of Enumerated type class StopCodon for each class of the stopping codons 
		StopCodon[] stopCodons={StopCodon.TAA,StopCodon.TGA,StopCodon.TAG};
		//making an array of indexes holding the index of each stopping codon found
		int[] stopCodonsIndices=new int[stopCodons.length];
		//loop adding each of the stopping codons' index to the indexes array if found, else -1 will be added
		for(int i=0;i<stopCodons.length;i++)
		{
			stopCodonsIndices[i]=findStopCodon(dna,startIndex,stopCodons[i]);	
		}
		//sorting the indexes accendingly
		java.util.Arrays.sort(stopCodonsIndices);
		//neglecting the -1 values as they are not found and returning the first value that is not -1 which will be the index of the first found valid stoping codon
		for(int i=0;i<stopCodonsIndices.length;i++)
		{
			if(stopCodonsIndices[i]==-1)continue;
			else return stopCodonsIndices[i];
		}
		//if no stopping codon found then -1 is returned
		return -1;
	}
	/* Validates the gene using the stopping codon index  as the number of chars between them must be a multiple of three, anything else is invalid */
	private boolean isValidGene(int start,int end)
	{
		return ((end-(start+3))%3==0)?true:false;
	}
	/* A method that returns the genes whom has characters more than given size as a String array for immutablility */
	public String[] getGenesMoreThan_Chars(int size)
	{
		if(!validateGenesList())throw new EmptySourceException("The given source for DNAStrand is empty! ");
		ArrayList<String> genesMoreThan=new ArrayList<String>();
		for(int i=0,n=genesList.size();i<n;i++)
		{
			if(genesList.get(i).length()>size)
			{
				genesMoreThan.add(genesList.get(i));
			}
			else continue;
		}
		return (genesMoreThan.size()>0)?genesMoreThan.toArray(new String[genesMoreThan.size()]):null;
	}
	/* A method that searches for the number of genes that have more charactes than the given size */
	public int getNumberOfGenesMoreThan_Chars(int size)
	{
		if(!validateGenesList())throw new EmptySourceException("The given source for DNAStrand is empty! ");
		int count=0;
		//checks each gene in the list for having a bigger size then returning it's count
		for(int i=0,n=genesList.size();i<n;i++)
		{
			if(genesList.get(i).length()>size)
			{
				count++;
			}
			else continue;
		}
		return count;
	}
	/* A method that searches in a dna for 'c' or 'g' characters, if found the count increases and in the end gets you the C-G ratio in the dna */
	public double cgRatio(String gene)
	{
		int count=0;
		//converts the gene to a charArray to check char by char
		char[] geneChars=gene.toUpperCase().toCharArray();
		for(int i=0;i<geneChars.length;i++)
		{
			if(geneChars[i]=='C' || geneChars[i]=='G')
			count++;
			else continue;
		}
		//casting one of the operands to be double for the output to be double as the other operand will be casted implicitly to double
		return (double)count/gene.length();
	}
	/* A method that returns the genes whom has C-G ratio more than given as a String array for immutablility */
	public String[] getGenesCGRatioMoreThanPercentage(double neededRatio)
	{
		if(!validateGenesList())throw new EmptySourceException("The given source for DNAStrand is empty! ");
		final double ratio=neededRatio;
		ArrayList<String> neededGenes=new ArrayList<>();
		for(int i=0,n=genesList.size();i<n;i++)
		{
			if(cgRatio(genesList.get(i))>ratio)
			{
				neededGenes.add(genesList.get(i));
			}
			else continue;
		}
		return (neededGenes.size()>0)?neededGenes.toArray(new String[neededGenes.size()]):null;
	}
	/* A method that checks how many times did string A occur in String B and returning the count */
	public int howMany(String stringa,String stringb)
	{	
		int counter=0;
		int currIndex=stringb.toUpperCase().indexOf(stringa.toUpperCase());
		
		if(currIndex==-1)return counter;
		else counter++;
		while(true)
		{
			currIndex=stringb.toUpperCase().indexOf(stringa.toUpperCase(),currIndex+stringa.length());
			if(currIndex!=-1)counter++;
			else break;
		}
		return counter;
	}
	/* A method that searches for the genes having a c-g ratio bigger than given and returning their count */
	public int getNumberOfCgRatioMoreThanPercentage(double neededRatio)
	{
		return (getGenesCGRatioMoreThanPercentage(neededRatio)!=null)?getGenesCGRatioMoreThanPercentage(neededRatio).length:0;
	}	
	/* A method that returns the number of valid genes extracted from the dna strand */
	public int getNumberOfGenes()
	{
		if(!validateGenesList())throw new EmptySourceException("The given source for DNAStrand is empty! ");
		return genesList.size();
	}
	/* A method that compares lengths of genes then returns the talles gene in the dna strand */
	public String getTallestGene()
	{
		if(!validateGenesList())throw new EmptySourceException("The given source for DNAStrand is empty! ");
		int maxLength=0;
		int neededIndex=-1;
		for(int i=0,n=genesList.size();i<n;i++)
		{
			if(genesList.get(i).length()>maxLength)
			{
				maxLength=genesList.get(i).length();
				neededIndex=i;
			}
			else continue;
		}	
		return (neededIndex!=-1)?genesList.get(neededIndex):"";	
	}
	/* A method that finds the tallest gene and returns it's length */
	public int getLengthTallestGene()
	{
		if(!validateGenesList())throw new EmptySourceException("The given source for DNAStrand is empty! ");
		return getTallestGene().length();	
	}
	/* the method that gets all the genes in the dna Strand */
	private ArrayList<String> getGenes(String dna)
	{
		//makes an arraylist of strings object
		ArrayList<String> genesList=new ArrayList<>();
		int startIndex=0;
		// infinite loop till there is no valid gene
		while(true)
		{
			//finds if there exists a valid gene starting at the specified startIndex, if not then empty string will return so it will break
			String gene=findGeneGeneralized(dna.toUpperCase(),startIndex);
			if(gene.isEmpty())break;
			else
			{
			// if finds a valid gene then it's added to the array
			genesList.add(gene);
			// gets a new StartIndex starting from directly after the previous gene by searching for the starting index of the gene from the previous starting index, then adding
			// the genes length to it getting the index directly after the previous gene and loops again
			startIndex=dna.indexOf(gene,startIndex)+gene.length();
			}
		}
		return genesList;
	}
	/* A method that takes dna strands from a URL or A file path */
	private String getDnaStrandFromSource(java.io.File path)
	{
		try(java.util.Scanner inputSource=new java.util.Scanner(path))
		{
		if(inputSource.hasNext()) return inputSource.next();
		else throw new EmptySourceException("The Source given: "+path+" is Empty!");
		}
		 catch(java.io.FileNotFoundException ex) { throw new EmptySourceException("The Source given: "+path+" Doesn't exist"); }
		 catch(java.lang.Exception ex) { ex.printStackTrace(); }
		 catch(java.lang.Throwable ex) { ex.printStackTrace(); }
		return null;
	}
	/* overloaded version */
	private String getDnaStrandFromSource(java.net.URL path)
	{
		try(java.util.Scanner inputSource=new java.util.Scanner(path.openStream()))
		{
		if(inputSource.hasNext()) return inputSource.next();
		else throw new EmptySourceException("The Source given: "+path+" is Empty!");
		}catch(java.net.MalformedURLException ex) { throw new EmptySourceException("The Source given: "+path+" Doesn't exist"); }
		 catch(java.io.IOException ex) { throw new EmptySourceException("An error occured due to the source given: "+path);}
		 catch(java.lang.Exception ex) { ex.printStackTrace(); }
		 catch(java.lang.Throwable ex) { ex.printStackTrace(); }
		return null;
	}
	
	/* A simple method to check the existance of string and not having a null reference */
	private boolean validateStrand(String strand)
	{
		return (strand!=null)?true:false;
	}
	/* A method that checks if a string is valid, if not a user Defined exception called EmptySourceException will be thrown*/
	private void checkStrand(String strand)
	{
		if(!validateStrand(strand)) throw new EmptySourceException("The Source given is Empty!");
		else {/*do nothing*/}
	}
	private boolean validateGenesList()
	{
		return (genesList!=null)?true:false;
	}
	public String[] getAllGenes()
	{
		if(!validateGenesList())throw new EmptySourceException("The given source for DNAStrand is empty! ");
		return genesList.toArray(new String[genesList.size()]);
	}
	private File getFile()	
	{
		JFileChooser chooser=new JFileChooser();
		try{
		do
		{
		System.out.println("Please select a DNA file");
		try{Thread.sleep(1000);}catch(InterruptedException ex){ ex.printStackTrace(); }
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setCurrentDirectory(new File("."));
		chooser.showOpenDialog(null);
		}while(chooser.getSelectedFile() == null && !chooser.getSelectedFile().isFile() && !chooser.getSelectedFile().getPath().endsWith(".fa"));
		}catch(NullPointerException ex)
		{
			getFile();
		}
		return chooser.getSelectedFile();
	}	
	// A private user Defined Exception class that can be thrown by the DNAParser Class only
	private class EmptySourceException extends RuntimeException{public EmptySourceException() { super(); } public EmptySourceException(String str){ super(str); } }
	// A private Enum that has all the possible stopping codons.
	private enum StopCodon {TAA,TAG,TGA;}
}