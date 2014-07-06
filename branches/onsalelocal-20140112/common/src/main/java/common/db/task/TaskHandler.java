package common.db.task;

import java.util.List;

import common.db.entity.Task;

public interface TaskHandler {
	int processTask(List<Task> task);
}
