package org.dei.Sprint2.Trees;

public class PrintTree {

    public static String toString(BST<NodeData> tree) {
        if (tree.root() == null) {
            return "Empty 2D Tree";
        }
        StringBuilder sb = new StringBuilder();
        buildString(tree.root(), sb, 0);
        return sb.toString();
    }

    private static void buildString(BST.Node<NodeData> node, StringBuilder sb, int depth) {
        if (node == null) {
            return;
        }

        // Indentation based on depth
        sb.append("  ".repeat(depth))
                .append("• Node(")
                .append(node.getElement().getCoordinate().getLatitude())
                .append(", ")
                .append(node.getElement().getCoordinate().getLongitude())
                .append(")")
                .append("\n");

        // Left subtree
        if (node.getLeft() != null) {
            sb.append("  ".repeat(depth))
                    .append("  ├─ Left:\n");
            buildString(node.getLeft(), sb, depth + 2);
        }

        // Right subtree
        if (node.getRight() != null) {
            sb.append("  ".repeat(depth))
                    .append("  └─ Right:\n");
            buildString(node.getRight(), sb, depth + 2);
        }
    }

    public static String toStringWithDistance(BST<NodeData> tree){
        if (tree.root() == null) {
            return "Empty 2D Tree";
        }
        StringBuilder sb = new StringBuilder();
        buildStringWithDistance(tree.root(), sb, 0);
        return sb.toString();
    }

    public static void buildStringWithDistance(BST.Node<NodeData> node, StringBuilder sb, int depth){
        if (node == null) {
            return;
        }

        double distance = ((NodeDataWithDist) node.getElement()).getDistance();

        // Indentation based on depth
        sb.append("  ".repeat(depth))
                .append("• Node(")
                .append(node.getElement().getCoordinate().getLatitude())
                .append(", ")
                .append(node.getElement().getCoordinate().getLongitude())
                .append(")")
                .append(" - Distance: ")
                .append(String.format("%.4f", distance))
                .append(" KM")
                .append("\n");

        // Left subtree
        if (node.getLeft() != null) {
            sb.append("  ".repeat(depth))
                    .append("  ├─ Left:\n");
            buildStringWithDistance(node.getLeft(), sb, depth + 2);
        }

        // Right subtree
        if (node.getRight() != null) {
            sb.append("  ".repeat(depth))
                    .append("  └─ Right:\n");
            buildStringWithDistance(node.getRight(), sb, depth + 2);
        }
    }

    public static String toString2(BST<NodeData> tree) {
        if (tree.root() == null) return "Empty 2D Tree";
        StringBuilder sb = new StringBuilder();
        buildVisualString(tree.root(), sb, 0);
        return sb.toString();
    }

    private static void buildVisualString(BST.Node<NodeData> node, StringBuilder sb, int depth) {
        if (node == null) return;

        // Print right subtree first (so it appears above)
        buildVisualString(node.getRight(), sb, depth + 1);

        // Indentation per depth level
        sb.append("    ".repeat(depth))
                .append("(")
                .append(node.getElement().getCoordinate().getLatitude())
                .append(", ")
                .append(node.getElement().getCoordinate().getLongitude())
                .append(")")
                .append("\n");

        // Then left subtree (appears below)
        buildVisualString(node.getLeft(), sb, depth + 1);
    }
}
