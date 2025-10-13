package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Unit tests for {@link PersonCard}.
 * - Ensures constructor renders tags sorted and rating visibility correctly.
 * - Covers private getStarString(int) via reflection.
 */
public class PersonCardTest {

    private static Method getStarStringMethod;

    @BeforeAll
    public static void setupFxAndReflection() throws Exception {
        try {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.startup(latch::countDown);
            latch.await(10, TimeUnit.SECONDS);
        } catch (IllegalStateException | UnsupportedOperationException e) {
            // JavaFX already started — ignore
        }

        getStarStringMethod = PersonCard.class.getDeclaredMethod("getStarString", int.class);
        getStarStringMethod.setAccessible(true);
    }

    @Test
    public void unratedPersonHidesRatingAndSortsTags() throws Exception {
        Person unrated = new PersonBuilder()
                .withName("Amy")
                .withPhone("22222222")
                .withAddress("There")
                .withTags("zzz", "aaa")
                .build();

        PersonCard card = runOnFxThread(() -> new PersonCard(unrated, 2));

        Label ratingLabel = (Label) getPrivateField(card, "rating");
        FlowPane tagsPane = (FlowPane) getPrivateField(card, "tags");

        assertFalse(ratingLabel.isVisible(), "Rating label should be hidden when no rating");
        assertEquals("", ratingLabel.getText(), "Rating text should be empty");

        List<String> tags = tagsPane.getChildren().stream()
                .filter(n -> n instanceof Label)
                .map(n -> ((Label) n).getText())
                .collect(Collectors.toList());
        assertEquals(List.of("aaa", "zzz"), tags);
    }

    @Test
    public void getStarStringReturnsCorrectStars() throws Exception {
        assertEquals("☆☆☆☆☆", invokeGetStarString(0));
        assertEquals("★★★☆☆", invokeGetStarString(3));
        assertEquals("★★★★★", invokeGetStarString(5));
    }

    @Test
    public void getStarStringClampsOutOfRange() throws Exception {
        assertEquals("☆☆☆☆☆", invokeGetStarString(-3));
        assertEquals("★★★★★", invokeGetStarString(10));
    }

    // ------------------------------------------------------------
    // helpers
    // ------------------------------------------------------------

    private static Object getPrivateField(Object target, String name) throws Exception {
        Field f = PersonCard.class.getDeclaredField(name);
        f.setAccessible(true);
        return f.get(target);
    }

    private static String invokeGetStarString(int value)
            throws InvocationTargetException, IllegalAccessException {
        return (String) getStarStringMethod.invoke(null, value);
    }

    private static <T> T runOnFxThread(java.util.concurrent.Callable<T> action) throws Exception {
        final Object[] box = new Object[1];
        final Exception[] ex = new Exception[1];
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                box[0] = action.call();
            } catch (Exception e) {
                ex[0] = e;
            } finally {
                latch.countDown();
            }
        });
        if (!latch.await(10, TimeUnit.SECONDS)) {
            throw new AssertionError("FX action timed out");
        }
        if (ex[0] != null) {
            throw ex[0];
        }
        @SuppressWarnings("unchecked")
        T out = (T) box[0];
        return out;
    }
}
