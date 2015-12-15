package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Girls Who Code on 11/21/2015.
 */

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.TouchSensorMultiplexer;

public class SwivelMode extends OpMode {

    Servo swivLeft, swivRight;
    Servo arm;
    double futureSwiv,leftSwivPos, rightSwivPos;
    TouchSensor sensorTouch;
    OpticalDistanceSensor OpticalDistSensor;

    public SwivelMode() {

    }

    public void init() {

        futureSwiv = time;
        leftSwivPos=0.5;
        rightSwivPos=0.5;

        //Left swivel servo
        try
        {
            swivLeft = hardwareMap.servo.get("sLeft");
        }
        catch (Exception p_exception)
        {
            swivLeft = null;
        }

        //Right swivel servo
        try
        {
            swivRight=hardwareMap.servo.get("sRight");
        }
        catch (Exception p_exception)
        {
            swivRight = null;
        }

        //Arm Servo
        try
        {
            arm=hardwareMap.servo.get("arm360");
        }
        catch (Exception p_exception)
        {
            arm=null;
        }

        //Touch Sensor
        try
        {
            sensorTouch = hardwareMap.touchSensor.get("tSens");
        }
        catch (Exception p_exeception)
        {
            sensorTouch = null;
        }

        //Optical Distance Sensor
        try
        {
            OpticalDistSensor = hardwareMap.opticalDistanceSensor.get ("c3");
        }
        catch (Exception p_exception)
        {
            OpticalDistSensor = null;
        }

    }
    public void loop()
    {
        resetServos();
        moveSwiv();

        swivLeft.setPosition(leftSwivPos);
        swivRight.setPosition((rightSwivPos));
        updateGamepadTelemetry();
        float direction = gamepad1.right_stick_y;
        direction = Range.clip(direction, -1, 1);
        arm.setPosition(scaleContinuous(-gamepad1.right_stick_y));
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

        if(sensorTouch!=null)
        {
            telemetry.addData ("17", "touchSensor: " + sensorTouch.isPressed());
            telemetry.addData ("18", "touchSensorV: " + sensorTouch.getValue());
        }
        telemetry.addData ("19", "touchSensor: " + sensorTouch);

        telemetry.addData ("20", "OpticalDistanceSensor: " + OpticalDistSensor.getLightDetected());
        telemetry.addData ("20", "opDS Light Detected: " + String.format("%.4f",OpticalDistSensor.getLightDetected()));
        telemetry.addData ("21", "opDS Light Detected Raw: " + OpticalDistSensor.getLightDetectedRaw());

        telemetry.addData ("20", "OpticalDistanceSensor: " + OpticalDistSensor.getLightDetected());

        telemetry.addData ("20", "opDS Light Detected: " + String.format("%.4f",OpticalDistSensor.getLightDetected()));
        telemetry.addData ("21", "opDS Light Detected Raw: " + OpticalDistSensor.getLightDetectedRaw());
        //telemetry.addData ("17", "cServo Dir: " + continuousTest.getDirection());
        //telemetry.addData ("18", "cServo Pos: " + continuousTest.getPosition());
        //telemetry.addData ("WHEEL", "WHEELPOS:" + wheelPos);
    }

    private double scaleContinuous (float value)
    {
        return ((value / 2) + .5);
    }

    public void moveSwiv()
    {
        if(gamepad1.left_bumper)
        {
            if(futureSwiv < time)
            {
                futureSwiv = time + .1;
                leftSwivPos += 0.1;
                leftSwivPos = Range.clip(leftSwivPos, 0, 1);
                rightSwivPos = 1 - leftSwivPos;
            }
        }

        if(gamepad1.right_bumper)
        {
            if(futureSwiv < time)
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
        if(gamepad1.start)
        {
            leftSwivPos=0.5;
            rightSwivPos=0.5;
        }
    }
}
