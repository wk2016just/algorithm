package greedytask;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class TaskSchedul {
	private List<Task> tasks = null; // 所有任务
	private List<Task> earlyTasks = null; // 早任务
	private List<Task> lateTasks = null; // 晚任务
	private Comparator<Task> comparator_d = null; // 将任务按惩罚大小降序排序
	private Comparator<Task> comparator_w = null; // 按(惩罚)w降序排列任务

	
	class Task { // 任务定义
		private int id;
		private int deadLine;
		private int weight;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getDeadLine() {
			return deadLine;
		}

		public void setDeadLine(int deadLine) {
			this.deadLine = deadLine;
		}

		public int getWeight() {
			return weight;
		}

		public void setWeight(int weight) {
			this.weight = weight;
		}
	}
	
	public TaskSchedul(int[] d, int[] w) {
		initComparator();
		init(d, w);
	}
	

	public void init(int[] d, int[] w) { // 导入初始任务
		tasks = new ArrayList<Task>();
		earlyTasks = new ArrayList<Task>();
		lateTasks = new ArrayList<Task>();
		int n = d.length;
		for (int i = 0; i < n; i++) {
			Task t = new Task();
			t.setId(i);
			t.setDeadLine(d[i]);
			t.setWeight(w[i]);
			tasks.add(t);
		}
		System.out.print("任务集为：");
		for (Task task : tasks) {
			System.out.print("a" + (task.getId() + 1) + "(d:"
					+ task.getDeadLine() + ",w:" + task.getWeight() + ") ");
		}
		System.out.println();
		Collections.sort(tasks, comparator_w); // 将任务按惩罚大小降序排序
	}

	public void initComparator() {
		comparator_d = new Comparator<Task>() { // 按deadline升序排列任务
			@Override
			public int compare(Task t1, Task t2) {
				if (t1.getDeadLine() > t2.getDeadLine()) {
					return 1;
				} else if (t1.getDeadLine() == t2.getDeadLine()) {
					return 0;
				} else {
					return -1;
				}
			}
		};
		comparator_w = new Comparator<Task>() { // 按(惩罚)w降序排列任务
			@Override
			public int compare(Task t1, Task t2) {
				if (t2.getWeight() > t1.getWeight()) {
					return 1;
				} else if (t2.getWeight() == t1.getWeight()) {
					return 0;
				} else {
					return -1;
				}
			}
		};
	}

	public void printList() {
		int punish = 0;
		System.out.print("贪心算法选择任务为：");
		for (Task t : earlyTasks) {
			System.out.print("a" + (t.getId() + 1) + " ");
		}
		System.out.print("\n被惩罚任务为：");
		for (Task t : lateTasks) {
			System.out.print("a" + (t.getId() + 1) + " ");
		}
		System.out.print("\n总的惩罚数为：");
		for (Task t : lateTasks) {
			punish += t.getWeight();
		}
		System.out.println(punish + "");
	}
	
		
	public static void main(String[] args) {
		int[] d = { 4,2,4,3,1,4,6}; // 各个任务的deadline
		int[] w = { 70, 60, 50, 40, 30,20,10}; // 各个任务的惩罚

		
		TaskSchedul ts = new TaskSchedul(d, w);
		ts.scheduleTask(); // 任务调度
		ts.printList(); // 输出信息
		System.out.println("-------用max{w1,12,...,wn}-wi替换后的结果-----");
		ts.rescheduleTask(); // 用max{w1,12,...,wn}-wi替换wi后，任务调度
		ts.printList(); // 输出信息
	}

	public void rescheduleTask() { // 用max{w1,12,...,wn}-wi替换wi
		int max = tasks.get(0).getWeight(); // 在init()函数中已经将tasks中的任务按惩罚大小降序排序
		for (Task task : tasks) {
			task.setWeight(max - task.getWeight());
		}
		System.out.print("任务集为：");
		for (Task task : tasks) {
			System.out.print("a" + (task.getId() + 1) + "(d:"
					+ task.getDeadLine() + ",w:" + task.getWeight() + ") ");
		}
		System.out.println();
		Collections.sort(tasks, comparator_w); // 将任务按惩罚大小降序排序
		earlyTasks = new ArrayList<Task>();
		lateTasks = new ArrayList<Task>();
		scheduleTask();
	}

	
	public void scheduleTask() { 
		int n = tasks.size();
		int[] NT = new int[n]; // 标记，用来判断任务集是否独立（按照期限递增排序没有一个是迟任务）即NT表示A中期限为t或更早的任务集
		for (int i = 0; i < n; i++) { // NT[0..n-1];
			NT[i] = 0;
		}
		/* 如果task期限大于temp期限并且time<task期限即在task的deadline范围以内，那么可以暂时加入独立集；
		 * 如果大于task期限，到之后再调度
		 * 如果仍然在task范围内但是time已经超出了NT个数，那么直接置为1
		 * 
		 * 第二次三次四次循环的task直接可以利用time=4形成的独立子集，只要有任务在他们的deadline范围内，肯定是不兼容
		 * a7最后一次
		 */
		for (int i = 0; i < n && NT[i] == 0; i++) {  //循环n个任务，每次判断Nt(A)
			int time = 1;
			Task task = tasks.get(i);
			for (int j = i + 1; j < n && NT[i] == 0 && NT[j] == 0; j++) {
				Task temp = tasks.get(j);
				if (task.getDeadLine() >= temp.getDeadLine()) { 
					/* 如果当前任务循环deadline次数之后，仍然比当前j的deadline大，那么j已经不能放入独立集了;比如a0判断到a4仍然满足，到a5,a6发现<time
					 * 如果初始就比task大，留到最后判断,比如a7比a0deadline大，那么留到最后判断
					 * 下一个task例如a2开始判断时，time=4.那么运行时time一直为4，如果当前a2的deadline 2>tmp的deadline则直接放不下
					 * a3,a5如此；到a7直接不用判断了
					 */
					if (time <= task.getDeadLine()) {  //当前任务期限时间比当前时间大
						time++;
					}
					if (time > task.getDeadLine()) {  //期限时间小于当前时间，加入晚任务并且不独立，之后循环就不用判断
						lateTasks.add(temp);
						NT[j] = 1;  //已经放不下
					}
				}
			}
		}
		
		for (int i = 0; i < n; i++) {
			if (NT[i] == 0) {  //根据标记的独立集把任务加入早任务列表
				earlyTasks.add(tasks.get(i));
			}
		}
		Collections.sort(earlyTasks, comparator_d);
	}

	




}

