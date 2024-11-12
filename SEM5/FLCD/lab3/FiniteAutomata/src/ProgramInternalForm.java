import java.util.ArrayList;
import java.util.List;

public class ProgramInternalForm {
    private List<Pair<String, Pair<Integer, Integer>>> tokenPostionPair;
    private List<Integer> types;

    public ProgramInternalForm() {
        this.tokenPostionPair = new ArrayList<>();
        this.types = new ArrayList<>();
    }

    public void add(Pair<String, Pair<Integer, Integer>> pair, Integer type)
    {
        this.tokenPostionPair.add(pair);
        this.types.add(type);
    }

    public String toString()
    {
        StringBuilder finalString = new StringBuilder();

        for(int i = 0; i < this.tokenPostionPair.size(); i++)
        {
            finalString.append(this.tokenPostionPair.get(i).getFirst())
                    .append(" ")
                    .append(this.tokenPostionPair.get(i).getSecond())
                    .append("\n");
        }

        return finalString.toString();
    }
}
