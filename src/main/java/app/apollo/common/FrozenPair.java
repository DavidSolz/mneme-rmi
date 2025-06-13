package app.apollo.common;

/***
 * Pair class represents a container that stores a pair of generic elements.
 *
 */
public class FrozenPair<First, Second> {

    /*** First element in pair */
    private First first;

    /*** Second element in pair */
    private Second second;

    /***
     * Default constructor that initializes pair.
     *
     * @param first  First element in pair
     * @param second Second elemen in pair
     */
    public FrozenPair(First first, Second second) {
        this.first = first;
        this.second = second;
    }

    /**
     *
     * @return First element from collection
     */
    public First getFirst() {
        return first;
    }

    /***
     *
     * @return Second element from collection.
     */
    public Second getSecond() {
        return second;
    }


}
