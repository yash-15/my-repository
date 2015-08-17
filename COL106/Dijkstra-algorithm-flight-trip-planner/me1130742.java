/**
			COL106 Assignment 5  Flight Trip Planner
			====== ============ ========================
												Designed By : 
											YASH KUMAR BANSAL 
											2013ME10742
											Group 3
*/

/// Implemeted using Dijkstra's Algorithm

import java.util.*;
import java.io.*;

public class me1130742
{	
	public static void main(String[] args) 
	{
		boolean debug = false;;
		int n,m,u,v,t1,t2,price,i,t;
		String name;
		Scanner input;
		File file= new File("test.txt");
		try
		{
			input = new Scanner(file);
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File test.txt not found.");
			System.out.println("Enter the data on console.");
			input = new Scanner(System.in);
		}
		n = input.nextInt();		
		m = input.nextInt();

		Graph g = new Graph(n);
		
		for(i=0;i<m;i++)
		{
			u = input.nextInt();
			v = input.nextInt();
			t1 = input.nextInt();
			t2 = input.nextInt();
			name = input.next();
			price = input.nextInt();
			
	if(debug)
		System.out.println(u+" "+v+" "+t1+" "+t2+" "+name+" "+price);

			u--;
			v--;
			t1=(t1/100)*60+(t1%100);
			t2=(t2/100)*60+(t2%100)+30;

			g.addEdge(u,v,t1,t2,price);
			if(debug)
				System.out.println("Edge inserted!");
		}

		t = input.nextInt();
		while(t-->0)
		{
			u = input.nextInt();
			v = input.nextInt();
			t1 = input.nextInt();
			t2 = input.nextInt();
			u--;
			v--;
			t1=(t1/100)*60+(t1%100);
			t2=(t2/100)*60+(t2%100)+30;
			
			price = g.fastDijkstra(u,v,t1,t2);
			System.out.println(price);
		}
	}
};
class EdgeNode
{
	int src,dest,depTime,arrTime,cost;
	EdgeNode(int src,int dest,int depTime,int arrTime,int cost)
	{
		this.src = src;
		this.dest = dest;
		this.depTime = depTime;
		this.arrTime = arrTime;
		this.cost = cost;
	}
	EdgeNode()
	{}
};

class DijkstraNode
{
	int arrTime,minCost;
	DijkstraNode(int t, int c)
	{
		arrTime = t;
		minCost = c;
	}
};

@SuppressWarnings("unchecked")
class Graph
{
	boolean fast,built;

	FastLinkedList<EdgeNode> oneList;
	
	FastLinkedList[] adj;
	EdgeNode tempEdge;

	int[][] dij;
	boolean[][] dep,arr;

	FastLinkedList[] dp;
	Heap<HeapNode> q;

	final int N;
	Graph(int n)
	{
		N = n;
		if(N<=100)
		{
			dij = new int[N][1500];
			dep = new boolean[N][1500];
			arr = new boolean[N][1500];
			adj = new FastLinkedList[N*1500];
				for(int i=0;i<N*1500;i++)
			adj[i] = new FastLinkedList<EdgeNode>();
			fast = true;
			built = false;
		}
		else
		{
			fast = false;
			adj = new FastLinkedList[N];
			for(int i=0;i<N;i++)
				adj[i] = new FastLinkedList<EdgeNode>();
		
		}
	}
	void addEdge(int u,int v,int t1,int t2,int price)
	{
		tempEdge = new EdgeNode(u,v,t1,t2,price);
		if(fast)
		{	
			adj[u*1500+t1].Insert(tempEdge);
			dep[u][t1]=true;
			arr[v][t2]=true;
		}
		else
			adj[u].Insert(tempEdge);
	}

	void build()
	{
		if(!fast)
			return;
		int u,i,j,curArr,nxtDep;
		for(u=0;u<N;u++)
		{
			curArr = -1;
			nxtDep = -1;
			for(i=0;i<1500;i++)
			{
				if(arr[u][i])
				{
					if(curArr!=-1)
					{
						tempEdge = new EdgeNode(u,u,curArr,i,0);
						adj[u*1500+curArr].Insert(tempEdge);
					}
					curArr = i;
				}
				else if(dep[u][i])
				{
					if(curArr!=-1)
					{
						tempEdge = new EdgeNode(u,u,curArr,i,0);
						adj[u*1500+curArr].Insert(tempEdge);
					}
				}
			}	
		}
		built = true;
	}

