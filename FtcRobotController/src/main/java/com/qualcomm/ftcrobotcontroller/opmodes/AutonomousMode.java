package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Sagar on 12/8/2015.
 */
public class AutonomousMode extends OpMode {


    Servo swivLeft, swivRight;
    Servo arm;
    double futureSwiv, leftSwivPos, rightSwivPos;

    //wheel stuff
    DcMotor backright, backleft, frontleft, frontright;
    Servo platform ,wheelTest;
    int wheelPos;
    double future;

    //platform warning
    int warning;

    public AutonomousMode()
    {

    }

    public void init()
    {
        swivLeft = hardwareMap.servo.get("sLeft");
        swivRight = hardwareMap.servo.get("sRight");
        arm = hardwareMap.servo.get("elevator");
        futureSwiv = time;
        leftSwivPos = 0.5;
        rightSwivPos = 0.5;                              
        //wheel stuff
        //RIGHTS ARE 1'S AND LEFTS ARE 2'S
        backleft = hardwareMap.dcMotor.get("back2");
        backright = hardwareMap.dcMotor.get("back1");
        frontleft = hardwareMap.dcMotor.get("front2");
        frontright = hardwareMap.dcMotor.get("front1");
        platform = hardwareMap.servo.get("360a");

        wheelPos = 0;
        future = time;
        warning =0;
    }

    public void loop()
    {
        if(time>=27)
            stop();
        else{
            if(time<=4) {
                //FORWARD
                driveAutoBot(0, 1);
            }
            else if(time>4&&time<=6)
            {
                //LEFT
                driveAutoBot(-1,0);
            }
            else if(time>6&&time<=8)
            {
                //PLATFORM MOVEMENT
                platform.setPosition(scaleContinuousWheel((float)0.5));
            }
        }
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
    }


    private double scaleContinuousWheel (float right_stick_x)
    {
        if(right_stick_x>0)
            warning++;
        else if(right_stick_x<0)
            warning--;

        return (right_stick_x / 2) + .5;


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


    private void updateGamepadTelemetry()
    {

        telemetry.addData ("00", "ver: 12/14 3:41");
        telemetry.addData( "Warning", "WARNING" + warning);
        warningMessage();

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

        telemetry.addData ("17", "ARM POS: " + arm.getPosition());

        telemetry.addData ("18", "Back Right: " + backright.getPower());
        telemetry.addData ("19", "Front Right: " + frontright.getPower());
        telemetry.addData ("20", "Back Left: " + backleft.getPower());
        telemetry.addData ("21", "Front Left: " + frontleft.getPower());
        // telemetry.addData ("16", "GP1 LTRIG: " + gamepad1.right_trigger);

        // Send telemetry data concerning gamepads to the driver station.

        //telemetry.addData ("17", "cServo Dir: " + continuousTest.getDirection());

        //telemetry.addData ("18", "cServo Pos: " + continuousTest.getPosition());

        telemetry.addData ("WHEEL", "WHEELPOS:" + wheelPos);

        telemetry.addData("22", "Warning" + warning);




    }

    private double scaleContinuousServo(float value)
    {
        return ((value / 2) + .5);
    }

    public void moveSwiv()
    {
        if (gamepad1.left_bumper)
        {
            if (futureSwiv < time)
            {
                futureSwiv = time + .1;
                leftSwivPos += 0.1;
                leftSwivPos = Range.clip(leftSwivPos, 0, 1);
                rightSwivPos = 1 - leftSwivPos;
            }
        }

        if (gamepad1.right_bumper)
        {
            if (futureSwiv < time)
            {
                futureSwiv = time + .1;
                leftSwivPos -= 0.1;
                leftSwivPos = Range.clip(leftSwivPos, 0, 1);
                rightSwivPos = 1 - leftSwivPos;
            }
        }
    }

    public void resetServos()
    {
        if (gamepad1.start)
        {
            leftSwivPos = 0.5;

            rightSwivPos = 0.5;
        }
    }

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
    public void driveAutoBot(float xa, float ya)
    {

        float x = xa;
        float y = -ya;

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
}

