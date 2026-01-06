package by.it.group410972.petroshevich.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListA<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    
    private Object[] data;
    private int size;
    private static final int INIT_CAPACITY = 10;


    public ListA() {
    data = new Object[INIT_CAPACITY];
    size = 0;
    }


    private void ensureCapacity() {
    if (size >= data.length) {
    int newCapacity = data.length * 2;
    Object[] newArr = new Object[newCapacity];
    for (int i = 0; i < size; i++) {
    newArr[i] = data[i];
    }
    data = newArr;
    }
    }

    @Override
    public String toString() {
    StringBuilder sb = new StringBuilder("[");
    for (int i = 0; i < size; i++) {
        sb.append(data[i]);
            if (i < size - 1) sb.append(", ");
    }
    sb.append("]");
    return sb.toString();
    }

    @Override
    public boolean add(E e) {
        ensureCapacity();
        data[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
    if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
    @SuppressWarnings("unchecked")
    E removed = (E) data[index];
    for (int i = index; i < size - 1; i++) {
        data[i] = data[i + 1];
    }
    size--;
    return removed;
    }

    @Override
    public int size() {
        return size;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public void add(int index, E element) {
    if (index < 0 || index > size) throw new IndexOutOfBoundsException();
    ensureCapacity();
    for (int i = size; i > index; i--) {
        data[i] = data[i - 1];
    }
    data[index] = element;
    size++;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (data[i].equals(o)) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        @SuppressWarnings("unchecked")
        E old = (E) data[index];
        data[index] = element;
        return old;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public void clear() {
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (data[i].equals(o)) return i;
        }
        return -1;

    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        @SuppressWarnings("unchecked")
        E value = (E) data[index];
        return value;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (data[i].equals(o)) return i;
        }
        return -1;

    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;

    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) { add(e); changed = true; }
        return changed;

    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        boolean changed = false;
        for (E e : c) { add(index++, e); changed = true; }
        return changed;

    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) while (remove(o)) changed = true;
        return changed;

    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (int i = 0; i < size; i++) {
            if (!c.contains(data[i])) { remove(i); i--; changed = true; }
        }
        return changed;

    }


    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) throw new IndexOutOfBoundsException();
        ListA<E> sub = new ListA<>();
        for (int i = fromIndex; i < toIndex; i++) sub.add(get(i));
        return sub;
    }


    @Override
    public ListIterator<E> listIterator(int index) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        return new ListIterator<E>() {
            int cursor = index;
            @Override public boolean hasNext() { return cursor < size; }
            @Override public E next() { return get(cursor++); }
            @Override public boolean hasPrevious() { return cursor > 0; }
            @Override public E previous() { return get(--cursor); }
            @Override public int nextIndex() { return cursor; }
            @Override public int previousIndex() { return cursor - 1; }
            @Override public void remove() { ListA.this.remove(--cursor); }
            @Override public void set(E e) { ListA.this.set(cursor - 1, e); }
            @Override public void add(E e) { ListA.this.add(cursor++, e); }
        };

    }

    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0); 
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            @SuppressWarnings("unchecked") T[] newArr = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
            for (int i = 0; i < size; i++) newArr[i] = (T) data[i];
            return newArr;
        }
        for (int i = 0; i < size; i++) a[i] = (T) data[i];
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        for (int i = 0; i < size; i++) arr[i] = data[i];
        return arr;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<E> iterator() {
        return null;
        }    
}

