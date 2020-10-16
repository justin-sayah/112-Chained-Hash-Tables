
/*
 * Author: Justin Sayah
 * 
 * A custom implementation of a chained hash table using linked lists.
 * 
 * Includes functionality to insert, search, and remove key value pairs.
 * 
 * Additional functionality to resize the hash table if necessary, as well as return all keys in table.
 * 
 */

public class ChainedHashTable implements HashTable{

	
	private class BucketNode {
		private Object key;
		private BucketNode next;
		private LLQueue<Object> values;
		
		private BucketNode(Object key, Object value) {
			this.key = key;
			values = new LLQueue<Object>();
			values.insert(value);
			this.next = null;
		}
	}
	
	
	private int numKeys;
	private BucketNode[] table;
	
	
	/*
	 * Constructor to initialize hash table with given size parameter.
	 */
	public ChainedHashTable(int size) {
		if (size <= 0){
			throw new IllegalArgumentException("size must be positive");
		}
		table = new BucketNode[size];
		int numKeys = 0;
	}
	
	/* first hash function */
    public int h1(Object key) {
        int h1 = key.hashCode() % table.length;
        if (h1 < 0) {
            h1 += table.length;
        }
        return h1;
    }
	/*
     * insert - insert the specified (key, value) pair in the hash table.
     * Returns true if the pair can be added and false if there is overflow.
     */
	public boolean insert(Object key, Object value) {
		if (key == null) {
            throw new IllegalArgumentException("key must be non-null");
        }
		int index = h1(key);
		
		if(table[index] == null) {
			table[index] = new BucketNode(key, value);
			numKeys++;
			return true;
		}
		
		boolean inserted = false;
		BucketNode prev = null;
		BucketNode trav = table[index];
		
		while(trav != null && inserted == false) {
			if(key.equals(trav.key)) {
				trav.values.insert(value);
				return true;
			}
			else {
				prev = trav;
				trav = trav.next;
			}
		}
		prev.next = new BucketNode(key,value);
		numKeys++;
		return true;
	}
	
	/*
     * search - search for the specified key and return the
     * associated collection of values, or null if the key 
     * is not in the table
     */
    public Queue<Object> search(Object key){
    	if (key == null) {
            throw new IllegalArgumentException("key must be non-null");
        }
    	
    	int index = h1(key);
    	if(table[index] == null) {
    		return null;
    	}
    	else {
			BucketNode trav = table[index];

			while (trav != null) {
				if (key.equals(trav.key)) {
					return trav.values;
				} else {
					trav = trav.next;
				}
			}
			return null;
    	}
    }
    
    /* 
     * remove - remove from the table the entry for the specified key
     * and return the associated collection of values, or null if the key 
     * is not in the table
     */
    public Queue<Object> remove(Object key){
    	if (key == null) {
            throw new IllegalArgumentException("key must be non-null");
        }
    	
    	int index = h1(key);
    	if(table[index] == null) {
    		return null;
    	}
    	
    	BucketNode prev = null;
    	BucketNode trav = table[index];
    	boolean keyFound = false;
    	
    	while(trav != null && keyFound == false) {
    		if(key.equals(trav.key)) {
    			Queue<Object> temp = trav.values;
    			table[index] = trav.next;
    			numKeys --;
    			return temp;
    		}
    		else {
    			prev = trav;
    			trav = trav.next;
    		}
    	}
    	return null;
    	
    }
    
    /* 
     * toString - print the table in its entirety
     */
    public String toString() {
    	String solution = "{";
    	
    	for(int i = 0; i < table.length; i++) {
    		if(table[i] == null) {
    			solution += "null, ";
    		}
    		else {
    			String helper = "[";
    			BucketNode trav = table[i];
    			while(trav != null) {
    				helper += trav.key.toString() + ", ";
    				trav = trav.next;
    			}
    			helper = helper.substring(0, helper.length() - 2);
    			helper += "], ";
    			solution += helper;
    		}
    	}
    	solution = solution.substring(0, solution.length() - 2);
    	solution += "}";
    	return solution;
    }
    
    /*
     * getNumKeys - retursn the number of keys in the hash table
     */
    public int getNumKeys() {
    	return numKeys;
    }
    
    public double load() {
    	return (double)numKeys/table.length; 
    }
    
    /*
     * getAllKeys - returns an Object array containing all the non-null keys in the hash table
     */
    public Object[] getAllKeys() {
    	Object[] solution = new Object[numKeys];
    	int pointer = 0;
    	
    	for(int i = 0; i < table.length; i++) {
    		if(table[i] != null) {
    			BucketNode trav = table[i];
    			while(trav != null) {
    				solution[pointer] = trav.key;
    				pointer++;
    				trav = trav.next;
    			}
    		}
    	}
    	return solution;
    }
    
    /*
     * resize - resizes an existing hash table to a newSize
     */
    public void resize(int newSize) {
    	if (newSize <= 0 || newSize < table.length){
			throw new IllegalArgumentException("size must be positive and larger than old size");
		}
    	
    	BucketNode[] newTable = new BucketNode[newSize];
    	BucketNode[] oldTable = table;
    	table = newTable;
    	
    	for(int i = 0; i < oldTable.length; i++) {
    		if(oldTable[i] != null) {
    			BucketNode trav = oldTable[i];
    			while(trav != null) {
    				while(!trav.values.isEmpty()) {
    					this.insert(trav.key, trav.values.remove());
    				}
    				trav = trav.next;
    			}
    		}
    	}
    }
}
