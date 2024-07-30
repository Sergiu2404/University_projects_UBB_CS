using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
//needed for Sql conn
using System.Data.SqlClient;

namespace Windows_Forms
{
    public partial class Form1 : Form
    {
        SqlConnection connection;
        SqlDataAdapter adapterSports;
        SqlDataAdapter adapterTeams;

        DataSet dataset;

        BindingSource bindingSports;
        BindingSource bindingTeams;

        SqlCommandBuilder commandBuilder;

        string querySports;
        string queryTeams;

        public Form1()
        {
            InitializeComponent();
            FillData();
        }


        string getConnectionString()
        {
            return "Data Source=DESKTOP-EBHU28B\\SQLEXPRESS;" +
                "Initial Catalog=Flashscore;Integrated Security=true;";

        }


        void FillData()
        {
            connection = new SqlConnection(getConnectionString());

            querySports = "select * from SportsTable";
            queryTeams = "select * from TeamsTable";

            adapterSports = new SqlDataAdapter(querySports, connection);
            adapterTeams = new SqlDataAdapter(queryTeams, connection);

            dataset = new DataSet();

            adapterSports.Fill(dataset, "SportsTable");
            adapterTeams.Fill(dataset, "TeamsTable");

            //fill in insert, update, and delete commands
            commandBuilder = new SqlCommandBuilder(adapterTeams);

            //DataRelation (parent-child rel) added to the dataset
            dataset.Relations.Add(
                "SportsTeams",
                dataset.Tables["SportsTable"].Columns["sportId"],
                dataset.Tables["TeamsTable"].Columns["sportId"]
                );


            //METH 1: fill data into DataGridViews using properties DataSource, DataMember
            this.sportsDataGridView.DataSource = dataset.Tables["SportsTable"];
            this.teamsDataGridView.DataSource = this.sportsDataGridView.DataSource;
            this.teamsDataGridView.DataMember = "SportsTeams";

            //METH 2: fill data into DataGridViews using Data Binding
            //bindingSports = new BindingSource();
            //bindingSports.DataSource = dataset.Tables["Sports"];
            //bindingTeams = new BindingSource(bindingSports, "SportsTeams");

            //this.dataGridView1.DataSource = bindingSports;
            //this.dataGridView2.DataSource = bindingTeams;

            commandBuilder.GetUpdateCommand();
        }


        private void updateEvent(object sender, EventArgs e)
        {
            try
            {
                adapterTeams.Update(dataset, "TeamsTable");
            }catch(Exception exception)
            {
                MessageBox.Show("Database update did not work\n" + exception.Message, "Error",
                    MessageBoxButtons.OK, MessageBoxIcon.Error);
                Console.WriteLine(exception.ToString());
            }
        }

    }
}
