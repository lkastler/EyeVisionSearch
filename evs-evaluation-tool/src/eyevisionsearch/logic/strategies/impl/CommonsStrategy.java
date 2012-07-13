package eyevisionsearch.logic.strategies.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import eyevisionsearch.logic.events.Event;
import eyevisionsearch.logic.events.NextEvent;
import eyevisionsearch.logic.events.NextWithResponsesEvent;
import eyevisionsearch.logic.helpers.LikertScaleFivePoint;
import eyevisionsearch.logic.helpers.StrategyHelper;
import eyevisionsearch.logic.strategies.Strategy;
import eyevisionsearch.logic.strategies.StrategyException;
import eyevisionsearch.logic.strategies.impl.commonstrategy.CommonContainer;

/**
 * strategy that parses the common information given by participants.
 * @author lkastler
 *
 */
public class CommonsStrategy extends Strategy {

	/**
	 * default constructor
	 */
	public CommonsStrategy() {
		super();
	}
	
	/**
	 * finds and returns the NextWithResponsesEvent that contains the feedback comments on given Iterator.
	 * @param it Iterator of Events where the NextWithResponsesEvent that contains the feedback comments should be found.
	 * @return the NextWithResponsesEvent that contains the feedback comments or null.
	 */
	private NextWithResponsesEvent getCommonsEvent(Iterator<Event> it){
		Event e;
		while((e = StrategyHelper.getFollowingEventByType(it, "next.response")) != null) {
			if(((NextWithResponsesEvent)e).getSection().contains("qn1")) {
				return (NextWithResponsesEvent)e;
			}
		}
		return null;
	}
	
	/**
	 * returns the feedback comments contained in given Iterator of Events.
	 * @param it Iterator of Events that contains the feedback comments.
	 * @return the feedback comments contained in given Iterator of Events.
	 */
	private String getComments(Iterator<Event> it) {
		Event e;
		while((e = StrategyHelper.getFollowingEventByType(it, "next.response")) != null) {
			
			if(((NextEvent)e).getSection().contains("feedback")) {
				String comment = ((NextWithResponsesEvent)e).getResponse("feedback");
				
				return new String(comment.getBytes());
			}
		}
		
		return null;
	}
	
	@Override
	public TableModel compile() throws Exception {
		
		ArrayList<CommonContainer> containerList = new ArrayList<CommonContainer>(list.size());
		
		for(int i = 0; i < list.size(); ++i) {
			Iterator<Event> it = list.get(i).iterator();
			
			String group = StrategyHelper.getGroup(it);
			
			NextWithResponsesEvent e = getCommonsEvent(it);			
			
			if(e == null)
				throw new StrategyException("Unexpected: no Event for '#qn1' found");
			
			String comment = getComments(it);
			
			if(comment == null)
				throw new StrategyException("Unexpected: no Event for '#feedback' found");
			
			log.debug(e.getResponsesKeys().toString());
			
			containerList.add(new CommonContainer(
				list.get(i).getId(),
				group,
				Integer.parseInt(e.getResponse("age")),
				e.getResponse("gender"),
				e.getResponse("profession"),
				LikertScaleFivePoint.instance().scaleFloat(Integer.parseInt(e.getResponse("imagesearch-experience"))),
				Integer.parseInt(e.getResponse("imagesearch-freqency")),
				comment
			));
		}
		

		Collections.sort(containerList, new Comparator<CommonContainer>() {

			// sorts the list by groups
			@Override
			public int compare(CommonContainer o1, CommonContainer o2) {
				if(o1.getGroup().equalsIgnoreCase(o2.getGroup())) {
					return o1.getId().compareToIgnoreCase(o2.getId());
				}
				
				return o1.getGroup().compareToIgnoreCase(o2.getGroup());
				
			}
		});
		
		ArrayList<Object[]> data = new ArrayList<Object[]>(list.size());
		for(CommonContainer c : containerList)
				data.add(c.rowToArray());
		
		Object[] cols = {"participants", "group", "age", "gender", 
				"profession", "experience", "usage", "comment"};
		
		return new DefaultTableModel(data.toArray(new Object[0][0]), cols);
	}

	@Override
	public String getName() {
		return "Common Questions Strategy";
	}

}
