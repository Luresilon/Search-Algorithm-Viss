package PathFinders;

import Imp.Support.Node;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class GreedyPathFinder extends PathFinder {
    private static final String NAME = "Greedy Best-First Search";

    public GreedyPathFinder(JPanel jPanel, Map<Integer, Integer> start, Map<Integer, Integer> finish,
                            Map<String, Component> componentMap, int sizeX, int sizeY) {
        super(jPanel, start, finish, componentMap, sizeX, sizeY);
    }

    @Override
    protected void search() throws InterruptedException {
        PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingDouble(Node::getCost));

        this.current.setCost(heuristic(this.current));
        openList.add(this.current);

        while (!openList.isEmpty()) {
            this.current = openList.poll();
            if (isFinish(this.current)) {
                this.done = true;
                break;
            }

            this.visitedNodes.add(this.current);

            for (Node neighbor : getNeighbors(this.current)) {
                if (!this.visitedNodes.contains(neighbor)) {
                    neighbor.setCost(heuristic(neighbor));
                    openList.add(neighbor);
                    move(neighbor.getX(), neighbor.getY());
                    Thread.sleep(10);
                }
            }
        }

        if (this.done) {
            traverse(-1, 1);
        }
    }


    private List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (x != 0 && y != 0) {
                    continue;
                }
                int newX = node.getX() + x;
                int newY = node.getY() + y;

                if (newX >= 0 && newX < this.sizeX && newY >= 0 && newY < this.sizeY
                        && (this.map.get(Collections.singletonMap(newX, newY)).accessCheck()
                        || this.map.get(Collections.singletonMap(newX, newY)).finished())
                        && !findNeighbourInList(this.visitedNodes, new Node(newX, newY))) {
                    neighbors.add(new Node(node, newX, newY));
                }
            }
        }

        return neighbors;
    }

    private double heuristic(Node node) {
        if (this.finish != null) {
            Map.Entry<Integer, Integer> e = this.finish.entrySet().iterator().next();
            int dx = Math.abs(node.getX() - e.getKey());
            int dy = Math.abs(node.getY() - e.getValue());
            return dx + dy;
        } else {
            return this.sizeY - node.getY();
        }
    }

    private boolean isFinish(Node node) {
        if (this.finish != null) {
            Map.Entry<Integer, Integer> e = this.finish.entrySet().iterator().next();
            return node.getX() == e.getKey() && node.getY() == e.getValue();
        } else {
            return node.getY() == this.sizeY - 1;
        }
    }

    @Override
    protected void findPath() throws InterruptedException {
        this.visitedNodes.add(this.current);

        addNeighborsToFreeCellList(iterator);

        try {
            search();
        } catch (InterruptedException e) {
            clear();
            throw e;
        }

        if (checkIfFinished()) {
            mapPath();
            System.out.println("Found path: " + this.current);
            traverse(-1, 1);
            this.done = true;
        }

        clear();

        throw new InterruptedException();
    }





    @Override
    protected void mapPath() throws InterruptedException {
        if (this.finish != null) {
            Map.Entry<Integer, Integer> e = this.finish.entrySet().iterator().next();

            this.current = this.activeNodes.stream()
                    .filter((n) -> (n.getX() == e.getKey() && n.getY() == e.getValue()))
                    .findAny()
                    .orElse(null);

            if (this.current != null) {
                if (this.start != null) {
                    Map.Entry<Integer, Integer> s = this.start.entrySet().iterator().next();
                    traverse(s.getKey(), s.getValue());
                } else {
                    traverse(-1, 1);
                }
            }
        } else if (this.activeNodes.stream().anyMatch((n) -> n.getY() == this.sizeY - 1)) {
            this.current = this.activeNodes.stream()
                    .filter((n) -> n.getY() == this.sizeY - 1)
                    .findAny()
                    .orElse(null);

            if (this.current != null) {
                traverse(-1, 1);
            }
        }
    }


    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String toString() {
        return NAME;
    }
}
