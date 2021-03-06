package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Sagar on 1/9/2016.
 */
public class SuperRoverine extends OpMode {

    //180 servos
    /**
     * Took out these too test new arm after Qualtifer one 1.9.16
     * Servo claw;
     * Servo swivLeft, swivRight;
     **/

    UltrasonicSensor leftSonic;
    // dont have the second one yet
    // UltrasonicSensor rightSonic;

    //using a different claw(has two 180 servos)
    Servo claw2;

    Servo arm;
    Servo platform ,wheelTest;
    double futureSwiv, futureClaw, leftSwivPos, rightSwivPos, clawPos, platformPos;

    //wheel stuff
    DcMotor backright, backleft, frontleft, frontright;

    //Testing longer stronger arm
    DcMotor swivel;

    int wheelPos;
    double future;

    OpticalDistanceSensor ODS;

    // to test the limit of the arm extending
    int armLimt;
    double armPower;

    //making stuff for reverse mode
    boolean reverse;
    double reverseFuture;

    //platform warning, how many turns before the wires catch
    int warning;


    public SuperRoverine()
    {

    }

    public void init()
    {
        try
        {
            ODS =hardwareMap.opticalDistanceSensor.get("ods");

        }
        catch (Exception p_exception)
        {
            ODS = null;
        }
        try
        {
            leftSonic =hardwareMap.ultrasonicSensor.get("ear1");

        }
        catch (Exception p_exception)
        {
            leftSonic = null;
        }



        //servo stuff
        try {
            claw2 = hardwareMap.servo.get("claw");
        }
        catch (Exception p_exception)
        {
            telemetry.addData ("-1", "NO claw");
            claw2 = null;
        }

        try
        {

            arm = hardwareMap.servo.get("elevator");
        }
        catch (Exception p_exception)
        {
            telemetry.addData ("-1", "NO elevator");
            arm = null;
        }

        try
        {
            platform = hardwareMap.servo.get("360a");
        }
        catch (Exception p_exception)
        {
            telemetry.addData ("-1", "NO 360a Platform");
            platform = null;
        }



        armLimt = 0;

        reverse = false;

        futureSwiv = time;
        leftSwivPos = 0.5;
        rightSwivPos = 0.5;

        futureClaw = time;
        clawPos = 0.5;
        //wheel stuff
        //RIGHTS ARE 1'S AND LEFTS ARE 2'S
        try
        {
            backleft = hardwareMap.dcMotor.get("back2");
        }
        catch (Exception p_exception)
        {
            telemetry.addData ("-1", "NO back2");
            backleft = null;
        }

        try
        {
            backright= hardwareMap.dcMotor.get("back1");
        }
        catch (Exception p_exception)
        {
            telemetry.addData ("-1", "NO back1");
            backright = null;

        }

        try
        {
            frontleft = hardwareMap.dcMotor.get("front2");
        }
        catch (Exception p_exception)
        {
            telemetry.addData ("-1", "NO front2");
            backright = null;
        }

        try
        {
            frontright = hardwareMap.dcMotor.get("front1");
        }
        catch (Exception p_exception)
        {
            telemetry.addData ("-1", "NO front1");
            frontright = null;
        }

        try {
            swivel = hardwareMap.dcMotor.get("swivel");
        }
        catch (Exception p_exception)
        {
            telemetry.addData ("-1", "NO swivel");
            swivel = null;
        }




        wheelPos = 0;
        future = time;
        warning =0;
    }

