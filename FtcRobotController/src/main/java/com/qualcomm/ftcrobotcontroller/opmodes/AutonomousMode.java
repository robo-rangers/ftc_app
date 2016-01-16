package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Sagar on 1/13/2016.
 */
//THIS IS THE BLUE CODE REMEMBER TO INVERSE THE NUMBER FOR THE RED CODE
public class AutonomousMode extends OpMode {

    //180 servos
    /**
     * Took out these too test new arm after Qualtifer one 1.9.16
     * Servo claw;
     * Servo swivLeft, swivRight;
     **/

    boolean detectedColor;
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

    // to test the limit of the arm extending
    int armLimt;
    double armPower;

    //making stuff for reverse mode
    boolean reverse;
    double reverseFuture;

    //platform warning, how many turns before the wires catch
    int warning;

    //AUTONOMOUS
    boolean whiteLight, whiteLight1;
    int step=1;
    OpticalDistanceSensor ODSCenter, ODSFront;
    Servo aVTurn,aVRaise;
    double aVRaisePos, aVTurnPos, futureVT, futureVR;
    /*
    aVTurn
    down = 1
    up = .42

    aVRaise
    left =.82
    right = .18

     */

    ColorSensor color;

    public AutonomousMode()
    {}
    public void init()
    {
        detectedColor = false;
        aVRaisePos = .42;
        aVTurnPos = .82;
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

        armLimt = 0;

        reverse = false;

        futureSwiv = time;
        leftSwivPos = 0.5;
        rightSwivPos = 0.5;

        futureClaw = time;
        clawPos = 0.0;

        //AUTONOMOUS
        wheelPos = 0;
        future = time;
        warning =0;
        whiteLight=false;
        whiteLight1=false;

        try
        {
            ODSCenter =hardwareMap.opticalDistanceSensor.get("odsC");

        }
        catch (Exception p_exception)
        {
            ODSCenter = null;
        }
        try
        {
            ODSFront =hardwareMap.opticalDistanceSensor.get("odsF");

        }
        catch (Exception p_exception)
        {
            ODSFront = null;
        }

        try
        {
            aVRaise =hardwareMap.servo.get("Raiser");

        }
        catch (Exception p_exception)
        {
            aVRaise = null;
        }
        try
        {
            aVTurn =hardwareMap.servo.get("Turner");

        }
        catch (Exception p_exception)
        {
            aVTurn = null;
        }
        try
        {
            color =hardwareMap.colorSensor.get("color");

        }
        catch (Exception p_exception)
        {
            color = null;
        }
    }

