using lab1_pb1_supermarket.entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace lab1_pb1_supermarket.entities
{
    public class Bill
    {
        public List<Product> Products { get; }

        public Bill(List<Product> products)
        {
            Products = products;
        }

        public static List<Bill> GenerateRandomBills(List<Product> products, int numberOfBills)
        {
            Random random = new Random();
            List<Bill> bills = new List<Bill>();

            for (int i = 0; i < numberOfBills; ++i)
            {
                List<Product> billProducts = new List<Product>();
                int productsInBill = random.Next(1, 6);

                for (int j = 0; j < productsInBill; ++j)
                {
                    int productIndex = random.Next(products.Count);
                    Product product = products[productIndex];
                    int quantity = random.Next(1, 6);
                    billProducts.Add(new Product(product.Name, quantity, product.Price));
                }

                bills.Add(new Bill(billProducts));
            }

            return bills;
        }
    }
}