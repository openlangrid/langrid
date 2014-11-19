/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2010 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation, either version 2.1 of the License, or (at 
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.p2pgridbasis.controller.jxta.dao;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.WeakHashMap;

import jp.go.nict.langrid.p2pgridbasis.controller.jxta.summary.DataSummary;
import jp.go.nict.langrid.p2pgridbasis.data.DataID;

import org.apache.log4j.Logger;

/**
 * 
 * 
 * @author Naoki Miyata
 * @author Masato Mori
 * @author Takao Nakaguchi
 */
public class MemoryDataSummaryDao implements DataSummaryDao {
	private Map<DataID, DataSummary> summaries;
	static private Logger logger = Logger.getLogger(MemoryDataSummaryDao.class);
		
	/**
	 * The constructor.
	 */
	public MemoryDataSummaryDao() {
		this.summaries = new LinkedHashMap<DataID, DataSummary>();
	}
	
	synchronized public void addDataSummary(DataSummary dataSummary) throws DataSummaryAlreadyExistsException {
		if(summaries.containsKey(dataSummary.getId())) {
			throw new DataSummaryAlreadyExistsException(dataSummary.getId());
		}
		
		summaries.put(dataSummary.getId(), dataSummary);
	}

	synchronized public void addLogSummary(DataSummary dataSummary){
		final int summaryMax = 5000;
		
		if(summaries.size() >= summaryMax){
			try {
				deleteDataSummary(summaries.entrySet().iterator().next().getValue().getId());
			} catch (DataSummaryNotFoundException e) {
			}
/*
			DataSummary[] allSummary = summaries.values().toArray(new DataSummary[0]);

			Arrays.sort(allSummary, new Comparator<DataSummary>(){
				public int compare(DataSummary o1, DataSummary o2) {
					Calendar time1 = o1.getLastUpdateDate();
					Calendar time2 = o2.getLastUpdateDate();
					return time1.compareTo(time2);
				}
			});
			
			DataID delID = allSummary[0].getId();
			
			if(dataSummary.getLastUpdateDate().compareTo(allSummary[0].getLastUpdateDate()) < 0){
				logger.debug("Skip data is old.");
				return;
			}

			// 
			// 
			try {
				deleteDataSummary(delID);
			} catch (DataSummaryNotFoundException e) {
			}
*/		}
		
		summaries.put(dataSummary.getId(), dataSummary);
	}

	synchronized public void deleteDataSummary(DataID id) throws DataSummaryNotFoundException {
		if (summaries.containsKey(id) == false) {
			throw new DataSummaryNotFoundException(id);
		}
		
		summaries.remove(id);
	}

	synchronized public DataSummary[] getAllDataSummaries() {
		return summaries.values().toArray(new DataSummary[0]);
	}

	synchronized public DataSummary getDataSummary(DataID id) throws DataSummaryNotFoundException {
		if (summaries.containsKey(id) == false) {
			throw new DataSummaryNotFoundException(id);
		}
		
		return this.summaries.get(id);
	}

	public void clear() {
		this.summaries.clear();
	}

    static Map<MemoryDataSummaryDao, String> daos = Collections.synchronizedMap(
    		new WeakHashMap<MemoryDataSummaryDao, String>());
    { daos.put(this, "");}
    static {
    	new Timer(true).scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				int daoc = 0, summaryc = 0;
				for(Map.Entry<MemoryDataSummaryDao, String> e : daos.entrySet()){
					daoc++;
					summaryc += e.getKey().summaries.size();
				}
				logger.info(String.format("%d daos, %d summaries", daoc, summaryc));
			}
		}, 1000 * 60 * 5, 1000 * 60 * 10);
    }
}
