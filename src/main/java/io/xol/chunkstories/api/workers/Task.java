//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.workers;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import io.xol.chunkstories.api.exceptions.tasks.CancelledTaskException;
import io.xol.chunkstories.api.util.concurrency.Fence;

public abstract class Task implements Fence {
	private ReentrantLock lock = new ReentrantLock();
	private State state = State.SCHEDULED;

	public final State getState() {
		return state;
	}

	public final boolean run(TaskExecutor taskExecutor) {
		try {
			lock.lock();
			if(state == State.CANCELLED)
				return true;

			state = State.RUNNING;
		} finally {
			lock.unlock();
		}

		boolean executionResult = task(taskExecutor);
		synchronized (this) {
			this.notifyAll();
		}

		try {
			lock.lock();
			if(executionResult)
				state = State.DONE;
			else
				state = State.SCHEDULED;
		} finally {
			lock.unlock();
		}
		return executionResult;
	}

	/** Tries to cancel the task, returns 'true' if it did so successfully before the task actually executed. */
	public boolean tryCancel() {
		try {
			lock.lock();

			// Missed it by a long shot
			if(state == State.DONE)
				return false;

			boolean missed = false;
			// Missed it by not much
			if(state == State.RUNNING)
				missed = true;

			// Cancel it even if we missed it because it might get rescheduled otherwise
			state = State.CANCELLED;
			return !missed;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public final void traverse() {
		// Avoid synchronizing if we can
		while (state != State.DONE && state != State.CANCELLED) {

			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					//e.printStackTrace();
				}
			}
		}
	}

	protected abstract boolean task(TaskExecutor taskExecutor);

	public enum State {
		SCHEDULED,
		RUNNING,
		CANCELLED,
		DONE
	}
}
