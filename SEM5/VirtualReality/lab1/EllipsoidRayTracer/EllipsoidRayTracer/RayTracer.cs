using System;

namespace rt
{
    class RayTracer
    {
        private Geometry[] geometries;
        private Light[] lights;

        public RayTracer(Geometry[] geometries, Light[] lights)
        {
            this.geometries = geometries;
            this.lights = lights;
        }

        private double ImageToViewPlane(int n, int imgSize, double viewPlaneSize)
        {
            return -n * viewPlaneSize / imgSize + viewPlaneSize / 2;
        }

        private Intersection FindFirstIntersection(Line ray, double minDist, double maxDist)
        {
            var intersection = Intersection.NONE;

            foreach (var geometry in geometries)
            {
                var intr = geometry.GetIntersection(ray, minDist, maxDist);

                if (!intr.Valid || !intr.Visible) continue;

                if (!intersection.Valid || !intersection.Visible)
                {
                    intersection = intr;
                }
                else if (intr.T < intersection.T)
                {
                    intersection = intr;
                }
            }

            return intersection;
        }

        private bool IsLit(Vector point, Light light)
        {
            // TODO: ADD CODE HERE
            const double minDistance = 1;
            var rayToLight = new Line(point, light.Position);
            var lightDistance = (point - light.Position).Length();

            foreach (var geometry in geometries)
            {
                if (!(geometry is RawCtMask))
                {

                    var intersection = geometry.GetIntersection(rayToLight, minDistance, lightDistance);

                    if (intersection.Visible)
                    {
                        return false;
                    }
                }
            }

            return true; // The point is illuminated if no geometry blocks the light
        }

        //public void Render(Camera camera, int width, int height, string filename)
        //{
        //    var background = new Color(0.2, 0.2, 0.2, 1.0);

        //    var image = new Image(width, height);

        //    for (var i = 0; i < width; i++)
        //    {
        //        for (var j = 0; j < height; j++)
        //        {
        //            // TODO: ADD CODE HERE
        //            image.SetPixel(i, j, background);
        //        }
        //    }

        //    image.Store(filename);
        //}

        public void Render(Camera camera, int width, int height, string filename, double scaleFactor = 1.0)
        {
            var backgroundColor = new Color(0, 0, 0, 1.0);
            var viewPlaneDirection = (camera.Up ^ camera.Direction).Normalize(); // Perpendicular vector to the camera direction

            var image = new Image(width, height);
            var viewPlaneCenter = camera.Direction * camera.ViewPlaneDistance;

            for (var xPixel = 0; xPixel < width; xPixel++)
            {
                var horizontalOffset = ImageToViewPlane(xPixel, width, camera.ViewPlaneWidth);

                for (var yPixel = 0; yPixel < height; yPixel++)
                {
                    var verticalOffset = ImageToViewPlane(yPixel, height, camera.ViewPlaneHeight);

                    // direction vector for the ray from the camera through the current pixel
                    var rayDirection = camera.Position + viewPlaneCenter + viewPlaneDirection * horizontalOffset + camera.Up * verticalOffset;
                    var ray = new Line(camera.Position, rayDirection); // ray from the camera to the point in the scene

                    // first inters with the scene geometries
                    var intersection = FindFirstIntersection(ray, camera.FrontPlaneDistance, camera.BackPlaneDistance);

                    if (!intersection.Visible)
                    {
                        image.SetPixel(xPixel, yPixel, backgroundColor);
                        continue;
                    }

                    // find the material and apply lighting
                    var material = intersection.Material;
                    var pixelColor = new Color();
                    var intersectionPoint = intersection.Position;
                    var viewDirection = (camera.Position - intersectionPoint).Normalize();
                 
                    foreach (var lightSource in lights)
                    {
                        var lightingColor = material.Ambient;

                        if (IsLit(intersectionPoint, lightSource))
                        {
                            var normalVector = intersection.Normal;

                            var lightDirection = (lightSource.Position - intersectionPoint).Normalize(); // dir form point to light
                            var reflectionDirection = (normalVector * (normalVector * lightDirection) * 2 - lightDirection).Normalize(); // dir of reflection
                            var diffuseFactor = normalVector * lightDirection; // how much light hits the surface
                            var specularFactor = viewDirection * reflectionDirection; // specular

                            if (diffuseFactor > 0)
                            {
                                lightingColor += material.Diffuse * lightSource.Diffuse * diffuseFactor;
                            }

                            if (specularFactor > 0)
                            {
                                lightingColor += material.Specular * lightSource.Specular * Math.Pow(specularFactor, material.Shininess);
                            }

                            lightingColor *= lightSource.Intensity;
                        }

                        pixelColor += lightingColor; // Accumulate the light from all lights
                    }

                    image.SetPixel(xPixel, yPixel, pixelColor);
                }
            }

            image.Store(filename);

        }


    }
}