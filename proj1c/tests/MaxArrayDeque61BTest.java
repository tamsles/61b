import deque.Deque61B;
import deque.LinkedListDeque61B;
import org.junit.jupiter.api.*;

import java.util.Comparator;
import deque.MaxArrayDeque61B;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class MaxArrayDeque61BTest {
    private static class StringLengthComparator implements Comparator<String> {
        public int compare(String a, String b) {
            return a.length() - b.length();
        }
    }

    @Test
    public void basicTest() {
        MaxArrayDeque61B<String> mad = new MaxArrayDeque61B<>(new StringLengthComparator());
        mad.addFirst("");
        mad.addFirst("2");
        mad.addFirst("fury road");
        assertThat(mad.max()).isEqualTo("fury road");
    }

    @Test
    public void testIterator() {
        Deque61B<String> d = new LinkedListDeque61B<>();
        d.addLast("a");
        d.addLast("b");
        d.addLast("c");

        StringBuilder sb = new StringBuilder();
        for (String s : d) {
            sb.append(s);
        }
        assertThat(sb.toString()).isEqualTo("abc");
    }
}
