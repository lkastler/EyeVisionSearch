package eyevisionsearch.logic.strategies.impl;

import java.util.Arrays;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import eyevisionsearch.logic.strategies.Strategy;
import eyevisionsearch.logic.strategies.impl.taskevaluationstrategy.ImprovedTaskEvaluation;

/**
 * creates a [participant, task, question, answer] table.
 * 
 * @author lkastler
 *
 */
public class TaskEvaluationStrategy extends Strategy {

	/**
	 * default constructor
	 */
	public TaskEvaluationStrategy() {
		super();
	}	
	
	@Override
	public TableModel compile() throws Exception {
		ImprovedTaskEvaluation ite = new ImprovedTaskEvaluation();
		
		ite.parseEvents(list);
		
		Object[][] obj = ite.getData();
		
		for(Object[] c : obj)
			log.debug(Arrays.toString(c));
		
		String[] cols = new String[20];
		
		for(int i = 0; i < cols.length; ++i)
			cols[i] = "";
		
		 
		return new DefaultTableModel(obj, cols);
	}

	@Override
	public String getName() {
		return "Task Evaluation Strategy";
	}

}
