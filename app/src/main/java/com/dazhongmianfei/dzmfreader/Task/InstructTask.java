package com.dazhongmianfei.dzmfreader.Task;

/**
 * 任务执行
 * 
 * @param <E>
 *            入口数据对象
 * @param <F>
 *            出口数据对象
 */
public abstract class InstructTask<E, F> implements Runnable {

	private E mE = null;
	private InstructTask<F, ?> mNextTask = null;

	public InstructTask(E e) {
		mE = e;
	}

	private void setE(E e) {
		mE = e;
	}

	/**
	 * 添加下一任务
	 * 
	 * @param nextTask
	 */
	public final void addNextTask(InstructTask<F, ?> nextTask) {
		mNextTask = nextTask;
	}

	@Override
	public final void run() {
		F f = doRun(mE);
		if (mNextTask != null) {
			if (f != null) {
				mNextTask.setE(f);
			}
			mNextTask.run();
		}
		doFinish();
	}

	/**
	 * 任务处理
	 * 
	 * @param e
	 * @return
	 */
	public abstract F doRun(E e);

	/**
	 * 当前任务完成
	 * 
	 */
	public void doFinish() {
	}
}
