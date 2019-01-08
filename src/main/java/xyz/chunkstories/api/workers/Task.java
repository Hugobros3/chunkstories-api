//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.workers;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import xyz.chunkstories.api.exceptions.tasks.CancelledTaskException;
import xyz.chunkstories.api.util.concurrency.Fence;

public abstract class Task implements Fence {
	private final ReentrantLock lock = new ReentrantLock();
	private int peopleWaiting = 0;
	private final Semaphore semaphore = new Semaphore(0);

	private State state = State.SCHEDULED;

	public final State getState() {
		return state;
	}

	public final boolean run(TaskExecutor taskExecutor) {
		try {
			lock.lock();
			if (state == State.CANCELLED)
				return true;

			state = State.RUNNING;
		} finally {
			lock.unlock();
		}

		boolean executionResult = task(taskExecutor);

		try {
			lock.lock();
			if (executionResult)
				state = State.DONE;
			else
				state = State.SCHEDULED;

			if (peopleWaiting > 0) {
				semaphore.release(peopleWaiting);
				peopleWaiting = 0;
			}
		} finally {
			lock.unlock();
		}
		return executionResult;
	}

	/** Tries to cancel the task, returns 'true' if it did so successfully before
	 * the task actually executed. */
	public boolean tryCancel() {
		try {
			lock.lock();

			// Missed it by a long shot
			if (state == State.DONE)
				return false;

			boolean missed = false;
			// Missed it by not much
			if (state == State.RUNNING)
				missed = true;

			// Cancel it even if we missed it because it might get rescheduled otherwise
			state = State.CANCELLED;

			if (peopleWaiting > 0) {
				semaphore.release(peopleWaiting);
				peopleWaiting = 0;
			}
			return !missed;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public final void traverse() {
		while (true) {
			try {
				lock.lock();
				if (state == State.DONE || state == State.CANCELLED)
					break;
				else
					peopleWaiting++;
			} finally {
				lock.unlock();
			}

			semaphore.acquireUninterruptibly();
		}
	}

	protected abstract boolean task(TaskExecutor taskExecutor);

	public enum State
	{
		SCHEDULED,
		RUNNING,
		CANCELLED,
		DONE
	}
}
