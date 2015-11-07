package com.qualcomm.ftcrobotcontroller.opmodes;

//SURIEL0:  READ ALL THE COMMENTS ESPECIALLY THE ONES MARKED SURIEL


//SURIEL1: import these files
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Emmanuel on 10/30/2015.
 */

/**
 * SURIEL2:  extend OpMode
 * OpMode is an abstract class therefore you must
 * override the abstract methods
 *  public void init(), public void loop()
 */
public class SurielTeleOp extends OpMode{

    //SURIEL3:  Instance variables
    //declare your instance varibles here, any constants or variables we might need
    //they could be ints and doubles that will help us keep track of our servo positions
    //We should also declare our motors here
    //examples

    //declaring motors
    DcMotor backRight, frontRight, backLeft, frontLeft;
    //Servo slapper;

    //position of the slapper , 0.0 to 1.0
    //double slapperPosition;

    /**
     * SURIEL4: default constructor DO IT
     * We are going to leave this blank because the init method (initialize method)
     * is going to handle the initial values of our instance variables
     */
    public SurielTeleOp()
    {

    }

    /**
     * SURIEL5:
     * init method
     * This method initializes all of your instance variables.
     * here we will connect each motor variable to the ACTUAL PHYSICAL MOTORs
     * on the Robot.
     */
    public void init()
    {
        /* SURIEL6: IMPORTANT: below are examples of this comment
		 * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */

        //SURIEL7: READ THE COMMENTS IN THIS METHOD

        //assuming we set up the PHYSICAL backLeft motor to motor_1
        backLeft =hardwareMap.dcMotor.get("back2");
        backRight =hardwareMap.dcMotor.get("back1");
        frontLeft =hardwareMap.dcMotor.get("front2");
        frontRight =hardwareMap.dcMotor.get("front1");

        //note slapper is a servo not a dcMotor
        //slapper = hardwareMap.servo.get("servo_1");

        //assign initial position of the slapper server must be between 0.0 and 1.0
        //slapperPosition = .3;

    }

    /**
     * SURIEL8:
     * This method will be called repeatedly
     * Think of this as a while(true) { } loop
     *
     * ******THIS IS WHERE MOST OF YOUR CODE WILL BE!! *****
     *
     */

    public void loop()
    {
        /**
         * SURIEL9:
         * First we should start by getting all the values from the
         * GAMEPAD or whatever we use
         * KEEP IN MIND that this is in a loop and all these values keep getting
         * updated constantly.
         */

        // SURIEL10: Remember float is a double with a smaller range
        // most of the methods require floats not doubles
        // throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
        // 1 is full down
        // direction: left_stick_x ranges from -1 to 1, where -1 is full left
        // and 1 is full right
        float throttle = -gamepad1.left_stick_y;
        float direction = gamepad1.left_stick_x;
        float right = throttle - direction;
        float left = throttle + direction;

        //SURIEL11: Most of the parameters in these methods have
        //preconditions of -1 to 1 or 0 to 1, basically small numbers
        //so we need to make sure that we do not go over or under these values
        //the static method clip in the Range class will help us do this

        // clip the right/left values so that the values never exceed +/- 1
        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);

        // SURIEL12:
        // the method scaleInput is implemented(Written) below
        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        right = (float)scaleInput(right);
        left =  (float)scaleInput(left);

        //SURIEL13:
        // This is when we actually give power to the motors
        // write the values to the motors
        frontRight.setPower(right);
        backRight.setPower(right);
        frontLeft.setPower(left);
        backLeft.setPower(left);


        //SURIEL14:
        // Get input for the buttons, which moves the slapper server
        // update the position of the slapper.
        /*if (gamepad1.a) {
            // if the A button is pushed on gamepad1, increment the position of
            // the slapper servo.
            slapperPosition += .1;
        }

        if (gamepad1.y) {
            // if the Y button is pushed on gamepad1, decrease the position of
            // the slapper servo.
            slapperPosition -= .1;
        }
*/
        // SURIEL15:
        // making sure that the slap position is between .2 and .8
        // clip the position values so that they never exceed their allowed range.
       // slapperPosition = Range.clip(slapperPosition, .2, .8);


        // SURIEL16
        // write position values to the slapper
       // slapper.setPosition(slapperPosition);

        /*SURIEL 17
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
        telemetry.addData("Text", "*** Robot Data***");
        //telemetry.addData("slapper", "slapper:  " + String.format("%.2f", slapperPosition));

        telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", left));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right));
        //telemetry.addData("right tgt pwr", "hellor: " + String.format("%.2f", right));

    }


    /* SURIEL 18:
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
