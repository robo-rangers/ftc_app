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
    Servo wheelTest, continuousTest;
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
        continuousTest = hardwareMap.servo.get("360a");

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

        wheelPos = turnXAxis(gamepad1.left_bumper, gamepad1.right_bumper, wheelPos);

        wheelTest.setPosition(scaleWheelPos(wheelPos));
        continuousTest.setPosition(scaleContinuous(gamepad1.right_stick_x));
        updateGamepadTelemetry();


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

    private int turnXAxis (boolean left, boolean right, int pos) {

        if (left) {
            if (future < time) {
                future = time + .1;


                pos--;

                //if (wheelPos <= -1)
                //    wheelPos = 0;
                pos = Math.abs(pos);
            }
        }

        if (right) {
            if(future < time) {
                future = time + .1;
                pos++;
            }

            /*if(wheelPos >= 21) {//lenght of the array, change if you change the array
                wheelPos = 20;
            }*/
        }
        return pos;
    }

    private double scaleContinuous (float right_stick_x) {

        return (right_stick_x / 2) + .5;

    }

    private void updateGamepadTelemetry ()
    {

        // Send telemetry data concerning gamepads to the driver station.
        telemetry.addData ("01", "GP1 LeftHor: " + gamepad1.left_stick_x);
        telemetry.addData ("02", "GP1 LeftVert: " + -gamepad1.left_stick_y);
        telemetry.addData ("03", "GP1 RightHor: " + gamepad1.right_stick_x);
        telemetry.addData ("04", "GP1 RightVert: " + -gamepad1.right_stick_y);
        telemetry.addData ("05", "GP1 B: " + gamepad1.b);
        telemetry.addData ("06", "GP1 A: " + gamepad1.a);
        telemetry.addData ("07", "GP1 X: " + gamepad1.x);
        telemetry.addData ("08", "GP1 A: " + gamepad1.y);
        telemetry.addData ("09", "GP1 bDPADL: " + gamepad1.dpad_left);
        telemetry.addData ("10", "GP1 bDPADR: " + gamepad1.dpad_right);
        telemetry.addData ("11", "GP1 bDPADUP: " + gamepad1.dpad_up);
        telemetry.addData ("12", "GP1 bDPADDOWN: " + gamepad1.dpad_down);
        telemetry.addData ("13", "GP1 LEFTB: " + gamepad1.left_stick_button);
        telemetry.addData ("14", "GP1 RIGHTB: " + gamepad1.right_stick_button);
        telemetry.addData ("15", "GP1 RTRIG: " + gamepad1.left_trigger);
        telemetry.addData ("16", "GP1 LTRIG: " + gamepad1.right_trigger);
        telemetry.addData ("17", "cServo Dir: " + continuousTest.getDirection());
        telemetry.addData ("18", "cServo Pos: " + continuousTest.getPosition());
        telemetry.addData ("WHEEL", "WHEELPOS:" + wheelPos);

    }
}

