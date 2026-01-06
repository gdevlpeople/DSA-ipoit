package by.it.group410972.petroshevich.lesson13;

import java.util.*;

public class GraphC {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Введите граф (пример: C->B, C->I, I->A, ...):");
        String input = sc.nextLine();

        Map<String, List<String>> graph = new TreeMap<>();
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

        Set<String> visited = new HashSet<>();
        Stack<String> stack = new Stack<>();
        for(String node : graph.keySet()){
            if(!visited.contains(node)) dfs(node, graph, visited, stack);
        }

        Map<String, List<String>> transposed = new TreeMap<>();
        for(String node : graph.keySet()) transposed.put(node, new ArrayList<>());
        for(String from : graph.keySet()){
            for(String to : graph.get(from)){
                transposed.get(to).add(from);
            }
        }

        visited.clear();
        while(!stack.isEmpty()){
            String node = stack.pop();
            if(!visited.contains(node)){
                List<String> component = new ArrayList<>();
                dfsCollect(node, transposed, visited, component);
                Collections.sort(component);
                for(String s : component) System.out.print(s);
                System.out.println();
            }
        }
    }

    private static void dfs(String node, Map<String, List<String>> graph, Set<String> visited, Stack<String> stack){
        visited.add(node);
        List<String> neighbors = new ArrayList<>(graph.get(node));
        Collections.sort(neighbors);
        for(String neighbor : neighbors){
            if(!visited.contains(neighbor)) dfs(neighbor, graph, visited, stack);
        }
        stack.push(node);
    }

    private static void dfsCollect(String node, Map<String, List<String>> graph, Set<String> visited, List<String> component){
        visited.add(node);
        component.add(node);
        List<String> neighbors = new ArrayList<>(graph.get(node));
        Collections.sort(neighbors);
        for(String neighbor : neighbors){
            if(!visited.contains(neighbor)) dfsCollect(neighbor, graph, visited, component);
        }
    }
}
