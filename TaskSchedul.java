package greedytask;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class TaskSchedul {
	private List<Task> tasks = null; // ��������
	private List<Task> earlyTasks = null; // ������
	private List<Task> lateTasks = null; // ������
	private Comparator<Task> comparator_d = null; // �����񰴳ͷ���С��������
	private Comparator<Task> comparator_w = null; // ��(�ͷ�)w������������

	
	class Task { // ������
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
	

	public void init(int[] d, int[] w) { // �����ʼ����
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
		System.out.print("����Ϊ��");
		for (Task task : tasks) {
			System.out.print("a" + (task.getId() + 1) + "(d:"
					+ task.getDeadLine() + ",w:" + task.getWeight() + ") ");
		}
		System.out.println();
		Collections.sort(tasks, comparator_w); // �����񰴳ͷ���С��������
	}

	public void initComparator() {
		comparator_d = new Comparator<Task>() { // ��deadline������������
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
		comparator_w = new Comparator<Task>() { // ��(�ͷ�)w������������
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
		System.out.print("̰���㷨ѡ������Ϊ��");
		for (Task t : earlyTasks) {
			System.out.print("a" + (t.getId() + 1) + " ");
		}
		System.out.print("\n���ͷ�����Ϊ��");
		for (Task t : lateTasks) {
			System.out.print("a" + (t.getId() + 1) + " ");
		}
		System.out.print("\n�ܵĳͷ���Ϊ��");
		for (Task t : lateTasks) {
			punish += t.getWeight();
		}
		System.out.println(punish + "");
	}
	
		
	public static void main(String[] args) {
		int[] d = { 4,2,4,3,1,4,6}; // ���������deadline
		int[] w = { 70, 60, 50, 40, 30,20,10}; // ��������ĳͷ�

		
		TaskSchedul ts = new TaskSchedul(d, w);
		ts.scheduleTask(); // �������
		ts.printList(); // �����Ϣ
		System.out.println("-------��max{w1,12,...,wn}-wi�滻��Ľ��-----");
		ts.rescheduleTask(); // ��max{w1,12,...,wn}-wi�滻wi���������
		ts.printList(); // �����Ϣ
	}

	public void rescheduleTask() { // ��max{w1,12,...,wn}-wi�滻wi
		int max = tasks.get(0).getWeight(); // ��init()�������Ѿ���tasks�е����񰴳ͷ���С��������
		for (Task task : tasks) {
			task.setWeight(max - task.getWeight());
		}
		System.out.print("����Ϊ��");
		for (Task task : tasks) {
			System.out.print("a" + (task.getId() + 1) + "(d:"
					+ task.getDeadLine() + ",w:" + task.getWeight() + ") ");
		}
		System.out.println();
		Collections.sort(tasks, comparator_w); // �����񰴳ͷ���С��������
		earlyTasks = new ArrayList<Task>();
		lateTasks = new ArrayList<Task>();
		scheduleTask();
	}

	
	public void scheduleTask() { 
		int n = tasks.size();
		int[] NT = new int[n]; // ��ǣ������ж������Ƿ�������������޵�������û��һ���ǳ����񣩼�NT��ʾA������Ϊt����������
		for (int i = 0; i < n; i++) { // NT[0..n-1];
			NT[i] = 0;
		}
		/* ���task���޴���temp���޲���time<task���޼���task��deadline��Χ���ڣ���ô������ʱ�����������
		 * �������task���ޣ���֮���ٵ���
		 * �����Ȼ��task��Χ�ڵ���time�Ѿ�������NT��������ôֱ����Ϊ1
		 * 
		 * �ڶ��������Ĵ�ѭ����taskֱ�ӿ�������time=4�γɵĶ����Ӽ���ֻҪ�����������ǵ�deadline��Χ�ڣ��϶��ǲ�����
		 * a7���һ��
		 */
		for (int i = 0; i < n && NT[i] == 0; i++) {  //ѭ��n������ÿ���ж�Nt(A)
			int time = 1;
			Task task = tasks.get(i);
			for (int j = i + 1; j < n && NT[i] == 0 && NT[j] == 0; j++) {
				Task temp = tasks.get(j);
				if (task.getDeadLine() >= temp.getDeadLine()) { 
					/* �����ǰ����ѭ��deadline����֮����Ȼ�ȵ�ǰj��deadline����ôj�Ѿ����ܷ����������;����a0�жϵ�a4��Ȼ���㣬��a5,a6����<time
					 * �����ʼ�ͱ�task����������ж�,����a7��a0deadline����ô��������ж�
					 * ��һ��task����a2��ʼ�ж�ʱ��time=4.��ô����ʱtimeһֱΪ4�������ǰa2��deadline 2>tmp��deadline��ֱ�ӷŲ���
					 * a3,a5��ˣ���a7ֱ�Ӳ����ж���
					 */
					if (time <= task.getDeadLine()) {  //��ǰ��������ʱ��ȵ�ǰʱ���
						time++;
					}
					if (time > task.getDeadLine()) {  //����ʱ��С�ڵ�ǰʱ�䣬�����������Ҳ�������֮��ѭ���Ͳ����ж�
						lateTasks.add(temp);
						NT[j] = 1;  //�Ѿ��Ų���
					}
				}
			}
		}
		
		for (int i = 0; i < n; i++) {
			if (NT[i] == 0) {  //���ݱ�ǵĶ���������������������б�
				earlyTasks.add(tasks.get(i));
			}
		}
		Collections.sort(earlyTasks, comparator_d);
	}

	




}

