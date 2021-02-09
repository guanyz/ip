package duke;

/**
 * Parses user commands and triggers corresponding effects.
 */
public class CommandParser {
    private static final int SPLIT_LIMIT = 2;
    private static final String DONE_REGEX_MATCH = "^(do(ne)?|finish(ed)?|completed?) \\d+$";
    private static final String DELETE_REGEX_MATCH = "^(delete|remove) \\d+$";
    private static final String TASK_REGEX_MATCH = "^(todo|deadline|event)( .+)?$";
    private static final String FIND_REGEX_MATCH = "^(find|search) \\d+$";

    private TaskList tasks;
    private Ui ui;

    public CommandParser(TaskList tasks, Ui ui) {
        this.tasks = tasks;
        this.ui = ui;
    }

    /**
     * Parses a user-input command and triggers the relevant effects.
     * @param userInput String representation of the command to be parsed.
     * @return String response to the command.
     * @throws DukeException If an Exception occurs due to a malformed command.
     */
    public String parseCommand(String userInput) throws DukeException {
        String reply;
        if (userInput.toLowerCase().equals("list")) {
            // display list
            reply = ui.displayList(tasks);

        } else if (userInput.toLowerCase().matches(DONE_REGEX_MATCH)) {
            // finish a task
            String[] bits = userInput.split(" ");

            int idx = Integer.parseInt(bits[1]);
            if (idx < 1 || idx > tasks.size()) {
                throw new DukeException("Oops! That doesn't appear to be a valid task number.");
            }

            Task finishedTask = tasks.get(idx);

            if (finishedTask.isDone()) {
                throw new DukeException("That task's already done!");
            }

            finishedTask.markAsDone();
            reply = ui.showDoneTask(finishedTask);

        } else if (userInput.toLowerCase().matches(DELETE_REGEX_MATCH)) {
            // manually remove task
            String[] bits = userInput.split(" ");

            int idx = Integer.parseInt(bits[1]);
            if (idx < 1 || idx > tasks.size()) {
                throw new DukeException("Oops! That doesn't appear to be a valid task number.");
            }

            Task removedTask = tasks.remove(idx);
            reply = ui.showRemovedTask(removedTask, tasks.size());

        } else if (userInput.toLowerCase().matches(TASK_REGEX_MATCH)) {
            // add task to list
            Task newTask = TaskParser.parseTask(userInput);
            tasks.add(newTask);
            reply = ui.showAddedTask(newTask, tasks.size());

        } else if (userInput.toLowerCase().matches(FIND_REGEX_MATCH)) {
            // find task in list
            String[] bits = userInput.split(" ", SPLIT_LIMIT);
            if (bits.length == 1) {
                throw new DukeException("Oops! Usage: find [search pattern]");
            }

            TaskList matchingTasks = tasks.find(bits[1]);
            reply = ui.displayList(matchingTasks);

        } else {
            throw new DukeException("I don't understand that command!");
        }
        return reply;
    }
}