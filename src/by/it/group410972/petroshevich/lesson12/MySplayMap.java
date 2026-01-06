package by.it.group410972.petroshevich.lesson12;
import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    private class Node {
        Integer key;
        String value;
        Node left, right, parent;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size = 0;

    // ==============================
    // ===== Основная логика splay
    // ==============================
    private void rotateLeft(Node x) {
        Node y = x.right;
        if (y != null) {
            x.right = y.left;
            if (y.left != null) y.left.parent = x;
            y.parent = x.parent;
        }
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        if (y != null) y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        if (y != null) {
            x.left = y.right;
            if (y.right != null) y.right.parent = x;
            y.parent = x.parent;
        }
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        if (y != null) y.right = x;
        x.parent = y;
    }

    private void splay(Node x) {
        while (x.parent != null) {
            if (x.parent.parent == null) {
                if (x.parent.left == x) rotateRight(x.parent);
                else rotateLeft(x.parent);
            } else if (x.parent.left == x && x.parent.parent.left == x.parent) {
                rotateRight(x.parent.parent);
                rotateRight(x.parent);
            } else if (x.parent.right == x && x.parent.parent.right == x.parent) {
                rotateLeft(x.parent.parent);
                rotateLeft(x.parent);
            } else if (x.parent.left == x && x.parent.parent.right == x.parent) {
                rotateRight(x.parent);
                rotateLeft(x.parent);
            } else {
                rotateLeft(x.parent);
                rotateRight(x.parent);
            }
        }
    }

    private Node findNode(Integer key) {
        Node z = root;
        Node last = null;
        while (z != null) {
            last = z;
            int cmp = key.compareTo(z.key);
            if (cmp < 0) z = z.left;
            else if (cmp > 0) z = z.right;
            else return z;
        }
        if (last != null) splay(last);
        return null;
    }

    // ==============================
    // ===== Методы Map
    // ==============================
    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }

        Node z = root;
        Node p = null;
        while (z != null) {
            p = z;
            int cmp = key.compareTo(z.key);
            if (cmp < 0) z = z.left;
            else if (cmp > 0) z = z.right;
            else {
                String old = z.value;
                z.value = value;
                splay(z);
                return old;
            }
        }

        Node newNode = new Node(key, value);
        newNode.parent = p;
        if (key < p.key) p.left = newNode;
        else p.right = newNode;
        splay(newNode);
        size++;
        return null;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node z = root;
        while (z != null) {
            int cmp = ((Integer) key).compareTo(z.key);
            if (cmp < 0) z = z.left;
            else if (cmp > 0) z = z.right;
            else {
                splay(z);
                return z.value;
            }
        }
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Node z = findNode((Integer) key);
        if (z == null) return null;
        splay(z);
        String oldVal = z.value;

        if (z.left == null) replace(z, z.right);
        else if (z.right == null) replace(z, z.left);
        else {
            Node y = subtreeMin(z.right);
            if (y.parent != z) {
                replace(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            replace(z, y);
            y.left = z.left;
            y.left.parent = y;
        }
        size--;
        return oldVal;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    private void replace(Node u, Node v) {
        if (u.parent == null) root = v;
        else if (u == u.parent.left) u.parent.left = v;
        else u.parent.right = v;
        if (v != null) v.parent = u.parent;
    }

    private Node subtreeMin(Node n) {
        while (n.left != null) n = n.left;
        return n;
    }

    private Node subtreeMax(Node n) {
        while (n.right != null) n = n.right;
        return n;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof String)) return false;
        return containsValueRecursive(root, (String) value);
    }

    private boolean containsValueRecursive(Node node, String value) {
        if (node == null) return false;
        if (node.value.equals(value)) return true;
        return containsValueRecursive(node.left, value) || containsValueRecursive(node.right, value);
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // ==============================
    // ===== Навигационные методы
    // ==============================
    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        return subtreeMin(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        return subtreeMax(root).key;
    }

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        return null;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node x = root;
        Integer res = null;
        while (x != null) {
            if (x.key < key) {
                res = x.key;
                x = x.right;
            } else x = x.left;
        }
        return res;
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        return null;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node x = root;
        Integer res = null;
        while (x != null) {
            if (x.key <= key) {
                res = x.key;
                x = x.right;
            } else x = x.left;
        }
        return res;
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        return null;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node x = root;
        Integer res = null;
        while (x != null) {
            if (x.key >= key) {
                res = x.key;
                x = x.left;
            } else x = x.right;
        }
        return res;
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        return null;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node x = root;
        Integer res = null;
        while (x != null) {
            if (x.key > key) {
                res = x.key;
                x = x.left;
            } else x = x.right;
        }
        return res;
    }

    // ==============================
    // ===== headMap / tailMap
    // ==============================
    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap map = new MySplayMap();
        addRange(root, map, null, toKey, inclusive);
        return map;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap map = new MySplayMap();
        addRange(root, map, fromKey, null, inclusive);
        return map;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        if (fromKey == null || toKey == null)
            return new MySplayMap();
        return (SortedMap) subMap((Integer) fromKey, true, (Integer) toKey, false);
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) return new MySplayMap();
        // вызываем именно реализацию с boolean, чтобы не попасть в рекурсию
        return (SortedMap) headMap((Integer) toKey, false);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) return new MySplayMap();
        return (SortedMap) tailMap((Integer) fromKey, true);
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        MySplayMap map = new MySplayMap();
        addRange(root, map, fromKey, toKey, fromInclusive && toInclusive);
        return map;
    }

    private void addRange(Node node, MySplayMap map, Integer from, Integer to, boolean inclusive) {
        if (node == null) return;
        addRange(node.left, map, from, to, inclusive);
        boolean inRange = (from == null || node.key > from || (inclusive && node.key.equals(from))) &&
                (to == null || node.key < to || (inclusive && node.key.equals(to)));
        if (inRange) map.put(node.key, node.value);
        addRange(node.right, map, from, to, inclusive);
    }

    // ==============================
    // ===== toString()
    // ==============================
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        buildString(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2); // удалить последнюю ", "
        sb.append("}");
        return sb.toString();
    }

    private void buildString(Node node, StringBuilder sb) {
        if (node == null) return;
        buildString(node.left, sb);
        sb.append(node.key).append("=").append(node.value).append(", ");
        buildString(node.right, sb);
    }

    // ===== Методы интерфейса, которые можно оставить пустыми =====
    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public NavigableSet<Integer> navigableKeySet() { throw new UnsupportedOperationException(); }
    @Override public NavigableSet<Integer> descendingKeySet() { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> descendingMap() { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> firstEntry() { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> lastEntry() { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> pollFirstEntry() { throw new UnsupportedOperationException(); }
    @Override public Map.Entry<Integer, String> pollLastEntry() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
}
