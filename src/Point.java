public class Point {
    public static final double MAXX = 1600.0;
    public static final double MAXY = 1000.0;

    public static final double MAXPX = 1800.0;
    public static final double MAXPY = 1200.0;

    public static double SCALEX1 = -1;
    public static double SCALEX2 = -1;
    public static double RANGEX = 0;

    public static double SCALEY1 = -1;
    public static double SCALEY2 = -1;
    public static double RANGEY = 0;

    public int px;
    public int py;

    public double x, y;

    public static void setScales(double scalex1, double scalex2, double scaley1, double scaley2)
    {
        SCALEX1 = scalex1;
        SCALEX2 = scalex2;
        SCALEY1 = scaley1;
        SCALEY2 = scaley2;

        RANGEX = SCALEX2 - SCALEX1;
        RANGEY = SCALEY2 - SCALEY1;
    }

    public Point (double x, double y)
    {
        if (x > SCALEX2 || x < SCALEX1 || y > SCALEY2 || y < SCALEY1)
        {
            throw new RuntimeException("Out of range");
        }

        px = (int) (((x-SCALEX1)/RANGEX * MAXX) + (MAXPX - MAXX)/2);
        py = (int) (MAXPY - (((y - SCALEY1)/RANGEY * MAXY) + (MAXPY - MAXY)/2));
    }

    public static int x (double x)
    {
        if (x > SCALEX2 || x < SCALEX1)
        {
            throw new RuntimeException("Out of range");
        }

        return (int) (((x-SCALEX1)/RANGEX * MAXX) + (MAXPX - MAXX)/2);
    }

    public static int y (double y)
    {
        if (y > SCALEY2 || y < SCALEY1)
        {
            throw new RuntimeException("Out of range");
        }

        return (int) (MAXPY - (((y - SCALEY1)/RANGEY * MAXY) + (MAXPY - MAXY)/2));
    }

}
