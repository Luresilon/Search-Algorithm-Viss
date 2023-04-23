//package PathFinders;
//
//import Imp.Support.Node;
//import Cells.Cell;
//
//import javax.swing.*;
//import java.awt.*;
//import java.util.*;
//
//public class AStarPathFinder extends PathFinder {
//
//    private static final String NAME = "A* Algorithm";
//
//    public AStarPathFinder(JPanel jPanel, Map<Integer, Integer> start, Map<Integer, Integer> finish,
//                           Map<String, Component> componentMap, int sizeX, int sizeY) {
//        super(jPanel, start, finish, componentMap, sizeX, sizeY);
//    }
//
//    /**
//     * Expands upon each active node until a path is found.
//     * Finish if no path found.
//     *
//     * @throws InterruptedException this stops the thread.
//     */
//    @Override
//    protected void search() throws InterruptedException {
//        while (!checkIfFinished()) {
//            if (this.noPath) {
//                break;
//            } else if (this.activeNodes.isEmpty()) {
//                setCurrent();
//            }
//
//            Thread.sleep(10);
//            Node n = getBestNode();
//            if (n == null) {
//                break;
//            }
//            this.current = n;
//            this.activeNodes.remove(n);
//            this.visitedNodes.add(n);
//            if (n.isFinish()) {
//                this.done = true;
//                return;
//            }
//            addNeighborsToFreeCellList(this.activeNodes.listIterator());
//        }
//    }
//
//    /**
//     * Returns the best node to explore next based on A* algorithm.
//     *
//     * @return best node to explore next
//     */
//    private Node getBestNode() {
//        Node best = null;
//        int bestScore = Integer.MAX_VALUE;
//        for (Node n : this.activeNodes) {
//            int score = (int) (n.getCost() + getManhattanDistance(n));
//            if (score < bestScore) {
//                best = n;
//                bestScore = score;
//            }
//        }
//        return best;
//    }
//
//    /**
//     * Calculates the Manhattan distance between current node and finish node.
//     *
//     * @param n current node
//     * @return Manhattan distance
//     */
//    private int getManhattanDistance(Node n) {
//        int dx = Math.abs(n.getX() - this.finish.get("x"));
//        int dy = Math.abs(n.getY() - this.finish.get("y"));
//        return dx + dy;
//    }
//
//    @Override
//    protected void addNeighborsToFreeCellList(ListIterator<Node> it) {
//        Node node;
//        int x, y;
//
//        for (int[] direction : new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}}) {
//            x = this.current.getX() + direction[0];
//            y = this.current.getY() + direction[1];
//
//            if (x < 0 || x >= this.sizeX || y < 0 || y >= this.sizeY) {
//                continue;
//            }
//
//            node = new Node(this.current, x, y);
//            Cell c = this.map.get(Collections.singletonMap(x, y));
//            if ((c.accessCheck() || c.finished()) && !findNeighbourInList(this.activeNodes, node) && !findNeighbourInList(this.visitedNodes, node)) {
//                move(x, y);
//                node.setCost(this.current.getCost() + 1);
//                it.add(node);
//                if (c.finished()) {
//                    this.done = true;
//                    return;
//                }
//            }
//        }
//    }
//
//    @Override
//    public String getName() {
//        return NAME;
//    }
//
//    @Override
//    public String toString() {
//        return NAME;
//    }
//}
