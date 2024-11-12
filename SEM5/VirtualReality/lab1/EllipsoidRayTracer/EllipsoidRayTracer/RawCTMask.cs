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
        ////acummulating color: c01 = c0*a0 + c1 * a1 * (1 - a0);  a01 = a0 + a1 * (1 - a0)
        //// intersection: mX + nY + pZ = q;  at + b = X;  ct + d = Y; et + f = Z and substitute each X, Y, Z inside mX + nY + pZ = q
        //// interseciotn is the first voxel that does not have0 oppacity
        // inv direction for faster inters calculation (avoid repeatedly dividing by the direction comp during inters calc and instead
        // multiply the bounding box bounds by the inverse of the direction vector components)
        //Vector inverseDirection = new Vector(1.0 / ray.Dx.X, 1.0 / ray.Dx.Y, 1.0 / ray.Dx.Z);

        //// calc inters distances for each axis
        //double tMinX = (_v0.X - ray.X0.X) * inverseDirection.X;
        //double tMinY = (_v0.Y - ray.X0.Y) * inverseDirection.Y;
        //double tMinZ = (_v0.Z - ray.X0.Z) * inverseDirection.Z;

        //double tMaxX = (_v1.X - ray.X0.X) * inverseDirection.X;
        //double tMaxY = (_v1.Y - ray.X0.Y) * inverseDirection.Y;
        //double tMaxZ = (_v1.Z - ray.X0.Z) * inverseDirection.Z;

        //// calculate overall tMin and tMax based on individual axes
        //double intersectionMin = Math.Max(Math.Max(Math.Min(tMinX, tMaxX), Math.Min(tMinY, tMaxY)), Math.Min(tMinZ, tMaxZ));
        //double intersectionMax = Math.Min(Math.Min(Math.Max(tMinX, tMaxX), Math.Max(tMinY, tMaxY)), Math.Max(tMinZ, tMaxZ));

        //// no valid inters
        //if (intersectionMax < Math.Max(intersectionMin, 0.0)) return Intersection.NONE;

        //// intersection dist to the bounds defined by minDistance and maxDistance
        //double startIntersection = Math.Max(intersectionMin, minDistance);
        //double endIntersection = Math.Min(intersectionMax, maxDistance);

        //if (startIntersection > endIntersection) return Intersection.NONE;

        //// step size, normal, color accumulation, and transparency handling
        //double stepSize = _scale; //step size for traversing the volume along the ray
        //double firstIntersectionDistance = -1;
        //Vector surfaceNormal = new Vector(0, 0, 0);
        //Color accumulatedColor = Color.NONE;
        //double lastTransparency = 1.0;
        //bool foundFirstIntersection = false;

        //// iterate through the ray from start to end, accumulating color and transparency
        //for (double t = startIntersection; t <= endIntersection; t += stepSize)
        //{
        //    Vector pointOnRay = ray.CoordinateToPosition(t);
        //    Color pointColor = GetColor(pointOnRay);

        //    // first non-transparent voxel (alpha > 0) to find the actual interseciton with the nut
        //    if (pointColor.Alpha > 0)
        //    {
        //        if (!foundFirstIntersection)
        //        {
        //            firstIntersectionDistance = t;
        //            surfaceNormal = GetNormal(pointOnRay); // normal at the point
        //            foundFirstIntersection = true;
        //        }

        //        // acucmulate color considering alpha transparency and previous color
        //        accumulatedColor += pointColor * pointColor.Alpha * lastTransparency;
        //        lastTransparency *= (1 - pointColor.Alpha);

        //        if (lastTransparency < 0) break; // stop If the transparency is low
        //    }
        //}

        //if (firstIntersectionDistance < 0) return Intersection.NONE;

        //return new Intersection(true, foundFirstIntersection, this, ray, firstIntersectionDistance, surfaceNormal, Material.FromColor(accumulatedColor), accumulatedColor);
        return Intersection.NONE;
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