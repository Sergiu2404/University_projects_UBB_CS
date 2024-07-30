using System;
using System.Configuration;
using System.Data;
using System.Data.SqlClient;
using System.Windows.Forms;

namespace a2
{
    public partial class Form1 : Form
    {
        SqlConnection connectionString;
        SqlDataAdapter daMaster;
        SqlDataAdapter daDetail;
        DataSet dataSet;

        BindingSource bindingMaster;
        BindingSource bindingDetail;

        SqlCommandBuilder commandBuilder;

        string queryMaster;
        string queryDetail;

        string masterTableName;
        string detailTableName;
        string foreignKey;
        string relation;

        public Form1()
        {
            InitializeComponent();
            FillData();
        }

        void FillData()
        {
            relation = ConfigurationManager.AppSettings["Relation"];
            masterTableName = ConfigurationManager.AppSettings[$"{relation}_MasterTable"];
            detailTableName = ConfigurationManager.AppSettings[$"{relation}_DetailTable"];
            foreignKey = ConfigurationManager.AppSettings[$"{relation}_ForeignKey"];
            connectionString = new SqlConnection(getConnectionString());

            queryMaster = $"SELECT * FROM {masterTableName}";
            queryDetail = $"SELECT * FROM {detailTableName}";

            daMaster = new SqlDataAdapter(queryMaster, connectionString);
            daDetail = new SqlDataAdapter(queryDetail, connectionString);
            dataSet = new DataSet();
            daMaster.Fill(dataSet, masterTableName);
            daDetail.Fill(dataSet, detailTableName);

            commandBuilder = new SqlCommandBuilder(daMaster);
            commandBuilder = new SqlCommandBuilder(daDetail);

            dataSet.Relations.Add($"{masterTableName}_{detailTableName}",
                                  dataSet.Tables[masterTableName].Columns[foreignKey],
                                  dataSet.Tables[detailTableName].Columns[foreignKey]);

            bindingMaster = new BindingSource();
            bindingDetail = new BindingSource();

            bindingMaster.DataSource = dataSet;
            bindingMaster.DataMember = masterTableName;
            bindingDetail.DataSource = bindingMaster;
            bindingDetail.DataMember = $"{masterTableName}_{detailTableName}";

            this.dataGridView1.DataSource = bindingMaster;
            this.dataGridView2.DataSource = bindingDetail;

            commandBuilder.GetUpdateCommand();
        }

        private string getConnectionString()
        {
            return "Data Source= .\\SQLEXPRESS;" +
                   "Initial Catalog=Flashscore;" +
                   "Integrated Security=True;";
        }

        private void updateTables_Click(object sender, EventArgs e)
        {
            try
            {
                daMaster.Update(dataSet, masterTableName);
                daDetail.Update(dataSet, detailTableName);
                MessageBox.Show("Table updated");
            }
            catch (Exception ex)
            {
                MessageBox.Show("[EXCEPTION]:" + ex.Message, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }
    }
}
