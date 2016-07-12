package io.github.juanjalvarez.socialnetwork;

public class Listing<E> {

	private int size;
	private E[] arr;

	@SuppressWarnings("unchecked")
	public Listing(int size) {
		this.size = size;
		arr = (E[]) new Object[size];
	}

	public Listing(E[] arr) {
		size = arr.length;
		this.arr = arr;
	}

	public synchronized int size() {
		return size;
	}

	private void ensureIndex(int idx) {
		if (idx < 0 || idx >= size)
			throw new IndexOutOfBoundsException("Invalid index: " + idx);
	}

	public synchronized void set(int idx, E val) {
		ensureIndex(idx);
		arr[idx] = val;
	}

	public synchronized E get(int idx) {
		ensureIndex(idx);
		return arr[idx];
	}

	public E[] getArray() {
		return (E[]) arr;
	}
}