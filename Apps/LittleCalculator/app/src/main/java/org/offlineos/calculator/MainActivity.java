package org.offlineos.calculator;

import android.app.*;
import android.os.Bundle;
import android.content.*;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.widget.*;
import android.text.*;
import org.json.*;
import java.util.*;

public class MainActivity extends Activity {
    private LinearLayout root, body;
    private TextView display, preview;
    private SharedPreferences prefs;
    private final ArrayList<String[]> history=new ArrayList<>();
    private String expression="";
    private boolean answered=false;
    private int selected=0, bg, ink, muted, tile, accent, pale;
    private float startX,startY;
    private boolean historyOpen=false;
    private int dp(float n){return (int)(getResources().getDisplayMetrics().density*n+.5f);}
    @Override public void onCreate(Bundle state){super.onCreate(state);prefs=getSharedPreferences("calculator",MODE_PRIVATE);expression=state==null?prefs.getString("expression",""):state.getString("expression","");selected=state==null?0:state.getInt("tab",0);loadHistory();build();}
    @Override public void onConfigurationChanged(Configuration c){super.onConfigurationChanged(c);build();}
    @Override protected void onSaveInstanceState(Bundle b){super.onSaveInstanceState(b);b.putString("expression",expression);b.putInt("tab",selected);}
    @Override protected void onPause(){super.onPause();prefs.edit().putString("expression",expression).apply();}
    private void loadHistory(){try{JSONArray a=new JSONArray(prefs.getString("history","[]"));for(int i=0;i<Math.min(a.length(),100);i++){JSONArray row=a.getJSONArray(i);history.add(new String[]{row.getString(0),row.getString(1)});}}catch(JSONException ignored){}}
    private void saveHistory(){JSONArray a=new JSONArray();for(String[] row:history)a.put(new JSONArray(Arrays.asList(row)));prefs.edit().putString("history",a.toString()).apply();}
    private GradientDrawable shape(int color,int radius){GradientDrawable d=new GradientDrawable();d.setColor(color);d.setCornerRadius(dp(radius));return d;}
    private TextView text(String value,int size,int color){TextView t=new TextView(this);t.setText(value);t.setTextSize(size);t.setTextColor(color);t.setGravity(Gravity.CENTER_VERTICAL);return t;}
    private Button button(String label,Runnable action){Button b=new Button(this);b.setText(label);b.setTextSize(17);b.setTextColor(ink);b.setAllCaps(false);b.setMinWidth(0);b.setMinimumWidth(0);b.setMinHeight(dp(48));b.setPadding(dp(8),0,dp(8),0);b.setBackground(shape(tile,18));b.setOnClickListener(v->action.run());return b;}
    private LinearLayout column(){LinearLayout l=new LinearLayout(this);l.setOrientation(LinearLayout.VERTICAL);return l;}
    private void build(){
        boolean dark=(getResources().getConfiguration().uiMode&Configuration.UI_MODE_NIGHT_MASK)==Configuration.UI_MODE_NIGHT_YES;
        bg=Color.parseColor(dark?"#141B19":"#F7F9F5");ink=Color.parseColor(dark?"#E4EBE5":"#202D28");muted=Color.parseColor(dark?"#A2B6AC":"#64766D");tile=Color.parseColor(dark?"#242F2A":"#EAF0E9");accent=Color.parseColor(dark?"#A5D8BD":"#32654E");pale=Color.parseColor(dark?"#354B3F":"#D8EADC");
        root=column();root.setBackgroundColor(bg);root.setPadding(dp(16),0,dp(16),dp(8));setContentView(root);
        root.setOnApplyWindowInsetsListener((v,insets)->{android.graphics.Insets s=insets.getInsets(WindowInsets.Type.systemBars()|WindowInsets.Type.displayCutout());root.setPadding(dp(16)+s.left,s.top,dp(16)+s.right,dp(8)+s.bottom);return insets;});
        // Insets API is used only on Android 11+, older releases use the window's inset handling.
        if(android.os.Build.VERSION.SDK_INT<30){root.setOnApplyWindowInsetsListener(null);root.setFitsSystemWindows(true);}
        TextView title=text("Little Calculator",20,ink);title.setTypeface(null,Typeface.BOLD);root.addView(title,new LinearLayout.LayoutParams(-1,dp(54)));
        HorizontalScrollView tabs=new HorizontalScrollView(this);tabs.setHorizontalScrollBarEnabled(false);LinearLayout strip=new LinearLayout(this);tabs.addView(strip);
        for(int i=0;i<Conversions.CATEGORIES.length;i++){final int index=i;Button b=button(Conversions.CATEGORIES[i],()->{selected=index;historyOpen=false;build();});b.setTextSize(14);b.setTextColor(i==selected?bg:muted);b.setBackground(shape(i==selected?accent:bg,22));LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-2,dp(44));lp.setMargins(0,0,dp(6),0);strip.addView(b,lp);if(i==selected)b.post(()->tabs.smoothScrollTo(Math.max(0,b.getLeft()-dp(30)),0));}
        root.addView(tabs,new LinearLayout.LayoutParams(-1,dp(52)));body=column();root.addView(body,new LinearLayout.LayoutParams(-1,0,1));if(selected==0){ScrollView sc=new ScrollView(this);sc.setFillViewport(true);LinearLayout inner=column();inner.setMinimumHeight(dp(530));body.addView(sc,new LinearLayout.LayoutParams(-1,-1));sc.addView(inner,new ScrollView.LayoutParams(-1,-1));body=inner;calculator();}else converter();root.requestApplyInsets();
    }
    private void calculator(){
        TextView hint=text("⌄  Pull down for history",13,muted);hint.setGravity(Gravity.CENTER);hint.setContentDescription("Open calculation history");hint.setOnClickListener(v->showHistory());body.addView(hint,new LinearLayout.LayoutParams(-1,dp(40)));
        LinearLayout screen=column();screen.setGravity(Gravity.BOTTOM);screen.setPadding(dp(8),0,dp(8),dp(18));
        display=text(expression.isEmpty()?"0":expression,40,ink);display.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);display.setMaxLines(3);display.setTextIsSelectable(true);screen.addView(display,new LinearLayout.LayoutParams(-1,0,1));
        preview=text("",22,muted);preview.setGravity(Gravity.RIGHT);screen.addView(preview,new LinearLayout.LayoutParams(-1,dp(34)));body.addView(screen,new LinearLayout.LayoutParams(-1,0,1));
        String[][] keys={{"AC","(",")","⌫"},{"7","8","9","÷"},{"4","5","6","×"},{"1","2","3","−"},{"%","0",".","+"}};
        for(String[] row:keys){LinearLayout line=new LinearLayout(this);for(String key:row){Button b=button(key,()->key(key));b.setTextSize(25);if("AC⌫÷×−+%()".contains(key)){b.setTextColor(accent);b.setBackground(shape(pale,20));}if(key.equals("⌫"))b.setContentDescription("Delete last character");if(key.equals("AC"))b.setContentDescription("Clear calculation");LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(0,-1,1);lp.setMargins(dp(3),dp(3),dp(3),dp(3));line.addView(b,lp);}body.addView(line,new LinearLayout.LayoutParams(-1,0,1));}
        Button equal=button("=",()->key("="));equal.setTextSize(30);equal.setTextColor(bg);equal.setBackground(shape(accent,22));LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,dp(58));lp.setMargins(dp(3),dp(6),dp(3),0);body.addView(equal,lp);refresh();
    }
    @Override public boolean dispatchTouchEvent(android.view.MotionEvent e){if(e.getAction()==MotionEvent.ACTION_DOWN){startX=e.getX();startY=e.getY();}if(e.getAction()==MotionEvent.ACTION_UP && selected==0 && !historyOpen && e.getY()-startY>dp(90) && Math.abs(e.getX()-startX)<dp(80)){showHistory();return true;}return super.dispatchTouchEvent(e);}
    private void key(String k){
        if(k.equals("AC")){expression="";answered=false;}else if(k.equals("⌫")){if(!expression.isEmpty())expression=expression.substring(0,expression.length()-1);answered=false;}
        else if(k.equals("=")){if(expression.isEmpty())return;try{String result=Calculator.format(Calculator.evaluate(expression));history.add(0,new String[]{expression,result});while(history.size()>100)history.remove(history.size()-1);saveHistory();expression=result;answered=true;}catch(IllegalArgumentException ex){preview.setText(ex.getMessage());return;}}
        else{if(answered && (Character.isDigit(k.charAt(0)) || k.equals(".") || k.equals("(")))expression="";answered=false;if(expression.length()<500)expression+=k;}
        refresh();
    }
    private void refresh(){display.setText(expression.isEmpty()?"0":expression);try{preview.setText(expression.isEmpty()?"":Calculator.format(Calculator.evaluate(expression)));}catch(IllegalArgumentException e){preview.setText("");}}
    private void showHistory(){
        if(historyOpen)return;historyOpen=true;LinearLayout panel=column();panel.setPadding(dp(20),dp(12),dp(20),dp(12));panel.setBackgroundColor(bg);TextView heading=text("History",22,ink);panel.addView(heading);TextView note=text("Tap a result to use it · latest 100 calculations",12,muted);panel.addView(note);
        ScrollView scroll=new ScrollView(this);LinearLayout rows=column();scroll.addView(rows);panel.addView(scroll,new LinearLayout.LayoutParams(-1,dp(330)));AlertDialog dialog=new AlertDialog.Builder(this).setView(panel).setPositiveButton("Close",null).setNeutralButton("Clear history",(d,w)->new AlertDialog.Builder(this).setMessage("Clear all calculation history?").setNegativeButton("Cancel",null).setPositiveButton("Clear",(dd,ww)->{history.clear();saveHistory();}).show()).create();
        if(history.isEmpty()){TextView empty=text("Your calculations will appear here.",16,muted);empty.setPadding(0,dp(30),0,0);rows.addView(empty);}for(String[] item:history){Button row=button(item[0]+"\n= "+item[1],()->{expression=item[1];answered=true;refresh();dialog.dismiss();});row.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);row.setPadding(dp(12),dp(12),dp(12),dp(12));LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);lp.setMargins(0,dp(8),0,0);rows.addView(row,lp);}dialog.setOnDismissListener(d->historyOpen=false);dialog.show();
    }
    private EditText amount; private TextView converted; private Spinner from,to;
    private void converter(){
        ScrollView scroll=new ScrollView(this);LinearLayout content=column();content.setPadding(dp(8),dp(20),dp(8),dp(12));scroll.addView(content);body.addView(scroll,new LinearLayout.LayoutParams(-1,-1));
        TextView heading=text(Conversions.CATEGORIES[selected],28,ink);heading.setTypeface(null,Typeface.BOLD);content.addView(heading);
        TextView help=text(selected==1?"Offline · manual rates in RUB per 1 unit":"Convert instantly on your device",14,muted);help.setPadding(0,dp(8),0,dp(20));content.addView(help);
        content.addView(text("Amount",13,muted));amount=new EditText(this);amount.setTextColor(ink);amount.setTextSize(36);amount.setSingleLine();amount.setInputType(android.text.InputType.TYPE_CLASS_NUMBER|android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL|android.text.InputType.TYPE_NUMBER_FLAG_SIGNED);amount.setFilters(new InputFilter[]{new InputFilter.LengthFilter(40)});amount.setText(prefs.getString("amount_"+selected,"1"));content.addView(amount,new LinearLayout.LayoutParams(-1,dp(72)));
        content.addView(text("From",13,muted));from=spinner();content.addView(from,new LinearLayout.LayoutParams(-1,dp(56)));from.setSelection(prefs.getInt("from_"+selected,0));
        Button swap=button("⇅  Swap units",()->{int f=from.getSelectedItemPosition();from.setSelection(to.getSelectedItemPosition());to.setSelection(f);updateConversion();});LinearLayout.LayoutParams swapLp=new LinearLayout.LayoutParams(-1,dp(48));swapLp.setMargins(0,dp(12),0,dp(12));content.addView(swap,swapLp);
        content.addView(text("To",13,muted));to=spinner();content.addView(to,new LinearLayout.LayoutParams(-1,dp(56)));to.setSelection(prefs.getInt("to_"+selected,1));
        converted=text("",32,accent);converted.setPadding(0,dp(24),0,dp(20));converted.setTextIsSelectable(true);content.addView(converted,new LinearLayout.LayoutParams(-1,-2));
        if(selected==1){content.addView(button("Edit currency rates",this::editRates),new LinearLayout.LayoutParams(-1,dp(52)));TextView stamp=text(prefs.getString("rate_note","No rates saved. Enter rates before converting."),13,muted);stamp.setPadding(0,dp(14),0,0);content.addView(stamp);}
        AdapterView.OnItemSelectedListener listener=new AdapterView.OnItemSelectedListener(){public void onItemSelected(AdapterView<?> p,View v,int pos,long id){updateConversion();}public void onNothingSelected(AdapterView<?> p){}};from.setOnItemSelectedListener(listener);to.setOnItemSelectedListener(listener);
        amount.addTextChangedListener(new TextWatcher(){public void beforeTextChanged(CharSequence s,int a,int c,int f){}public void onTextChanged(CharSequence s,int a,int before,int count){updateConversion();}public void afterTextChanged(Editable e){}});updateConversion();
    }
    private Spinner spinner(){Spinner s=new Spinner(this);ArrayAdapter<String> a=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,Conversions.UNITS[selected]){@Override public View getView(int pos,View v,android.view.ViewGroup parent){TextView t=(TextView)super.getView(pos,v,parent);t.setTextColor(ink);t.setTextSize(20);return t;}};a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);s.setAdapter(a);s.setBackground(shape(tile,12));return s;}
    private double rate(int index){if(index==0)return 1;String value=prefs.getString("rate_"+Conversions.UNITS[1][index],"");if(value.isEmpty())throw new IllegalArgumentException("Set rates for these currencies");return Double.parseDouble(value);}
    private void updateConversion(){if(amount==null||converted==null||from==null||to==null)return;String raw=amount.getText().toString();int f=from.getSelectedItemPosition(),t=to.getSelectedItemPosition();prefs.edit().putString("amount_"+selected,raw).putInt("from_"+selected,f).putInt("to_"+selected,t).apply();if(raw.isEmpty()){converted.setText("—");return;}try{double v=Double.parseDouble(raw.replace(',','.'));double result=selected==1?(f==t?v:v*rate(f)/rate(t)):Conversions.convert(selected,f,t,v);converted.setText(Calculator.format(result));}catch(IllegalArgumentException e){converted.setTextSize(20);converted.setText(e instanceof NumberFormatException?"Enter a valid number":e.getMessage());return;}converted.setTextSize(32);}
    private void editRates(){LinearLayout fields=column();fields.setPadding(dp(24),dp(8),dp(24),0);fields.setBackgroundColor(bg);TextView hint=text("RUB for 1 unit. Use your own current rates.\nNo automatic downloads.",14,muted);fields.addView(hint);EditText[] entries=new EditText[4];for(int i=1;i<5;i++){fields.addView(text(Conversions.UNITS[1][i],14,muted));EditText e=new EditText(this);e.setTextColor(ink);e.setInputType(8194);e.setSingleLine();e.setFilters(new InputFilter[]{new InputFilter.LengthFilter(24)});e.setText(prefs.getString("rate_"+Conversions.UNITS[1][i],""));fields.addView(e);entries[i-1]=e;}
        ScrollView scroll=new ScrollView(this);scroll.addView(fields);AlertDialog dialog=new AlertDialog.Builder(this).setTitle("Currency rates").setView(scroll).setNegativeButton("Cancel",null).setPositiveButton("Save",null).create();dialog.setOnShowListener(d->dialog.getButton(-1).setOnClickListener(v->{SharedPreferences.Editor edit=prefs.edit();for(int i=0;i<4;i++){String raw=entries[i].getText().toString().trim().replace(',','.');if(raw.isEmpty()){edit.remove("rate_"+Conversions.UNITS[1][i+1]);continue;}try{double n=Double.parseDouble(raw);if(!Double.isFinite(n)||n<=0)throw new NumberFormatException();edit.putString("rate_"+Conversions.UNITS[1][i+1],raw);}catch(NumberFormatException ex){entries[i].setError("Enter a positive rate");return;}}edit.putString("rate_note","Manually saved: "+java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.MEDIUM,java.text.DateFormat.SHORT).format(new Date())).apply();dialog.dismiss();build();}));dialog.show();}
}
