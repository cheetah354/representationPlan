package represplan;

import java.sql.*;
import java.util.ArrayList;


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
        String sql = "SELECT NAME, LASTNAME FROM teacher";
        
        ResultSet rs = stmt.executeQuery(sql);
        
        String name;
        while(rs.next()){
            name = rs.getString(1) + " " + rs.getString(2);
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
                   " NOTES        TEXT, " + 
                   " OVERTIME         INT);";
  }
    
}
