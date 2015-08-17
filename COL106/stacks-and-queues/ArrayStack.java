@SuppressWarnings("unchecked")
class ArrayStack<E> implements Stack<E>
{
	public static int MAX=100000;
	int top;
		E[] data = (E[]) new Object[MAX];
		public ArrayStack()
	{
		top=-1;
		System.out.println("H");
		
		for(int i=0;i<MAX;i++)
		{
			data[i] = (E) new Object();	
		}
	}
	
	public int size()
	{
		return top+1;
	}

	public boolean isEmpty()
	{
		return top<0;
	}

	public E top() throws EmptyStackException
	{
		if(this.isEmpty())
			throw new EmptyStackException();
		else
			return data[top];
	}

	public void push(E element)
	{
		top++;
		data[top]=element;
		for(int i=0;i<=top;i++)
			System.out.print(data[i].toString()+" ");
		System.out.println("");
	}

	public E pop() throws EmptyStackException
	{
		if(this.isEmpty())
			throw new EmptyStackException();
		else
		{
			top--;
			return data[top+1];
		}
	}
}
