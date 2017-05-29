/**
			COL106 Assignment 3 AVL Tree
			====== ============ ====================
												Designed By : 
											YASH KUMAR BANSAL 
											2013ME10742
											Group 3
*/
// AVL Tree is implemented in this assignment.


import java.util.Scanner;
import java.io.*;

public class AVLTree
{
	boolean debug = false;
	class Node
	{
		int value,count,height,weight;
		Node parent,left,right;
		boolean isLeft;
		Node(int x, Node par, boolean l)
		{
			value  = x;
			count  = 1;
			height = 1;
			weight = 1;
			parent = par;
			left   = null;
			right  = null;
			isLeft = l;
		}
	}

	Node root; 
	Node tmpNode,tmpNodeS,auxNode;
	Node x,y;

	public static void main(String[] args) 
	{
		int y,k,b;
		boolean dbg = false;
		String fileName = "input.txt";
		AVLTree obj = null;
		Scanner input = new Scanner(System.in);
		
		System.out.println("This application implements the AVL Tree");
		System.out.println("The tree will be read from file input.txt");
		System.out.println("Enter 1 to change input file");
		System.out.println("Enter 2 to build new tree");
		System.out.println("Enter 0 to continue");

		y=input.nextInt();
		if(y==-1)
		{
			dbg ^= true;
			y=input.nextInt();
		}
		if(y==1)
		{
			System.out.print("Enter the name of new input file : ");
			fileName = input.next();
		}
		else if(y==2)
			fileName = null;
		try
		{
			obj = new AVLTree(fileName,dbg);
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File not found! Tree could not be loaded!");
			try
			{
				obj = new AVLTree(null,dbg);
			}
			catch(FileNotFoundException e2)
			{
				System.out.println("Terminating...");
			}
		}
		
		System.out.println("Enter 1 followed by a number to insert a salary");
		System.out.println("Enter 2 followed by a number to delete a salary");
		System.out.println("Enter 3 followed by two numbers l and r to get the number of salaries in [l,r]");
		System.out.println("Enter 4 to see the maximum salary");
		System.out.println("Enter 5 to see the minimum salary");
		System.out.println("Enter 0 to stop\n");

		while(true)
		{
			y = input.nextInt();
			if(y==0)
				break;
			else if(y==1)
			{
				k = input.nextInt();
				obj.Insert(k);
				System.out.println("Salary inserted!");
			}
			else if(y==15)
				System.out.println(obj.showTree(obj.getRoot()));
			else if(y==-1)
				obj.Debug();
			else 
			{
				try
				{
					if(y==2)
					{
						k = input.nextInt();
						b=obj.Delete(k);
						if(b==-1)
							System.out.println("Deletion successful!");
						else
							System.out.println(k+" not found in the tree. Closest value to "+k+" is "+b);
					}
					else if(y==3)
					{
						y = input.nextInt();
						k = input.nextInt();
						if(y>k)
							System.out.println("This interval is not a valid interval!");
						else
						{
							b = obj.Range(y,k);
							System.out.println("Number of salaries found : " + b);
						}
					}
					else if(y==4)
					{
						y = obj.Max();
						System.out.println("Maximum salary is "+y);
					}
					else if(y==5)
					{
						y = obj.Min();
						System.out.println("Minimum salary is "+y);
					}
				}
				catch(EmptyTreeException e)
				{
					System.out.println("Tree is empty!");
				}
			}
		}
	}

	//constructor
	AVLTree(String fileName,boolean dbg) throws FileNotFoundException
	{
		root = null;
		tmpNode = null;
		auxNode = null;
		debug = dbg;
		if(fileName == null)
			return;
		File file= new File(fileName);
		if(!file.exists())
			throw new FileNotFoundException();
		else
		{
			Scanner fin = new Scanner(file);
			int t,v;
			t = fin.nextInt();
			while(t-->0)
				Insert(fin.nextInt());
		}
		if(debug)
			System.out.println(showTree(root));
	}

	public void Insert(int val)
	{
		try
		{
			if(Search(val))	//tmpNode updates in the Search()
			{
				tmpNode.count++;
				tmpNode.weight++;
				tmpNode = tmpNode.parent;
			}
			else if(tmpNode.value > val)
			{
				Node z = new Node(val,tmpNode,true);
				tmpNode.left = z; 
			}
			else
			{
				Node z = new Node(val,tmpNode,false);
				tmpNode.right = z; 
			}
			balanceAVLTree();
		}
		catch(EmptyTreeException e)
		{
			if(debug)
				System.out.println("Tree is empty! Creating root...");
			Node z = new Node(val,null,false);
			root = z;
		}
	}