    public void loop()
    {
        //resets all servos by pressing start


        //Handles the swivel
        mooperScooper();

        //moving the claw
        claw2.setPosition(rightSwivPos);
        moveDCSwivel();

        //move the arm
        arm.setPosition(scaleContinuousWheel2(-gamepad1.right_stick_y));

        //HANDLES PLATFORM
        platform.setPosition(scaleContinuousWheel());

        handlingReverse();

        //handle the driving
        if(!reverse)
            driveBot();
        else
            reverseDriveBot();

        updateGamepadTelemetry();




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

  M                    if(pos >= 21) //lenght of the array, change if you change the array
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


    //this is used for the platfrom
    private double scaleContinuousWheel ()
    {
        platformPos=0.5;
        if(gamepad1.right_trigger>0) {
            warning++;
            platformPos -= gamepad1.right_trigger/2;
        }
        else if(gamepad1.left_trigger>0) {
            warning--;
            platformPos += gamepad1.left_trigger/2;
        }

        return platformPos;

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

    public void mooperScooper() {
        //move claw and swivel up in increments
        if (gamepad1.left_bumper) {
            if (futureSwiv < time) {
                futureSwiv = time + .1;
                leftSwivPos -= 0.05;
                leftSwivPos = Range.clip(leftSwivPos, 0, 1);
                rightSwivPos = leftSwivPos;
            }
        }

        //move claw and swivel down in increments
        if (gamepad1.right_bumper) {
            if (futureSwiv < time) {
                futureSwiv = time + .1;
                leftSwivPos += 0.05;
                leftSwivPos = Range.clip(leftSwivPos, 0, 1);
                rightSwivPos = leftSwivPos;
            }
        }
    }

    public void moveSwiv(double left)
    {
        leftSwivPos=left;
        rightSwivPos=1-left;
    }




    // swivel, arm, moop
    public void moveDCSwivel()
    {
        if (gamepad1.dpad_up)
        {
            swivel.setPower(-.3);
        }
        else if (gamepad1.dpad_down)
        {
            swivel.setPower(.25);
        }
        else
        {
            swivel.setPower(0);
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

    public void handlingReverse()
    {
        if(gamepad1.a)
        {
            if (reverseFuture < time)
            {
                reverseFuture = time + .1;
                reverseFuture += 0.05;
                if(reverse)
                    reverse = false;
                else
                    reverse = true;
            }

        }
    }

    public void reverseDriveBot()
    {

        float x = -gamepad1.left_stick_x;
        float y = gamepad1.left_stick_y;

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

    boolean detectWhiteTape ()

    {
        //
        // Assume not.
        //
        boolean l_return = false;

        if (ODS != null)
        {
            //
            // Is the amount of light detected above the threshold for white
            // tape?
            //
            if (ODS.getLightDetected () > 0.8)
            {
                l_return = true;
            }
        }

        //
        // Return
        //
        return l_return;

    } // a_ods_white_tape_detected

    private void updateGamepadTelemetry()
    {

        telemetry.addData ("00", "ver: 1/13/16 4:02");
        telemetry.addData( "Warning", "WARNING" + warning);
        warningMessage();
        telemetry.addData("01", "rightSwivPos: " + rightSwivPos);

        telemetry.addData("0", "GP1 LeftHor: " + gamepad1.left_stick_x);
        telemetry.addData ("2", "GP1 LeftVert: " + -gamepad1.left_stick_y);

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



        if(arm!=null)
        {
            telemetry.addData("25", "Elevator" + arm.getPosition());
        }
        else
            telemetry.addData("25", "NO elevator");

        if(platform!=null)
        {
            telemetry.addData("26", "Platform" + platform.getPosition());
        }
        else
            telemetry.addData("26", "NO Platform");


        if(claw2!=null)
        {
            telemetry.addData("27", "Claw" + claw2.getPosition());
        }
        else
        {
            telemetry.addData("27", "NO CLAW");
        }

        if(leftSonic!=null)
        {
            telemetry.addData("29", "ultrasonic sensor" + leftSonic.getUltrasonicLevel());
            telemetry.addData("30", "ultrasonic sensor" + leftSonic.status());

        }
        else
        {
            telemetry.addData("29", "ULTRASONIC SENSOR is not here");
        }

        if(ODS != null)
        {
            telemetry.addData("31a", "ods " + ODS.getLightDetected());
            telemetry.addData("31b", "ods " + ODS.getLightDetectedRaw());
        }
        else
        {
            telemetry.addData("31", "ODS is not here");
        }

        if(detectWhiteTape() == true)
        {
            telemetry.addData("32","I see the white light!");

        }
        else
        {
            telemetry.addData("32","I don't see the white light!");
        }



    }
}
