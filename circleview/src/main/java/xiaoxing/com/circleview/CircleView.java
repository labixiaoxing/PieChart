package xiaoxing.com.circleview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.math.BigDecimal;

public class CircleView extends View {

    private Context mContext;

    private int circleColor;
    private int arcColor;
    private int textColor;
    private float textSize;
    private float rate = 0;

    private float old_rate = 0;//老的rate，实现动画效果

    private float mWidth;//整个图的宽
    private float mHeight;//整个图的高

    private float innerCircleWidth;//内圆的宽
    private float outterArcWidth;//外弧形的宽

    private Paint mPaint;

    public CircleView(Context context) {
        this(context,null);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        //初始化属性
        initAttr(context,attrs);

    }



    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.circleView);
        if (ta != null) {
            circleColor = ta.getColor(R.styleable.circleView_circleColor, Color.RED);
            arcColor = ta.getColor(R.styleable.circleView_arcColor, Color.RED);
            textColor = ta.getColor(R.styleable.circleView_textColor, Color.BLACK);
            textSize = ta.getDimension(R.styleable.circleView_textSize, 13);
            rate = ta.getFloat(R.styleable.circleView_rate,0);
            ta.recycle();
        }
    }

    public void setRate(float rate){
        old_rate = this.rate;
        this.rate = rate;
        animation();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if(widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(100,100);
        }else if(widthSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(100,heightSpecSize);
        }else if(heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSpecSize,100);
        }else{
            setMeasuredDimension(Math.max(widthSpecSize,heightSpecSize),Math.max(widthSpecSize,heightSpecSize));
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //保证整个view是正方形
        mWidth = Math.max(w,h);
        mHeight = Math.max(w,h);
        //设置内圆的宽
        innerCircleWidth = mWidth / 2;
        //设置外弧的宽
        outterArcWidth = mWidth / 10;
        //重新绘制
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint = new Paint();
        mPaint.setTextSize(textSize);
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
        //画内圆
        mPaint.setColor(circleColor);
        canvas.drawCircle(mWidth / 2,mHeight / 2,innerCircleWidth / 2,mPaint);
        //画文字
        mPaint.setColor(textColor);
        Paint.FontMetrics fontMetrics=new Paint.FontMetrics();
        mPaint.getFontMetrics(fontMetrics);
        float offset=(fontMetrics.descent+fontMetrics.ascent)/2;
        canvas.drawText(rate + "%",mWidth / 2,mHeight / 2 - offset ,mPaint);
        //画弧
        mPaint.setColor(arcColor);
        mPaint.setStrokeWidth(outterArcWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        //计算弧的弧度
        float sweapAngle = 360 * rate / 100;
        RectF rect = new RectF(0 + outterArcWidth,0 + outterArcWidth ,mWidth - outterArcWidth ,mHeight - outterArcWidth);
        canvas.drawArc(rect,-90,sweapAngle,false,mPaint);
    }

    private void animation() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(old_rate,rate);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                BigDecimal b = new BigDecimal(Float.parseFloat(animation.getAnimatedValue().toString()));
                rate = b.setScale(1,BigDecimal.ROUND_HALF_UP).floatValue();
                postInvalidate();
            }
        });
        valueAnimator.setDuration((long)(2000 * Math.abs(old_rate - rate) / 100));
        valueAnimator.start();
    }

}
