package com.example.task_tracker.config;

import java.time.LocalDate;
import java.util.List;

import com.example.task_tracker.project.model.Project;
import com.example.task_tracker.project.repository.ProjectRepository;
import com.example.task_tracker.task.model.Task;
import com.example.task_tracker.task.model.TaskPriority;
import com.example.task_tracker.task.model.TaskStatus;
import com.example.task_tracker.task.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Initializes demo data for first-time runs of the application.
 * <p>
 * This component inserts a couple of example projects and tasks when the
 * application starts and the database does not yet contain any tasks.
 * <p>
 * Controlled via {@code tasktracker.demo-data.enabled}. If the flag is {@code false},
 * no data will be inserted even if the database is empty.
 */
@Component
@Profile("!test")
public class DemoDataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoDataInitializer.class);

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final boolean demoDataEnabled;

    /**
     * Creates a new {@code DemoDataInitializer} with the required repositories.
     *
     * @param taskRepository the repository used for task persistence
     * @param projectRepository the repository used for project persistence
     * @param demoDataEnabled flag that controls whether demo data may be inserted
     */
    public DemoDataInitializer(
        TaskRepository taskRepository, ProjectRepository projectRepository,
        @Value("${tasktracker.demo-data.enabled:true}") boolean demoDataEnabled)
    {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.demoDataEnabled = demoDataEnabled;
    }

    @Override
    public void run(String... args) {
        if (!demoDataEnabled) {
            log.info(
                "Demo data initialization disabled via 'tasktracker.demo-data.enabled=false'.");
            return;
        }

        // Do not touch existing data! If there are tasks, we assume the user already works with the app.
        if (taskRepository.count() > 0) {
            log.debug("Skipping demo data initialization because tasks already exist.");
            return;
        }

        log.info("No tasks found, creating demo projects and tasks …");

        // --- Create some example projects ---
        Project taskTracker = new Project();
        taskTracker.setName("Task Tracker");
        taskTracker.setDescription("Tasks for this demo application.");
        taskTracker.setColor("#3b82f6");

        Project masterThesis = new Project();
        masterThesis.setName("Master Thesis");
        masterThesis.setDescription("Writing, experiments and evaluations.");
        masterThesis.setColor("#a855f7");

        Project household = new Project();
        household.setName("Household");
        household.setDescription("Recurring chores and to-dos at home.");
        household.setColor("#22c55e");

        projectRepository.saveAll(List.of(taskTracker, masterThesis, household));

        // --- Create some example tasks ---
        Task t1 = new Task(
            "Add project support to Task Tracker",
            "Introduce projects as a way to group tasks in the demo app.", TaskStatus.IN_PROGRESS,
            TaskPriority.HIGH, LocalDate.now().plusDays(3));
        t1.setProject(taskTracker);

        Task t2 = new Task(
            "Polish frontend styling",
            "Refine table layout, sorting and filters for the task list.", TaskStatus.OPEN,
            TaskPriority.MEDIUM, LocalDate.now().plusDays(7));
        t2.setProject(taskTracker);

        Task t3 = new Task(
            "Outline thesis chapters", "Draft the chapter structure and main research questions.",
            TaskStatus.OPEN, TaskPriority.HIGH, LocalDate.now().plusWeeks(2));
        t3.setProject(masterThesis);

        Task t4 = new Task(
            "Deep clean kitchen", "Do a thorough clean-up including fridge and oven.",
            TaskStatus.OPEN, TaskPriority.LOW, null);
        t4.setProject(household);

        taskRepository.saveAll(List.of(t1, t2, t3, t4));

        log.info(
            "Demo data initialization finished: {} projects, {} tasks.", projectRepository.count(),
            taskRepository.count());
    }

}