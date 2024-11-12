using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace lab8_distributed_protocols
{
    public class Variable
    {
        public int Value { get; set; }
        public HashSet<string> Subscribers { get; private set; }
        public event Action<string, int> OnChange;

        public Variable(int initialValue)
        {
            Value = initialValue;
            Subscribers = new HashSet<string>();
        }

        public void Subscribe(string processId)
        {
            Subscribers.Add(processId);
        }

        public void Unsubscribe(string processId)
        {
            Subscribers.Remove(processId);
        }

        public void SetValue(int newValue, string processId)
        {
            if (Value != newValue)
            {
                Value = newValue;
                NotifySubscribers(processId);
            }
        }

        private void NotifySubscribers(string processId)
        {
            foreach (var subscriber in Subscribers)
            {
                if (subscriber != processId)
                {
                    OnChange?.Invoke(subscriber, Value);
                }
            }
        }
    }
}
