/**
			COL106 Assignment 2 WordFrequencyCounter
			====== ============ ====================
												Designed By : 
											YASH KUMAR BANSAL 
											2013ME10742
											Group 3
*/
// Hashing By Chaining and MergeSort are implemented in this assignment.

import java.util.Scanner;
import java.util.Random;
import java.io.*;

/// Class Node defines the nodes for Linked List
class Node
{
	String word;
	int count;
	Node next;
	Node()
	{
		word="";
		count=0;
		next=null;
	}
	Node(String s)
	{
		word=s;
		count=1;
		next=null;
	}

	String getString()
	{
		return this.word;
	}
	int getCount()
	{
		return this.count;
	}
	Node getNext()
	{
		return this.next;
	}

};

// ArrayNode is different from List node as it does not have next (pointer)
class ArrayNode
{
	String word;
	int count;
	ArrayNode()
	{
		word="";
		count=0;
	}
	String getString()
	{
		return this.word;
	}
	int getCount()
	{
		return this.count;
	}
	void setNode(Node node)			//Store data from a List node
	{
		this.word = node.getString();
		this.count= node.getCount();
	}
};

class LinkedList
{
	Node front;

	LinkedList()
	{
		front = new Node();
	}
	
	Node getFront()
	{
		return front;
	}
	
	// increase count for a given node of link-list
	void addCount(Node node)
	{
		node.count++;
	}
	// insert a new word s after node
	void Insert(String s, Node node)
	{
		Node temp = new Node(s);
		temp.next=node.next;
		node.next=temp;
		//System.out.println("New node for "+temp.word +" after "+node.word);
	}

	// delete the first node after front
	// objects are not required to be deallocated in Java
	void DeleteHead()
	{
		if(front.next!=null)
			front.next=front.next.next;
	} 
};
	
public class WordFrequencyCounter
{
	// mod and multiplier used in calculating hash should remain constant
	final int MOD;
	final int multiplier;
	
	int words;
	int hashList[];
	Node tempNode;

	// hashtable is an array of linked-lists
	LinkedList[] hashTable;

	// list stores the words with frequency greater than threshold
	// sorted stores the words in decreasing order of frequency
	ArrayNode[] list,sorted;

	boolean debug;
	WordFrequencyCounter(String fileName, int threshold)
	{
		// if the multiplier used in Horner's rule is chosen randomly,
		// then the hashing function is also random
		Random seed = new Random();
		switch(seed.nextInt(4))
		{
			case  0 : multiplier = 37;	break;
			case  1 : multiplier = 41;	break;
			case  2 : multiplier = 43;	break;
			default : multiplier = 47;	break;
		}
		// 100003 is prime and not close to a power of 2
		MOD = 100003;
		
		// assuming that there are atmost 10,000 distinct words in the file
		hashList = new int[100000];

		//debug true shows the intermediate steps also
		debug=false;

		hashTable = new LinkedList[MOD];
		for(int i = 0 ; i<MOD ; i++)
			hashTable[i] = new LinkedList();
		try
		{
			words=ParseFile(fileName);
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Input file '"+fileName+"' not found!");
			return;
		}
		list= new ArrayNode[words];
		for(int i=0;i<words;i++)
			list[i] = new ArrayNode(); 
		words = Retrieve(list,threshold);
		sorted= new ArrayNode[words];
		for(int i=0;i<words;i++)
			sorted[i] = new ArrayNode(); 
		MergeSort(0,words);
		showResults();
	}

	public static void main(String[] args) 
	{
		int K;
		String file = "test.txt";
		System.out.println("");
		System.out.println("Welcome to WORD-FREQUENCY-COUNTER Application");
		System.out.println("");
		System.out.println("The application will process test.txt by default");
		System.out.println("Enter the threshold frequency of words or -1 to change file name.");
		System.out.print("Enter your choice : ");
		Scanner input = new Scanner(System.in);
		K = input.nextInt();
		if(K==-1)
		{
			System.out.print("Enter the file name to process : ");
			file = input.next();
			System.out.print("Enter the threshold frequency of words : ");
			K=input.nextInt();
		}
		WordFrequencyCounter obj = new WordFrequencyCounter(file,K);
	}
	