	int fastDijkstra(int u,int v,int t1,int t2)
	{
		if(t1>t2)
			return -1;

		if(!fast)
			return dijkstra(u,v,t1,t2);

		if(!built)
			build();
		for(int i=0;i<N;i++)
			Arrays.fill(dij[i],Integer.MAX_VALUE);
		q = new Heap<HeapNode>(N*1500,true);
		HeapNode tmpNode,addNode,destNode;
		EdgeNode flight;
		int city,time,cost,src,dest;

		try
		{
			while(t1<=t2)
			{
				dij[u][t1]=0;
				if(dep[u][t1])
				{
					addNode = new HeapNode(u,t1,0);
					q.Insert(addNode);
				}
				t1++;
			}
			while(!q.isEmpty())
			{
				tmpNode = q.Top();
				q.Delete();
				city = tmpNode.city;
				cost = tmpNode.cost;
				time = tmpNode.time;
				src  = city*1500+time;
//				System.out.println("In Queue : "+(city+1)+" "+(time/60)+":"+(time%60)+" "+cost);

				if(tmpNode.city==v)
					return tmpNode.cost;
				
				flight = (EdgeNode) adj[src].getFront();
				
				while(flight!=null)
				{
					if(flight.arrTime > t2)
					{
						flight = (EdgeNode) adj[src].getNext(flight);
						continue;
					}
					dest = flight.dest*1500+flight.arrTime;
					if(cost+flight.cost<dij[flight.dest][flight.arrTime])
					{
						if(!q.hasNode(dest))
						{
							addNode = new HeapNode(flight.dest,flight.arrTime,(cost+flight.cost));
							q.Insert(addNode);
							dij[flight.dest][flight.arrTime] = cost+flight.cost;
//				System.out.println("       i : "+(flight.dest+1)+" "+(flight.arrTime/60)+":"+(flight.arrTime%60)+" "+(cost+flight.cost));
						}
						else
						{
							destNode = q.getNode(dest);
							dij[flight.dest][flight.arrTime] = cost+flight.cost;
							destNode.cost=cost+flight.cost;
							q.Update(flight.dest*1500+flight.arrTime);						
//				System.out.println("       u : "+(flight.dest+1)+" "+(flight.arrTime/60)+":"+(flight.arrTime%60)+" "+(cost+flight.cost));
						}
					}
					flight = (EdgeNode) adj[src].getNext(flight);
				}
			}
			return -1;
		}
		catch(OverflowException e)
		{
			System.out.println("Source not inserted in heap");
			return -1;
		}
		catch(CustomException e)
		{
			System.out.println("Linked list internal error!");
			return -1;
		}
	}

	int dijkstra(int u,int v,int t1,int t2)
	{
		// dp list stores data in increasing order of cost
		dp = new FastLinkedList[N];
		for(int i=0;i<N;i++)
			dp[i] = new FastLinkedList<DijkstraNode>();
		q  = new Heap<HeapNode>();
		HeapNode tmpNode,addNode;
		EdgeNode flight;
		int city,time,cost;

		addNode = new HeapNode(u,t1,0);
		try
		{
			q.Insert(addNode);
		}
		catch(OverflowException e)
		{
			System.out.println("Source not inserted in heap");
		}
		while(!q.isEmpty())
		{
			tmpNode = q.Top();
			q.Delete();
			if(tmpNode.city==v)
				return tmpNode.cost;
			city = tmpNode.city;
			cost = tmpNode.cost;
			time = tmpNode.time;
//			System.out.println("In Queue : "+(city+1)+" "+cost);
			if(!viable(city,time,cost,true))
				continue;
			flight = (EdgeNode) adj[city].getFront();
			
			try
			{
				while(flight!=null)
				{
					if(flight.depTime < time || flight.arrTime > t2)
					{
						flight = (EdgeNode) adj[city].getNext(flight);
						continue;
					}
					if(viable(flight.dest,flight.arrTime,cost+flight.cost,false))
					{
//						System.out.println(" : yes");
						addNode = new HeapNode(flight.dest,flight.arrTime,cost+flight.cost);
						q.Insert(addNode);	
					}
					else
//						System.out.println(" : no");
					flight = (EdgeNode) adj[city].getNext(flight);
				}
			}
			catch(CustomException e)
			{
				System.out.println("Linked list internal error!");
				return -1;
			}
			catch(OverflowException e)
			{
				System.out.println("Priority Queue full!");
				return -1;
			}
		}
		return -1;
	}
	boolean viable(int dest,int t1,int cost,boolean recheck)
	{
//		System.out.println("Try Insert "+cost+","+t1+" at "+(dest+1));
		DijkstraNode tmpNode,addNode; 
		tmpNode = (DijkstraNode) dp[dest].getFront();
		if(tmpNode==null)
		{
//			System.out.println("   : first node");
			addNode = new DijkstraNode(t1,cost);
			dp[dest].Insert(addNode);
			return true;
		}
		try
		{
			while(tmpNode!=null)
			{
				if((cost<tmpNode.minCost && t1<=tmpNode.arrTime)
				||(cost<=tmpNode.minCost && t1<tmpNode.arrTime))
				{
					addNode = new DijkstraNode(t1,cost);
				/*	System.out.println("   : "+tmpNode.toString());
					System.out.println("   : "+tmpNode.minCost+","+tmpNode.arrTime);	
				*/	dp[dest].insertBefore(addNode,tmpNode);
					tmpNode = addNode;
//					System.out.println("Add: "+addNode.minCost+","+addNode.arrTime);
					//tmpNode = (DijkstraNode) dp[dest].getNext(tmpNode);
					while(tmpNode != null)
					{
				/*		System.out.println("   : "+tmpNode.toString());
						System.out.println("   : "+tmpNode.minCost+","+tmpNode.arrTime);
						System.out.println("   : "+dp[dest].getCurrent().toString());
						System.out.println("   : "+((DijkstraNode)dp[dest].getCurrent()).minCost+","+((DijkstraNode)dp[dest].getCurrent()).arrTime);
				*/		if(tmpNode.minCost>=cost && tmpNode.arrTime>=t1)
						{
							dp[dest].Delete(tmpNode);
							tmpNode = (DijkstraNode) dp[dest].getCurrent();
//							System.out.println("     : deleted");
						}
						else
							return true;
					}
				}
				else if(cost == tmpNode.minCost && t1==tmpNode.arrTime)
					return recheck;
				else if(cost>= tmpNode.minCost && t1>=tmpNode.arrTime)
					return false;
				else
				{
//					System.out.println("   : "+tmpNode.minCost+","+tmpNode.arrTime);
					tmpNode = (DijkstraNode) dp[dest].getNext(tmpNode);			
				}
			}

			if(tmpNode == null)
			{
				addNode = new DijkstraNode(t1,cost);
				dp[dest].insertAfter(addNode,dp[dest].getPrevious());
//				System.out.println("   : Last element");				
//				System.out.println("   : "+addNode.minCost+","+addNode.arrTime);
				return true;
			}
		}
		catch(CustomException e)
		{
			System.out.println("Linked list internal error!");
		}
		throw new RuntimeException();
	}
}

