package by.it.group410972.petroshevich.lesson14;

import java.util.*;

public class SitesB {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DSU dsu = new DSU();

        while (true) {
            String line = sc.nextLine().trim();
            if (line.equals("end")) break;
            String[] parts = line.split("\\+");
            if (parts.length != 2) continue;

            String a = parts[0].trim();
            String b = parts[1].trim();

            dsu.makeSet(a);
            dsu.makeSet(b);
            dsu.union(a, b);
        }

        // Подсчёт размеров кластеров
        Map<String, Integer> clusterSize = new HashMap<>();
        for (String site : dsu.parent.keySet()) {
            String root = dsu.find(site);
            clusterSize.put(root, clusterSize.getOrDefault(root, 0) + 1);
        }

        List<Integer> sizes = new ArrayList<>(clusterSize.values());
        sizes.sort(Collections.reverseOrder());

        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(sizes.get(i));
        }
    }

    static class DSU {
        Map<String, String> parent = new HashMap<>();
        Map<String, Integer> size = new HashMap<>();

        void makeSet(String x) {
            parent.putIfAbsent(x, x);
            size.putIfAbsent(x, 1);
        }

        String find(String x) {
            if (!parent.get(x).equals(x))
                parent.put(x, find(parent.get(x))); // сжатие пути
            return parent.get(x);
        }

        void union(String a, String b) {
            String rootA = find(a);
            String rootB = find(b);
            if (rootA.equals(rootB)) return;

            // эвристика по размеру
            if (size.get(rootA) < size.get(rootB)) {
                String tmp = rootA;
                rootA = rootB;
                rootB = tmp;
            }

            parent.put(rootB, rootA);
            size.put(rootA, size.get(rootA) + size.get(rootB));
        }
    }
}
