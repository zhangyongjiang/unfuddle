package common.db.dao.impl;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import common.db.dao.ConfigDao;
import common.db.dao.TaskDao;
import common.db.entity.Configuration;
import common.db.entity.Task;
import common.db.task.TaskStatus;

@Repository
public class TaskDaoImpl extends GenericDaoImpl implements TaskDao {

	@Override
	public List<Task> getReadyTasks(String type, int size) {
        String uuid = UUID.randomUUID().toString();
        long now = System.currentTimeMillis();
        String sql = "update Task set updated=" + now + ", handler='" + uuid + "', status='Pending' where status ='Ready' and type='" + type + "' order by updated limit " + size;
        getJdbcTemplate().update(sql);
        List<Task> tasks = query(Task.class, Collections.singletonMap("handler", uuid));
        return tasks;
	}

	@Override
	public void updateStatus(List<Task> tasks) {
		for(Task task : tasks) {
			if(!TaskStatus.Succeed.equals(task.getStatus())) {
				task.setStatus(TaskStatus.Failed);
			}
			else {
				task.setSucceed(task.getSucceed() + 1);
			}
			task.setTried(task.getTried() + 1);
			updateEntity(task, "status", "tried", "succeed");
		}
	}
}
