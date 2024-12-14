package model.binaryTree;

public class NodeChange {
    private String name;
    private Integer oldPosition;
    private Integer newPosition;

    public NodeChange() {
    }

    public NodeChange(String name, Integer oldPosition, Integer newPosition) {
        this.name = name;
        this.oldPosition = oldPosition;
        this.newPosition = newPosition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOldPosition() {
        return oldPosition;
    }

    public void setOldPosition(Integer oldPosition) {
        this.oldPosition = oldPosition;
    }

    public Integer getNewPosition() {
        return newPosition;
    }

    public void setNewPosition(Integer newPosition) {
        this.newPosition = newPosition;
    }

    @Override
    public String toString() {
        return "NodeChange{" +
                "name='" + name + '\'' +
                ", oldPosition=" + oldPosition +
                ", newPosition=" + newPosition +
                '}';
    }
}
