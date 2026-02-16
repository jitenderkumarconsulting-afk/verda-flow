import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MySqlConnection {

	public static void main(String args[]) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://54.205.192.100/verdaflow?useSSL=false&useUnicode=yes&characterEncoding=UTF-8",
					"weed_dbuser", "weed#2018");
			// here sonoo is database name, root is username and password
			System.out.println("The connection is " + con);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
