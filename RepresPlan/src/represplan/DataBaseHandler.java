package represplan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DataBaseHandler {
    
    //private Connection c;
    
  public DataBaseHandler(){
    Connection c = null;
    Statement stmt = null;
    try {
      //connect to db
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:plan.db");
      //this.c = c;
      //System.out.println("Opened database successfully");
      
      String[] tableNames = {"teacher", "week"};

      stmt = c.createStatement();
      
      DatabaseMetaData md = c.getMetaData();
      ResultSet rs = null;
      
      //check if tables exist
      
      String sql = null;
      for(int i = 0; i < tableNames.length; i++){
        rs = md.getTables(null, null, tableNames[i], null);
        if(!rs.next()){
            switch (i){
                case 0: sql = createTableTeacher();
                        System.out.println("teacher table created");
                    break;
                case 1: sql = createTableWeek();
                        System.out.println("week table created");
                    break;
            }
            stmt.executeUpdate(sql);
        }
      }
      stmt.close();
      c.close();
    } catch ( Exception e ) {
      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      System.exit(0);
    }
    System.out.println("Opened database successfully");
    
  }
  
  public ArrayList<String> getAllTeacher(){
      //Class.forName("org.sqlite.JDBC");
      ArrayList<String> teacher = new ArrayList<>();
      try{
        Connection c = DriverManager.getConnection("jdbc:sqlite:plan.db");
        Statement stmt = c.createStatement();
        String sql = "SELECT TEACHERID, NAME, LASTNAME FROM teacher";
        
        ResultSet rs = stmt.executeQuery(sql);
        
        String name;
        while(rs.next()){
            name = rs.getString(1) + " " +
                    rs.getString(2) + " " + rs.getString(3);
            teacher.add(name);
        }
        
        stmt.close();
        c.close();
      }catch(Exception e){
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         System.exit(0);
      }
      
      
      return teacher;
  }
  
  public ArrayList<String> getPlan(int teacher, int weekday, int row){
    ArrayList<String> plan = new ArrayList<>();
      
    try{
        Connection c = DriverManager.getConnection("jdbc:sqlite:plan.db");
        Statement stmt = c.createStatement();
        //String sqlTeacherID = "SELECT TEACHERID FROM teacher where LASTNAME=";
        
        String sql = "SELECT CLASS, HOUR FROM week WHERE WEEKDAY='" +
                weekday + "' AND TEACHERID='" + teacher + "' ORDER BY ID";
        
        ResultSet rs = null;
        //plan.add(teacher);
        
        ArrayList<String> hours = new ArrayList<>();
        ArrayList<String> classTeach = new ArrayList<>();
        
        int z = 0;
        //retrieve plan for teacher
        rs = stmt.executeQuery(sql);
            
        //get values
        while(rs.next()){
            classTeach.add(rs.getString(1));
            hours.add(rs.getString(2)); 
        }
            //rewrite it for table handling
        int adder = 1;
        for(int j = 0; j < row; j++){
            if(j < hours.size()){
                if(Integer.valueOf(hours.get(j)) == j + adder){
                    plan.add(classTeach.get(j));
                    //System.out.println("entered");
                    //adder--;
                }else{
                    //adder++;
                    while(Integer.valueOf(hours.get(j)) - adder != j){
                        adder++;
                        plan.add("-");
                    }
                    //adder--;
                    //if(Integer.valueOf(hours.get(j)) - adder == j  /*|| j+1 == hours.size()*/){
                    plan.add(classTeach.get(j));
                    //adder = 1;
                    //}
                }
                //plan.add(sql);8
            }else{
                plan.add("-.-");
            }
        }
            z = 0;
        
        stmt.close();
        c.close();
      }catch(Exception e){
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         System.exit(0);
    }  
        
        
    return plan;
  }
  
  /*
  public List<List<String>> getPlan(ArrayList<String> teacher, int weekday, int row){
    List<List<String>> plan = new ArrayList<List<String>>();
      
    try{
        Connection c = DriverManager.getConnection("jdbc:sqlite:plan.db");
        Statement stmt = c.createStatement();
        //String sqlTeacherID = "SELECT TEACHERID FROM teacher where LASTNAME=";
        
        
        String sql = "SELECT CLASS, HOUR FROM week WHERE WEEK='" +
                weekday + "' TEACHERID='";
        
        ResultSet rs = null;
        plan.add(teacher);
        
        ArrayList<String> hours = new ArrayList<>();
        
        //add Lists in plan
        for(int j = 0; j < row; j++){
            plan.add(new ArrayList<>());
        }
        
        int z = 0;
        //retrieve plan for each teacher
        for(int i = 0; i < teacher.size(); i++){
            rs = stmt.executeQuery(sql + teacher.get(i) + "'");
            
            //get values
            while(rs.next()){
                hours.add(rs.getString(2) + rs.getString(1)); 
            }
            //rewrite it for table handling
            for(int j = 0; j < row; j++){
                while(j < hours.size()){
                    plan.get(j).add(sql);
                }
            }
            z = 0;
        }
        
        stmt.close();
        c.close();
      }catch(Exception e){
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         System.exit(0);
    }  
        
        
    return plan;
  }
  */
  
  private String createTableTeacher(){
      //call new Dialog
      return "CREATE TABLE teacher " +
                   "(TEACHERID INT PRIMARY KEY     NOT NULL," +
                   " NAME           TEXT    NOT NULL, " + 
                   " LASTNAME       TEXT     NOT NULL, " + 
                   " NOTES          TEXT, " + 
                   " OVERTIME       INT);";
  }
  
  private String createTableWeek(){
      //call new Dialog
      return " CREATE TABLE week " +
                   "(ID INT PRIMARY KEY     NOT NULL," +
                   " WEEKDAY         INT    NOT NULL," +
                   " CLASS           TEXT   NOT NULL, " + 
                   " TEACHERID       INT    NOT NULL, " +
                   " HOUR         INT);";
  }
    
}
