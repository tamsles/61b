import jh61b.utils.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class ArrayDeque61BTest {

    @Test
    @DisplayName("ArrayDeque61B has no fields besides backing array and primitives")
    void noNonTrivialFields() {
        List<Field> badFields = Reflection.getFields(ArrayDeque61B.class)
                .filter(f -> !(f.getType().isPrimitive()
                        || f.getType().equals(Object[].class)
                        || f.isSynthetic()))
                .toList();

        assertWithMessage("Found fields that are not array or primitives")
                .that(badFields)
                .isEmpty();
    }

    @Test
    @DisplayName("New deque should be empty")
    void testConstructor() {
        ArrayDeque61B<Integer> ad = new ArrayDeque61B<>();

        assertThat(ad.isEmpty()).isTrue();
        assertThat(ad.size()).isEqualTo(0);
        assertThat(ad.toList()).isEmpty();
    }

    @Test
    @DisplayName("addFirst should add items to front in correct order")
    void testAddFirst() {
        ArrayDeque61B<Integer> ad = new ArrayDeque61B<>();

        ad.addFirst(10);
        ad.addFirst(20);
        ad.addFirst(30);

        assertThat(ad.size()).isEqualTo(3);
        assertThat(ad.toList()).containsExactly(30, 20, 10).inOrder();
    }

    @Test
    @DisplayName("addLast should add items to back in correct order")
    void testAddLast() {
        ArrayDeque61B<Integer> ad = new ArrayDeque61B<>();

        ad.addLast(10);
        ad.addLast(20);
        ad.addLast(30);

        assertThat(ad.size()).isEqualTo(3);
        assertThat(ad.toList()).containsExactly(10, 20, 30).inOrder();
    }

    @Test
    @DisplayName("addFirst and addLast should work together")
    void testAddFirstAndAddLast() {
        ArrayDeque61B<Integer> ad = new ArrayDeque61B<>();

        ad.addFirst(10);
        ad.addLast(20);
        ad.addFirst(5);
        ad.addLast(30);

        assertThat(ad.size()).isEqualTo(4);
        assertThat(ad.toList()).containsExactly(5, 10, 20, 30).inOrder();
    }

    @Test
    @DisplayName("removeFirst should remove and return front item")
    void testRemoveFirst() {
        ArrayDeque61B<Integer> ad = new ArrayDeque61B<>();

        ad.addLast(10);
        ad.addLast(20);
        ad.addLast(30);

        assertThat(ad.removeFirst()).isEqualTo(10);
        assertThat(ad.size()).isEqualTo(2);
        assertThat(ad.toList()).containsExactly(20, 30).inOrder();
    }

    @Test
    @DisplayName("removeLast should remove and return back item")
    void testRemoveLast() {
        ArrayDeque61B<Integer> ad = new ArrayDeque61B<>();

        ad.addLast(10);
        ad.addLast(20);
        ad.addLast(30);

        assertThat(ad.removeLast()).isEqualTo(30);
        assertThat(ad.size()).isEqualTo(2);
        assertThat(ad.toList()).containsExactly(10, 20).inOrder();
    }

    @Test
    @DisplayName("removeFirst and removeLast should work together")
    void testRemoveFirstAndRemoveLast() {
        ArrayDeque61B<Integer> ad = new ArrayDeque61B<>();

        ad.addLast(10);
        ad.addLast(20);
        ad.addLast(30);
        ad.addLast(40);

        assertThat(ad.removeFirst()).isEqualTo(10);
        assertThat(ad.removeLast()).isEqualTo(40);
        assertThat(ad.size()).isEqualTo(2);
        assertThat(ad.toList()).containsExactly(20, 30).inOrder();
    }

    @Test
    @DisplayName("Removing from empty deque should return null")
    void testRemoveEmpty() {
        ArrayDeque61B<Integer> ad = new ArrayDeque61B<>();

        assertThat(ad.removeFirst()).isNull();
        assertThat(ad.removeLast()).isNull();
        assertThat(ad.size()).isEqualTo(0);
        assertThat(ad.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("get should return correct items")
    void testGet() {
        ArrayDeque61B<String> ad = new ArrayDeque61B<>();

        ad.addLast("a");
        ad.addLast("b");
        ad.addLast("c");

        assertThat(ad.get(0)).isEqualTo("a");
        assertThat(ad.get(1)).isEqualTo("b");
        assertThat(ad.get(2)).isEqualTo("c");
    }

    @Test
    @DisplayName("get should return null for invalid indices")
    void testGetOutOfBounds() {
        ArrayDeque61B<String> ad = new ArrayDeque61B<>();

        ad.addLast("a");
        ad.addLast("b");

        assertThat(ad.get(-1)).isNull();
        assertThat(ad.get(2)).isNull();
        assertThat(ad.get(100)).isNull();
    }

    @Test
    @DisplayName("Deque should resize correctly when full")
    void testResize() {
        ArrayDeque61B<Integer> ad = new ArrayDeque61B<>();

        for (int i = 0; i < 20; i++) {
            ad.addLast(i);
        }

        assertThat(ad.size()).isEqualTo(20);

        for (int i = 0; i < 20; i++) {
            assertThat(ad.get(i)).isEqualTo(i);
        }

        assertThat(ad.toList())
                .containsExactly(
                        0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
                        10, 11, 12, 13, 14, 15, 16, 17, 18, 19
                ).inOrder();
    }

    @Test
    @DisplayName("Deque should handle wraparound correctly")
    void testWrapAround() {
        ArrayDeque61B<Integer> ad = new ArrayDeque61B<>();

        for (int i = 0; i < 8; i++) {
            ad.addLast(i);
        }

        for (int i = 0; i < 4; i++) {
            assertThat(ad.removeFirst()).isEqualTo(i);
        }

        for (int i = 8; i < 12; i++) {
            ad.addLast(i);
        }

        assertThat(ad.size()).isEqualTo(8);
        assertThat(ad.toList()).containsExactly(4, 5, 6, 7, 8, 9, 10, 11).inOrder();
    }

    @Test
    @DisplayName("Mixed operations should maintain correct order")
    void testMixedOperations() {
        ArrayDeque61B<Integer> ad = new ArrayDeque61B<>();

        ad.addFirst(2);   // [2]
        ad.addFirst(1);   // [1, 2]
        ad.addLast(3);    // [1, 2, 3]
        ad.addLast(4);    // [1, 2, 3, 4]

        assertThat(ad.removeFirst()).isEqualTo(1); // [2, 3, 4]
        assertThat(ad.removeLast()).isEqualTo(4);  // [2, 3]

        ad.addFirst(1);   // [1, 2, 3]
        ad.addLast(4);    // [1, 2, 3, 4]

        assertThat(ad.toList()).containsExactly(1, 2, 3, 4).inOrder();
        assertThat(ad.size()).isEqualTo(4);
    }
}