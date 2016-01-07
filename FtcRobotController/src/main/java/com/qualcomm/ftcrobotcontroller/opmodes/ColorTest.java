package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Sagar on 1/7/2016.
 */

//this was created for the SOLE purpose for testing the color sensor
public class ColorTest extends OpMode {

    /**
    //180 servos
    Servo swivLeft, swivRight;
    Servo elevator;
    Servo claw;
    double futureSwiv, futureClaw, leftSwivPos, rightSwivPos, clawPos, platformPos;
    **/
    //wheel stuff
    DcMotor backright, backleft, frontleft, frontright;
    Servo platform ,wheelTest;
    int wheelPos;
    double future;

    //platform warning, how many turns before the wires catch
    int warning;

    ColorSensor RGB;

    public ColorTest()
    {

    }

    public void init()
    {
        //servo stuff



        try
        {
            RGB = hardwareMap.colorSensor.get("rainbow");
        }
        catch (Exception p_exception)
        {
            RGB = null;
        }


        //wheel stuff
        //RIGHTS ARE 1'S AND LEFTS ARE 2'S
        try
        {
            backleft = hardwareMap.dcMotor.get("back2");
        }
        catch (Exception p_exception)
        {
            backleft = null;
        }

        try
        {
            backright= hardwareMap.dcMotor.get("back1");
        }
        catch (Exception p_exception)
        {
            backright = null;

        }

        try
        {
            frontleft = hardwareMap.dcMotor.get("front2");
        }
        catch (Exception p_exception)
        {
            backright = null;
        }

        try
        {
            frontright = hardwareMap.dcMotor.get("front1");
        }
        catch (Exception p_exception)
        {
            frontright = null;
        }

        try
        {
            platform = hardwareMap.servo.get("360a");
        }
        catch (Exception p_exception)
        {
            platform = null;
        }


        wheelPos = 0;
        future = time;
        warning =0;
    }

    public void loop()
    {
        //resets all servos by pressing start


        //Handles the swivel




        //moving the claw


        //HANDLES PLATFORM


        driveBot();

        updateGamepadTelemetry();

        RGB.enableLed(true);

    }


    public void stop()
    {

    }

    // THIS WAS CALLED ON WHEEL TEST (180 SERVO)
    public double scaleInput(double dVal)
    {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.40, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00 };

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

    //NOT USED
    /*
    private int pos180Servo (boolean left, boolean right, int pos) {

        if (left) {
            if (future < time) {
                future = time + .1;
                pos--;

                if(pos <= -1)
                    pos = 0;
            }
        }

        if (right)
        {
            if(future < time)
            {
                future = time + .1;
                pos++;

                    if(pos >= 21) //lenght of the array, change if you change the array
                        pos = 20;
            }
        }
        return pos;
    } */

    //this is used for the arm(elevator), its different because we dont need a warning for this
    private double scaleContinuousWheel2(float right_stick_y)
    {
        return (right_stick_y / 2) + .5;
    }




    private void warningMessage()
    {
        if (warning >= 100)
        {
            telemetry.addData("23", "MAYDAY MAYDAY TOO MUCH TO THE RIGHT!!!!");
        }
        else if(warning <= -100)
        {
            telemetry.addData("23", "MAYDAY MAYDAY TOO MUCH TO THE LEFT!!!!");
        }

    }
    /*
    private double scaleContinuousServo(float value)
    {
        return ((value / 2) + .5);
    }*/





    public void driveBot()
    {

        float x = gamepad1.left_stick_x;
        float y = -gamepad1.left_stick_y;

        //negate both to change which way is forward
        float left = -(x+y);
        float right = -(x-y);

        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);

        right = (float)scaleInput(right);
        left =  (float)scaleInput(left);

        frontright.setPower(right);
        backright.setPower(right);
        frontleft.setPower(left);
        backleft.setPower(left);
    }
    private void updateGamepadTelemetry()
    {

        telemetry.addData ("00", "ver: 12/14 3:41");
        telemetry.addData( "Warning", "WARNING" + warning);
        warningMessage();

        /*
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

        telemetry.addData ("17", "ARM POS: " + elevator.getPosition());

        telemetry.addData ("18", "Back Right: " + backright.getPower());
        telemetry.addData ("19", "Front Right: " + frontright.getPower());
        telemetry.addData ("20", "Back Left: " + backleft.getPower());
        telemetry.addData ("21", "Front Left: " + frontleft.getPower());
        */
        // telemetry.addData ("16", "GP1 LTRIG: " + gamepad1.right_trigger);

        // Send telemetry data concerning gamepads to the driver station.

        //telemetry.addData ("17", "cServo Dir: " + continuousTest.getDirection());

        //telemetry.addData ("18", "cServo Pos: " + continuousTest.getPosition());

        telemetry.addData ("WHEEL", "WHEELPOS:" + wheelPos);

        //telemetry.addData("22", "Warning" + warning);


        if(RGB !=null)
        {
            telemetry.addData ("28", "Amount of light " + RGB.alpha());
            telemetry.addData("29", "The hue " + RGB.argb());
            telemetry.addData("30", "blue light " + RGB.blue());
            telemetry.addData("31", "green light " + RGB.green());
            telemetry.addData("32", "red light " + RGB.red());
        }
    }
}


