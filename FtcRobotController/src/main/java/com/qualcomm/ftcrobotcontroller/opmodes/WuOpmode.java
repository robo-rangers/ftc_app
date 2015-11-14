package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Girls Who Code on 10/31/2015.
 */
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class WuOpmode extends OpMode{

    DcMotor backright, backleft, frontleft, frontright;
    Servo wheelTest;
    int wheelPos;
    double future;

    public WuOpmode(){

    }

    public void init() //initialize
    {
        backleft = hardwareMap.dcMotor.get("back1");
        backright = hardwareMap.dcMotor.get("front1");
        frontleft = hardwareMap.dcMotor.get("back2");
        frontright = hardwareMap.dcMotor.get("front1");
        wheelTest = hardwareMap.servo.get("180a");

        wheelPos = 10;
        future = time;
    }
    public void loop()
    {

        // throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
        // 1 is full down
        // direction: left_stick_x ranges from -1 to 1, where -1 is full left
        // and 1 is full right

        float direction = gamepad1.left_stick_x;
        // clip the right/left values so that the values never exceed +/- 1
        direction = Range.clip(direction, 0, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        direction = (float)scaleInput(direction);
        //left =  (float)scaleInput(left);

        // update the position of the arm.
        /*
        if(gamepad1.left_trigger != 0){
            telemetry.addData("leftTrig", "leftTrig:  " + gamepad1.left_trigger);//String.format("%.2f", wheelPos));
        }

        if(gamepad1.right_trigger != 0){
            telemetry.addData("rightTrig", "rightTrig:  " + gamepad1.right_trigger);//String.format("%.2f", wheelPos));

        }*/
        //increments from .2 straight to .5
       // wheelPos = Range.clip(wheelPos, .2, .5);

        turnXAxis(gamepad1.left_bumper, gamepad1.right_bumper);
        //moveYAxis(gamepad1.left_trigger, gamepad1.right_trigger);

        // write position values to the wrist and claw servo
        wheelTest.setPosition(scaleWheelPos(wheelPos));


        /*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
        /*
        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("wheel", "wheel:  " + String.format("%.2f", wheelPos));
        //telemetry.addData("time", "time:  " + String.format("%.2f", time));
        //telemetry.addData("future", "future:  " + String.format("%.2f", future));
        telemetry.addData("future", "RAF POWER:  " + String.format("%.2f", scaleWheelPos(wheelPos)));
        */
        update_gamepad_telemetry();

    }

    public void stop() {

    }


    /*
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }
    private double scaleWheelPos(int servoValue)
    {
        double[] values = {0.0, .05, .1 , .15, .2 , .25, .3 , .35, .4 , .45, .5, .55, .6, .65, .7, .75, .8, .85, .9, .95, 1.0 };
        int val = servoValue % values.length;
        return values[val];
    }

    private void turnXAxis (boolean left, boolean right) {

        if (left) {
            if (future < time) {
                future = time + .1;


                wheelPos--;

                if (wheelPos <= -1)
                    wheelPos = 0;
                wheelPos = Math.abs(wheelPos);
            }
        }

        if (right) {
            if(future < time) {
                future = time + .1;
                wheelPos++;
            }
            /*if (wheelPos < 1) {
                wheelPos += 0.1;
            }*/

            if(wheelPos >= 21) {//lenght of the array, change if you cahnge the array
                wheelPos = 20;
            }

        }
    }
    public void update_gamepad_telemetry ()
    {
        //
        // Send telemetry data concerning gamepads to the driver station.
        //
        telemetry.addData ("01", "GP1 Left: " + -gamepad1.left_stick_x);
        telemetry.addData ("02", "GP1 Right: " + -gamepad1.right_stick_y);
        telemetry.addData ("03", "GP1 LT: " + gamepad1.left_trigger);
        telemetry.addData ("04", "GP1 RT: " + gamepad1.right_trigger);
        telemetry.addData ("05", "GP1 B: " + gamepad1.b);
        telemetry.addData ("06", "GP1 A: " + gamepad1.a);
        telemetry.addData ("07", "GP1 X: " + gamepad1.x);


    } // update_gamepad_telemetry
}

