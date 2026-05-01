package deque;

import java.util.ArrayDeque;
import java.util.Comparator;

public class MaxArrayDeque61B<T> extends ArrayDeque61B<T> {
    private Comparator<T> comparator;


    public T max(){
        return max(comparator);
    }

    public T max(Comparator<T> c){
        if(isEmpty()){
            return null;
        }

        T best = get(0);
        for(T x : this){
            if(c.compare(best,x) < 0){
                best = x;
            }
        }
        return best;
    }

    public MaxArrayDeque61B(Comparator<T> c){
        comparator = c;
    }
}
