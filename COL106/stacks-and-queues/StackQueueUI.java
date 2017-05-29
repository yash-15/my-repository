import java.util.Scanner;
class StackQueueUI
{
	public static void main(String[] args)
	{
		Scanner s = new Scanner(System.in);
		
		System.out.println("This application implements stack / queue ADT.");
		System.out.println("  Press 1 to use a stack");
		System.out.println("  Press 2 to use a queue");
		System.out.print("Enter your choice : ");
		int ch=s.nextInt();
		if(ch==1)
		{
			@SuppressWarnings("unchecked")
			ArrayStack<Integer> ADT = new ArrayStack<Integer>();
			int element;
			System.out.println("Stack created successfully!");
			System.out.println(" Press 1 to push an element ");
			System.out.println(" Press 2 to view the top element ");
			System.out.println(" Press 3 to  pop the top element ");
			System.out.println(" Press 0 to exit ");
				
			while(ch!=0)
			{
				System.out.print("Enter your choice : ");
				ch=s.nextInt();
				if(ch==1)
				{
					System.out.print("Enter the element : ");
					ch=s.nextInt();
					ADT.push(ch);
				}
				else if(ch==2)
				{
					try
					{
						element = ADT.top();
						System.out.println("Top element of the stack is : " + element);
					}
					catch(EmptyStackException e)
					{
						System.out.println("Stack is already empty!");
					}
				}
				else if(ch==3)
				{
					try
					{
						element = ADT.pop();
						System.out.println("Element popped from the stack is : " + element);
					}
					catch(EmptyStackException e)
					{
						System.out.println("Stack is already empty!");
					}
				}
			}
		}
		else if(ch==2)
		{
			@SuppressWarnings("unchecked")
			ArrayQueue<Integer> ADT = new ArrayQueue<Integer>();
			int element;
			System.out.println("Queue created successfully!");
			System.out.println(" Press 1 to enqueue an element ");
			System.out.println(" Press 2 to    view the front element ");
			System.out.println(" Press 3 to dequeue the front element ");
			System.out.println(" Press 0 to exit ");
				
			while(ch!=0)
			{
				System.out.print("Enter your choice : ");
				ch=s.nextInt();
				if(ch==1)
				{
					System.out.print("Enter the element : ");
					ch=s.nextInt();
					ADT.enqueue(ch);
				}
				else if(ch==2)
				{
					try
					{
						element = ADT.front();
						System.out.println("Front element of the queue is : " + element);
					}
					catch(EmptyQueueException e)
					{
						System.out.println("Queue is already empty!");
					}
				}
				else if(ch==3)
				{
					try
					{
						element = ADT.dequeue();
						System.out.println("Element popped from the queue is : " + element);
					}
					catch(EmptyQueueException e)
					{
						System.out.println("Queue is already empty!");
					}
				}
			}
		}
		else
		{
			System.out.println("Invalid choice!");
		}
	}
}