    public void loop()
    {
        aVTurn.setPosition(aVTurnPos);
        aVRaise.setPosition(aVRaisePos);

        if(time>=27)
            step = 6;

        //MOVING FORWARDS UNTIL DETECTS WHITE LINE
        if(step==1)
        {
            color.enableLed(true);
            if(time<6&&ODSFront.getLightDetectedRaw()<80&&!whiteLight1){
                driveAutoBot(0,(float).4);//GOESBACKWARDS
            }
            else{
                whiteLight=true;
                stopBot();
                future=time;
                step++;
            }
        }
        //MOVE BACK A LITTLE DUE TO THE INERTIA... ROBOT ROTATES
        else if(step==2) {
            if (time < future + 1) {
                stopBot();
            }
            else if (time < future + 1.2) {
                driveAutoBot(0, (float) -.25);
            }
            else {
                stopBot();
                step++;
                future = time;
            }
        }
        //LOOKING FOR BOTH COLOR SENSORS. IF IT DOESN'T THEN THE ROBOT GIVES UP
        else if(step==3)
        {
            if(whiteLight)
            {
                //rotate in the direction that we need (MUST TEST)
                driveAutoBot((float).3,0);          //******************************REDMODE -.3
                if(time>future+4)
                    step = 6;
                else if(ODSCenter.getLightDetectedRaw()>=23&&ODSFront.getLightDetectedRaw()>=80) //WE NEED TO TEST A RANGE FOR THE WHITES
                {
                    whiteLight1=true;
                    stopBot();
                    step++;
                    future = time;
                }
            }
        }
        //ONCE THE SENSORS ARE PARALLEL TO THE WHITE LINE, IT SET POSITION OF THE AUTONOMOUS ARM
        else if(step==4)
        {
            if(whiteLight1&&whiteLight)
            {
                //future=time;

                if(time<future+1){
                    stopBot();
                    turnAV("blue");//******************************REDMODE pass "red"
                    //aVTurn.setPosition(0);//SET THE POSITION OF THE V FOR THE BUTTON
                    //IFSTATEMENT TO DETECT COLOR,    IF ELSE     aVTurnPos = .82 else aVTurnPos = .18


                }
                else{
                    future=time;
                    step++;
                }
            }
        }
        //MOVES FORWARD SO IT CAN HIT THE BUTTON
        else if(step==5) {
            if (time < future + 1) {


                driveAutoBot(0, (float) .2);
                if(!detectedColor)
                {
                    turnAV("blue");//******************************REDMODE pass "red"
                }


            } else {
                stopBot();
                future = time;
                step++;
            }
        }
        //STOP ROBOT
        else if(step == 6)
        {
            stopBot();
        }

        updateGamepadTelemetry();
    }
    public void turnAV(String c)
    {
        if(c.equals("red"))
        {
            if(color.red()>0&&color.green()==0&&color.blue()==0) //CHECKING FOR RED
            {
                aVTurnPos = .82;
                detectedColor = true;

            }
            else if(color.red()==0&&color.green()==0&&color.blue()>0)  //CHECKING FOR BLUE
            {
                aVTurnPos = .18;
                detectedColor = true;

            }


        }
        else if(c.equals("blue"))
        {
            if(color.red()>0&&color.green()==0&&color.blue()==0) //CHECKING FOR RED
            {
                aVTurnPos = .18;//turn right
                detectedColor = true;

            }
            else if(color.red()==0&&color.green()==0&&color.blue()>0)  //CHECKING FOR BLUE
            {
                aVTurnPos = .82;
                detectedColor = true;

            }

        }

    }
    public void moveDCSwivel() {
        swivel.setPower(gamepad2.right_stick_y/2);
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
        if(gamepad2.left_stick_x<0) {
            warning++;
            platformPos += -gamepad2.left_stick_x/2;
        }
        else if(gamepad2.left_stick_x>0) {
            warning--;
            platformPos += -gamepad2.left_stick_x/2;
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
        if (gamepad2.left_bumper) {
            if (futureSwiv < time) {
                futureSwiv = time + .1;
                leftSwivPos -= 0.05;
                leftSwivPos = Range.clip(leftSwivPos, 0, 1);
                rightSwivPos = leftSwivPos;
            }
        }

        //move claw and swivel down in increments
        if (gamepad2.right_bumper) {
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
    public void moveArm()
    {
        if (gamepad2.dpad_up) {
            arm.setPosition(.7);
        }
        else if (gamepad2.dpad_down)
        {
            arm.setPosition(.3);
        }
        else {
            arm.setPosition(.5);
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

        float x = gamepad1.left_stick_x;
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

        if (ODSCenter != null)
        {
            //
            // Is the amount of light detected above the threshold for white
            // tape?
            //
            if (ODSCenter.getLightDetected () > 0.8)
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

        if(ODSCenter != null)
        {
            telemetry.addData("31a", "ods " + ODSCenter.getLightDetected());
            telemetry.addData("31b", "ods " + ODSCenter.getLightDetectedRaw());
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

        if(ODSFront != null)
        {
            telemetry.addData("33a", "odsFront " + ODSFront.getLightDetected());
            telemetry.addData("33b", "odsFront " + ODSFront.getLightDetectedRaw());
        }
        else
        {
            telemetry.addData("34", "ODSFront is not here");
        }

        if(detectWhiteTape() == true)
        {
            telemetry.addData("36","I see the white light!");

        }
        else
        {
            telemetry.addData("37","I don't see the white light!");
        }

        telemetry.addData("38","step #: " + step);
    }
    public void driveAutoBot(float xa,float ya)
    {

        float x=xa;
        float y=-ya;

//negatebothtochangewhichwayisforward
        float left=-(x+y);
        float right=-(x-y);

        right=Range.clip(right,-1,1);
        left=Range.clip(left,-1,1);

        right=(float)scaleInput(right);
        left=(float)scaleInput(left);

        frontright.setPower(right);
        backright.setPower(right);
        frontleft.setPower(left);
        backleft.setPower(left);
    }
    public void stopBot()
    {
        frontright.setPower(0);
        backright.setPower(0);
        frontleft.setPower(0);
        backleft.setPower(0);
    }
}