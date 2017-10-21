

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * @author toby
 * A collection that takes up a fixed space in memory. 
 * Useful when old data can afford to be overwritten, and there is no need for excessive memory use. 
 *
 * @param <E>
 */
public class CircularArrayRing<E> extends AbstractCollection<E> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2209962888622925179L;
	
	private int headIndex;
	private E[] arr;
	private int noItems;
	private int N;
	
	public CircularArrayRing(){
		this(20);
	}
	
	@SuppressWarnings("unchecked")
	public CircularArrayRing(int size){
		N = size;
		arr = ((E[])new Object[N]);
		noItems = 0;
		headIndex = -1;
	}
	
	public boolean add(E e){
		synchronized(this){
			headIndex = (headIndex+1)%arr.length;
			E prev = arr[headIndex];
			arr[headIndex] = e;
			
			if (noItems < N)
				noItems++;
	
			if (prev!= null){	
				if (prev.equals(e))
					return false;
				else{
					return true;
				}
			}
			else
				return true;
		}
	}
	
	
	public E get(int index) throws IndexOutOfBoundsException {
		synchronized(this){
			if (index >= noItems || index < 0)
				throw new IndexOutOfBoundsException("index given was larger than number of elements");
			int ind = (headIndex-index);
			if (ind < 0)
			    ind += N;
			return arr[ind];
		}
	}

	@Override
	public Iterator<E> iterator() {
		synchronized(this){
			return new Iterator<E>(){
				
				private int iterations = 0;
				
				@Override
				public boolean hasNext() {
					if (iterations < noItems)
						return true;
					else
						return false;
				}
	
				@Override
				public E next() throws NoSuchElementException {
					if (!hasNext())
						throw new NoSuchElementException();
					return get(iterations++);
				}
				
			};
		}	
		
	}

	@Override
	public int size() {
		return noItems;
	}



}
