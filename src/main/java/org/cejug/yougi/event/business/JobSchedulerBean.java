/* Yougi is a web application conceived to manage user groups or
 * communities focused on a certain domain of knowledge, whose members are
 * constantly sharing information and participating in social and educational
 * events. Copyright (C) 2011 Hildeberto Mendonça.
 *
 * This application is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 *
 * This application is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * There is a full copy of the GNU Lesser General Public License along with
 * this library. Look for the file license.txt at the root level. If you do not
 * find it, write to the Free Software Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA.
 * */
package org.cejug.yougi.event.business;

import org.cejug.yougi.business.AbstractBean;
import org.cejug.yougi.entity.JobScheduler;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Hildeberto Mendonca - http://www.hildeberto.com
 */
@Stateless
public class JobSchedulerBean extends AbstractBean<JobScheduler> {

	@PersistenceContext
    private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public JobSchedulerBean() {
		super(JobScheduler.class);
	}
	
	public List<JobScheduler> findAll() {
		return em.createQuery("select js from JobScheduler js order by js.name asc", JobScheduler.class).getResultList();
	}

    public List<String> findUnscheduledJobNames() {
        List<JobScheduler> schedulers = findAll();
        JobOperator jo = BatchRuntime.getJobOperator();
        Set<String> jobNames = jo.getJobNames();

        for(String jobName: jobNames) {
            for(JobScheduler jobScheduler: schedulers) {
                if(jobScheduler.getName().equals(jobName)) {
                    jobNames.remove(jobName);
                    break;
                }
            }
        }
        return new ArrayList<>(jobNames);
    }
}