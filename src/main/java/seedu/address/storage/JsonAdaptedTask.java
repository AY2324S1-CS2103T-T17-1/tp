package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.model.tag.Tag;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Description;
import seedu.address.model.task.Status;
import seedu.address.model.task.Task;
import seedu.address.storage.exceptions.json.IllegalJsonDescriptionValueException;
import seedu.address.storage.exceptions.json.IllegalJsonValueException;

/**
 * Jackson-friendly version of {@link Task}.
 */
class JsonAdaptedTask {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Task's %s field is missing!";

    private final String description;
    private final boolean status;
    private final Deadline deadline;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedTask} with the given task details.
     */
    @JsonCreator
    public JsonAdaptedTask(@JsonProperty("name") String description,
                           @JsonProperty("tags") List<JsonAdaptedTag> tags,
                           @JsonProperty("status") boolean status,
                           @JsonProperty("deadline") Deadline deadline) {
        this.description = description;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        this.status = status;
        this.deadline = deadline;
    }

    /**
     * Converts a given {@code Task} into this class for Jackson use.
     */
    public JsonAdaptedTask(Task source) {
        description = source.getDescription().fullDescription;
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
        status = source.getStatus().isCompleted();
        deadline = source.getDeadline();
    }

    /**
     * Converts this Jackson-friendly adapted task object into the model's {@code Task} object.
     *
     * @throws IllegalJsonValueException if there were any data constraints violated in the adapted task.
     */
    public Task toModelType() throws IllegalJsonValueException {
        final List<Tag> taskTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            taskTags.add(tag.toModelType());
        }

        if (description == null) {
            throw new IllegalJsonDescriptionValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Description.class.getSimpleName()));
        }

        if (!Description.isValidDescription(description)) {
            throw new IllegalJsonDescriptionValueException(Description.MESSAGE_CONSTRAINTS);
        }

        final Description modelName = new Description(description);

        final Status modelStatus = new Status(status);

        return new Task(modelName, modelStatus, deadline);
    }

}
