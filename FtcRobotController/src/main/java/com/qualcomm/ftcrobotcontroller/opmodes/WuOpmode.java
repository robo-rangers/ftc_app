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
    double wheelPos;

    public WuOpmode(){

    }

    public void init() //initialize
    {
        backleft = hardwareMap.dcMotor.get("motor_1");
        backright = hardwareMap.dcMotor.get("motor_2");
        frontleft = hardwareMap.dcMotor.get("motor_3");
        frontright = hardwareMap.dcMotor.get("motor_4");
        wheelTest = hardwareMap.servo.get("S1");

        wheelPos = 0.0;
    }
    public void loop()
    {
        // throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
        // 1 is full down
        // direction: left_stick_x ranges from -1 to 1, where -1 is full left
        // and 1 is full right
        float direction = gamepad1.left_stick_x;

        // clip the right/left values so that the values never exceed +/- 1
        direction = Range.clip(direction, -1, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        direction = (float)scaleInput(direction);
        //left =  (float)scaleInput(left);

        // update the position of the arm.
        if (gamepad1.x) {
            // if the A button is pushed on gamepad1, increment the position of
            // the arm servo.
            wheelPos += direction;
        }

        if (gamepad1.b) {
            // if the Y button is pushed on gamepad1, decrease the position of
            // the arm servo.
            wheelPos -= direction;
        }

        // write position values to the wrist and claw servo
        wheelTest.setPosition(wheelPos);

        /*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */

        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("wheel", "wheel:  " + String.format("%.2f", wheelPos));
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
}

