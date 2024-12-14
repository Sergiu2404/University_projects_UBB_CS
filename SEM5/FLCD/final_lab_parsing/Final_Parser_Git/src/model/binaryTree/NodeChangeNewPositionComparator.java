package model.binaryTree;

import java.util.Comparator;

public class NodeChangeNewPositionComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        NodeChange nodeChange1 = (NodeChange) o1;
        NodeChange nodeChange2 = (NodeChange) o2;

        return nodeChange2.getNewPosition() - nodeChange1.getNewPosition();
    }
}
