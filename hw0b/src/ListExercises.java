import java.util.ArrayList;
import java.util.List;

public class ListExercises {

    /** Returns the total sum in a list of integers */
	public static int sum(List<Integer> L) {
        // TODO: Fill in this function.
        int sum = 0;
        for(Integer i : L) {
            sum += i;
        }
        return sum;
    }

    /** Returns a list containing the even numbers of the given list */
    public static List<Integer> evens(List<Integer> L) {
        // TODO: Fill in this function.
        List<Integer> evens = new ArrayList<>();
        for(Integer i : L){
            if (i % 2 == 0) {
                evens.add(i);
            }
        }
        return evens;
    }

    /** Returns a list containing the common item of the two given lists */
    public static List<Integer> common(List<Integer> L1, List<Integer> L2) {
        // TODO: Fill in this function.
        List<Integer> common = new ArrayList<>();
        for(int i = 0; i < L1.size(); i++) {
            Integer x = L1.get(i);
            if(L2.contains(x) && !common.contains(x)){
                common.add(x);
            }
        }
        return common;
    }


    /** Returns the number of occurrences of the given character in a list of strings. */
    public static int countOccurrencesOfC(List<String> words, char c) {
        // TODO: Fill in this function.
        int res = 0;
        for(int i = 0; i < words.size(); i++) {
            String word = words.get(i);

            for(int j = 0; j < word.length(); j++) {
                if(word.charAt(j) == c) {}
            }
        }
        return 0;
    }
}
