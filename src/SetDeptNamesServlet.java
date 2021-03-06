import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class SetDeptNamesServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DB_Handler handler = new DB_Handler();
        handler.init();
        ArrayList<String> depList = new ArrayList<>();//list of all departments

        try {//select all departments
            Statement stmt = handler.getConn().createStatement();
            ResultSet rs = stmt.executeQuery("select name from departments");
            while(rs.next()){
                depList.add(rs.getString(1));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        String deptNames = "";
        for(String dep : depList){
            //System.out.println(dep);
            deptNames = deptNames + dep + "/";
        }
        response.addCookie(new Cookie("deptNames", URLEncoder.encode(deptNames, "UTF-8")));
        handler.close();
        response.sendRedirect("patient/makeAppointment.jsp");
    }
}
