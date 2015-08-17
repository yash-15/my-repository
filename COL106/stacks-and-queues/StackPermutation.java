/**
			COL106 Assignment 1 StackPermutation
			====== ============ ================
												Designed By : 
											YASH KUMAR BANSAL 
											2013ME10742
*/

/**
								CONTENTS
								--------
	1. Stack.java 					->	Interface of Stack
	2. Queue.java					->	Interface of Queue
	3. ArrayStack.java				->	Implementation of Stack
	4. ArrayQueue.java				->	Implementation of Queue
	5. EmptyStackException.java		->	Extension of Exception class
	6. EmptyQueueException.java		->	Extension of Exception class
	7. StackQueueUI.java			->	Basic interface to use stack/queue
	8. StackPermutation.java		->	Check "Stack Permutation"
*/

import java.util.Scanner;

public class StackPermutation
{
	public static <E> boolean CheckStackPermutation(ArrayQueue<E> Q1, ArrayQueue<E> Q2, ArrayStack<E> S, ArrayQueue<String> operations)
	{
		while(!Q2.isEmpty())
		{
			try
			{
				if(Q2.front()==S.top())
				{
					operations.enqueue("Enqueue(Q2,Pop(S))");
					S.pop();
					Q2.dequeue();
					continue;
				}
			}
			catch(EmptyStackException e)
			{

			}
			catch(EmptyQueueException e)
			{
				System.err.println("Internal error occured!");
			}
			try
			{
				if(Q2.front()==Q1.front())
				{
					operations.enqueue("Enqueue(Q2,Dequeue(Q1))");
					Q1.dequeue();
					Q2.dequeue();
				}
				else
				{
					operations.enqueue("Push(S,Dequeue(Q1))");
					S.push(Q1.dequeue());
				}
			}
			catch(EmptyQueueException e)
			{
				return false;
			}
		}
		return true;
	}
	public static void main(String[] args)
	{
		int n;
		int i;
		boolean isStackPermutation=false;
		Scanner s = new Scanner(System.in);
		System.out.println();
		System.out.println("This application checks whether the given permutation is a *stack permutation* !");
		System.out.print("Enter the number of elements : ");
		n=s.nextInt();
		@SuppressWarnings("unchecked")
		ArrayQueue<Integer> Q1 = new ArrayQueue<Integer>();
		ArrayQueue<Integer> Q2 = new ArrayQueue<Integer>();
		ArrayStack<Integer> S = new ArrayStack<Integer>();
		ArrayQueue<String> operations = new ArrayQueue<String>();
		
		System.out.println("Enter the "+n+"-integer permutation : ");

		for(i=1;i<=n;i++)
		{
			Q1.enqueue(i);
			Q2.enqueue(s.nextInt());
		}
		isStackPermutation=CheckStackPermutation(Q1,Q2,S,operations);
		if(isStackPermutation)
		{
			System.out.println();
			System.out.println(" Yes. It is a stack permutation.");
			System.out.println();
			System.out.println(" It can be obtained by following operations : ");
			for(i=1;!operations.isEmpty();i++)
				try
				{
					System.out.println(i+"  "+operations.dequeue());
				}
				catch(EmptyQueueException e)
				{
					System.err.println("Internal error occured!");
				}
		}
		else
		{
			System.out.println();
			System.out.println(" No. It is not a stack permutation.");
			System.out.println();
			System.out.println("Following operations can be performed successfully : ");
			for(i=1;!operations.isEmpty();i++)
				try
				{
					System.out.println(i+"  "+operations.dequeue());
				}
				catch(EmptyQueueException e)
				{
					System.err.println("Internal error occured!");
				}
			
			System.out.println();

			try
			{
				System.out.println("But now "+Q2.front()+" can not be inserted into Q2.");
			}
			catch(EmptyQueueException e)
			{
				System.err.println("Internal error occured!");
			}
		}
	}
}