class HeapNode implements Comparable<HeapNode>
{
	int city,time,cost;
	HeapNode(int city, int time, int cost)
	{
		this.city  = city;
		this.time   = time;
		this.cost   = cost;
	}
	public int compareTo(HeapNode obj)
	{
		if(this.cost < obj.cost)
			return -1;
		else if(this.cost > obj.cost)
			return 1;
		else if(this.time < obj.time)
			return -1;
		else if(this.time > obj.time)
			return 1;
		else
			return this.city - obj.city;
	}
	public int hashCode()
	{
		return city*1500+time;
	}
};

@SuppressWarnings("unchecked")
class Heap< E extends Comparable<E> >
{
	boolean changePriority;
	int size,pos,temp;
	final int MAX;
	E tempObj;

	int[] nodePos;
	E[] arr;
	
	Heap()
	{
		MAX = 1000000;
		arr = (E[]) new Comparable[MAX+1];
		changePriority = false;
		size=0;
	}
	Heap(int maxsize)
	{
		MAX = maxsize;
		arr = (E[]) new Comparable[MAX+1];
		changePriority = false;
		size=0;
	}
	Heap(int maxsize,boolean allow)
	{
		MAX = maxsize;
		arr = (E[]) new Comparable[MAX+1];
		changePriority = allow;
		if(changePriority)
		{
			try
			{
				nodePos = new int[MAX+1];
			}
			catch(OutOfMemoryError e)
			{
				changePriority = false;
			}
		}
		size=0;
	}
	boolean isEmpty()
	{
		return size==0;
	}
	void Insert(E data) throws OverflowException
	{
		if(size==MAX)
			throw new OverflowException();
		size++;
		if(data == null)
			System.out.println("No data!");	 
	 
		arr[size] = data;
		if(changePriority)
			nodePos[arr[size].hashCode()]=size;
		HeapifyUp(size);
	}

	E Top() 
	{
		if(size==0)
			throw new RuntimeException();
		return arr[1];
	}

	void Delete()
	{
		if(size==0)
			throw new RuntimeException();
		arr[1]=arr[size];
		size--;
		if(changePriority)
			nodePos[arr[1].hashCode()]=1;
		HeapifyDown(1);
	}

	void HeapifyUp(int pos)
	{
		while(pos>1)
		{
			if(arr[pos].compareTo(arr[pos/2])<0)
			{
				Swap(pos,pos/2);
				pos/=2;
			}
			else
				break;
		}
	}

