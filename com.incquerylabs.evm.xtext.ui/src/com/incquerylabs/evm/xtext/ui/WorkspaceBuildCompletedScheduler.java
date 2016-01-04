package com.incquerylabs.evm.xtext.ui;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.incquery.runtime.evm.api.Executor;
import org.eclipse.incquery.runtime.evm.api.Scheduler;

public class WorkspaceBuildCompletedScheduler extends Scheduler implements IResourceChangeListener {

	public static ISchedulerFactory FACTORY = new ISchedulerFactory() {

		@Override
		public Scheduler prepareScheduler(Executor executor) {
			WorkspaceBuildCompletedScheduler scheduler = new WorkspaceBuildCompletedScheduler(executor);
			ResourcesPlugin.getWorkspace().addResourceChangeListener(scheduler);
			return scheduler;
		}

	};

	protected WorkspaceBuildCompletedScheduler(Executor executor) {
		super(executor);
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
			schedule();
		}
	}

	@Override
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}
}
