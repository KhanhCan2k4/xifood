package vn.edu.tdc.snake.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Snack extends View {
    private String TAG = getContext().getClass().getSimpleName();
    //Properties
    private Paint paint;
    private Point p0;
    private int radius;
    //private int curX, curY;
    //private int WIDTH = 7000;
    //private int HEIGHT = 7000;
    //private int widthSolution, heightSolution;
    private int big;
    //private int scrollStep = 10, scrollLoop = 1;
    private boolean move = false;
    private ArrayList<Point> snackPoints;
    private ArrayList<Point> grassPoints;

    //private int directX, directY;

    public Snack(Context context) {
        super(context);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        p0 = new Point();
        p0.x = 200; p0.y = 200; radius = 60;
        big = 6;
        snackPoints = new ArrayList<Point>();
        snackPoints.add(p0);
        for (int i=1; i<big;++i) {
            Point point = new Point();
            point.x = p0.x + i * radius;
            point.y = p0.y;
            snackPoints.add(point);
        }
        grassPoints = new ArrayList<Point>();
        for (int i = 0; i<99;++i) {
            Point point = new Point();
            grassPoints.add(point);
        }
        /*
        // Lấy đối tượng WindowManager
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        // Tạo một đối tượng DisplayMetrics để lưu trữ thông tin về độ phân giải màn hình
        DisplayMetrics metrics = new DisplayMetrics();
        // Lấy thông tin về độ phân giải màn hình
        wm.getDefaultDisplay().getMetrics(metrics);
        widthSolution = metrics.widthPixels;
        heightSolution = metrics.heightPixels;

         */
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //scrH = h;
        for (Point point:grassPoints) {
            point.x = new Random().nextInt(w);
            point.y = new Random().nextInt(h);
        }
    }
/*
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Đặt kích thước của View
        setMeasuredDimension(WIDTH, heightMeasureSpec);
    }

 */

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //cx = cx + 50;
        //cy = cy + 25;
        //canvas.drawCircle(cx, cy, radius, paint);
        drawSnack(canvas);
        drawGrass(canvas);
    }
    private void drawSnack(Canvas canvas) {
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.YELLOW);
        colors.add(Color.GREEN);
        colors.add(Color.BLUE);
        colors.add(Color.CYAN);
        colors.add(Color.MAGENTA);

        for (Point point: snackPoints) {
            if (point == p0) {
                paint.setColor(Color.BLACK); 
            }
            else {
                int color = colors.remove(0);
                paint.setColor(color);
                colors.add(color);
            }
            canvas.drawCircle(point.x, point.y, radius, paint);
        }
    }
    private void drawGrass(Canvas canvas) {
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.MAGENTA);
        colors.add(Color.GREEN);
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        colors.add(Color.CYAN);
        colors.add(Color.YELLOW);

        for (Point point: grassPoints) {
            int color = colors.get((int) Math.floor(Math.random() * colors.size()));
            paint.setColor(color);

            canvas.drawCircle(point.x, point.y, 30, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //scrollTo(scrollStep*scrollLoop++, 0);
                //p0.x = (int)event.getX() + scrollStep*(scrollLoop-1);
                //Log.d("test", event.getX() + "");
                if (((int) event.getX()- p0.x)*((int) event.getX()- p0.x)+ ((int) event.getY()- p0.y)*((int) event.getY()- p0.y) <= radius*radius) {
                    move = true;
                }
                else {
                    move = false;
                }
                //cx = (int) event.getX();
                //cy = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                //Log.d(TAG, "UP");
                //cx = (int) event.getX();
                //cy = (int) event.getY();
                // Khi người dùng chạm vào màn hình, hủy bỏ bất kỳ cuộn nào đang diễn ra
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.d(TAG, "MOVE");
                if (move) {
                    // Di chuyển phần thân rắn (Chưa có phần đầu)
                    for (int i=big-1; i>0;--i) {
                        snackPoints.get(i).x = snackPoints.get(i-1).x;
                        snackPoints.get(i).y = snackPoints.get(i-1).y;
                    }

                    int dx = (int) event.getX()- p0.x;
                    int dy = (int) event.getY()- p0.y;

                    //Log.d("test", dx+":"+dy);

                    if (dx*dx + dy*dy <= radius*radius) {
                        p0.x = (int) event.getX();
                        p0.y = (int) event.getY();
                    }
                    else {
                        for (int i = 2; ; ++i) {
                            if ((dx/i)*(dx/i) + (dy/i)*(dy/i) <= radius*radius) {
                                p0.x += (dx/i);
                                p0.y += (dy/i);
                                break;
                            }
                        }
                    }


                    for (int j = 0; j<grassPoints.size();++j) {
                        int dxx = grassPoints.get(j).x - p0.x;
                        int dyy = grassPoints.get(j).y - p0.y;
                        if (dxx*dxx + dyy*dyy <= radius*radius) {
                            grassPoints.remove(grassPoints.get(j));
                            j--;
                            big++;
                            Point point = new Point();
                            point.x = p0.x;
                            point.y = p0.y;
                            snackPoints.add(0, point);
                            p0 = snackPoints.get(0);
                            radius++;
                        }
                    }

                    //Log.d("test", getMeasuredWidth() + ":" +getMeasuredHeight());

                    //p0.x = (int) event.getX();
                    //p0.y = (int) event.getY();
                }
                break;
        }

        invalidate();
        return true;
    }
}
