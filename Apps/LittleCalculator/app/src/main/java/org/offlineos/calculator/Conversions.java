package org.offlineos.calculator;

public final class Conversions {
    public static final String[] CATEGORIES={"Calc", "Currency", "Length", "Weight", "Temperature", "Area", "Volume", "Speed", "Time"};
    public static final String[][] UNITS={ {}, {"RUB", "USD", "EUR", "CNY", "GBP"}, {"Metres", "Kilometres", "Centimetres", "Millimetres", "Inches", "Feet", "Yards", "Miles"}, {"Kilograms", "Grams", "Tonnes", "Pounds", "Ounces"}, {"Celsius", "Fahrenheit", "Kelvin"}, {"Square metres", "Square kilometres", "Hectares", "Acres", "Square feet"}, {"Litres", "Millilitres", "Cubic metres", "US gallons", "Imperial gallons"}, {"km/h", "m/s", "mph", "Knots"}, {"Seconds", "Minutes", "Hours", "Days", "Weeks"} };
    private static final double[][] FACTORS={ {}, {}, {1,1000,.01,.001,.0254,.3048,.9144,1609.344}, {1,.001,1000,.45359237,.028349523125}, {}, {1,1000000,10000,4046.8564224,.09290304}, {1,.001,1000,3.785411784,4.54609}, {1,3.6,1.609344,1.852}, {1,60,3600,86400,604800} };
    public static double convert(int category,int from,int to,double value) {
        if (!Double.isFinite(value)) throw new IllegalArgumentException("Enter a finite number");
        if(category==4) {
            double c=from==0?value:from==1?(value-32)*5/9:value-273.15;
            if(c < -273.1500001) throw new IllegalArgumentException("Below absolute zero");
            return to==0?c:to==1?c*9/5+32:c+273.15;
        }
        return value*FACTORS[category][from]/FACTORS[category][to];
    }
}
