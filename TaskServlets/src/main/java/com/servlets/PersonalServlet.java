package com.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import com.dboperations.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 * Servlet implementation class PersonalServlet
 */
@WebServlet("/PersonalServlet")
public class PersonalServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private final StudentDB sDB = new StudentDB("mongodb://localhost:27017", "db", "students");
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PersonalServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter printWriter = response.getWriter();                
        response.setContentType("text/html");
        
        try {
        	if(request.getParameter("id") == null)
        	{
        		ArrayList<String> students = new ArrayList<String>();
            	students = sDB.getAllStudents();
            	
            	printWriter.println("Students:");
            	for(String student: students) {
            		printWriter.println(student);
            	}
        	}
        	else
        	{
        		String id = request.getParameter("id");
        		String res;
        		
        		res = sDB.getStudent(Long.parseLong(id));
        		printWriter.println("Student:");
        		printWriter.println(res);
        	}        	
        }
        catch(NumberFormatException e) {
        	printWriter.println("Incorrect parameter");
        }
        finally {
        	printWriter.close();
        }
	}
    
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
        PrintWriter printWriter = response.getWriter();     
        ObjectMapper objectMapper = new ObjectMapper();
        String line;
        
        try {
        	StringBuilder sb = new StringBuilder();
            BufferedReader reader = request.getReader();            
            while ((line = reader.readLine()) != null) {
              sb.append(line);
            }
            String requestBody = sb.toString();
        	
            Student student = objectMapper.readValue(requestBody, Student.class);
        	String res = sDB.insertStudent(student.getFname(), student.getLname(), student.getCourse(), student.getFaculty(), student.getId());
        	
        	printWriter.println("Student created:");
        	printWriter.println(res);
        }
        catch(com.fasterxml.jackson.databind.exc.InvalidFormatException e) {
        	printWriter.println("Incorrect student data");
        }
        finally {
        	printWriter.close();
        }
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
        PrintWriter printWriter = response.getWriter();        
        ObjectMapper objectMapper = new ObjectMapper();
        String line;
        
        try {
        	StringBuilder sb = new StringBuilder();
            BufferedReader reader = request.getReader();            
            while ((line = reader.readLine()) != null) {
              sb.append(line);
            }
            String requestBody = sb.toString();
        	
            Student student = objectMapper.readValue(requestBody, Student.class);
            
        	String res = sDB.updateStudent(student.getFname(), student.getLname(), student.getCourse(), student.getFaculty(), student.getId());
        	printWriter.println("Updating:");
        	printWriter.println(res);
        }
        catch(com.fasterxml.jackson.databind.exc.InvalidFormatException e) {
        	printWriter.println("Incorrect student data");
        }
        finally {
        	printWriter.close();
        }
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
        PrintWriter printWriter = response.getWriter();        
        String res;
        
        try {
        	if(request.getParameter("id") == null) {        		
        		res = sDB.deleteAllStudents();
        		printWriter.println(res);
        	}
        	else {
        		String id = request.getParameter("id");
        		res = sDB.deleteStudent(Long.parseLong(id));
        		printWriter.println(res);
        	}
        }
        catch(NumberFormatException e) {
        	printWriter.println("Incorrect parameter");
        }
        finally {
        	printWriter.close();
        }
	}

}
