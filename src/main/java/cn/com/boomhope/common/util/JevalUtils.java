package cn.com.boomhope.common.util;

import java.util.Map;

import org.apache.log4j.Logger;

import sun.util.logging.resources.logging;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

public class JevalUtils {

	protected static Logger log = Logger.getLogger(JevalUtils.class);
	/**
	 * 
	 * Map<String, String> variables = new HashMap<String, String>();
    	variables.put("smax", "102");
    	variables.put("smin", "98");
    	variables.put("savg", "150");
    	variables.put("t", "100");
    	variables.put("pc", "5");
    	variables.put("tc", "10");
    	variables.put("sub", sub);
    	
    	Evaluator evaluator = new Evaluator();
    	
    	evaluator.setVariables(variables);
    	
    	System.out.println("smax>=t 等于 "+evaluator.getBooleanResult(expMax));
    	
	 * @param expression
	 * @param variables
	 */
	public static final boolean calExpression(String expression, Map<String, String> variables){
		Evaluator evaluator = new Evaluator();
		evaluator.setVariables(variables);
    	
    	try {
    		return evaluator.getBooleanResult(expression);
		} catch (EvaluationException e) {
			log.error(e);
		}
    	return false;
	}
	
	public static final boolean testCalExpression(String expression, Map<String, String> variables) throws Exception{
		Evaluator evaluator = new Evaluator();
		evaluator.setVariables(variables);
    	
    	return evaluator.getBooleanResult(expression);
	}
}
