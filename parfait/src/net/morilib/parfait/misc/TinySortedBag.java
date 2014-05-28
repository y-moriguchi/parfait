package net.morilib.parfait.misc;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

public class TinySortedBag<T> implements Iterable<T> {

	//
	private static class Iter<T> implements Iterator<T> {

		//
		private Iterator<Map.Entry<T, Integer>> iter;
		private Map.Entry<T, Integer> ent = null;
		private int cnt = 0;

		//
		Iter(Iterator<Map.Entry<T, Integer>> i) {
			iter = i;
			if(i.hasNext())  ent = i.next();
		}

		@Override
		public boolean hasNext() {
			return (ent != null &&
					(cnt < ent.getValue().intValue() ||
							iter.hasNext()));
		}

		@Override
		public T next() {
			if(ent == null) {
				throw new NoSuchElementException();
			} else if(cnt >= ent.getValue().intValue()) {
				ent = iter.next();
				cnt = 0;
			}
			cnt++;
			return ent.getKey();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	//
	private SortedMap<T, Integer> elems;

	/**
	 * 
	 */
	public TinySortedBag() {
		elems = new TreeMap<T, Integer>();
	}

	/**
	 * 
	 * @param keys
	 */
	public TinySortedBag(Iterable<T> keys) {
		elems = new TreeMap<T, Integer>();
		for(T t : keys)  add(t);
	}

	/**
	 * 
	 * @param t
	 */
	public void add(T t) {
		if(elems.containsKey(t)) {
			elems.put(t, new Integer(elems.get(t).intValue() + 1));
		} else {
			elems.put(t, new Integer(1));
		}
	}

	/**
	 * 
	 * @return
	 */
	public Set<T> toSet() {
		return new TreeSet<T>(elems.keySet());
	}

	/**
	 * 
	 * @return
	 */
	public Iterator<T> immutableIterator() {
		return new Iter<T>(elems.entrySet().iterator());
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		return immutableIterator();
	}

}
