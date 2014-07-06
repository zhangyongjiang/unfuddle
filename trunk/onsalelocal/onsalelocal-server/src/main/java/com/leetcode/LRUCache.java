package com.leetcode;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LRUCache {
	private static final Logger logger = Logger.getLogger(LRUCache.class.getSimpleName());
	
	private int capacity;
	private Map<Integer, CacheItem> cache;
	private CacheItem first;
	private CacheItem last;

	public LRUCache(int capacity) {
		this.capacity = capacity;
		cache = new HashMap<Integer, CacheItem>();
	}

	public int get(int key) {
		synchronized (cache) {
	        CacheItem ci = cache.get(key);
	        if(ci == null)
	        	return -1;
	        if(ci != last) {
	        	moveToLast(ci);
	        }
	        return ci.value;
        }
	}
	
	private void moveToLast(CacheItem ci) {
    	ci.prev.next = ci.next;
    	ci.next.prev = ci.prev;
    	ci.prev = last;
    	ci.next = null;
    	last.next = ci;
    	last = ci;
	}
	
	private void checkCapacity() {
		if(cache.size()>=capacity) {
			CacheItem next = first.next;
			next.prev = null;
			cache.remove(first.key);
			logger.log(Level.FINE, "remove " + first.key + ":" + first.value);
			first = next;
		}
	}

	public void set(int key, int value) {
		synchronized (cache) {
			checkCapacity();
	        CacheItem ci = cache.get(key);
	        if(ci ==  null) {
				ci = new CacheItem();
				ci.key = key;
				ci.value = value;
				cache.put(key, ci);
				
				if(first == null) {
					first = ci;
					last = ci;
				}
				else {
					last.next = ci;
					ci.prev = last;
					ci.next = null;
					last = ci;
				}
	        }
	        else if (ci != last){
	        	moveToLast(ci);
	        }
		}
	}

	public void print() {
		if(first == null)
			System.out.println("empty cache");
		else
			first.print();
	}

	private static class CacheItem {
		int key;
		int value;
		CacheItem prev;
		CacheItem next;
		
		public void print() {
			CacheItem head = this;
			while(head.prev != null)
				head = head.prev;
			do {
				System.out.print(head.key + ":" + head.value + " ");
				head = head.next;
			} while (head != null);
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
	    LRUCache cache = new LRUCache(3);
	    cache.set(0, 0);
	    cache.set(1, 11);
	    cache.set(2, 22);
	    cache.print();

	    cache.get(1);
	    cache.print();
	    
	    cache.set(3, 33);
	    cache.print();
    }
}