	// processes the file given as parameter
	private int ParseFile(String fileName) throws FileNotFoundException
	{
		File file= new File(fileName);
		if(!file.exists())
			throw new FileNotFoundException(file.getName());
		
		String str,word;
		char ch;
		int hash,words=0;
		int i,j,len;
		Scanner in = new Scanner(file);
		while(in.hasNext())
		{
			str=in.next().toLowerCase();
			if(debug)
				System.out.println(str);
			len=str.length();
			word = "";
			hash = 0;
			for(i=0;i<len;i++)
			{
				ch=str.charAt(i);
				if(str.charAt(i)>='a' && str.charAt(i)<='z')
				{
					word=word+ch;
					hash=(hash*multiplier + (ch-'a' + 1)) % MOD;
				}
				else if(str.charAt(i)=='\'')
				{
					word=word+ch;
					hash=(hash*multiplier + 28) % MOD;
				}
				else
				{
					if(word!="")
						if(buildHash(word,hash)==1)
						{
							hashList[words]=hash;
							words++;
						}
					word = "";
					hash = 0;
				}			
			}
			if(word!="")
				if(buildHash(word,hash)==1)
				{
					hashList[words]=hash;
					words++;
				}
		}
		return words;
	}
	
	// buildHash() takes in word and its hash and inserts into hashTable
	// returns 1 if the word has not occured before and 0 otherwise
	private int buildHash(String s, int hash)
	{
		if(debug)
			System.out.println("# " + s + " : "+ hash);
		tempNode=hashTable[hash].getFront();
		while(tempNode.getNext()!=null)
		{
			tempNode=tempNode.getNext();
			if(s.compareTo(tempNode.getString())==0)
			{
				hashTable[hash].addCount(tempNode);
				return 0;
			}
		}
		hashTable[hash].Insert(s,tempNode);
		return 1;	
	}
	
	// Retrieve() fills list[] with words occuring more than K times
	// from the hashTable in O(n) and returns number of such words
	private int Retrieve(ArrayNode[] list,int K)
	{
		int i,j,count=0,hash;	
		
		for(i=0;i<words;i++)
		{
			hash=hashList[i];
			tempNode=hashTable[hash].getFront();
			tempNode=tempNode.getNext();
			if(tempNode.getCount()>K)
			{
				list[count].setNode(tempNode);
				if(debug)
					System.out.println("--> "+list[count].getString()+" : "+list[count].getCount());
				count++;
			}
			hashTable[hash].DeleteHead();
		}
		return count;
	}

	// MergeSort gives sorted array in sorted[] from list[] in O(nlog(n))
	// where n is the number of words returned by Retrieve()
	private void MergeSort(int a, int b)	//[a,b)
	{ 
		int i,j,k;
		if(a>=b-1)
			return;
		int mid=a+(b-a)/2;
		MergeSort(a,mid);
		MergeSort(mid,b);
		i=a;
		j=mid;
		for(k=a;k<b && i<mid && j<b;k++)
		{
			if(list[i].getCount()>=list[j].getCount())
			{
				sorted[k]=list[i];
				i++;
			}
			else
			{
				sorted[k]=list[j];
				j++;
			}
		}
		while(i<mid)
		{
			sorted[k]=list[i];
			i++;
			k++;
		}
		while(j<b)
		{
			sorted[k]=list[j];
			j++;
			k++;
		}
		for(k=a;k<b;k++)
			list[k]=sorted[k];
	}

	//display the words in file with frequencies in decreasing order
	public void showResults()
	{
		System.out.println("Word : Frequency ");
		if(words==0)
			System.out.println("No such words found!");
		for(int i=0;i<words;i++)
			System.out.println(sorted[i].getString()+" : "+sorted[i].getCount());
	}
}
