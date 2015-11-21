package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Girls Who Code on 11/21/2015.
 */

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.qualcomm.robotcore.hardware.DcMotor;

import com.qualcomm.robotcore.hardware.Servo;

import com.qualcomm.robotcore.util.Range;


public class SwivelMode extends OpMode {
    Servo swivLeft, swivRight;

    double future,leftSwivPos, rightSwivPos;


    public SwivelMode() {


    }


    public void init() {

        swivLeft= hardwareMap.servo.get("pLeft");

        swivRight=hardwareMap.servo.get("pRight");


        future = time;

        leftSwivPos=0.5;

        rightSwivPos=0.5;

    }


    public void loop() {

        moveSwiv();

        swivLeft.setPosition(leftSwivPos);

        swivRight.setPosition((rightSwivPos));

        updateGamepadTelemetry();

    }


    public void stop() {


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

        //telemetry.addData ("17", "cServo Dir: " + continuousTest.getDirection());

        //telemetry.addData ("18", "cServo Pos: " + continuousTest.getPosition());

        //telemetry.addData ("WHEEL", "WHEELPOS:" + wheelPos);


    }

    public void moveSwiv()

    {

        if(gamepad1.b)

        {

            if(future < time) {

                future = time + .1;

                leftSwivPos += 0.1;

                rightSwivPos = 1 - leftSwivPos;

            }

        }

        if(gamepad1.x)

        {

            if(future < time) {

                future = time + .1;

                leftSwivPos -= 0.1;

                rightSwivPos = 1 - leftSwivPos;

            }

        }

    }



}
