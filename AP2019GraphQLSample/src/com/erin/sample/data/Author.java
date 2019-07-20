package com.erin.sample.data;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.ibm.AP2019.bean.AuthorData;
import com.ibm.cics.server.CCSIDErrorException;
import com.ibm.cics.server.Channel;
import com.ibm.cics.server.ChannelErrorException;
import com.ibm.cics.server.CodePageErrorException;
import com.ibm.cics.server.Container;
import com.ibm.cics.server.ContainerErrorException;
import com.ibm.cics.server.InvalidProgramIdException;
import com.ibm.cics.server.InvalidRequestException;
import com.ibm.cics.server.InvalidSystemIdException;
import com.ibm.cics.server.IsCICS;
import com.ibm.cics.server.LengthErrorException;
import com.ibm.cics.server.NotAuthorisedException;
import com.ibm.cics.server.Program;
import com.ibm.cics.server.RolledBackException;
import com.ibm.cics.server.Task;
import com.ibm.cics.server.TerminalException;

public class Author {
	private int isCICSrc;
	private Task task;
	private boolean isCICS ;
	private AuthorData authorData = null;
	
	public Author(){
		isCICSrc = IsCICS.getApiStatus();
		task = Task.getTask();
		isCICS = isCICSrc == IsCICS.CICS_REGION_AND_API_ALLOWED;
	}

	public Map<String, String> getAuthor(String authorId){
		//System.out.println(authorId);
		if(isCICS){
			try {
				task = Task.getTask();
				if (task == null) {
					System.err.print("ERROR: Can't get Task");
				} else {
					Channel authorChannel=task.getChannel("QUARY-BOOK");
					// put data into container with the created channel
					// use author-id from container QUARY-BOOKDATA as input
					Container adc = authorChannel.createContainer("QUARY-AUTHRDATA");
					adc.putString(authorId);
					
					// link to cobol program FCAUTHR with channel QUARY-BOOK, container QUARY-AUTHRDATA
					Program QuaryAuthor = new Program();
					QuaryAuthor.setName("FCAUTHR");
					
					QuaryAuthor.link(authorChannel);
					
					Channel currentCobolChannel = task.getChannel("QUARY-BOOK");
                    
					Container adco = currentCobolChannel.getContainer("QUARY-AUTHRDATA");
					if (adco != null)
					{	
						authorData = new AuthorData(adco.get());
						return ImmutableMap.of("id", authorData.getAuthorId(),
			                    "firstName", authorData.getFirstName(),
			                    "lastName", authorData.getLastName());
					}
					
				}
			}catch (ChannelErrorException | ContainerErrorException | CCSIDErrorException | CodePageErrorException e) {
			e.printStackTrace();
			} catch (InvalidRequestException e) {
				e.printStackTrace();
			} catch (LengthErrorException e) {
				e.printStackTrace();
			} catch (InvalidSystemIdException e) {
				e.printStackTrace();
			} catch (NotAuthorisedException e) {
				e.printStackTrace();
			} catch (InvalidProgramIdException e) {
				e.printStackTrace();
			} catch (RolledBackException e) {
				e.printStackTrace();
			} catch (TerminalException e) {
				e.printStackTrace();
			}
		}
		return ImmutableMap.of("id", "null",
	                    "firstName", "null",
	                    "lastName", "null");
	}
	
	

}
