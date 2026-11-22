import org.offlineos.calculator.*;
public final class EngineTest {
    static int checks;
    static void near(double actual,double expected){checks++;if(Math.abs(actual-expected)>1e-8*Math.max(1,Math.abs(expected)))throw new AssertionError(actual+" != "+expected);}
    static void rejects(String expression){checks++;try{Calculator.evaluate(expression);}catch(IllegalArgumentException e){return;}throw new AssertionError("Accepted "+expression);}
    public static void main(String[] args){
        near(Calculator.evaluate("2+3×4"),14);near(Calculator.evaluate("(2+3)×4"),20);near(Calculator.evaluate("−2×−3"),6);near(Calculator.evaluate("200×15%"),30);near(Calculator.evaluate("1−−2"),3);near(Calculator.evaluate(".5+.25"),.75);
        rejects("1÷0");rejects("(2+3");rejects("1..2");rejects("2x3");rejects("");
        near(Conversions.convert(2,7,0,1),1609.344);near(Conversions.convert(3,3,1,1),453.59237);near(Conversions.convert(4,1,0,32),0);near(Conversions.convert(4,0,1,100),212);near(Conversions.convert(4,2,0,0),-273.15);near(Conversions.convert(5,3,0,1),4046.8564224);near(Conversions.convert(6,3,0,1),3.785411784);near(Conversions.convert(6,4,0,1),4.54609);near(Conversions.convert(7,2,0,60),96.56064);near(Conversions.convert(8,4,0,1),604800);
        for(int c:new int[]{2,3,4,5,6,7,8})for(int f=0;f<Conversions.UNITS[c].length;f++)for(int t=0;t<Conversions.UNITS[c].length;t++)near(Conversions.convert(c,t,f,Conversions.convert(c,f,t,300)),300);
        checks++;try{Conversions.convert(4,0,2,-274);throw new AssertionError("Below absolute zero accepted");}catch(IllegalArgumentException expected){}
        System.out.println(checks+" checks passed");
    }
}
