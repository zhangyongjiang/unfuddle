package common.db.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import common.db.dao.TaskDao;
import common.db.entity.Task;

@Service("taskProcessorCenter")
@Transactional(readOnly=true)
public class TaskProcessCenterImpl implements TaskProcessorCenter {
	private static final Logger log = Logger.getLogger(TaskProcessCenterImpl.class);
	private Map<String, TaskHandler> handlers = new HashMap<String, TaskHandler>();
	
	@Autowired private TaskDao taskDao;
	
	@Override
	public void register(String type, TaskHandler handler) {
		handlers.put(type, handler);
	}
	
	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public int processTask(String type, int size) {
		TaskHandler handler = handlers.get(type);
		List<Task> tasks = taskDao.getReadyTasks(type, size);
		log.info("got " + tasks.size() + " tasks for " + type);
		if(tasks.size() > 0) {
			handler.processTask(tasks);
			taskDao.updateStatus(tasks);
		}
		return tasks.size();
	}
}
