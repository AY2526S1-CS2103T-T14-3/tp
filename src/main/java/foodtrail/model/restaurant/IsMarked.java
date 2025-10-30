package foodtrail.model.restaurant;

/**
 * Represents the visited status of a restaurant.
 * A restaurant can either be marked as visited or not visited.
 */
public class IsMarked {
    private boolean visited;

    public IsMarked(boolean visited) {
        this.visited = visited;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    @Override
    public String toString() {
        return visited ? "[ X ] " : "[   ] ";
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof IsMarked)) {
            return false;
        }

        IsMarked otherIsMarked = (IsMarked) other;
        return visited == otherIsMarked.visited;
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(visited);
    }
}
