package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.control.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorImpl;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.technototes.library.hardware.motor.EncodedMotor;
import com.technototes.library.hardware.motor.EncodedMotorGroup;
import com.technototes.library.hardware.servo.Servo;
import com.technototes.library.subsystem.motor.EncodedMotorSubsystem;
import com.technototes.logger.Stated;

/** Shooter subsystem
 *
 */
public class ShooterSubsystem extends EncodedMotorSubsystem implements Stated<Double> {
    public EncodedMotor motor1, motor2;
    public Servo servo;

    //public double targetVelo = 0.0;


    // Copy your PID Coefficients here
    public static PIDFCoefficients MOTOR_VELO_PID = new PIDFCoefficients(14, 0.4, 0, 13.5);

    // Copy your feedforward gains here
//    public static double kV = 1 / 3000.0;//2655.0;
//    public static double kA = 0;
//    public static double kStatic = 0;

    // Timer for calculating desired acceleration
    // Necessary for kA to have an affect
//    private final ElapsedTime veloTimer = new ElapsedTime();
//    private double lastTargetVelo = 0.0;
//
//    private final VelocityPIDFController veloController = new VelocityPIDFController(MOTOR_VELO_PID, kV, kA, kStatic);
    public ShooterSubsystem(EncodedMotor<DcMotorEx> m1, EncodedMotor<DcMotorEx> m2, Servo s){
        super(m1, m2);
        motor1 = m1;
        motor2 = m2;
        m1.setPIDFCoeffecients(MOTOR_VELO_PID);
        m2.setPIDFCoeffecients(MOTOR_VELO_PID);
        servo = s;
        s.setRange(0.5, 1);

    }
    public void setVelocity(double p){
        //targetVelo = p*3000;//2655;
        motor1.setVelocity(p);
        motor2.setVelocity(p);
    }

    public double getVelocity(){
        return motor1.getVelocity();
    }
    public double getIdleVelocity(){
        return 500; //idle speed here
    }
    public boolean isAtIdleVelocity(){
        return getIdleVelocity() <= getVelocity();
    }

    public void setFlapPosition(double pos){
        servo.setPosition(pos);
    }

    public double getFlapPosition(){
        return servo.getPosition();
    }

    @Override
    public Double getState() {
        return getVelocity();
    }

//    @Override
//    public void periodic() {
//
//        // Call necessary controller methods
//        veloController.setTargetVelocity(targetVelo);
//        veloController.setTargetAcceleration((targetVelo - lastTargetVelo) / veloTimer.seconds());
//        veloTimer.reset();
//
//        lastTargetVelo = targetVelo;
//
//        // Get the velocity from the motor with the encoder
//        double motorPos = ((DcMotorEx) motor.getDevice()).getCurrentPosition();
//        double motorVelo = ((DcMotorEx) motor.getDevice()).getVelocity();
//
//        // Update the controller and set the power for each motor
//        double power = veloController.update(motorPos, motorVelo);
//        motor.setSpeed(targetVelo > 0.05*2655 ? power : 0);
//    }
}