	// returns -1 if val is found in the tree
	// returns the closest value to val otherwise
	public int Delete(int val) throws EmptyTreeException
	{
		try
		{
			if(Search(val))
			{
				if(debug)
					System.out.println(val+" found in the tree");
				if(tmpNode.count>1)
				{
					tmpNode.count--;
					tmpNode.weight--;
					tmpNode = tmpNode.parent;
				}
				else
				{
					if(tmpNode.left != null && tmpNode.right != null)
					{
						if(Successor(tmpNode)) //updates auxNode
						{
							tmpNode.value = auxNode.value;
							tmpNode.count = auxNode.count;
							tmpNode = auxNode;	
						}
						else
						{
							System.out.println("Hacked!");
							System.exit(1);
						}
					}
					if(tmpNode.left == null && tmpNode.right == null)
					{
						if(tmpNode.parent == null)
							root=null;
						else if(tmpNode.isLeft)
							tmpNode.parent.left = null;
						else 
							tmpNode.parent.right = null;
						tmpNode = tmpNode.parent;
					}
					else if(tmpNode.right == null)
					{
						tmpNode.left.parent = tmpNode.parent;
						if(tmpNode.parent == null)
						{
							root = tmpNode.left;
							tmpNode.left.isLeft = false;
						}
						else if(tmpNode.isLeft)
							tmpNode.parent.left = tmpNode.left;
						else 
						{
							tmpNode.parent.right = tmpNode.left;
							tmpNode.left.isLeft = false; 
						}
						tmpNode = tmpNode.parent;
					}
					else if(tmpNode.left == null)
					{
						tmpNode.right.parent = tmpNode.parent;
						if(tmpNode.parent == null)
							root = tmpNode.right;
						else if(tmpNode.isLeft)
						{
							tmpNode.parent.left = tmpNode.right;
							tmpNode.right.isLeft = true;
						} 
						else 
							tmpNode.parent.right = tmpNode.right;
						tmpNode = tmpNode.parent;
					}					
				}
				balanceAVLTree();
				return -1;
			}
			else
			{
				if(debug)
					System.out.println(val+" not found in the AVL tree!");
				int suc,pre;
				suc = Successor(val);
				pre = Predecessor(val);
				
				if(debug)
					System.out.println("Pre : "+pre+" , suc: "+suc);
				if(suc == -1)
					return pre;
				else if(pre == -1)
					return suc;
				else if(suc - val <= val - pre)
					return suc;
				else
					return pre;
			}
		}
		catch(EmptyTreeException e)
		{
			throw new EmptyTreeException();
		}
	}

	public int Min() throws EmptyTreeException
	{
		int ans=0;
		auxNode = root;
		if(auxNode == null)
			throw new EmptyTreeException();
		while(auxNode!=null)
		{
			ans = auxNode.value;
			auxNode = auxNode.left;
		}
		return ans;
	}
	
	public int Max() throws EmptyTreeException
	{
		int ans=0;
		auxNode = root;
		if(auxNode == null)
			throw new EmptyTreeException();
		while(auxNode!=null)
		{
			ans = auxNode.value;
			auxNode = auxNode.right;
		}
		return ans;
	}

	// give the number of nodes with values in range [x,y]
	public int Range(int x,int y) throws EmptyTreeException
	{
		if(root == null)
			throw new EmptyTreeException();
		return Query(x,y,root,Min(),Max());
	}


	void balanceAVLTree()
	{
		int hL,hR,h2L,h2R,wL,wR;
		while(tmpNode!=null)
		{
			if(debug)
				System.out.println("Balancing "+tmpNode.value);
			if(tmpNode.left != null)
				hL = tmpNode.left.height;
			else
				hL = 0;
			if(tmpNode.right != null)
				hR = tmpNode.right.height;
			else
				hR = 0;
			
			if(hL-hR == 2)
			{
				auxNode = tmpNode.left;
				if(auxNode.left != null)
					h2L = auxNode.left.height;
				else
					h2L = 0;
				if(auxNode.right != null)
					h2R = auxNode.right.height;
				else
					h2R = 0;
				if(h2R>h2L)
				{
					leftRotate(auxNode);
					if(debug)
						System.out.println(showTree(root));
				}	
				rightRotate(tmpNode);
				if(debug)
					System.out.println(showTree(root));
			}

			else if(hL-hR == -2)
			{
				auxNode = tmpNode.right;
				if(auxNode.left != null)
					h2L = auxNode.left.height;
				else
					h2L = 0;
				if(auxNode.right != null)
					h2R = auxNode.right.height;
				else
					h2R = 0;
				if(h2L>h2R)
				{
					rightRotate(auxNode);
					if(debug)
						System.out.println(showTree(root));
				}
				leftRotate(tmpNode);
				if(debug)
					System.out.println(showTree(root));
			}

			if(tmpNode.left != null)
			{
				hL = tmpNode.left.height;
				wL = tmpNode.left.weight;
			}
			else
			{
				hL = 0;
				wL = 0;
			}	
			if(tmpNode.right != null)
			{
				hR = tmpNode.right.height;
				wR = tmpNode.right.weight;
			}
			else
			{
				hR = 0;
				wR = 0;
			}
			
			tmpNode.height = 1 + Math.max(hL,hR);
			tmpNode.weight = tmpNode.count + wL + wR;

			root = tmpNode;
			tmpNode = tmpNode.parent; 
		}
	}

