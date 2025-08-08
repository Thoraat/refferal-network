package com.example.backend.service;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReferralNetworkService {
    private final Map<String, List<String>> graph = new HashMap<>();
    private final Map<String, String> referredBy = new HashMap<>();

    public boolean addReferral(String referrerId, String candidateId) {
        if (referrerId.equals(candidateId)) return false; // no self-referrals
        if (referredBy.containsKey(candidateId)) return false; // already referred
        if (createsCycle(referrerId, candidateId)) return false; // no cycles

        graph.putIfAbsent(referrerId, new ArrayList<>());
        graph.get(referrerId).add(candidateId);
        referredBy.put(candidateId, referrerId);
        return true;
    }

    public List<String> getDirectReferrals(String userId) {
        return graph.getOrDefault(userId, Collections.emptyList());
    }

    private boolean createsCycle(String referrerId, String candidateId) {
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(candidateId);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(referrerId)) return true;
            if (visited.contains(current)) continue;
            visited.add(current);
            queue.addAll(graph.getOrDefault(current, Collections.emptyList()));
        }

        return false;
    }

    public int getTotalReach(String userId) {
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();

        queue.add(userId);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            for (String neighbor : graph.getOrDefault(current, new ArrayList<>())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        return visited.size(); // total reach (excluding self)
    }

    public List<String> getTopReferrers(int k) {
        Map<String, Integer> reachCount = new HashMap<>();

        for (String userId : graph.keySet()) {
            int reach = getTotalReach(userId);
            reachCount.put(userId, reach);
        }

        return reachCount.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue()) // descending
                .limit(k)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public Map<String, Set<String>> getAllReachSets() {
        Map<String, Set<String>> allReach = new HashMap<>();
        for (String user : graph.keySet()) {
            allReach.put(user, getReachSet(user));
        }
        return allReach;
    }

    private Set<String> getReachSet(String userId) {
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(userId);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            for (String neighbor : graph.getOrDefault(current, new ArrayList<>())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        return visited;
    }

    public List<String> getTopUniqueExpanders(int k) {
        Map<String, Set<String>> allReach = getAllReachSets();
        Set<String> covered = new HashSet<>();
        List<String> result = new ArrayList<>();

        for (int i = 0; i < k; i++) {
            String bestUser = null;
            int maxNew = -1;

            for (Map.Entry<String, Set<String>> entry : allReach.entrySet()) {
                String user = entry.getKey();
                Set<String> reachSet = new HashSet<>(entry.getValue());
                reachSet.removeAll(covered); // Only consider new reach
                if (reachSet.size() > maxNew && !result.contains(user)) {
                    maxNew = reachSet.size();
                    bestUser = user;
                }
            }

            if (bestUser == null) break;
            result.add(bestUser);
            covered.addAll(allReach.get(bestUser));
        }

        return result;
    }

    public Map<String, Integer> getFlowCentralityScores() {
        Map<String, Integer> scores = new HashMap<>();
        Set<String> allUsers = new HashSet<>(graph.keySet());
        for (List<String> targets : graph.values()) allUsers.addAll(targets);

        for (String user : allUsers) scores.put(user, 0);

        // Run BFS from every user
        for (String source : allUsers) {
            Map<String, Integer> distFromSource = bfsDistances(source);

            for (String target : allUsers) {
                if (source.equals(target)) continue;

                int distST = distFromSource.getOrDefault(target, Integer.MAX_VALUE);
                if (distST == Integer.MAX_VALUE) continue;

                for (String v : allUsers) {
                    if (v.equals(source) || v.equals(target)) continue;
                    int distSV = distFromSource.getOrDefault(v, Integer.MAX_VALUE);
                    Map<String, Integer> distFromV = bfsDistances(v);
                    int distVT = distFromV.getOrDefault(target, Integer.MAX_VALUE);

                    if (distSV + distVT == distST) {
                        scores.put(v, scores.get(v) + 1);
                    }
                }
            }
        }

        return scores;
    }

    private Map<String, Integer> bfsDistances(String start) {
        Map<String, Integer> dist = new HashMap<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(start);
        dist.put(start, 0);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            int currDist = dist.get(current);
            for (String neighbor : graph.getOrDefault(current, new ArrayList<>())) {
                if (!dist.containsKey(neighbor)) {
                    dist.put(neighbor, currDist + 1);
                    queue.add(neighbor);
                }
            }
        }

        return dist;
    }



}
