package common.db.dao;

import java.util.List;

import common.db.entity.Task;

public interface TaskDao extends GenericDao {

	List<Task> getReadyTasks(String type, int size);

	void updateStatus(List<Task> tasks);

}
