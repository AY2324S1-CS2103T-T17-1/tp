package seedu.address.model.task;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeadlineTest {

    @Test
    public void stringFormat() {
        final Deadline originalDeadline = new Deadline(LocalDateTime.of(2024,10,25,10,0));

        assertTrue(originalDeadline.toString().equals("2024-10-25 10:00"));
    }

    @Test
    public void equals() {
        final Deadline originalDeadline = new Deadline(LocalDateTime.of(2024,10,25,10,0));

        assertTrue(originalDeadline.equals(originalDeadline));

        final Deadline otherDeadline = new Deadline(LocalDateTime.of(2024,10,25,10,0));

        assertTrue(originalDeadline.equals(otherDeadline));

        final Deadline notEquals = new Deadline(LocalDateTime.of(2024,10,25,10,10));

        assertFalse(originalDeadline.equals(notEquals));

        final Status notDeadline = new Status();

        assertFalse(originalDeadline.equals(notDeadline));
    }

    @Test
    public void isValidDate() {
        String validDate = "25-10-2023";

        assertTrue(Deadline.isValidDate(validDate));

        String invalidDate = "32-10-2023";

        assertFalse(Deadline.isValidDate(invalidDate));
    }
}
