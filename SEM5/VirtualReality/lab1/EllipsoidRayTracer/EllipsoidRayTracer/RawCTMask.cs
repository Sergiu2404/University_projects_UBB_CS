using System;
using System.IO;
using System.Text.RegularExpressions;

namespace rt;

public class RawCtMask: Geometry
{
    private readonly Vector _position;
    private readonly double _scale;
    private readonly ColorMap _colorMap;
    private readonly byte[] _data;

    private readonly int[] _resolution = new int[3];
    private readonly double[] _thickness = new double[3];
    private readonly Vector _v0;
    private readonly Vector _v1;

    public RawCtMask(string datFile, string rawFile, Vector position, double scale, ColorMap colorMap) : base(Color.NONE)
    {
        _position = position;
        _scale = scale;
        _colorMap = colorMap;

        var lines = File.ReadLines(datFile);
        foreach (var line in lines)
        {
            var kv = Regex.Replace(line, "[:\\t ]+", ":").Split(":");
            if (kv[0] == "Resolution")
            {
                _resolution[0] = Convert.ToInt32(kv[1]);
                _resolution[1] = Convert.ToInt32(kv[2]);
                _resolution[2] = Convert.ToInt32(kv[3]);
            } else if (kv[0] == "SliceThickness")
            {
                _thickness[0] = Convert.ToDouble(kv[1]);
                _thickness[1] = Convert.ToDouble(kv[2]);
                _thickness[2] = Convert.ToDouble(kv[3]);
            }
        }

        //v0 and v1 corners of the bounding box for 3d volume
        _v0 = position;
        _v1 = position + new Vector(_resolution[0]*_thickness[0]*scale, _resolution[1]*_thickness[1]*scale, _resolution[2]*_thickness[2]*scale);

        var len = _resolution[0] * _resolution[1] * _resolution[2];
        _data = new byte[len];
        using FileStream f = new FileStream(rawFile, FileMode.Open, FileAccess.Read);
        if (f.Read(_data, 0, len) != len) //read voxel data from file
        {
            throw new InvalidDataException($"Failed to read the {len}-byte raw data");
        }
    }
    
    private ushort Value(int x, int y, int z)
    {
        if (x < 0 || y < 0 || z < 0 || x >= _resolution[0] || y >= _resolution[1] || z >= _resolution[2])
        {
            return 0;
        }

        return _data[z * _resolution[1] * _resolution[0] + y * _resolution[0] + x];
    }

    public override Intersection GetIntersection(Line ray, double minDistance, double maxDistance)
    {
        // Step size, normal, color accumulation, and transparency handling
        double stepSize = _scale;
        double firstIntersectionDistance = -1;
        Vector surfaceNormal = new Vector(0, 0, 0);
        Color accumulatedColor = Color.NONE;
        double lastTransparency = 1.0;
        bool foundFirstIntersection = false;

        Vector inverseDirection = new Vector(1.0 / ray.Dx.X, 1.0 / ray.Dx.Y, 1.0 / ray.Dx.Z);

        // Calculate intersection distances along each axis (X, Y, Z)
        double tMinX = (_v0.X - ray.X0.X) * inverseDirection.X;
        double tMinY = (_v0.Y - ray.X0.Y) * inverseDirection.Y;
        double tMinZ = (_v0.Z - ray.X0.Z) * inverseDirection.Z;

        double tMaxX = (_v1.X - ray.X0.X) * inverseDirection.X;
        double tMaxY = (_v1.Y - ray.X0.Y) * inverseDirection.Y;
        double tMaxZ = (_v1.Z - ray.X0.Z) * inverseDirection.Z;

        double[] tMin = { tMinX, tMinY, tMinZ };
        double[] tMax = { tMaxX, tMaxY, tMaxZ };

        double intersectionMin = Math.Max(Math.Max(Math.Min(tMin[0], tMax[0]), Math.Min(tMin[1], tMax[1])), Math.Min(tMin[2], tMax[2]));
        double intersectionMax = Math.Min(Math.Min(Math.Max(tMin[0], tMax[0]), Math.Max(tMin[1], tMax[1])), Math.Max(tMin[2], tMax[2]));

        double startIntersection = Math.Max(intersectionMin, minDistance);
        double endIntersection = Math.Min(intersectionMax, maxDistance);


        // If there is no valid intersection, return none
        if (startIntersection > endIntersection) return Intersection.NONE;

        // Iterate through the ray from start to end, accumulating color and transparency
        for (double t = startIntersection; t <= endIntersection; t += stepSize)
        {
            Vector pointOnRay = ray.CoordinateToPosition(t);
            Color pointColor = GetColor(pointOnRay);

            // Check for the first non-transparent voxel (alpha > 0)
            if (pointColor.Alpha > 0)
            {
                if (!foundFirstIntersection)
                {
                    firstIntersectionDistance = t;
                    surfaceNormal = GetNormal(pointOnRay);
                    foundFirstIntersection = true;
                }

                // Accumulate color and update transparency
                accumulatedColor += pointColor * pointColor.Alpha * lastTransparency;
                lastTransparency *= (1 - pointColor.Alpha);
            }
        }

        // If no intersection was found, return none
        if (firstIntersectionDistance < 0) return Intersection.NONE;

        // Return the final intersection details
        return new Intersection(true, foundFirstIntersection, this, ray, firstIntersectionDistance, surfaceNormal, Material.FromColor(accumulatedColor), accumulatedColor);
    }







    private int[] GetIndexes(Vector v) //3d pos -> voxel grid coords
    {
        return new []{
            (int)Math.Floor((v.X - _position.X) / _thickness[0] / _scale), 
            (int)Math.Floor((v.Y - _position.Y) / _thickness[1] / _scale),
            (int)Math.Floor((v.Z - _position.Z) / _thickness[2] / _scale)};
    }
    private Color GetColor(Vector v)
    {
        int[] idx = GetIndexes(v);

        ushort value = Value(idx[0], idx[1], idx[2]);
        return _colorMap.GetColor(value);
    }

    private Vector GetNormal(Vector v) //compute the normal computing gradients
    {
        int[] idx = GetIndexes(v);
        double x0 = Value(idx[0] - 1, idx[1], idx[2]);
        double x1 = Value(idx[0] + 1, idx[1], idx[2]);
        double y0 = Value(idx[0], idx[1] - 1, idx[2]);
        double y1 = Value(idx[0], idx[1] + 1, idx[2]);
        double z0 = Value(idx[0], idx[1], idx[2] - 1);
        double z1 = Value(idx[0], idx[1], idx[2] + 1);

        return new Vector(x1 - x0, y1 - y0, z1 - z0).Normalize();
    }
}