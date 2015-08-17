class ArrayQueue<E> implements Queue<E>
{
	public static final int MAX=100000;
	int size,front,rear;
	
	@SuppressWarnings("unchecked")
	E[] data = (E[]) new Object[MAX];
	
	public ArrayQueue()
	{
		size=0;
		front=0;
		rear=-1;
	}
	
	public int size()
	{
		return size;
	}

	public boolean isEmpty()
	{
		return size<=0;
	}

	public E front() throws EmptyQueueException
	{
		if(this.isEmpty())
			throw new EmptyQueueException();
		else
			return data[front];
	}

	public void enqueue(E element)
	{
		rear++;
		if(rear==MAX)
			rear=0;
		data[rear]=element;
		size++;
	}

	public E dequeue() throws EmptyQueueException
	{
		if(this.isEmpty())
			throw new EmptyQueueException();
		else
		{
			E element = data[front];
			size--;
			front++;
			if(front==MAX)
				front=0;
			return element;
		}
	}
}
