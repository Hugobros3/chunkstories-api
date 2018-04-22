//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.workers;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

import io.xol.chunkstories.api.exceptions.tasks.CancelledTaskException;
import io.xol.chunkstories.api.util.concurrency.Fence;

public abstract class Task implements Fence
{
	private AtomicBoolean done = new AtomicBoolean(false);
	
	private AtomicBoolean cancelled = new AtomicBoolean(false);
	private Semaphore wait = new Semaphore(0);
	
	public boolean isDone()
	{
		return done.get();
	}
	
	public boolean isCancelled()
	{
		return cancelled.get();
	}
	
	public void cancel()
	{
		signalCancelled();
	}

	public final boolean run(TaskExecutor taskExecutor)
	{
		if (!done.get() && (cancelled.get() || task(taskExecutor))) {
			signalDone();
		}
		
		return done.get();
	}
	
	private final void signalDone() {
		/*synchronized(this)
		{
			done = true;
			notifyAll();
		}*/
		if(done.compareAndSet(false, true))
			wait.release(1);
	}

	private final void signalCancelled() {
		if(cancelled.compareAndSet(false, true))
			wait.release(1);
	}

	@Override
	public final void traverse() {
		while(true)
		{
			if(done.get()) // Avoid synchronizing if we can
				break;
			
			if(cancelled.get()) 
				throw new CancelledTaskException(this);
			
			wait.acquireUninterruptibly();
			/*synchronized(this)
			{
				if(done) // Check again
					break;
				
				if(cancelled) 
					throw new CancelledTaskException(this);
				
				try // Well we wait
				{
					wait();
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}*/
		}
	}

	protected abstract boolean task(TaskExecutor taskExecutor);
}
