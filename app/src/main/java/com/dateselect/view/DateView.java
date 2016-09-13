package com.dateselect.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.dateselect.R;
import com.dateselect.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by 王统根
 * time 2016/8/23
 * desc 日期选择
 */
public class DateView extends View{
    private int resouceId = -1;
    //是否可点击 默认可点击
    private boolean isClick = true;
    private int mulitUnSelectColor;
    private int mulitSelectColor;
    private int selectTextColor;
    private int unSelectTextColor;
    private int weekColor;
    private String[] weekDays = {"周日","周一","周二","周三","周四","周五","周六"};
    //列
    private static final int NUM_COLUMNS = 7;
    //行
    private static final int NUM_ROWS = 7;
    //字体大小
    private int textSize = 14;
    private DisplayMetrics mDisplayMetrics;
    private int mColumnSize;//控件宽度
    private int mRowSize;//控件高度
    private int mSelYear,mSelMonth,mSelDay;
    private int mCurrYear,mCurrMonth,mCurrDay;
    private Paint mPaint;
    private String [][] daysString;
    private DateClick dateClick;
    private List<String> selectedDays = new ArrayList<>();
    private List<String> selectedDaysState = new ArrayList<>();
    private boolean selectMode = false;
    public DateView(Context context) {
        super(context);
        init();
    }

