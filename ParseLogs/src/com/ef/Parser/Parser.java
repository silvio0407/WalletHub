package com.ef.Parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;

import com.ef.Parser.DAO.LogDAO;
import com.ef.Parser.entity.Log;
import com.ef.Parser.util.ParserUtils;
import com.efe.enumeration.SituationDurationEnum;

public class Parser {
	private static Logger LOGGER = Logger.getLogger(Parser.class.toString());
	
	private static final Integer LINE_VALUE_PARAMETER = 5;
	private static final Integer OPERATION_THRESHOLD_ONE_HUNDRED = 100;
	private static final Integer OPERATION_THRESHOLD_TWO_HUNDRED_AND_FIFTY = 250;
		
	private static final String ARG_PATH_LOG_FILE = "--accesslog";
	private static final String ARG_START_DATE = "--startDate";
	private static final String ARG_DURATION = "--duration";
	private static final String ARG_THRESHOLD = "--threshold";
	private static final String DELIMETER_EQUAL = "=";
	private static final String DELIMETER_DOTE = ".";
	
	private static final String[] DURATION_VALID = {SituationDurationEnum.DAILY.getDescription(), SituationDurationEnum.HOURLY.getDescription()};
	
	private static LocalDateTime requestEndTime = null;
	private static LocalDateTime startDate = null;
	private static String duration = null;
	private static String pathLogFile = null;
	private static Integer threshold = null;
	private static Integer numberRegister = 0;
	
	private static List<String> errosLine = new ArrayList<String>();
	
	public static void main(String[] args) {
		
		if(args.length != 4){
			LOGGER.info("Invalid number of arguments, expected 4 but currently is " + args.length);
			System.exit(0);
		}
		
		preperInformationForProcessLogFile(args);
		
		if(ArrayUtils.contains(DURATION_VALID,duration)){
			try
	        {
	            FileReader fr = new FileReader(pathLogFile);
	            BufferedReader br = new BufferedReader(fr);
	            String line;
	            List<Log> logs = new ArrayList<Log>();
	            while ((line = br.readLine()) != null)
	            {
	            	String[] logInformations = line.split("\\|");
	            	
	            	if(logInformations.length == LINE_VALUE_PARAMETER)
	            	{
	        			String logDate =  logInformations[0];
	        			
	        			LocalDateTime logDateRequestAccess = ParserUtils.formatDate(logDate);
	        			
	        			if((logDateRequestAccess.isEqual(startDate) || logDateRequestAccess.isAfter(startDate)) &&  (logDateRequestAccess.isEqual(requestEndTime) || logDateRequestAccess.isBefore(requestEndTime))){
	        				 Log log = new Log(logInformations[0], logInformations[1],logInformations[2], Long.valueOf(logInformations[3]),logInformations[4]);
	 	                    logs.add(log);
	        			}
	            	}else{
	            		errosLine.add(line);
	            	}
	            }
	            
	            br.close();
	            
	            Stream<Map.Entry<String,List<Log>>> listGroupingByIp = groupLogInformations(logs);
	            	
	            	listGroupingByIp.forEach(entry -> {

	            		List<Log> list = entry.getValue();
	                    
	            		list.forEach(log -> {
	            			
	            			String messageIpBlocked = generateMessageIpBlocked(log.getIp());
	            			
	                    	new LogDAO().salvar(log, messageIpBlocked);
	                    	
	                    	System.out.println(log.toString());
	                    });
	                    //itens.forEach(it -> System.out.println(it.toString()));
	                    
	                }); 
	            		
	            /*}else{
	            	System.out.println("Não foi encontrado mais que 100 acessos");
	            }*/
	            
	        }
	        catch (FileNotFoundException e)
	        {
	        	LOGGER.info("File not found!");
	        }
	        catch (IOException e)
	        {
	            e.printStackTrace();
	        }catch (Exception ex) {
	        	LOGGER.severe("Inconsistent Data, please verify the parameters informated.");
			}
		}else{
			LOGGER.info("Please, verify the information for argument --duration, the only valid arguments are hourly or daily, but was informated:  " + duration);
		}
	}
	
	private static Stream<Map.Entry<String,List<Log>>> groupLogInformations(List<Log> logs){
		
		Stream<Map.Entry<String,List<Log>>> listGroupingByIp = logs.stream().collect(Collectors.groupingBy(Log::getIp)).entrySet().stream().filter(e -> e.getValue().size() > numberRegister);
		
		return listGroupingByIp;
	}
	
	private static void preperInformationForProcessLogFile(String[] args){
		
		for (String arg : args) {
			
			String[] infoArgs = arg.split(DELIMETER_EQUAL);
			
			boolean isValid = ParserUtils.isValidArgument(infoArgs);
			
			if (isValid && infoArgs[0].equals(ARG_PATH_LOG_FILE)) {
				
				pathLogFile = infoArgs[1];
				
			}else if (isValid && infoArgs[0].equals(ARG_START_DATE)) {
				
				startDate = ParserUtils.formatDate(infoArgs[1].replace(DELIMETER_DOTE, " "));
				
				System.out.println("startDate : " + startDate);
				
			}else if (isValid && infoArgs[0].equals(ARG_DURATION)) {
				
				duration = infoArgs[1];
				
				System.out.println("duration : " + duration);
				
			}else if (isValid && infoArgs[0].equals(ARG_THRESHOLD)) {
				
				threshold = Integer.parseInt(infoArgs[1]);
				System.out.println("threshold : " + threshold);
			}
		}
		
		if(OPERATION_THRESHOLD_ONE_HUNDRED.intValue() == threshold && SituationDurationEnum.HOURLY.getDescription().equals(duration)){
			numberRegister = OPERATION_THRESHOLD_ONE_HUNDRED;
			requestEndTime = startDate.plusHours(1l);
		}else if (OPERATION_THRESHOLD_TWO_HUNDRED_AND_FIFTY.intValue() == threshold && SituationDurationEnum.DAILY.getDescription().equals(duration)){
			numberRegister = OPERATION_THRESHOLD_TWO_HUNDRED_AND_FIFTY;
			requestEndTime = startDate.plusDays(1l);
		}else{
			
		}
	}
	
	private static String generateMessageIpBlocked(String ip){
		
		StringBuilder msg = new StringBuilder();
		msg.append("IP ");
		msg.append(ip);
		msg.append(" was blocked because it reached the allowed request limit ");
		msg.append(duration);
		msg.append(" greater than ");
		msg.append(numberRegister);
		
		return msg.toString();
	}
}