	void leftRotate(Node x)
	{
		if(debug)
			System.out.println("Left rotating "+x.value);
		if(x.right == null)
		{
			System.out.println("Hacked!");
			System.exit(1);
		}
		y = x.right;
		x.weight -= y.weight;
		if(y.left != null)
		{
			x.right = y.left;
			x.weight += y.left.weight;
			y.weight -= y.left.weight;
			y.left.parent = x;
			y.left.isLeft = false;
		}
		else
			x.right = null;
		if(x.isLeft)
			x.parent.left = y;
		else if(x.parent != null)
			x.parent.right = y;
		y.parent = x.parent;
		y.isLeft = x.isLeft;
		y.left = x;
		x.parent = y;
		x.isLeft = true;
		y.weight += x.weight;
	}

	void rightRotate(Node x)
	{
		if(debug)
			System.out.println("Right rotating "+x.value);
		if(x.left == null)
		{
			System.out.println("Hacked!");
			System.exit(1);
		}
		y = x.left;
		x.weight -= y.weight;
		if(y.right != null)
		{
			x.left = y.right;
			x.weight += y.right.weight;
			y.weight -= y.right.weight;
			y.right.parent = x;
			y.right.isLeft = true;
		}
		else
			x.left = null;
		if(x.isLeft)
			x.parent.left = y;
		else if(x.parent != null)
			x.parent.right = y;
		y.parent = x.parent;
		y.isLeft = x.isLeft;
		y.right = x;
		x.parent = y;
		x.isLeft = false;
		y.weight += x.weight;
	}

	// returns true if node is found
	// updates tmpNode to x if found 
	// or to its 'parent' otherwise
	boolean Search(int x) throws EmptyTreeException
	{
		if(root == null)
			throw new EmptyTreeException();
		tmpNodeS = root;
		tmpNode = root;
		while(tmpNodeS!=null)
		{
			tmpNode = tmpNodeS;
			if(tmpNode.value == x)
				return true;
			else if(tmpNode.value > x)
				tmpNodeS = tmpNodeS.left;
			else
				tmpNodeS = tmpNodeS.right;
		}
		return false;
	}

	// returns the value of next smaller element than val
	int Predecessor(int val)
	{
		int ans=-1;
		auxNode = root;
		while(auxNode != null)
		{
			if(auxNode.value<val)
			{
				ans = auxNode.value;
				auxNode = auxNode.right;
			}
			else
				auxNode = auxNode.left;
		}
		return ans;
	}

	// returns the value of next larger element than val
	int Successor(int val)
	{
		int ans=-1;
		auxNode = root;
		while(auxNode != null)
		{
			if(auxNode.value>val)
			{
				ans = auxNode.value;
				auxNode = auxNode.left;
			}
			else
				auxNode = auxNode.right;
		}
		return ans;
	}

	// true if successor exists in subtree
	// updates successor node as auxNode
	boolean Successor(Node nd)
	{
		auxNode = nd.right;
		if(auxNode == null)
			return false;
		while(auxNode.left != null)
			auxNode = auxNode.left;
		return true;
	}
	
	// give the number of nodes with values in range [x,y]
	private int Query(int x, int y, Node nd, int lft, int rt)
	{
		if(nd == null)
			return 0;
		if(x <= lft && y >= rt)
			return nd.weight;
		else if(x > rt || y < lft)
			return 0;
		else if(y < nd.value)
			return Query(x,y,nd.left,lft,nd.value-1);
		else if(x > nd.value)
			return Query(x,y,nd.right,nd.value+1,rt);
		else
			return Query(x,y,nd.left,lft,nd.value-1) + nd.count + Query(x,y,nd.right,nd.value+1,rt);
	}

	public String showTree(Node z)
	{
		String s = "";
		if(z == null)
			s = "null";
		else
			//s = "(" + z.value + "," + showTree(z.left) + "," + showTree(z.right) + ")";
			s = "(" + z.value +"["+z.weight+"]," + showTree(z.left) + "," + showTree(z.right) + ")";
		return s;
	} 

	public String showNode(Node z)
	{
		String s = "";
		if(z == null)
			s = "null";
		else
			s = z.value + " : "+z.height;
		return s;
	}

	public void Debug()
	{
		debug ^= true;
	}

	public Node getRoot()
	{
		return root;
	}
}

class EmptyTreeException extends Exception
{
	public EmptyTreeException()
	{

	}
}
