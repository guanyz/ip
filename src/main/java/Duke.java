import java.util.ArrayList;
import java.util.Scanner;

public class Duke {
    public static void main(String[] args) {
        String DIVIDER = "____________________________________________________________";

        ArrayList<Task> tasks = new ArrayList<Task>();

        // initialize scanner
        Scanner sc = new Scanner(System.in);

        // welcome sequence
        System.out.println(DIVIDER);
        System.out.println("Welcome to Duke!");
        System.out.println("What can I do for you?");
        System.out.println(DIVIDER);

        // get user input
        String userInput = sc.nextLine();

        // loop until the user exits
        while (!userInput.toLowerCase().equals("bye")) {
            System.out.println(DIVIDER);
            try {
                // display list
                if (userInput.toLowerCase().equals("list")) {
                    for (int i = 0; i < tasks.size(); i++) {
                        String item = String.valueOf(i + 1) + ". " + tasks.get(i).toString();
                        System.out.println(item);
                    }
                // finish a task
                } else if (userInput.toLowerCase().matches("^(do(ne)?|finish(ed)?|completed?) \\d+$")) {
                    String[] bits = userInput.split(" ");
                    int idx = Integer.parseInt(bits[1]) - 1; // zero-indexed task index
                    if (idx < 0 || idx >= tasks.size()) {
                        throw new Exception("Oops! That doesn't appear to be a valid task number.");
                    } else {
                        tasks.get(idx).finish();
                    }
                } else if (userInput.toLowerCase().matches("^(delete|remove) \\d+$")) {
                    String[] bits = userInput.split(" ");
                    int idx = Integer.parseInt(bits[1]) - 1; // zero-indexed task index
                    if (idx < 0 || idx >= tasks.size()) {
                        throw new Exception("Oops! That doesn't appear to be a valid task number.");
                    } else {
                        System.out.println("Removed task: " + tasks.remove(idx).toString());
                        System.out.printf("You now have %d items on your todo list.\n", tasks.size());
                    }
                // add task to list
                } else if (userInput.toLowerCase().matches("^(todo|deadline|event)( .+)?$")) {
                    String[] bits = userInput.split(" ");
                    if (bits.length == 1) {
                        throw new DukeException("Oops! The description of a task cannot be empty.");
                    } else {
                        // format entry
                        StringBuilder descBuilder = new StringBuilder();
                        for (int i = 1; i < bits.length; i++) {
                            descBuilder.append(bits[i]);
                            descBuilder.append(" ");
                        }
                        String desc = descBuilder.toString().trim();

                        Task newTask = new Task(desc.toString()); // placeholder
                        if (bits[0].toLowerCase().equals("todo")) {
                            newTask = new Todo(desc.toString());
                        } else if (bits[0].toLowerCase().equals("event")) {
                            String[] taskParts = desc.toString().split(" /on ");
                            if (taskParts.length == 1) {
                                throw new DukeException("Oops! Usage: event [desc] /on [date]");
                            } else {
                                newTask = new Event(taskParts[0], taskParts[1]);
                            }
                        } else if (bits[0].toLowerCase().equals("deadline")) {
                            String[] taskParts = desc.toString().split(" /by ");
                            if (taskParts.length == 1) {
                                throw new DukeException("Oops! Usage: deadline [desc] /by [date]");
                            } else {
                                newTask = new Deadline(taskParts[0], taskParts[1]);
                            }
                        }
                        tasks.add(newTask);
                        System.out.println("I've added this task: " + newTask.toString());
                        System.out.printf("You now have %d items on your todo list.\n", tasks.size());
                    }
                } else {
                    throw new DukeException("I don't understand that command!");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            System.out.println(DIVIDER);

            // get next input
            userInput = sc.nextLine();
        }

        // exit sequence
        System.out.println(DIVIDER);
        System.out.println("Bye! Hope to see you again :)");
        System.out.println(DIVIDER);
    }
}
