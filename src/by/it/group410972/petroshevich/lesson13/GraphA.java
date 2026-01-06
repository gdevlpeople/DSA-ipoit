package by.it.group410972.petroshevich.lesson13;

import java.util.*;

public class GraphA {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Введите граф (пример: 0 -> 2, 1 -> 3, 2 -> 3, 0 -> 1):");
        String input = sc.nextLine();

        // Разбор строк
        Map<String, List<String>> graph = new TreeMap<>(); 
        String[] edges = input.split(",");
        for(String edge : edges){
            edge = edge.trim(); // убрать пробелы
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());
            graph.get(from).add(to);
        }


        List<String> sorted = topoSort(graph);
        System.out.println(String.join(" ", sorted));
    }

    private static List<String> topoSort(Map<String, List<String>> graph){
        Map<String, Integer> inDegree = new HashMap<>();
        for(String node : graph.keySet()) inDegree.put(node, 0);

        for(String from : graph.keySet()){
            for(String to : graph.get(from)){
                inDegree.put(to, inDegree.get(to) + 1);
            }
        }

        PriorityQueue<String> pq = new PriorityQueue<>();
        for(String node : inDegree.keySet()){
            if(inDegree.get(node) == 0) pq.offer(node);
        }

        List<String> result = new ArrayList<>();
        while(!pq.isEmpty()){
            String node = pq.poll();
            result.add(node);
            for(String neighbor : graph.get(node)){
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if(inDegree.get(neighbor) == 0) pq.offer(neighbor);
            }
        }

        return result;
    }
}
