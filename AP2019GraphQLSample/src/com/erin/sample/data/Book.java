package com.erin.sample.data;

import java.util.Map;
import com.ibm.AP2019.bean.BookData;
import com.google.common.collect.ImmutableMap;
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

public class Book {
	private int isCICSrc;
	private Task task;
	private boolean isCICS ;
	private BookData bookData = null;
	
	public Book(){
		isCICSrc = IsCICS.getApiStatus();
		task = Task.getTask();
		isCICS = isCICSrc == IsCICS.CICS_REGION_AND_API_ALLOWED;
	}
	
	public Map<String, Object> getBook(String bookId){
		if(isCICS){
			try {
				task = Task.getTask();
				if (task == null) {
					System.err.print("ERROR: Can't get Task");
				} else {
					Channel bookChannel = task.createChannel("QUARY-BOOK");
					Container bdc = bookChannel.createContainer("QUARY-BOOKDATA");
					bdc.putString(bookId);//like "book-01   "
					
					// link to cobol program FCBOOK with channel QUARY-BOOK, container QUARY-BOOKDATA
					Program QuaryBook = new Program();
					QuaryBook.setName("FCBOOK");
					
					QuaryBook.link(bookChannel);
					
					Channel currentCobolChannel = task.getChannel("QUARY-BOOK");
					Container bdco = currentCobolChannel.getContainer("QUARY-BOOKDATA");
					//For GraphQL DevOps hands on  
					// I had changed the source code to make it different with before. 
					// bookData.getBookName() = "2019_" + bookData.getBookName()
					if (bdco != null)
					{	
						bookData = new BookData(bdco.get());
						return  ImmutableMap.of("id", bookData.getBookId(),
			                    "name", "2019_"+bookData.getBookName(),
			                    "pageCount", bookData.getPagecount(),
			                    "authorId", bookData.getAuthrId());
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
	                    "name", "null",
	                    "pageCount", "null",
	                    "authorId", "null");
	}
}
