using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

//necessary for connection to the database
using System.Data;
using System.Data.SqlClient;


namespace console_app_database_SEM1
{
    internal class Program
    {
        static void Main(string[] args)
        {
            string connectionString = "Data Source=DESKTOP-EBHU28B\\SQLEXPRESS;" + "Initial Catalog=Flashscore;Integrated Security=true;"; //server name + db name
            SqlConnection connection = new SqlConnection(connectionString);
            
            connection.Open();
            string sportsTableContent = "select * from Sports";
            SqlCommand command = new SqlCommand(sportsTableContent, connection);
            
            using(SqlDataReader reader = command.ExecuteReader())
            {
                while (reader.Read())
                {
                    Console.WriteLine("{0}, {1}, {2}", reader[0], reader[1], reader[2]);
                }
            }

            SqlDataAdapter adapterFlashscore = new SqlDataAdapter(sportsTableContent, connection);
            DataSet dataSet = new DataSet();

            adapterFlashscore.Fill(dataSet, "Sports");
            foreach (DataRow row in dataSet.Tables["Sports"].Rows)
                Console.WriteLine("{0}, {1}, {2}", row["sportId"], row["sportName"], row["sportDescription"]);
            connection.Close();
        }
    }
}
