using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Transactions;

namespace lab1_pb1_supermarket.entities
{
    public class Supermarket    
    {
        private List<Product> products;
        private List<Bill> consumedBills = new List<Bill>();
        private int money;

        private Mutex checkMutex = new Mutex();
        private Mutex productMutex = new Mutex();

        private int counter = 0;

        public Supermarket(List<Product> products)
        {
            this.products = products;
            this.money = 0;
        }

        public void ProcessBill(Bill bill)
        {
            int totalPrice = 0;

            foreach (Product billProduct in bill.Products)
            {
                Product product = products.Find(p => p.Name == billProduct.Name);
                if (product != null)
                {
                    productMutex.WaitOne();

                    product.Quantity -= billProduct.Quantity;
                    product.SoldQuantity += billProduct.Quantity;
                    totalPrice += product.Price * billProduct.Quantity;

                    productMutex.ReleaseMutex();
                }
            }

            consumedBills.Add(bill);
            money += totalPrice;

            

            counter++;
            if (counter % 2 == 0)
            {
                checkMutex.WaitOne();
                CheckEverything();
                checkMutex.ReleaseMutex();
            }
        }


        public bool CheckEverything()
        {
            Console.WriteLine("Checking inventory...");

            Dictionary<string, int> checkerMap = new Dictionary<string, int>();
            Dictionary<string, int> productSalesMoney = new Dictionary<string, int>();

            foreach (Product product in products)
            {
                checkerMap[product.Name] = 0;
                productSalesMoney[product.Name] = 0;
            }

            int totalMoney = 0;

            foreach (Bill bill in consumedBills)
            {
                foreach (Product billProduct in bill.Products)
                {
                    productMutex.WaitOne();
                    checkerMap[billProduct.Name] += billProduct.Quantity;
                    int moneyFromProduct = billProduct.Quantity * billProduct.Price;
                    productSalesMoney[billProduct.Name] += moneyFromProduct;
                    totalMoney += moneyFromProduct;
                    productMutex.ReleaseMutex();
                }
            }

            foreach (var entry in checkerMap)
            {
                Product product = products.Find(p => p.Name == entry.Key);
                int moneyForProduct = productSalesMoney[entry.Key];
                Console.WriteLine($"Product: {entry.Key}, Sold: {entry.Value}, Remaining: {product.Quantity}, " +
                                  $"Sold Quantity Recorded: {product.SoldQuantity}, Money Generated: {moneyForProduct}");
            }

            foreach (Product product in products)
            {
                if (checkerMap[product.Name] != product.SoldQuantity || product.SoldQuantity > product.Quantity)
                {
                    Console.WriteLine($"Data inconsistency found for product: {product.Name}!");
                    return false;
                }
            }
            bool moneyCheck = (totalMoney == money);

            Console.WriteLine("Inventory check complete.");

            Console.WriteLine(moneyCheck ? $"Total money is consistent: {totalMoney} from sold products" : "Data inconsistency found in total money!");


            return moneyCheck;
        }
    }
}
