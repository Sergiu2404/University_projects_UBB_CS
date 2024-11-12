using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace lab1_pb1_supermarket.entities
{
    public class Product
    {
        public string Name { get; set; }
        public int Quantity { get; set; }
        public int Price { get; set; }
        public int SoldQuantity { get; set; }
        
        public Product(string name, int quantity, int price) {
            Name = name;
            Quantity = quantity;
            Price = price;
            SoldQuantity = 0;
        }

        public string ToString()
        {
            return $"name: {Name}, quantity: {Quantity}, price: {Price}, sold: {SoldQuantity}";
        }
    }
}