    public DateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        init();
    }

    public DateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context,attrs);
        init();
    }

    private void init(){
        initSize();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mDisplayMetrics = getResources().getDisplayMetrics();
        daysString = new String[NUM_ROWS][NUM_COLUMNS];
        Calendar calendar = Calendar.getInstance();
        mCurrYear = calendar.get(Calendar.YEAR);
        mCurrMonth = calendar.get(Calendar.MONTH);
        mCurrDay = calendar.get(Calendar.DATE);
        setSelectYearMonth(mCurrYear,mCurrMonth,mCurrDay);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.date_view);
        int n = array.getIndexCount();
        for (int i = 0; i < n; i++){
            int arr = array.getIndex(i);
            resouceId = array.getResourceId(arr,0);
            switch (arr){
                case R.styleable.date_view_selected_color:
                    mulitSelectColor =  resouceId > 0? array.getResources().getColor(resouceId) : array.getColor(arr, Color.parseColor("#FE9900"));
                    break;
                case R.styleable.date_view_unSelect_color:
                    mulitUnSelectColor = resouceId > 0? array.getResources().getColor(resouceId) : array.getColor(arr,Color.parseColor("#0198da"));
                    break;
                case R.styleable.date_view_selectText_color:
                    selectTextColor = resouceId > 0? array.getResources().getColor(resouceId) : array.getColor(arr,Color.WHITE);
                    break;
                case R.styleable.date_view_unSelectText_color:
                    unSelectTextColor = resouceId > 0? array.getResources().getColor(resouceId) : array.getColor(arr,Color.WHITE);
                    break;
                case R.styleable.date_view_week_color:
                    weekColor = resouceId > 0? array.getResources().getColor(resouceId) : array.getColor(arr,Color.GRAY);
                    break;
            }
        }

        array.recycle();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        initSize();
        mPaint.setTextSize(textSize*mDisplayMetrics.scaledDensity);
        int mMonthDays = DateUtils.getMonthDays(mSelYear, mSelMonth);
        int weekNumber = DateUtils.getFirstDayWeek(mSelYear, mSelMonth);
        for (int i = 0; i < weekDays.length; i++){
            int startX = (int) (mColumnSize * i + (mColumnSize - mPaint.measureText(weekDays[i]))/2);
            int startY = (int) (mRowSize/2 - (mPaint.ascent() + mPaint.descent())/2);

            mPaint.setColor(Color.GRAY);
            canvas.drawText(weekDays[i], startX, startY, mPaint);

        }
        for(int day = 0;day < mMonthDays;day++){
            String dayString = (day+1+"").trim();
            int column = (day+weekNumber - 1) % 7;
            int row = (day+weekNumber - 1) / 7 +1;
            daysString[row][column]=dayString;
            int startX = (int) (mColumnSize * column + (mColumnSize - mPaint.measureText(dayString))/2);
            int startY;
            startY = (int) (mRowSize * row + mRowSize/2 - (mPaint.ascent() + mPaint.descent())/2);
            int startRecX = mColumnSize * column;
            int startRecY = mRowSize * row;
            int endRecX = startRecX + mColumnSize;
            int endRecY = startRecY + mRowSize;
            if (selectMode){//多选
//                selectedDaysState.clear();
                //TODO
                if (selectedDays.contains(dayString)){
                    selectedDaysState.add("1");
                    mPaint.setColor(mulitSelectColor);
                }else {
                    selectedDaysState.add("0");
                    mPaint.setColor(mulitUnSelectColor);
                }
                canvas.drawCircle(startRecX+(endRecX - startRecX)/2,startRecY+(endRecY - startRecY)/2,(endRecY - startRecY)/3,mPaint);
            }else {//单选
                int intDay = Integer.parseInt(dayString);
                //TODO 判断是否是当前月
                if (intDay == mSelDay && mSelMonth == mCurrMonth && mSelYear == mCurrYear){
                    mPaint.setColor(mulitSelectColor);
//                    canvas.drawCircle(startRecX+(endRecX - startRecX)/2,startRecY+(endRecY - startRecY)/2,(endRecY - startRecY)/2,mPaint);
                }else {
                    mPaint.setColor(mulitUnSelectColor);
                }
                canvas.drawCircle(startRecX+(endRecX - startRecX)/2,startRecY+(endRecY - startRecY)/2,(endRecY - startRecY)/3,mPaint);
//                if (intDay == mSelDay){
//                    mPaint.setColor(Color.WHITE);
//                }else {
//                    mPaint.setColor(Color.GRAY);
//                }
            }

            mPaint.setColor(Color.WHITE);
            canvas.drawText(dayString, startX, startY, mPaint);
        }
    }

    private int downX = 0,downY = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isClick){
            return true;
        }
        int eventCode=  event.getAction();
        switch(eventCode){
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) event.getX();
                int upY = (int) event.getY();
                if(Math.abs(upX-downX) < 10 && Math.abs(upY - downY) < 10){//点击事件
                    performClick();
                    doClickAction((upX + downX)/2,(upY + downY)/2);
                }
                break;
        }
        return true;
    }
    private void doClickAction(int x,int y){
        selectedDaysState.clear();
        int row = y / mRowSize;
        int column = x / mColumnSize;
        String strDay = daysString[row][column];
        if (strDay == null){
            return;
        }
        if (selectMode){
            if (selectedDays.contains(strDay)){//包含这一天的话,取消这一天
                selectedDays.remove(strDay);
            }else {
                selectedDays.add(strDay);
            }
        }

        setSelectYearMonth(mSelYear,mSelMonth,Integer.parseInt(daysString[row][column]));
        invalidate();
        //执行activity发送过来的点击处理事件
        if(dateClick != null){
            dateClick.onClickOnDate();
        }
    }

    /**
     * 设置日期的点击回调事件
     * @author shiwei.deng
     *
     */
    public interface DateClick{
        public void onClickOnDate();
    }

    /**
     * 设置年月
     * @param year
     * @param month
     */
    private void setSelectYearMonth(int year,int month,int day){
        mSelYear = year;
        mSelMonth = month;
        mSelDay = day;
    }
    /**
     * 设置日期点击事件
     * @param dateClick
     */
    public void setDateClick(DateClick dateClick) {
        this.dateClick = dateClick;
    }

    /**
     * 设置选择样式,单选还是多选
     * @param selectMode
     */
    public void setSelectMode(boolean selectMode){
        this.selectMode = selectMode;
        if (!selectMode){
            selectedDays.clear();
            selectedDaysState.clear();
        }
        setSelectYearMonth(mCurrYear,mCurrMonth,mCurrDay);
        invalidate();
    }

    /**
     * 左点击，日历向前翻
     */
    public void onLeftClick(){
        int year = mSelYear;
        int month = mSelMonth;
        int day = mSelDay;
        if(month == 0){//若果是1月份，则变成12月份
            year = mSelYear-1;
            month = 11;
        }else if(DateUtils.getMonthDays(year, month) == day){
            //如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
            month = month-1;
            day = DateUtils.getMonthDays(year, month);
        }else{
            month = month-1;
        }
        setSelectYearMonth(year,month,day);
        invalidate();
    }

    /**
     * 右点击，日历向后翻
     */
    public void onRightClick(){
        int year = mSelYear;
        int month = mSelMonth;
        int day = mSelDay;
        if(month == 11){//若果是12月份，则变成1月份
            year = mSelYear+1;
            month = 0;
        }else if(DateUtils.getMonthDays(year, month) == day){
            //如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
            month = month + 1;
            day = DateUtils.getMonthDays(year, month);
        }else{
            month = month + 1;
        }
        setSelectYearMonth(year,month,day);
        invalidate();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if(heightMode == MeasureSpec.AT_MOST){
            heightSize = mDisplayMetrics.densityDpi * 200;
        }
        if(widthMode == MeasureSpec.AT_MOST){
            widthSize = mDisplayMetrics.densityDpi * 300;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    /**
     * 初始化列宽行高
     */
    private void initSize(){
        mColumnSize = getWidth() / NUM_COLUMNS;
        mRowSize = getHeight() / NUM_ROWS;
    }

    public void setIsClick(boolean isClick){
        this.isClick = isClick;
    }

    /**
     * 只是显示信息
     * @param selectedDays
     */
    public void setSelectedDays(List<String> selectedDays){
        setIsClick(false);
        this.selectedDays = selectedDays;
        invalidate();
    }

    public List<String> getSelectedDays(){
        return selectedDays;
    }

    /**
     * 每天的状态值
     * @return
     */
    public List<String> getSelectedDaysState(){
        return selectedDaysState;
    }

    public String getCurrentDay(){
        return mSelYear+"/"+mSelMonth+"/"+mSelDay;
    }
}
