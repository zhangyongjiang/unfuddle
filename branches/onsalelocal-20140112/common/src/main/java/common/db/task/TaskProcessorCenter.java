package common.db.task;

public interface TaskProcessorCenter {

	int processTask(String type, int size);

	void register(String type, TaskHandler handler);

}
