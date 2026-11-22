package org.offlineos.calculator;

import java.math.BigDecimal;

public final class Calculator {
    public static String format(double value) {
        if (!Double.isFinite(value)) throw new IllegalArgumentException("Result is too large");
        if (value == 0) return "0";
        return BigDecimal.valueOf(value).round(new java.math.MathContext(12)).stripTrailingZeros().toPlainString();
    }
    public static double evaluate(String expression) {
        if (expression.length() > 500) throw new IllegalArgumentException("Expression is too long");
        Parser p = new Parser(expression.replace('×', '*').replace('÷', '/').replace('−', '-'));
        double v = p.sum(); p.space();
        if (p.i != p.s.length()) throw new IllegalArgumentException("Check the expression");
        if (!Double.isFinite(v)) throw new IllegalArgumentException("Result is too large");
        return v;
    }
    private static final class Parser {
        final String s; int i;
        Parser(String s) { this.s=s; }
        void space() { while(i<s.length() && Character.isWhitespace(s.charAt(i))) i++; }
        boolean take(char c) { space(); if(i<s.length() && s.charAt(i)==c) { i++; return true; } return false; }
        double sum() { double v=product(); while(true) { if(take('+')) v+=product(); else if(take('-')) v-=product(); else return v; } }
        double product() { double v=unary(); while(true) { if(take('*')) v*=unary(); else if(take('/')) { double d=unary(); if(d==0) throw new IllegalArgumentException("Cannot divide by zero"); v/=d; } else return v; } }
        double unary() { if(take('+')) return unary(); if(take('-')) return -unary(); return atom(); }
        double atom() {
            double v;
            if(take('(')) { v=sum(); if(!take(')')) throw new IllegalArgumentException("Close the parentheses"); }
            else { space(); int start=i; while(i<s.length() && (Character.isDigit(s.charAt(i)) || s.charAt(i)=='.')) i++;
                if(start==i) throw new IllegalArgumentException("Enter a number");
                try { v=Double.parseDouble(s.substring(start,i)); } catch(NumberFormatException e) { throw new IllegalArgumentException("Check the number"); }
            }
            while(take('%')) v/=100;
            return v;
        }
    }
}
