package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Sagar on 12/8/2015.
 */
public class DriveMode extends OpMode {

//this mode was created to test the 6 wheel drive


    //wheel stuff
    DcMotor backright, backleft, frontleft, frontright;

    int wheelPos;
    double future;

    //platform warning
    int warning;

    public DriveMode()
    {

    }

    public void init()
    {

        //wheel stuff
        //RIGHTS ARE 1'S AND LEFTS ARE 2'S
        backleft = hardwareMap.dcMotor.get("back2");
        backright = hardwareMap.dcMotor.get("back1");
        frontleft = hardwareMap.dcMotor.get("front2");
        frontright = hardwareMap.dcMotor.get("front1");


        wheelPos = 0;
        future = time;
        warning =0;
    }

    public void loop()
    {


        driveBot();

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
}
