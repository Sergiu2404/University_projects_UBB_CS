using System;


namespace rt
{
    public class Ellipsoid : Geometry
    {
        private Vector Center { get; }
        private Vector SemiAxesLength { get; }
        private double Radius { get; }
        
        
        public Ellipsoid(Vector center, Vector semiAxesLength, double radius, Material material, Color color) : base(material, color)
        {
            Center = center;
            SemiAxesLength = semiAxesLength;
            Radius = radius;
        }

        public Ellipsoid(Vector center, Vector semiAxesLength, double radius, Color color) : base(color)
        {
            Center = center;
            SemiAxesLength = semiAxesLength;
            Radius = radius;
        }

        public override Intersection GetIntersection(Line ray, double minDistance, double maxDistance)
        {
            // to ellipsoid local coords system (scaled them based on semi-axes of the ellipsoid)
            Vector normalizedRayDirection = new Vector(ray.Dx.X / SemiAxesLength.X, ray.Dx.Y / SemiAxesLength.Y, ray.Dx.Z / SemiAxesLength.Z);
            Vector rayOriginToCenter = new Vector((ray.X0 - Center).X / SemiAxesLength.X, (ray.X0 - Center).Y / SemiAxesLength.Y, (ray.X0 - Center).Z / SemiAxesLength.Z);

            // coef of the quadratic equation (inters of ellipsoid with ray-line): at^2+bt+c = 0
            double aCoefficient = normalizedRayDirection.Length2();
            double bCoefficient = normalizedRayDirection * rayOriginToCenter * 2; //interaction between the rays direction and its origin's position relative to the ellipsoids center
            double cCoefficient = rayOriginToCenter.Length2() - Radius * Radius;

            // calculate discriminnt
            double discriminant = bCoefficient * bCoefficient - 4 * aCoefficient * cCoefficient;

            if (discriminant < 0)
            {
                return Intersection.NONE;
            }

            double tNear, tFar;
            // exactly one inters tNear = tFar
            if (Math.Abs(discriminant) < 0)
            {
                tNear = -bCoefficient / (2 * aCoefficient);
                tFar = double.NaN; // no inters
            }
            else // Calculate the 2 possible inters dist along the ray
            {
                double sqrtDiscriminant = Math.Sqrt(discriminant);
                double inverse2a = 1 / (2 * aCoefficient);
                tNear = (-bCoefficient - sqrtDiscriminant) * inverse2a;
                tFar = (-bCoefficient + sqrtDiscriminant) * inverse2a;
            }

            // check if inters points are in the valid dist range
            bool isVisible = (tNear >= minDistance && tNear <= maxDistance) || (tFar >= minDistance && tFar <= maxDistance);

            double closestIntersectionDistance = (tNear >= minDistance ? tNear : tFar);

            if (double.IsNaN(closestIntersectionDistance))
            {
                return Intersection.NONE;
            }

            // pos of the 3d intersection point along the ray
            var intersectionPosition = ray.CoordinateToPosition(closestIntersectionDistance);

            // normal vector at the intersection point helping at the ligthing
            Vector surfaceNormal = new Vector(
                ((intersectionPosition - Center) * 2).X / (SemiAxesLength.X * SemiAxesLength.X),
                ((intersectionPosition - Center) * 2).Y / (SemiAxesLength.Y * SemiAxesLength.Y),
                ((intersectionPosition - Center) * 2).Z / (SemiAxesLength.Z * SemiAxesLength.Z)
            ).Normalize();

            return new Intersection(true, isVisible, this, ray, closestIntersectionDistance, surfaceNormal, Material, Color);
        }

    }
}
