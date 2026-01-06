package by.it.group410972.petroshevich.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import org.w3c.dom.Node;

public class ListC<E> implements List<E> {

    private static class Node<E> {
        E val;
        Node<E> next;
        Node<E> prev;
        Node(E v) { val = v; }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size = 0;

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> cur = head;
        while (cur != null) {
            sb.append(cur.val);
            cur = cur.next;
            if (cur != null) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        Node<E> n = new Node<>(e);
        if (size == 0) head = tail = n;
        else {
            tail.next = n;
            n.prev = tail;
            tail = n;
        }
        size++;
        return true;
    }

    private Node<E> getNode(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node<E> cur;
        if (index < size/2) {
            cur = head;
            for (int i=0;i<index;i++) cur = cur.next;
        } else {
            cur = tail;
            for (int i=size-1;i>index;i--) cur = cur.prev;
        }
        return cur;
    }

    @Override
    public E get(int index) {
        return getNode(index).val;
    }

    @Override
    public E set(int index, E element) {
        Node<E> n = getNode(index);
        E old = n.val;
        n.val = element;
        return old;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        if (index == size) { add(element); return; }
        Node<E> cur = getNode(index);
        Node<E> n = new Node<>(element);
        n.next = cur;
        n.prev = cur.prev;
        if (cur.prev != null) cur.prev.next = n;
        cur.prev = n;
        if (index == 0) head = n;
        size++;
    }

    @Override
    public E remove(int index) {
        Node<E> cur = getNode(index);
        if (cur.prev != null) cur.prev.next = cur.next;
        if (cur.next != null) cur.next.prev = cur.prev;
        if (index == 0) head = cur.next;
        if (index == size-1) tail = cur.prev;
        size--;
        return cur.val;
    }

    @Override
    public boolean remove(Object o) {
        Node<E> cur = head;
        while (cur != null) {
            if (Objects.equals(cur.val, o)) {
                removeNode(cur);
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    private void removeNode(Node<E> cur) {
        if (cur.prev != null) cur.prev.next = cur.next;
        if (cur.next != null) cur.next.prev = cur.prev;
        if (cur == head) head = cur.next;
        if (cur == tail) tail = cur.prev;
        size--;
    }

    @Override
    public void clear() {
        head = tail = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        Node<E> cur = head;
        for (int i=0;i<size;i++) {
            if (Objects.equals(cur.val,o)) return i;
            cur = cur.next;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        Node<E> cur = tail;
        for (int i=size-1;i>=0;i--) {
            if (Objects.equals(cur.val,o)) return i;
            cur = cur.prev;
        }
        return -1;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public boolean isEmpty() {
        return size==0;
    }

    @Override
    public int size() {
        return size;
    }

    // ---- Optional ---- //

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object x : c) if (!contains(x)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E x : c) add(x);
        return !c.isEmpty();
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        for (E x : c) add(index++, x);
        return !c.isEmpty();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object x : c) while (remove(x)) changed = true;
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.next;
            if (!c.contains(cur.val)) {
                removeNode(cur);
                changed = true;
            }
            cur = next;
        }
        return changed;
    }

    @Override
    public List<E> subList(int from, int to) {
        if (from<0 || to>size || from>to) throw new IndexOutOfBoundsException();
        ListC<E> res = new ListC<>();
        Node<E> cur = getNode(from);
        for (int i=from;i<to;i++) {
            res.add(cur.val);
            cur = cur.next;
        }
        return res;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ListIterator<E>() {
            Node<E> cur = (index < size) ? getNode(index) : null;
            int idx = index;

            @Override public boolean hasNext() { return idx < size; }
            @Override public E next() { E r = cur.val; cur = cur.next; idx++; return r; }
            @Override public boolean hasPrevious() { return idx > 0; }
            @Override public E previous() { cur = (cur==null?tail:cur.prev); idx--; return cur.val; }
            @Override public int nextIndex() { return idx; }
            @Override public int previousIndex() { return idx-1; }
            @Override public void remove() { ListC.this.remove(--idx); }
            @Override public void set(E e) { ListC.this.set(idx-1,e); }
            @Override public void add(E e) { ListC.this.add(idx++,e); }
        };
    }

    @Override
    public ListIterator<E> listIterator() { return listIterator(0); }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        Node<E> cur = head;
        for (int i=0;i<size;i++) { arr[i] = cur.val; cur = cur.next; }
        return arr;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        Object[] arr = toArray();
        if (a.length < size) return (T[]) arr;
        System.arraycopy(arr,0,a,0,size);
        if (a.length > size) a[size]=null;
        return a;
    }

    @Override
    public Iterator<E> iterator() {
        return listIterator();
    }
}