	void HeapifyDown(int pos)
	{
		if(pos>(size/2))
			return;
		else if(pos==(size/2) && size%2==0)
		{
			if(arr[pos].compareTo(arr[pos+pos])>0)
				Swap(pos,pos+pos);
		}
		else
		{
			if(arr[pos+pos].compareTo(arr[pos+pos+1])<0)
			{
				if(arr[pos].compareTo(arr[pos+pos])>0)
				{
					Swap(pos,pos+pos);
					HeapifyDown(pos+pos);
				}
			}
			else
			{
				if(arr[pos].compareTo(arr[pos+pos+1])>0)
				{
					Swap(pos,pos+pos+1);
					HeapifyDown(pos+pos+1);
				}
			}
		}
	}

	void Swap(int a,int b)
	{
		tempObj = arr[a];
		arr[a]  = arr[b];
		arr[b]  = tempObj;
		if(changePriority)
		{
			nodePos[arr[a].hashCode()]=a;
			nodePos[arr[b].hashCode()]=b;
		}
	}

	boolean hasNode(int index)
	{
		if(index<0 || index>=MAX || !changePriority)
			throw new RuntimeException();
		return nodePos[index]>0;
	}

	E getNode(int index)
	{
		if(index<0 || index>=MAX || !changePriority)
			throw new RuntimeException();
		return arr[nodePos[index]];
	}

	void Update(int index) throws RuntimeException
	{
		if(index<0 || index>=MAX || !changePriority)
			throw new RuntimeException();
		pos = nodePos[index];
		HeapifyDown(pos);
		HeapifyUp(pos);
	}	

	void Show()
	{
		for(int i=1;i<=size;i++)
		{
			if(i>1 && (i&(i-1))==0)
				System.out.println("");
			System.out.print(arr[i].toString()+" ");
		}
		System.out.println("");
	}
};

class FastLinkedList<E>
{
	class ListNode<E>
	{
		E data;
		ListNode<E> next;
		public ListNode(E data)
		{
			this.data = data;
		}
	};

	ListNode<E> front,tmpNode,cache0,cache1;

	boolean isEmpty()
	{
		return front==null;
	}

	public E getFront()
	{
		cache0 = null;
		cache1 = front;
		if(front == null)
			return null;
		return front.data;
	}

	public void Insert(E node)	// insert at beginning
	{
		tmpNode = new ListNode<E>(node);
		if(front==null)
			front = tmpNode;
		else
		{
			tmpNode.next = front;
			front = tmpNode;
		}
		cache0 = null;
		cache1 = front;
	}
	public void insertAfter(E node,E prev) throws CustomException
	{
		if(prev==null)
		{
			Insert(node);
			return;
		}
		tmpNode = new ListNode<E>(node);
		if(cache0==null)
			throw new CustomException();
		if(cache0.data == prev)
		{
			tmpNode.next = cache1;
			cache0.next = tmpNode;
			cache0 = tmpNode;
		}
		else
			throw new CustomException();
	}
	public void insertBefore(E node,E curr) throws CustomException
	{
		if(curr==null)
			throw new CustomException();
		if(curr == front.data)
		{
			Insert(node);
			return;
		}
		tmpNode = new ListNode<E>(node);
		
		if(cache1==null)
			throw new CustomException();
		if(cache1.data == curr)
		{
			tmpNode.next = cache1;
			cache0.next = tmpNode;
			cache0 = tmpNode;
		}
		else
			throw new CustomException();
	}
	public void Delete(E node)throws CustomException
	{
		if(cache1==null)
			throw new CustomException();
		if(node == cache1.data)
		{
			if(cache1 == front)
			{
				front = front.next;
				cache1 = front;
			}
			else
			{
				cache0.next = cache1.next;
				cache1 = cache1.next;
			}
		}
		else
			throw new CustomException();
	}
	public E getNext(E data) throws CustomException
	{
		if(cache1==null)
			throw new CustomException();
		if(cache1.data == data)
		{
			cache0 = cache1;
			cache1 = cache1.next;
			if(cache1 == null)
				return null;
			else
				return cache1.data;
		}
		else
		{
			System.out.println("$$"+cache1.data.toString());
			System.out.println("$$"+data.toString());
			throw new CustomException();
		}
	}
	public E getCurrent()
	{
		if(cache1 == null)
			return null;
		else
			return cache1.data;
	}
	public E getPrevious()
	{
		if(cache0 == null)
			return null;
		else
			return cache0.data;
	}
};

class OverflowException extends Exception
{
	public OverflowException()
	{

	}
}

class CustomException extends Exception
{
	CustomException()
	{
		throw new RuntimeException(); 
	}
};
