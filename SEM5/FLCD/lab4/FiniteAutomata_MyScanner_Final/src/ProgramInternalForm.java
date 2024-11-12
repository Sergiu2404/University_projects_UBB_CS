import utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class ProgramInternalForm {

    private List<Pair<String, Pair<Integer, Integer>>> tokenPositionPairsList;


    public ProgramInternalForm() {
        this.tokenPositionPairsList = new ArrayList<>();
    }

    public void add(Pair<String, Pair<Integer, Integer>> newPair){
        this.tokenPositionPairsList.add(newPair);
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < this.tokenPositionPairsList.size(); i++) {
            stringBuilder.append(this.tokenPositionPairsList.get(i).getKey())
                    .append(" at position ")
                    .append(this.tokenPositionPairsList.get(i).getValue())
                    .append("\n");
        }

        return stringBuilder.toString();
    }
}
