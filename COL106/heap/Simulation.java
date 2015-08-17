/**
			COL106 Assignment 4 Simulation based on Heap
			====== ============ ========================
												Designed By : 
											YASH KUMAR BANSAL 
											2013ME10742
											Group 3
*/
import java.util.Scanner;
import java.util.Random;
import java.util.Comparator;

class Counter implements Comparable<Counter> 
{
	int index,length;
	double lastOut;

	Counter()
	{
		index   = 0;
		length  = 0;
		lastOut = 0.0;
	}
	public int hashCode()
	{
		return index;
	}
	public int compareTo(Counter obj)
	{
		if(this.length < obj.length)
			return -1;
		else if(this.length > obj.length)
			return 1;
		else
			return this.index - obj.index;
	}	
	public String toString()
	{
		String str="("+this.index+","+this.length+","+this.lastOut+")";
		return str;
	}
}

class Customer implements Comparable<Customer>
{
	int queNo;
	double arrTime,depTime;
	void Set(int q, double in, double out)
	{
		queNo   = q;
		arrTime = in;
		depTime = out;
	}
	public int compareTo(Customer obj)
	{
		if(this.depTime < obj.depTime)
			return -1;
		else if(this.depTime > obj.depTime)
			return 1;
		else
			return this.queNo - obj.queNo;
	}
	public String toString()
	{
		String str="("+this.queNo+","+this.arrTime+","+this.depTime+")";
		return str;
	}
}

public class Simulation
{
	boolean debug;
	Heap<Counter> h1;
	Heap<Customer> h2;
	double totalWaitTime;

	Simulation(int n, int k, double lambda, double myu, double sigma)
	{
		debug = false;
		totalWaitTime = 0.0;
		
		h1 = new Heap<Counter>(Counter.class,k,true);
		h2 = new Heap<Customer>(Customer.class,n); 
		
		if(debug)
			System.out.println("Heaps created!");
		
		createCounters(k);
		serveCustomers(n,lambda,myu,sigma);
		System.out.printf("\nSimulation Completed Successfully\nAverage Waiting Time is : %.2f\n",(totalWaitTime/n));
	}
	
	void createCounters(int k)
	{
		//Inserting k 'counters' in Heap1
		Counter tempCounter;
		for(int i=0;i<k;i++)
		{
			try
			{
				tempCounter = Counter.class.newInstance();
				tempCounter.index = i;
				h1.Insert(tempCounter);
			}
			catch(OverflowException e)
			{
				System.out.println("Too many counters in the store...");
			}
			catch(InstantiationException e)
			{
				System.out.println("Node tempCounter not created!!");
			}
			catch(IllegalAccessException e)
			{
				System.out.println("Node tempCounter not created!");
			}
		}

		if(debug)
			h1.Show();
	}
	void serveCustomers(int n, double lambda, double myu, double sigma)
	{
		//processing n customers
		double arrTime = 0.0,serviceTime,waitTime,depTime;
		
		Random r = new Random();
		
		Counter tempCounter;
		Customer tempCustomer;

		for(int i=1;i<=n;i++)
		{
			arrTime+=(-Math.log(r.nextDouble()))/lambda;
			// now arrTime stores arrival time of ith customer
			
			while(!h2.isEmpty())
			{
				tempCustomer = h2.Top();
				if(tempCustomer.depTime<arrTime)
				{
					// departure takes place now
					tempCounter = h1.getNode(tempCustomer.queNo);	
					// gives counter from which the customer leaves 
					tempCounter.length--;
					// length decreases but time at which counter will get empty remains same
					if(tempCounter.index!=tempCustomer.queNo)
					{
						System.out.println("2 Something went seriously wrong...");
					}
					h1.Update(tempCounter.index);
					if(debug)
					{
						System.out.printf("Departure from queue %d at time %.2f\n",tempCustomer.queNo,tempCustomer.depTime);
						h1.Show();
					}
					h2.Delete();
				}
				else
					break;	
			}
			
			serviceTime = myu+sigma*r.nextGaussian();

			try
			{
				// arrival of ith customer takes place now
				tempCounter = h1.Top();
				// ith customer goes to counter pointed by tempCounter
				waitTime = Math.max(0.0,tempCounter.lastOut - arrTime);
				depTime = arrTime+waitTime+serviceTime;
				totalWaitTime+=waitTime;
				try
				{
					tempCustomer = Customer.class.newInstance();
					tempCustomer.Set(tempCounter.index, arrTime, depTime);
					h2.Insert(tempCustomer);
				}
				catch(OverflowException e)
				{
					System.out.println("Too many customers in the store simultaneously...");
				}
				catch(InstantiationException e)
				{
					System.out.println("Node for customer not created!!");
				}
				catch(IllegalAccessException e)
				{
					System.out.println("Node for customer not created!");
				}
				tempCounter.length++;
				tempCounter.lastOut = depTime;
				h1.Update(tempCounter.index);
				if(debug)
				{
					System.out.printf("Arrival of customer %d in queue %d at time %.2f\n",i,tempCounter.index,arrTime);
					h1.Show();
				}
			}
			catch(RuntimeException e)
			{
				System.out.println("No counters in the store...");
			}		
		}
	}
	public static void main(String[] args) 
	{
		int n,k;
		double lambda,myu,sigma;
		Scanner input = new Scanner(System.in);
		System.out.print("Enter number of customers      : ");
		n      = input.nextInt();
		System.out.print("Enter number of counters       : ");
		k      = input.nextInt();
		System.out.print("Enter average arrival rate     : ");
		lambda = input.nextDouble();
		System.out.print("Enter average service time     : ");
		myu    = input.nextDouble();
		System.out.print("Enter variance of service time : ");
		sigma  = input.nextDouble(); 
		Simulation obj = new Simulation(n,k,lambda,myu,Math.sqrt(sigma));
			
	}
}

@SuppressWarnings("unchecked")
class Heap<E extends Comparable<E>	 >
{
	boolean changePriority;
	int size,pos,temp;
	static final int MAX=1000000;
	E tempObj;
	
	int[] nodePos = new int[MAX];
	E[] arr = (E[]) new Comparable[MAX+1];
	
	Heap(Class<E> obj,int maxsize, boolean allow)
	{
		changePriority = allow;
		size=0;
	for(int i=0;i<=maxsize;i++)
			try
			{
				arr[i] = obj.newInstance();
			}
			catch(InstantiationException e)
			{
				System.out.println("Nodes not created!!");
			}
			catch(IllegalAccessException e)
			{
				System.out.println("Nodes not created!");
			}
	}
	Heap(Class<E> obj,int maxsize)
	{
		changePriority = false;
		size=0;
		for(int i=0;i<=maxsize;i++)
			try
			{
				arr[i] = obj.newInstance();
			}
			catch(InstantiationException e)
			{
				System.out.println("Nodes not created!!");
			}
			catch(IllegalAccessException e)
			{
				System.out.println("Nodes not created!");
			}
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
		while(pos>0)
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
}
class OverflowException extends Exception
{
	public OverflowException()
	{

	}
}
