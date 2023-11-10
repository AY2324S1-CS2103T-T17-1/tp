package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TASK_DESC;
import static seedu.address.logic.commands.CommandTestUtil.MEMBER_DESC_DG;
import static seedu.address.logic.commands.CommandTestUtil.MEMBER_DESC_UG;
import static seedu.address.logic.commands.CommandTestUtil.TASK_DESC_DG;
import static seedu.address.logic.commands.CommandTestUtil.TASK_DESC_UG;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESC_DG;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MEMBER_DG;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MEMBER_UG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEMBER;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_TASK;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_TASK;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_TASK;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditTaskDescriptor;
import seedu.address.model.member.Member;
import seedu.address.model.task.Description;
import seedu.address.testutil.EditTaskDescriptorBuilder;

public class EditCommandParserTest {
    private static final String MEMBER_EMPTY = " " + PREFIX_MEMBER;

    private final EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        String errString = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.COMMAND_WORD) + "\nUsage: "
                + EditCommand.MESSAGE_USAGE;

        // no index specified
        assertParseFailure(parser, VALID_DESC_DG, ParserUtil.MESSAGE_INVALID_INDEX);

        // no field specified
        assertParseFailure(parser, "1", EditCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", errString);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + TASK_DESC_DG, ParserUtil.MESSAGE_INVALID_INDEX);

        // zero index
        assertParseFailure(parser, "0" + TASK_DESC_DG, ParserUtil.MESSAGE_INVALID_INDEX);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", ParserUtil.MESSAGE_INVALID_INDEX);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", ParserUtil.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_TASK_DESC, Description.MESSAGE_CONSTRAINTS); // invalid name

        // while parsing {@code PREFIX_MEMBER} alone will reset the members of the {@code Task} being edited,
        // parsing it together with a valid member results in error
        assertParseFailure(parser, "1" + MEMBER_DESC_UG + MEMBER_DESC_DG + MEMBER_EMPTY,
                Member.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + MEMBER_DESC_UG + MEMBER_EMPTY + MEMBER_DESC_DG,
                Member.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + MEMBER_EMPTY + MEMBER_DESC_UG + MEMBER_DESC_DG,
                Member.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_TASK_DESC,
                Description.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_TASK;
        String userInput = targetIndex.getOneBased() + MEMBER_DESC_DG
                + TASK_DESC_DG + MEMBER_DESC_UG;

        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withDescription(VALID_DESC_DG)
                .withMembers(VALID_MEMBER_DG, VALID_MEMBER_UG).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_TASK;
        String userInput = targetIndex.getOneBased() + TASK_DESC_DG;

        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder()
                .withDescription(VALID_DESC_DG).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        Index targetIndex = INDEX_THIRD_TASK;
        String userInput = targetIndex.getOneBased() + TASK_DESC_DG;
        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withDescription(VALID_DESC_DG).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // members
        userInput = targetIndex.getOneBased() + MEMBER_DESC_UG;
        descriptor = new EditTaskDescriptorBuilder().withMembers(VALID_MEMBER_UG).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        // More extensive testing of duplicate parameter detections is done in
        // AddCommandParserTest#parse_repeatedNonTagValue_failure()

        // valid followed by invalid
        Index targetIndex = INDEX_FIRST_TASK;
        String userInput = targetIndex.getOneBased() + TASK_DESC_DG + INVALID_TASK_DESC;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DESCRIPTION));

        // invalid followed by valid
        userInput = targetIndex.getOneBased() + INVALID_TASK_DESC + TASK_DESC_UG;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DESCRIPTION));

        // mulltiple valid fields repeated
        userInput = targetIndex.getOneBased() + TASK_DESC_DG + TASK_DESC_UG + TASK_DESC_DG;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DESCRIPTION));
    }

    @Test
    public void parse_resetMembers_success() {
        Index targetIndex = INDEX_THIRD_TASK;
        String userInput = targetIndex.getOneBased() + MEMBER_EMPTY;

        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withMembers().build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
