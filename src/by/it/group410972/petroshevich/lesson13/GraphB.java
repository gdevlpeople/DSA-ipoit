package by.it.group410972.petroshevich.lesson13;

import java.util.*;

public class GraphB {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Введите граф (пример: 1 -> 2, 1 -> 3, 2 -> 3):");
        String input = sc.nextLine();

        Map<String, List<String>> graph = new HashMap<>();
        String[] edges = input.split(",");
        for(String edge : edges){
            edge = edge.trim();
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>()); 
            graph.get(from).add(to);
        }

        boolean hasCycle = detectCycle(graph);
        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean detectCycle(Map<String, List<String>> graph){
        Map<String, Integer> state = new HashMap<>();
        for(String node : graph.keySet()) state.put(node, 0);

        for(String node : graph.keySet()){
            if(state.get(node) == 0){
                if(dfs(node, graph, state)) return true;
            }
        }
        return false;
    }

    private static boolean dfs(String node, Map<String, List<String>> graph, Map<String, Integer> state){
        state.put(node, 1);
        for(String neighbor : graph.get(node)){
            if(state.get(neighbor) == 1) return true;
            if(state.get(neighbor) == 0){
                if(dfs(neighbor, graph, state)) return true;
            }
        }
        state.put(node, 2); 
        return false;
    }
}
