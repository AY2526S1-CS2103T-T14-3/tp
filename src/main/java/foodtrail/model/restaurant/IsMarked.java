package foodtrail.model.restaurant;

/**
 * Represents a Restaurant's marked status in the restaurant directory.
 */
public class IsMarked {

    public final boolean isMarked;

    /**
     * Constructs an {@code IsMarked}.
     *
     * @param isMarked A valid marked status.
     */
    public IsMarked(boolean isMarked) {
        this.isMarked = isMarked;
    }

    @Override
    public String toString() {
        return isMarked ? "[X]" : "[ ]";
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof IsMarked // instanceof handles nulls
                && isMarked == ((IsMarked) other).isMarked); // state check
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(isMarked);
    }
}
