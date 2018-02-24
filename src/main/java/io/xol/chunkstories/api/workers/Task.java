//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.workers;

import io.xol.chunkstories.api.exceptions.tasks.CancelledTaskException;
import io.xol.chunkstories.api.util.concurrency.Fence;

public abstract class Task implements Fence
{
	private boolean done = false;
	private boolean cancelled = false;
	
	public boolean isDone()
	{
		return done;
	}
	
	public boolean isCancelled()
	{
		return cancelled;
	}
	
	public void cancel()
	{
		signalCancelled();
	}

	public final boolean run(TaskExecutor taskExecutor)
	{
		if (!done && (cancelled || task(taskExecutor))) {
			signalDone();
		}
		
		return done;
	}
	
	private final void signalDone() {
		synchronized(this)
		{
			done = true;
			notifyAll();
		}
	}

	private final void signalCancelled() {
		synchronized(this)
		{
			cancelled = true;
			notifyAll();
		}
	}

	@Override
	public final void traverse() {
		while(true)
		{
			if(done) // Avoid synchronizing if we can
				break;
			
			if(cancelled) 
				throw new CancelledTaskException(this);
			
			synchronized(this)
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
			}
		}
	}

	protected abstract boolean task(TaskExecutor taskExecutor);
}
