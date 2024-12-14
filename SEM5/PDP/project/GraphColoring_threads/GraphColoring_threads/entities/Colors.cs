using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GraphColoring.entities
{
    public static class Colors
    {
        private static string[] colors;

        public static void SetNoColors(int noColors)
        {
            colors = new string[noColors];
        }

        public static void SetColorName(int colorId, string color)
        {
            colors[colorId - 1] = color;
        }

        public static Dictionary<int, string> GetNodesToColors(int[] codes)
        {
            Dictionary<int, string> map = new Dictionary<int, string>();

            for (int index = 0; index < codes.Length; index++)
            {
                string color = colors[codes[index] - 1];
                map[index] = color;
            }

            return map;
        }

        public static int GetNoColors()
        {
            return colors.Length;
        }
    }
}
